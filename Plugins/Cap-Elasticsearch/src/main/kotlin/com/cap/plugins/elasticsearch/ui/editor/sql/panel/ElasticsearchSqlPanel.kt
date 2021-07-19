package com.cap.plugins.elasticsearch.ui.editor.sql.panel

import com.cap.plugins.common.action.CapAction
import com.cap.plugins.common.constant.CapCommonConstant
import com.cap.plugins.common.http.model.Method
import com.cap.plugins.common.http.model.Request
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.common.service.CapActionManager
import com.cap.plugins.common.task.CapTaskExecution
import com.cap.plugins.common.util.*
import com.cap.plugins.elasticsearch.client.ElasticsearchClient
import com.cap.plugins.elasticsearch.client.ElasticsearchClients
import com.cap.plugins.elasticsearch.config.ElasticsearchConfiguration
import com.cap.plugins.elasticsearch.config.ElasticsearchPersistentStateSetting
import com.cap.plugins.elasticsearch.constant.ElasticsearchConstant
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.ui.editor.model.SqlResponseContext
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchBodyPanel
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchPanel
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchResultPanel
import com.cap.plugins.elasticsearch.ui.editor.sql.ElasticsearchSqlFile
import com.cap.plugins.elasticsearch.ui.editor.sql.model.ElasticsearchSqlQueryType
import com.fasterxml.jackson.databind.node.ObjectNode
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.JBUI
import com.intellij.util.ui.UIUtil
import javax.swing.JPanel
import javax.swing.JTextField

class ElasticsearchSqlPanel : ElasticsearchPanel {
    val project: Project
    val file: ElasticsearchSqlFile

    private val config = service<ElasticsearchPersistentStateSetting>()

    var clusters: ComboBox<String>
    private var client: ElasticsearchClient? = null
    private var fetchSize = JTextField()
    private var submitSelectedOnly = JBCheckBox(
        I18nResources.getMessageDefault(
            "elasticsearch.sql.submit.selected.only",
            ElasticsearchI18n.SQL_SUBMIT_SELECTED_ONLY
        )
    )

    override var bodyPanel: ElasticsearchBodyPanel
    override var resultPanel: ElasticsearchResultPanel

    private val splitter: Splitter

    constructor(project: Project, file: ElasticsearchSqlFile) : super() {
        this.project = project
        this.file = file

        this.bodyPanel = ElasticsearchSqlBodyPanel(project)
        this.resultPanel = ElasticsearchSqlResultPanel(project, this)

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

        val fetchSizePanel = JPanel()
        fetchSize.inputVerifier = INT_NON_ZERO_VERIFIER
        fetchSize.text = "10"
        fetchSizePanel.addLabelled(
            I18nResources.getMessageDefault(
                "elasticsearch.sql.fetch.size",
                ElasticsearchI18n.SQL_FETCH_SIZE
            ), fetchSize
        )

        submitSelectedOnly.isSelected = true

        val explainAction = CapAction(
            I18nResources.getMessageDefault(
                "elasticsearch.request.explain.text",
                ElasticsearchI18n.REQUEST_EXPLAIN_TEXT
            ),
            I18nResources.getMessageDefault(
                "elasticsearch.request.explain.description",
                ElasticsearchI18n.REQUEST_EXPLAIN_DESCRIPTION
            ),
            Icons.Actions.PROFILE_ICON
        ) {
            explainRequest()
        }

        val executeAction = CapAction(
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
            executeRequest()
        }

        val toolbar = layoutWCE(
            layoutWCE(
                clusterPanel,
                if (file.queryType == ElasticsearchSqlQueryType.X_PACK_SQL) fetchSizePanel else null,
            ),
            layoutWCE(
                submitSelectedOnly,
                CapActionManager.getInstance().createActionToolbar("Cap Elasticsearch Sql Tools", true) {
                    if (file.queryType == ElasticsearchSqlQueryType.X_PACK_SQL) {
                        listOf(executeAction)
                    } else {
                        listOf(explainAction, executeAction)
                    }
                },
            )
        )
        toolbar.border = JBUI.Borders.customLine(JBColor.border(), 0, 0, 1, 0)

        splitter = Splitter(true, 0.3f)
        splitter.divider.background = UIUtil.SIDE_PANEL_BACKGROUND

        splitter.firstComponent = bodyPanel
        splitter.secondComponent = resultPanel

        add(layoutNCS(toolbar, splitter))
        file.clusterProperties?.let {
            clusters.selectedItem = it[CapCommonConstant.CLUSTER_NAME]
        }
        changeClient()
    }

    private fun changeClient() {
        if (!ElasticsearchClients.clients.containsKey(clusters.selectedItem.toString())) {
            val configuration = ElasticsearchConfiguration()
            configuration.putAll(config.elasticsearchClusters[clusters.selectedItem]!!)
            ElasticsearchClients.clients[clusters.selectedItem.toString()] = ElasticsearchClient(configuration)
        }
        client = ElasticsearchClients.clients[clusters.selectedItem.toString()]
    }

    private fun getExplainRequest(): Request {
        if (file.queryType == ElasticsearchSqlQueryType.NLP_ELASTICSEARCH_SQL) {
            return getNLPChinaExplainRequest()
        }
        if (file.queryType == ElasticsearchSqlQueryType.OPENDISTRO_ELASTICSEARCH_SQL) {
            return getOpenDistroExplainRequest()
        }
        throw RuntimeException()
    }

    private fun getNLPChinaExplainRequest(): Request {
        val query = if (submitSelectedOnly.isSelected) {
            bodyPanel.getSelectedText() ?: bodyPanel.getText()
        } else {
            bodyPanel.getText()
        }
        val objectNode = ObjectMapper.jsonMapper.readTree("{}") as ObjectNode
        objectNode.put("sql", query)

        return Request(
            host = config.elasticsearchClusters[clusters.selectedItem]!![ElasticsearchConstant.CLUSTER_PROPS_URL],
            path = if ((client?.config?.get(ElasticsearchClient.ELASTICSEARCH_SQL_PLUGIN_VERSION)?.toString()
                    ?: "") >= "7.5.0.0"
            ) {
                "/_nlpcn/sql/explain"
            } else {
                "/_sql/_explain"
            },
            body = ObjectMapper.jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode),
            method = Method.POST
        )
    }

    private fun getOpenDistroExplainRequest(): Request {
        val query = if (submitSelectedOnly.isSelected) {
            bodyPanel.getSelectedText() ?: bodyPanel.getText()
        } else {
            bodyPanel.getText()
        }
        val objectNode = ObjectMapper.jsonMapper.readTree("{}") as ObjectNode
        objectNode.put("query", query)

        return Request(
            host = config.elasticsearchClusters[clusters.selectedItem]!![ElasticsearchConstant.CLUSTER_PROPS_URL],
            path = "/_opendistro/_sql/_explain",
            body = ObjectMapper.jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode),
            method = Method.POST
        )
    }

    private fun getExecuteRequest(): Request {
        if (file.queryType == ElasticsearchSqlQueryType.NLP_ELASTICSEARCH_SQL) {
            return getNLPChinaQueryRequest()
        }
        if (file.queryType == ElasticsearchSqlQueryType.OPENDISTRO_ELASTICSEARCH_SQL) {
            return getOpenDistroQueryRequest()
        }
        if (file.queryType == ElasticsearchSqlQueryType.X_PACK_SQL) {
            return getXPackQueryRequest()
        }
        throw RuntimeException()
    }

    private fun getNLPChinaQueryRequest(): Request {
        val query = if (submitSelectedOnly.isSelected) {
            bodyPanel.getSelectedText() ?: bodyPanel.getText()
        } else {
            bodyPanel.getText()
        }
        val objectNode = ObjectMapper.jsonMapper.readTree("{}") as ObjectNode
        objectNode.put("sql", query)

        return Request(
            host = config.elasticsearchClusters[clusters.selectedItem]!![ElasticsearchConstant.CLUSTER_PROPS_URL],
            path = if ((client?.config?.get(ElasticsearchClient.ELASTICSEARCH_SQL_PLUGIN_VERSION)?.toString()
                    ?: "") >= "7.5.0.0"
            ) {
                "/_nlpcn/sql"
            } else {
                "/_sql"
            },
            body = ObjectMapper.jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode),
            method = Method.POST
        )
    }

    private fun getOpenDistroQueryRequest(): Request {
        val query = if (submitSelectedOnly.isSelected) {
            bodyPanel.getSelectedText() ?: bodyPanel.getText()
        } else {
            bodyPanel.getText()
        }
        val objectNode = ObjectMapper.jsonMapper.readTree("{}") as ObjectNode
        objectNode.put("query", query)

        return Request(
            host = config.elasticsearchClusters[clusters.selectedItem]!![ElasticsearchConstant.CLUSTER_PROPS_URL],
            path = "/_opendistro/_sql",
            body = ObjectMapper.jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode),
            method = Method.POST
        )
    }

    private fun getXPackQueryRequest(): Request {
        val query = if (submitSelectedOnly.isSelected) {
            bodyPanel.getSelectedText() ?: bodyPanel.getText()
        } else {
            bodyPanel.getText()
        }

        val objectNode = ObjectMapper.jsonMapper.readTree("{}") as ObjectNode
        objectNode.put("query", query)

        if (!fetchSize.text.isNullOrEmpty()) {
            objectNode.put("fetch_size", fetchSize.text.toInt())
        }

        return Request(
            host = config.elasticsearchClusters[clusters.selectedItem]!![ElasticsearchConstant.CLUSTER_PROPS_URL],
            path = if ((client?.config?.get(ElasticsearchClient.ELASTICSEARCH_VERSION)?.toString()?.let {
                    it.split(".").subList(0, 2).joinToString(".")
                } ?: "current") <= "7.5") {
                "/_xpack/sql?format=json"
            } else {
                "/_sql?format=json"
            },
            body = ObjectMapper.jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectNode),
            method = Method.POST
        )
    }

    override fun executeRequest() {
        executeRequest(getExecuteRequest()) {
            resultPanel.updateResult(it)
        }
    }

    fun explainRequest() {
        executeRequest(getExplainRequest()) {
            resultPanel.updateResult(it)
        }
    }

    private fun executeRequest(
        request: Request,
        responseListener: Listener<SqlResponseContext>
    ) {
        WriteCommandAction.runWriteCommandAction(project) {
            UIUtil.invokeLaterIfNeeded {
                resultPanel.startLoading()
                CapTaskExecution(execution = {
                    val elasticsearchClient = ElasticsearchClients.clients[clusters.selectedItem.toString()]!!
                    elasticsearchClient.prepareExecutionResponse(request, false).execute()
                }).success {
                    val requestAndResponse =
                        SqlResponseContext(
                            request = request,
                            response = it
                        )
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

    override fun dispose() {
    }
}