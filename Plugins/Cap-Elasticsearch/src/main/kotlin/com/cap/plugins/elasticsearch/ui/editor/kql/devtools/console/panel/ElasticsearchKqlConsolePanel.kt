package com.cap.plugins.elasticsearch.ui.editor.kql.devtools.console.panel

import com.cap.plugins.common.http.model.Method
import com.cap.plugins.common.http.model.Request
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.task.CapTaskExecution
import com.cap.plugins.common.util.*
import com.cap.plugins.elasticsearch.client.ElasticsearchClient
import com.cap.plugins.elasticsearch.client.ElasticsearchClients
import com.cap.plugins.elasticsearch.config.ElasticsearchConfiguration
import com.cap.plugins.elasticsearch.config.ElasticsearchPersistentStateSetting
import com.cap.plugins.elasticsearch.constant.ElasticsearchConstant
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.model.ElasticsearchResponse
import com.cap.plugins.elasticsearch.ui.editor.kql.panel.ElasticsearchKqlBodyPanel
import com.cap.plugins.elasticsearch.ui.editor.model.ResponseContext
import com.cap.plugins.elasticsearch.ui.editor.model.RestResponseContext
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchBodyPanel
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchPanel
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchResultPanel
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.Splitter
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import javax.swing.JPanel

class ElasticsearchKqlConsolePanel : ElasticsearchPanel {
    val project: Project

    private val config = service<ElasticsearchPersistentStateSetting>()

    var clusters: ComboBox<String>

    override var bodyPanel: ElasticsearchBodyPanel
    override var resultPanel: ElasticsearchResultPanel

    private val splitter: Splitter

    constructor(
        project: Project,
        file: VirtualFile,
        textEditor: TextEditor
    ) : super() {
        this.project = project

        this.bodyPanel =
            ElasticsearchKqlBodyPanel(project)
        this.resultPanel =
            ElasticsearchKqlConsoleResultPanel(
                project,
                this
            )

        val clusterPanel = JPanel()
        clusters = ComboBox(config.elasticsearchClusters.keys.toTypedArray())
        clusterPanel.addLabelled(
            I18nResources.getMessageDefault(
                "elasticsearch.cluster",
                ElasticsearchI18n.CLUSTER
            ), clusters
        )

        clusters.addActionListener {
            changeClient()
        }

        val toolbar = layoutWCE(clusterPanel)
        toolbar.border = JBUI.Borders.customLine(JBColor.border(), 0, 0, 1, 0)

        splitter = Splitter(true, 0.3f)
        splitter.divider.background = UIUtil.SIDE_PANEL_BACKGROUND

        splitter.firstComponent = textEditor.component
        splitter.secondComponent = resultPanel

        add(layoutNCS(toolbar, splitter))
        changeClient()
    }

    private fun changeClient() {
        if (!ElasticsearchClients.clients.containsKey(clusters.selectedItem.toString())) {
            val configuration = ElasticsearchConfiguration()
            configuration.putAll(config.elasticsearchClusters[clusters.selectedItem]!!)
            ElasticsearchClients.clients[clusters.selectedItem.toString()] = ElasticsearchClient(configuration)
        }
    }

    private fun isSearchRequest(request: Request): Boolean {
        return request.path.contains("_search")
    }

    private fun executeRequest(request: Request) {
        executeRequest(request) {
            resultPanel.updateResult(it)
        }
    }

    private fun executeRequest(
        request: Request,
        responseListener: Listener<ResponseContext>
    ) {
        val mappingExecution = if (isSearchRequest(request)) {
            val mappingUrl = "${request.path.substring(0, request.path.indexOf("_search"))}/_mapping"
            val mappingRequest = Request(
                host = request.host,
                path = mappingUrl,
                method = Method.GET
            )
            ElasticsearchClients.clients[clusters.selectedItem.toString()]!!.prepareExecutionRestResponse(
                mappingRequest,
                false
            )
        } else {
            null
        }
        val requestExecution =
            ElasticsearchClients.clients[clusters.selectedItem.toString()]!!.prepareExecutionRestResponse(
                request,
                false
            )

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
                    val requestAndResponse = RestResponseContext(
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

    fun executeRequest(method: String, url: String, body: String?) {
        executeRequest(
            Request(
                host = if (url.startsWith("/")) ElasticsearchClients.clients[clusters.selectedItem.toString()]!!.clusterProperties[ElasticsearchConstant.CLUSTER_PROPS_URL] else null,
                path = url,
                body = body ?: "",
                method = Method.valueOf(method)
            )
        )
    }

    fun openDocumentation(documentation: String) {
        (resultPanel as ElasticsearchKqlConsoleResultPanel).openDocumentation(documentation)
    }

    override fun executeRequest() {
    }

    override fun dispose() {
    }
}