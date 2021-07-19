package com.cap.plugins.elasticsearch.ui.explorer

import com.cap.plugins.common.action.CapAction
import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.common.component.CapMenu
import com.cap.plugins.common.component.CapPopupMenu
import com.cap.plugins.common.constant.CapCommonConstant
import com.cap.plugins.common.constant.CapCommonI18n
import com.cap.plugins.common.http.model.Method
import com.cap.plugins.common.http.model.Request
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.common.service.CapActionManager
import com.cap.plugins.common.ui.explorer.CapClusterToolWindowExplorer
import com.cap.plugins.common.ui.explorer.table.CapExplorerTableNode
import com.cap.plugins.common.ui.explorer.tree.CapClusterTreeFolderNode
import com.cap.plugins.common.ui.explorer.tree.CapClusterTreeRootNode
import com.cap.plugins.common.ui.explorer.tree.CapExplorerTreeNode
import com.cap.plugins.common.util.*
import com.cap.plugins.elasticsearch.client.ElasticsearchClient
import com.cap.plugins.elasticsearch.config.ElasticsearchConfiguration
import com.cap.plugins.elasticsearch.config.ElasticsearchPersistentStateSetting
import com.cap.plugins.elasticsearch.constant.ElasticsearchConstant
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.icon.ElasticsearchIcons
import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlFileType
import com.cap.plugins.elasticsearch.ui.editor.kql.ElasticsearchKqlFileSystem
import com.cap.plugins.elasticsearch.ui.editor.rest.ElasticsearchRestFile
import com.cap.plugins.elasticsearch.ui.editor.rest.ElasticsearchRestFileSystem
import com.cap.plugins.elasticsearch.ui.editor.sql.ElasticsearchSqlFile
import com.cap.plugins.elasticsearch.ui.editor.sql.ElasticsearchSqlFileSystem
import com.cap.plugins.elasticsearch.ui.editor.sql.model.ElasticsearchSqlQueryType
import com.cap.plugins.elasticsearch.ui.explorer.dialog.ElasticsearchClusterDialog
import com.cap.plugins.elasticsearch.ui.explorer.dialog.ElasticsearchIndexDialog
import com.cap.plugins.elasticsearch.ui.explorer.tree.CellRenderer
import com.cap.plugins.elasticsearch.ui.explorer.tree.ElasticsearchClusterRootTreeNode
import com.cap.plugins.elasticsearch.ui.explorer.tree.ElasticsearchIndexTreeNode
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.testFramework.LightVirtualFile
import com.intellij.ui.ScrollPaneFactory
import com.intellij.util.EditSourceOnDoubleClickHandler
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import javax.swing.tree.TreeNode
import javax.swing.tree.TreePath

@Keep
class ElasticsearchClusterToolWindowExplorer(project: Project) :
    CapClusterToolWindowExplorer(project, "Elasticsearch") {

    val config = service<ElasticsearchPersistentStateSetting>()

    init {
        createToolbar()
        createTree()
        setContent(layoutNCS(getSearchTextField(), ScrollPaneFactory.createScrollPane(tree)))
    }

    /**
     * 创建工具栏
     */
    override fun createToolbar() {
        toolbar = CapActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, true) {
            listOf(
                CapAction(
                    I18nResources.getMessageDefault("action.cluster.add", CapCommonI18n.ACTION_CLUSTER_ADD),
                    Icons.Actions.ADD_CLUSTER_ICON
                ) {
                    val menu = CapPopupMenu()

                    menu.add(
                        I18nResources.getMessageDefault(
                            "action.cluster.add.elasticsearch",
                            ElasticsearchI18n.CLUSTER_ADD_ELASTICSEARCH
                        ), ElasticsearchIcons.ELASTICSEARCH_ICON
                    ) {
                        val dialog = ElasticsearchClusterDialog(null)
                        dialog.show()
                        if (dialog.isOK) {
                            val props = dialog.getCluster()
                            background(
                                project,
                                I18nResources.getMessageDefault(
                                    "background.progress.title.cluster.add",
                                    CapCommonI18n.BACKGROUND_PROGRESS_TITLE_CLUSTER_ADD,
                                    "Elasticsearch",
                                    props[CapCommonConstant.CLUSTER_NAME]
                                ),
                                taskQueue
                            ) {
                                treeRoot.add(ElasticsearchClusterRootTreeNode(project, dialog.getCluster()))
                                treeModel.reload(treeRoot)
                                config.addElasticsearchCluster(props)
                            }
                        }
                    }
                    menu.show(toolbar, 0 * 26 + 2, toolbar!!.height)
                },
                CapAction(I18nResources.getMessageDefault("action.cluster.edit", CapCommonI18n.ACTION_CLUSTER_EDIT),
                    I18nResources.getMessageDefault(
                        "action.cluster.edit.description",
                        CapCommonI18n.ACTION_CLUSTER_EDIT_DESCRIPTION
                    ),
                    Icons.Actions.EDIT_SOURCE_ICON,
                    {
                        clusterTreeRootNodeSelected()
                    }) {
                    val clusterNode = tree.selectionPaths[0].lastPathComponent as CapClusterTreeRootNode
                    val dialog = ElasticsearchClusterDialog(ElasticsearchConfiguration().also {
                        it.putAll(config.elasticsearchClusters[clusterNode.clusterProperties[CapCommonConstant.CLUSTER_NAME]!!]!!)
                    })
                    dialog.show()
                    if (dialog.isOK) {
                        val props = dialog.getCluster()
                        background(
                            project,
                            I18nResources.getMessageDefault(
                                "background.progress.title.cluster.edit",
                                CapCommonI18n.BACKGROUND_PROGRESS_TITLE_CLUSTER_EDIT,
                                "Elasticsearch",
                                props[CapCommonConstant.CLUSTER_NAME]
                            ),
                            taskQueue
                        ) {
                            clusterNode.clusterProperties.putAll(props)
                            config.addElasticsearchCluster(props)
                            clusterNode.disconnect()
                            clusterNode.refresh()
                        }
                    }
                },
                removeClusterAction,
                refreshAction,
                Separator.create(),
                CapAction("Plugins", Icons.General.PLUGIN_ICON, { hasClusterNode() }) {
                    val menu = CapPopupMenu()
                    menu.add("nlpchina-sql", Icons.General.QUERY_EDITOR_ICON) {
                        openSqlEditor(null, ElasticsearchSqlQueryType.NLP_ELASTICSEARCH_SQL)
                    }
                    menu.add("opendistro-sql", Icons.General.QUERY_EDITOR_ICON) {
                        openSqlEditor(null, ElasticsearchSqlQueryType.OPENDISTRO_ELASTICSEARCH_SQL)
                    }

                    menu.show(toolbar, 4 * 26 + 2 - 1 * 19, toolbar!!.height)
                },
                CapAction("Kibana", ElasticsearchIcons.KIBANA_ICON, { hasClusterNode() }) {
                    val menu = CapPopupMenu()
                    menu.add(
                        CapMenu(
                            I18nResources.getMessageDefault(
                                "action.kibana.dev.tools",
                                ElasticsearchI18n.KIBANA_DEV_TOOLS
                            ), ElasticsearchIcons.KIBANA_ICON
                        ).also { menu ->
                            menu.add(
                                I18nResources.getMessageDefault(
                                    "action.kibana.dev.tools.console",
                                    ElasticsearchI18n.KIBANA_DEV_TOOLS_CONSOLE
                                )
                            ) {
                                openKqlDevToolConsole(null)
                            }
                        }
                    )

                    menu.show(toolbar, 5 * 26 + 2 - 1 * 19, toolbar!!.height)
                },
                CapAction("X-Pack", ElasticsearchIcons.X_PACK_ICON, { hasClusterNode() }) {
                    val menu = CapPopupMenu()
                    menu.add(
                        "x-pack-sql", Icons.General.QUERY_EDITOR_ICON
                    ) {
                        openSqlEditor(null, ElasticsearchSqlQueryType.X_PACK_SQL)
                    }

                    menu.show(toolbar, 6 * 26 + 2 - 1 * 19, toolbar!!.height)
                },
                Separator.create()
            )
        }
    }

    /**
     * 创建树
     */
    override fun createTree() {
        super.createTree()

        tree.emptyText.text = "Add Elasticsearch cluster"
        tree.cellRenderer = CellRenderer()

        // 加载保存的集群
        config.elasticsearchClusters.forEach {
            val configuration = ElasticsearchConfiguration()
            configuration.putAll(it.value)
            treeRoot.add(ElasticsearchClusterRootTreeNode(project, configuration))
        }

        // 展开树
        tree.expandPath(TreePath(treeRoot))

        // 添加鼠标右键事件
        tree.addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    // 一定要选中了节点进行右键才会弹出菜单
                    val paths = tree.selectionPaths
                    if (paths.isNullOrEmpty()) {
                        return
                    }

                    // 右键菜单
                    val menu = CapPopupMenu()

                    // 选中单个节点的时候才显示以下菜单
                    if (paths.size == 1) {
                        // 获取选中的第一个节点
                        val path = paths.first()
                        // 获取选中节点的最后一个组件
                        val last = path.lastPathComponent as CapExplorerTreeNode
                        // 第一个节点的选中路径的第1段是集群的根节点
                        val clusterNode = path.path[1] as ElasticsearchClusterRootTreeNode

                        // 添加公共菜单
                        // 添加刷新菜单
                        menu.add(
                            I18nResources.getMessageDefault("action.refresh", CapCommonI18n.ACTION_REFRESH),
                            Icons.Actions.REFRESH_ICON
                        ) {
                            refreshNode(last)
                        }

                        // 集群根菜单
                        if (last is ElasticsearchClusterRootTreeNode) {
                            menu.add(JSeparator())
                            if (!last.isconnect()) {
                                // 连接菜单
                                menu.add(
                                    I18nResources.getMessageDefault(
                                        "action.cluster.connect",
                                        CapCommonI18n.ACTION_CLUSTER_CONNECT
                                    ),
                                    Icons.Actions.CLUSTER_CONNECT_ICON
                                ) {
                                    refreshNode(last)
                                }
                            }
                            if (last.isconnect()) {
                                // 断开连接菜单
                                menu.add(
                                    I18nResources.getMessageDefault(
                                        "action.cluster.disconnect",
                                        CapCommonI18n.ACTION_CLUSTER_DISCONNECT
                                    ),
                                    Icons.Actions.CLUSTER_DISCONNECT_ICON
                                ) {
                                    tree.collapsePath(path)
                                    last.disconnect()
                                    treeModel.reload(last)
                                }
                            }
                            // 重新连接菜单
                            menu.add(
                                I18nResources.getMessageDefault(
                                    "action.cluster.reconnect",
                                    CapCommonI18n.ACTION_CLUSTER_RECONNECT
                                ),
                                Icons.Actions.CLUSTER_RECONNECT_ICON
                            ) {
                                last.disconnect()
                                refreshNode(last)
                            }
                            // 移除集群菜单
                            menu.add(
                                I18nResources.getMessageDefault(
                                    "action.cluster.delete",
                                    CapCommonI18n.ACTION_CLUSTER_DELETE
                                ), Icons.Actions.REMOVE_ICON
                            ) {
                                removeCluster()
                            }

                            menu.add(JSeparator())
                            CapMenu("Plugins", Icons.General.PLUGIN_ICON).also {
                                it.add(
                                    "nlpchina-sql", Icons.General.QUERY_EDITOR_ICON
                                ) {
                                    openSqlEditor(
                                        clusterNode.clusterProperties,
                                        ElasticsearchSqlQueryType.NLP_ELASTICSEARCH_SQL
                                    )
                                }
                                it.add("opendistro-sql", Icons.General.QUERY_EDITOR_ICON) {
                                    openSqlEditor(
                                        clusterNode.clusterProperties,
                                        ElasticsearchSqlQueryType.OPENDISTRO_ELASTICSEARCH_SQL
                                    )
                                }
                            }.also {
                                menu.add(it)
                            }
                            CapMenu("Kibana", ElasticsearchIcons.KIBANA_ICON).also {
                                it.add(
                                    CapMenu(
                                        I18nResources.getMessageDefault(
                                            "action.kibana.dev.tools",
                                            ElasticsearchI18n.KIBANA_DEV_TOOLS
                                        ), ElasticsearchIcons.KIBANA_ICON
                                    ).also { menu ->
                                        menu.add(
                                            I18nResources.getMessageDefault(
                                                "action.kibana.dev.tools.console",
                                                ElasticsearchI18n.KIBANA_DEV_TOOLS_CONSOLE
                                            )
                                        ) {
                                            openKqlDevToolConsole(clusterNode.clusterProperties)
                                        }
                                    }
                                )
                            }.also {
                                menu.add(it)
                            }
                            CapMenu("X-Pack", ElasticsearchIcons.X_PACK_ICON).also {
                                it.add("x-pack") {
                                    openRestEditor(
                                        clusterProperties = clusterNode.clusterProperties,
                                        elasticsearchClient = clusterNode.elasticsearchClient!!,
                                        request = Request(
                                            path = "/_xpack",
                                            method = Method.GET
                                        ),
                                        autoCommit = true
                                    )
                                }
                                it.add(
                                    "x-pack-sql", Icons.General.QUERY_EDITOR_ICON
                                ) {
                                    openSqlEditor(clusterNode.clusterProperties, ElasticsearchSqlQueryType.X_PACK_SQL)
                                }
                            }.also {
                                menu.add(it)
                            }

                            menu.add(JSeparator())
                            val clusterMenu = CapMenu("Cluster API")
                            menu.add(clusterMenu)
                            // 可以转换成表格的
                            mutableMapOf(
                                "/" to "Cluster Info",
                                "/_cluster/health" to "Cluster Health",
                            ).forEach { entry ->
                                clusterMenu.add(
                                    name = entry.value,
                                    text = entry.value,
                                    description = entry.value
                                ) {
                                    openRestEditor(
                                        clusterProperties = clusterNode.clusterProperties,
                                        elasticsearchClient = clusterNode.elasticsearchClient!!,
                                        request = Request(
                                            path = entry.key,
                                            method = Method.GET
                                        ),
                                        autoCommit = true
                                    )
                                }
                            }
                            clusterMenu.add(JSeparator())
                            // 不用转换成表格的
                            mutableMapOf(
                                "/_cluster/settings" to "Cluster Settings",
                                "/_cluster/state" to "Cluster State",
                                "/_cluster/stats" to "Cluster Stats",
                                "/_template" to "Templates"
                            ).forEach { entry ->
                                clusterMenu.add(
                                    name = entry.value,
                                    text = entry.value,
                                    description = entry.value
                                ) {
                                    openRestEditor(
                                        clusterProperties = clusterNode.clusterProperties,
                                        elasticsearchClient = clusterNode.elasticsearchClient!!,
                                        request = Request(
                                            path = entry.key,
                                            method = Method.GET
                                        ),
                                        autoCommit = true
                                    )
                                }
                            }
                        }

                        if (last.parent is ElasticsearchClusterRootTreeNode) {
                            last as CapClusterTreeFolderNode
                            if (last.userObject == ElasticsearchConstant.NODES) {
                                val nodesMenu = CapMenu("Nodes API")
                                menu.add(nodesMenu)

                                mutableMapOf(
                                    "/_nodes" to "Nodes Info",
                                    "/_nodes/stats" to "Nodes Stats",
                                    "/_nodes/plugins" to "Nodes Plugins"
                                ).forEach { entry ->
                                    nodesMenu.add(
                                        name = entry.value,
                                        text = entry.value,
                                        description = entry.value
                                    ) {
                                        openRestEditor(
                                            clusterProperties = clusterNode.clusterProperties,
                                            elasticsearchClient = clusterNode.elasticsearchClient!!,
                                            request = Request(
                                                path = entry.key,
                                                method = Method.GET
                                            ),
                                            autoCommit = true
                                        )
                                    }
                                }
                            }
                            if (last.userObject == ElasticsearchConstant.INDICES) {
                                val indicesMenu = CapMenu("Indices API")
                                menu.add(indicesMenu)
                                // 需要打开编辑器的
                                mutableMapOf(
                                    "/_alias" to "Index Alias",
                                    "/_mapping" to "Index Mapping",
                                    "/_settings" to "Index Settings"
                                ).forEach { entry ->
                                    indicesMenu.add(entry.value) {
                                        openRestEditor(
                                            clusterProperties = clusterNode.clusterProperties,
                                            elasticsearchClient = clusterNode.elasticsearchClient!!,
                                            request = Request(
                                                path = entry.key,
                                                method = Method.GET
                                            ),
                                            autoCommit = true
                                        )
                                    }
                                }

                                // 独立的菜单
                                // 弹出对话框的
                                menu.add(JSeparator())
                                menu.add("Create index") { name ->
                                    val dialog = ElasticsearchIndexDialog(project)
                                    dialog.show()
                                    if (dialog.isOK) {
                                        val indexName = dialog.indexName.text
                                        val body = dialog.bodyPanel.getText()
                                        clusterNode.elasticsearchClient!!.prepareExecutionResponse(
                                            request = Request(
                                                host = clusterNode.clusterProperties[ElasticsearchConstant.CLUSTER_PROPS_URL],
                                                path = "/${indexName}",
                                                body = body,
                                                method = Method.PUT
                                            ), check = false
                                        ).success {
                                            notify(
                                                "$name for \"${indexName}\" success.\n${it.content}",
                                                NotificationType.INFORMATION
                                            )
                                            refreshNodeOnly(last)
                                        }.failure {
                                            notify(
                                                "$name for \"${indexName}\" failure.\n${it.message}",
                                                NotificationType.ERROR
                                            )
                                        }.execute()
                                    }
                                }
                                menu.add("Force merge all index") { name ->
                                    Messages.showInputDialog(
                                        I18nResources.getMessageDefault(
                                            "action.index.force.merge.segments",
                                            ElasticsearchI18n.ACTION_INDEX_FORCE_MERGE_SEGMENTS
                                        ),
                                        name,
                                        Messages.getQuestionIcon(),
                                        "1",
                                        INT_VALIDATOR
                                    )?.run {
                                        executeAndNotify(
                                            clusterNode,
                                            "/_forcemerge?max_num_segments=${this}",
                                            Method.POST,
                                            name,
                                            last.userObject
                                        ) {
                                            refreshNodeOnly(last)
                                        }
                                    }
                                }
                                menu.add(JSeparator())
                                mutableMapOf(
                                    "/_cache/clear" to "Clear all index cache",
                                    "/_flush" to "Flush all index",
                                    "/_refresh" to "Refresh all index",
                                ).forEach { entry ->
                                    menu.add(entry.value) {
                                        executeConfirmAndNotify(
                                            clusterNode,
                                            entry.key,
                                            Method.POST,
                                            entry.value,
                                            last.userObject
                                        ) {
                                            refreshNode(last)
                                        }
                                    }
                                }
                            }
                        }

                        if (last is ElasticsearchIndexTreeNode) {
                            // 需要打开编辑器的
                            menu.add(JSeparator())
                            mutableMapOf(
                                "/${last.userObject}" to "Index Info",
                                "/${last.userObject}/_stats" to "Index Stats"
                            ).forEach { entry ->
                                menu.add(entry.value) {
                                    openRestEditor(
                                        clusterProperties = clusterNode.clusterProperties,
                                        elasticsearchClient = clusterNode.elasticsearchClient!!,
                                        request = Request(
                                            path = entry.key,
                                            method = Method.GET
                                        ),
                                        autoCommit = true
                                    )
                                }
                            }

                            // 弹出对话框的
                            menu.add(JSeparator())
                            menu.add("Clone index") { name ->
                                Messages.showInputDialog(
                                    I18nResources.getMessageDefault(
                                        "action.index.clone.message",
                                        ElasticsearchI18n.ACTION_INDEX_CLONE_MESSAGE
                                    ),
                                    name,
                                    Messages.getQuestionIcon(),
                                    "",
                                    IDENTIFIER_VALIDATOR
                                )?.run {
                                    executeAndNotify(
                                        clusterNode,
                                        "/${last.userObject}/_clone/${this}",
                                        Method.PUT,
                                        name,
                                        last.userObject
                                    ) {
                                        refreshNodeOnly(clusterNode.getIndices())
                                    }
                                }
                            }
                            menu.add("Create index alias") { name ->
                                Messages.showInputDialog(
                                    I18nResources.getMessageDefault(
                                        "action.index.new.alias.message",
                                        ElasticsearchI18n.ACTION_INDEX_NEW_ALIAS_MESSAGE
                                    ),
                                    name,
                                    Messages.getQuestionIcon(),
                                    "",
                                    IDENTIFIER_VALIDATOR
                                )?.run {
                                    executeAndNotify(
                                        clusterNode,
                                        "/${last.userObject}/_alias/${this}",
                                        Method.PUT,
                                        name,
                                        last.userObject
                                    ) {
                                        refreshNodeOnly(clusterNode.getAliases())
                                    }
                                }
                            }
                            menu.add("Force merge index") { name ->
                                Messages.showInputDialog(
                                    I18nResources.getMessageDefault(
                                        "action.index.force.merge.segments",
                                        ElasticsearchI18n.ACTION_INDEX_FORCE_MERGE_SEGMENTS
                                    ),
                                    name,
                                    Messages.getQuestionIcon(),
                                    "1",
                                    INT_VALIDATOR
                                )?.run {
                                    executeAndNotify(
                                        clusterNode,
                                        "/${last.userObject}/_forcemerge?max_num_segments=${this}",
                                        Method.POST,
                                        name,
                                        last.userObject
                                    ) {
                                        refreshNodeOnly(clusterNode.getIndices())
                                    }
                                }
                            }

                            // 直接发送后台请求的，需要提示
                            menu.add(JSeparator())
                            mutableMapOf(
                                "/${last.userObject}/_cache/clear" to "Clear index cache"
                            ).also {
                                if (last.index.status.equals("open", true)) {
                                    it["/${last.userObject}/_close"] = "Close index"
                                }
                                if (last.index.status.equals("close", true)) {
                                    it["/${last.userObject}/_open"] = "Open index"
                                }

                                if (last.indexInfo?.settings?.index?.frozen == true) {
                                    it["/${last.userObject}/_unfreeze"] = "Unfreeze index"
                                }
                                if (last.indexInfo?.settings?.index?.frozen == false) {
                                    it["/${last.userObject}/_freeze"] = "Freeze index"
                                }
                            }.forEach { entry ->
                                menu.add(entry.value) {
                                    executeConfirmAndNotify(
                                        clusterNode,
                                        entry.key,
                                        Method.POST,
                                        entry.value,
                                        last.userObject
                                    ) {
                                        refreshNode(last)
                                    }
                                }
                            }
                            menu.add("Delete index") { name ->
                                executeConfirmAndNotify(
                                    clusterNode,
                                    "/${last.userObject}",
                                    Method.DELETE,
                                    name,
                                    last.userObject
                                ) {
                                    refreshNodeOnly(clusterNode.getIndices())
                                    refreshNodeOnly(clusterNode.getAliases())
                                }
                            }

                            // 直接发送后台请求的
                            menu.add(JSeparator())
                            mutableMapOf(
                                "/${last.userObject}/_flush" to "Flush index",
                                "/${last.userObject}/_refresh" to "Refresh index",
                            ).forEach { entry ->
                                menu.add(entry.value) {
                                    executeAndNotify(
                                        clusterNode,
                                        entry.key,
                                        Method.POST,
                                        entry.value,
                                        last.userObject
                                    ) {
                                        refreshNode(last)
                                    }
                                }
                            }
                        }
                    }

                    // 在鼠标位置显示菜单
                    menu.show(tree, e.x, e.y)
                }
            }
        })

        // 添加鼠标双击事件
        object : EditSourceOnDoubleClickHandler.TreeMouseListener(tree, null) {
            override fun processDoubleClick(
                e: MouseEvent,
                dataContext: DataContext,
                treePath: TreePath
            ) {
                val paths = tree.selectionPaths
                if (paths.isEmpty()) {
                    return
                }

                // 选中单个节点的时候才显示以下菜单
                if (paths.size == 1) {
                    // 获取选中的第一个节点
                    val path = paths.first()
                    // 获取选中节点的最后一个组件
                    val last = path.lastPathComponent as CapExplorerTreeNode
                    // 第一个节点的选中路径的第1段是集群的根节点
                    val clusterNode = path.path[1] as ElasticsearchClusterRootTreeNode
                    // 双击展开节点
                    last.expand()

                    if (last is ElasticsearchIndexTreeNode) {
                        openRestEditor(
                            clusterProperties = last.clusterProperties,
                            elasticsearchClient = clusterNode.elasticsearchClient!!,
                            request = Request(
                                path = "/${last.userObject}/_search",
                                body = "{\n  \"from\": 0,\n  \"size\": 10,\n  \"query\": {\n    \"match_all\": {}\n  }\n}",
                                method = Method.GET
                            ),
                            autoCommit = true
                        )
                    }
                }
            }
        }.installOn(tree)

        // 添加回车事件
        tree.registerKeyboardAction({
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED)

        // 树节点选中事件
        tree.addTreeSelectionListener {
            it?.newLeadSelectionPath?.lastPathComponent?.let { node ->
                val browserExplorer = project.service<ElasticsearchBrowserToolWindowExplorer>()

                val tabbedPane = JTabbedPane()

                tabbedPane.addTab(
                    I18nResources.getMessageDefault("tabbed.tab.properties", CapCommonI18n.TABBED_TAB_PROPERTIES),
                    browserExplorer.table
                )
                browserExplorer.tableModel.updateDetails(project, node as TreeNode)

                // 添加监听器
                browserExplorer.tableModel.listener = (node as CapExplorerTableNode).listener

                background(
                    project,
                    I18nResources.getMessageDefault(
                        "background.progress.title.node.properties.loading",
                        CapCommonI18n.BACKGROUND_PROGRESS_TITLE_NODE_PROPERTIES_LOADING,
                        "$node"
                    ),
                    taskQueue
                ) {
                    foreground {
                        browserExplorer.tableModel.updateDetails(project, node as TreeNode)
                    }
                }

                // 先清除所有组件
                browserExplorer.dispose()
                // 再增加组件
                browserExplorer.add(tabbedPane)
            }
        }
    }


    /**
     * 移除集群
     */
    override fun removeCluster(task: (() -> Unit)?) {
        val clusterNode = tree.selectionPaths[0].lastPathComponent as CapClusterTreeRootNode
        super.removeCluster {
            if (clusterNode is ElasticsearchClusterRootTreeNode) {
                config.removeElasticsearchCluster(clusterNode.userObject as ElasticsearchConfiguration)
            }
            // 更新属性列表
            project.service<ElasticsearchBrowserToolWindowExplorer>().table.dataList = emptyList()
        }
    }

    override fun refreshNode(node: CapExplorerTreeNode, task: (() -> Unit)?) {
        super.refreshNode(node) {
            project.service<ElasticsearchBrowserToolWindowExplorer>().tableModel.updateDetails(project, node)
        }
    }

    private fun refreshNodeOnly(node: CapExplorerTreeNode) {
        super.refreshNode(node) {
        }
    }

    private fun executeConfirmAndNotify(
        clusterNode: ElasticsearchClusterRootTreeNode,
        path: String,
        method: Method,
        operate: String,
        userObject: Any,
        task: (() -> Unit)
    ) {
        if (Messages.OK == Messages.showOkCancelDialog(
                I18nResources.getMessageDefault(
                    "action.index.operate.confirm",
                    ElasticsearchI18n.ACTION_INDEX_OPERATE_CONFIRM,
                    operate
                ),
                I18nResources.getMessageDefault(
                    "cap.plugin.title",
                    CapCommonI18n.CAP_PLUGIN_TITLE
                ),
                I18nResources.getMessageDefault(
                    "button.text.ok",
                    CapCommonI18n.BUTTON_TEXT_OK
                ),
                I18nResources.getMessageDefault(
                    "button.text.cancel",
                    CapCommonI18n.BUTTON_TEXT_CANCEL
                ),
                Messages.getQuestionIcon()
            )
        ) {
            executeAndNotify(clusterNode, path, method, operate, userObject, task)
        }
    }

    private fun executeAndNotify(
        clusterNode: ElasticsearchClusterRootTreeNode,
        path: String,
        method: Method,
        operate: String,
        userObject: Any,
        task: (() -> Unit)
    ) {
        clusterNode.elasticsearchClient!!.prepareExecutionResponse(
            request = Request(
                host = clusterNode.clusterProperties[ElasticsearchConstant.CLUSTER_PROPS_URL],
                path = path,
                method = method
            ), check = false
        ).success {
            notify(
                "$operate for \"${userObject}\" success.\n${it.content}",
                NotificationType.INFORMATION
            )
            task.invoke()
        }.failure {
            notify(
                "$operate for \"${userObject}\" failure.\n${it.message}",
                NotificationType.ERROR
            )
        }.execute()
    }

    private fun openRestEditor(
        clusterProperties: ElasticsearchConfiguration,
        elasticsearchClient: ElasticsearchClient,
        request: Request,
        autoCommit: Boolean
    ) {
        ElasticsearchRestFileSystem.INSTANCE?.openEditor(
            ElasticsearchRestFile(
                project = project,
                clusterProperties = clusterProperties,
                elasticsearchClient = elasticsearchClient,
                request = request
            ).also {
                it.autoCommit = autoCommit
            }
        )
    }

    private fun openSqlEditor(
        clusterProperties: ElasticsearchConfiguration?,
        queryType: ElasticsearchSqlQueryType
    ) {
        ElasticsearchSqlFileSystem.INSTANCE?.openEditor(
            ElasticsearchSqlFile(
                project = project,
                clusterProperties = clusterProperties,
                queryType = queryType
            )
        )
    }

    private fun openKqlDevToolConsole(
        clusterProperties: ElasticsearchConfiguration?
    ) {
        ElasticsearchKqlFileSystem.INSTANCE?.openDevTool(
            file = LightVirtualFile("DevTools Console", ElasticsearchKqlFileType.INSTANCE, ""),
            cluster = clusterProperties?.get(CapCommonConstant.CLUSTER_NAME)
        )
    }

    override fun dispose() {
    }
}