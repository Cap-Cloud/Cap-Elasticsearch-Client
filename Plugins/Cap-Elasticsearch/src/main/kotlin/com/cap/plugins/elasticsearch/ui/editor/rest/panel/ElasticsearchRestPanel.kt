package com.cap.plugins.elasticsearch.ui.editor.rest.panel

import com.cap.plugins.common.action.CapAction
import com.cap.plugins.common.component.table.CapPageParameter
import com.cap.plugins.common.component.table.CapSortModel
import com.cap.plugins.common.component.table.CapTableItem
import com.cap.plugins.common.http.model.Method
import com.cap.plugins.common.http.model.Request
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.common.service.CapActionManager
import com.cap.plugins.common.task.CapTaskExecution
import com.cap.plugins.common.util.*
import com.cap.plugins.elasticsearch.constant.ElasticsearchConstant
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.model.ElasticsearchResponse
import com.cap.plugins.elasticsearch.ui.editor.model.ResponseContext
import com.cap.plugins.elasticsearch.ui.editor.model.RestResponseContext
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchBodyPanel
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchPanel
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchResultPanel
import com.cap.plugins.elasticsearch.ui.editor.rest.ElasticsearchRestFile
import com.fasterxml.jackson.databind.node.ObjectNode
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.EnumComboBoxModel
import com.intellij.ui.JBColor
import com.intellij.ui.SearchTextField
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import org.apache.http.client.utils.URIBuilder

class ElasticsearchRestPanel : ElasticsearchPanel {

    val project: Project
    val file: ElasticsearchRestFile

    private val methodCombo = ComboBox(EnumComboBoxModel(Method::class.java))
    lateinit var lastSearchRequest: Request

    private val urlField = object : SearchTextField() {
        override fun toClearTextOnEscape(): Boolean {
            return false
        }
    }

    override var bodyPanel: ElasticsearchBodyPanel
    override var resultPanel: ElasticsearchResultPanel

    private val splitter: Splitter

    constructor(project: Project, elasticsearchRestFile: ElasticsearchRestFile) : super() {
        this.project = project
        this.file = elasticsearchRestFile

        bodyPanel = ElasticsearchRestBodyPanel(project)
        resultPanel = ElasticsearchRestResultPanel(project, this)

        val toolbar = layoutWCE(
            methodCombo,
            urlField,
            CapActionManager.getInstance().createActionToolbar("Cap Elasticsearch Rest Tools", true) {
                listOf(
                    CapAction(
                        I18nResources.getMessageDefault(
                            "elasticsearch.request.execute.text",
                            ElasticsearchI18n.REQUEST_EXECUTE_TEXT
                        ),
                        I18nResources.getMessageDefault(
                            "elasticsearch.request.execute.description",
                            ElasticsearchI18n.REQUEST_EXECUTE_DESCRIPTION
                        ),
                        Icons.Actions.EXECUTE_ICON
                    ) {
                        executeRequestManual()
                    }
                )
            })
        toolbar.border = JBUI.Borders.customLine(JBColor.border(), 0, 0, 1, 0)

        UIUtil.addUndoRedoActions(urlField.textEditor)

        bodyPanel.setText(elasticsearchRestFile.request.body)
        methodCombo.selectedItem = elasticsearchRestFile.request.method
        urlField.text = elasticsearchRestFile.request.path

        methodCombo.addItemListener {
            val selectedMethod = it.item as Method
            bodyPanel.isVisible = selectedMethod.hasBody
        }

        splitter = Splitter(true, 0.3f)
        splitter.divider.background = UIUtil.SIDE_PANEL_BACKGROUND

        splitter.firstComponent = bodyPanel
        splitter.secondComponent = resultPanel

        add(layoutNCS(toolbar, splitter))
    }

    override fun executeRequest() {
        if (file.autoCommit) {
            executeRequestManual()
        }
    }

    private fun executeRequestManual() {
        val request = getRequest()
        splitter.proportion = if (request.body.isNullOrEmpty()) 0.0f else 0.3f
        executeRequest(request)
    }

    fun updateAndExecuteLastSearchRequest(sortModel: CapSortModel): Collection<out CapTableItem> {
        updateLastSearchRequest(sortModel)
        return executeAndProcessLastSearchRequest()
    }

    fun updateAndExecuteLastSearchRequest(pageParameter: CapPageParameter): Collection<out CapTableItem> {
        updateLastSearchRequest(pageParameter)
        return executeAndProcessLastSearchRequest()
    }

    private fun getMethod(): Method {
        return methodCombo.selectedItem as Method
    }

    private fun getRequest(): Request {
        val body = if (getMethod().hasBody) {
            bodyPanel.getText()
        } else {
            ""
        }
        return Request(
            host = file.clusterProperties[ElasticsearchConstant.CLUSTER_PROPS_URL],
            path = urlField.text,
            body = body,
            method = getMethod()
        )
    }

    private fun updateFromRequest(request: Request) {
        urlField.text = request.path
        methodCombo.selectedItem = request.method
        bodyPanel.setText(request.body)
    }

    private fun isSearchRequest(request: Request): Boolean {
        return request.path.contains("_search")
    }

    private fun executeRequest(request: Request) {
        executeRequest(request) {
            updateFromRequest(it.request)
            resultPanel.updateResult(it)
        }
    }

    private fun executeRequest(
        request: Request,
        responseListener: Listener<ResponseContext>
    ) {
        val mappingExecution = if (isSearchRequest(request)) {
            lastSearchRequest = request
            val mappingUrl = "${request.path.substring(0, request.path.indexOf("_search"))}/_mapping"
            val mappingRequest = Request(
                host = request.host,
                path = mappingUrl,
                method = Method.GET
            )
            file.elasticsearchClient.prepareExecutionRestResponse(mappingRequest, false)
        } else {
            null
        }
        val requestExecution = file.elasticsearchClient.prepareExecutionRestResponse(request, false)

        WriteCommandAction.runWriteCommandAction(project) {
            UIUtil.invokeLaterIfNeeded {
                resultPanel.startLoading()
                CapTaskExecution(execution = {
                    val mappingFuture = mappingExecution?.executeOnPooledThread()
                    val response = requestExecution.execute()
                    if (mappingFuture != null) {
                        val mappingResponse = mappingFuture.get()
                        Pair<ElasticsearchResponse, ElasticsearchResponse>(response, mappingResponse)
                    } else {
                        Pair(response, null)
                    }
                }).success {
                    val requestAndResponse =
                        RestResponseContext(
                            request = request,
                            response = it.first
                        )
                    requestAndResponse.mappingResponse = it.second?.content
                    responseListener.invoke(requestAndResponse)
                }.failure {
                    showErrorDialog(it.message ?: "", it)
                }.finally { _, _ ->
                    UIUtil.invokeLaterIfNeeded {
                        resultPanel.stopLoading()
                    }
                }.executeQuietly()
                resultPanel.stopLoading()
            }
        }
    }

    private fun updateLastSearchRequest(pageParameter: CapPageParameter) {
        lastSearchRequest = when (lastSearchRequest.method) {
            Method.GET,
            Method.POST -> {
                val body = if (lastSearchRequest.body.isBlank()) "{}" else lastSearchRequest.body
                val objectNode = ObjectMapper.jsonMapper.readTree(body) as ObjectNode

                if (pageParameter.pageStart >= 0) {
                    objectNode.put("from", pageParameter.pageStart)
                } else {
                    objectNode.remove("from")
                }
                if (pageParameter.pageSize >= 0) {
                    objectNode.put("size", pageParameter.pageSize)
                } else {
                    objectNode.remove("size")
                }
                val uriBuilder = URIBuilder(lastSearchRequest.path)
                val params = uriBuilder.queryParams.asSequence()
                    .filter { it.name != "from" }
                    .filter { it.name != "size" }
                    .toList()
                uriBuilder.setParameters(params)
                lastSearchRequest.copy(
                    body = ObjectMapper.jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode),
                    path = uriBuilder.toString()
                )
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    private fun updateLastSearchRequest(sortModel: CapSortModel) {
        lastSearchRequest = when (lastSearchRequest.method) {
            Method.GET,
            Method.POST -> {
                val body = if (lastSearchRequest.body.isBlank()) "{}" else lastSearchRequest.body
                val objectNode = ObjectMapper.jsonMapper.readTree(body) as ObjectNode

                if (sortModel.getSortFields().isNotEmpty()) {
                    val sorts = ObjectMapper.jsonMapper.createArrayNode()
                    sortModel.getSortFields().forEach {
                        sorts.add(it.jsonNode)
                    }
                    objectNode.set("sort", sorts)
                } else {
                    objectNode.remove("sort")
                }
                lastSearchRequest.copy(
                    body = ObjectMapper.jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode),
                    path = lastSearchRequest.path
                )
            }
            else -> {
                throw IllegalArgumentException()
            }
        }
    }

    private fun executeAndProcessLastSearchRequest(): Collection<out CapTableItem> {
        var result: Collection<out CapTableItem> = emptyList()
        executeRequest(lastSearchRequest) {
            updateFromRequest(it.request)
            result = resultPanel.updateResult(it)
        }
        return result
    }

    override fun dispose() {
    }
}