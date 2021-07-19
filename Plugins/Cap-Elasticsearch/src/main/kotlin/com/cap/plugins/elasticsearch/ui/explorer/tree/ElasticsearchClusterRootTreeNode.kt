package com.cap.plugins.elasticsearch.ui.explorer.tree

import com.cap.plugins.common.constant.CapCommonConstant
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.ui.explorer.tree.CapClusterTreeFolderNode
import com.cap.plugins.common.ui.explorer.tree.CapClusterTreeNode
import com.cap.plugins.common.ui.explorer.tree.CapClusterTreeRootNode
import com.cap.plugins.elasticsearch.client.ElasticsearchClient
import com.cap.plugins.elasticsearch.client.ElasticsearchClients
import com.cap.plugins.elasticsearch.config.ElasticsearchConfiguration
import com.cap.plugins.elasticsearch.config.ElasticsearchPersistentStateSetting
import com.cap.plugins.elasticsearch.constant.ElasticsearchConstant
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.model.cluster.ClusterHealth
import com.cap.plugins.elasticsearch.ui.explorer.ElasticsearchBrowserToolWindowExplorer
import com.cap.plugins.elasticsearch.ui.explorer.ElasticsearchClusterToolWindowExplorer
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import javax.swing.event.TableModelEvent
import javax.swing.tree.DefaultMutableTreeNode

class ElasticsearchClusterRootTreeNode(
    private val project: Project,
    override val clusterProperties: ElasticsearchConfiguration
) : CapClusterTreeRootNode(clusterProperties) {

    var elasticsearchClient: ElasticsearchClient? = null
    var health: ClusterHealth? = null
    var status: String? = null

    override fun connect() {
        refresh()
    }

    override fun isconnect(): Boolean {
        return elasticsearchClient != null
    }

    override fun disconnect() {
        elasticsearchClient?.close()
        ElasticsearchClients.clients.remove(clusterProperties[CapCommonConstant.CLUSTER_NAME]!!)
        elasticsearchClient = null
        health = null
        status = null
        removeAllChildren()
        add(loadingNode)
    }

    override fun reconnect() {
        refresh()
    }

    override fun refresh() {
        disconnect()
        super.refresh()
    }

    override fun expand() {
        if (elasticsearchClient == null) {
            elasticsearchClient = ElasticsearchClient(clusterProperties)
            ElasticsearchClients.clients[clusterProperties[CapCommonConstant.CLUSTER_NAME]!!] = elasticsearchClient
        }
        health = elasticsearchClient?.clusterHealth()
        status = health?.status
        super.expand()
    }

    override var listener: ((e: TableModelEvent) -> Unit)?
        get() = {
            if (it.type == TableModelEvent.UPDATE) {
                val explorer = project.service<ElasticsearchBrowserToolWindowExplorer>()
                val tableModel = explorer.tableModel
                val config = service<ElasticsearchPersistentStateSetting>()

                val key = tableModel.getValueAt(it.firstRow, 1) as String
                val value = tableModel.getValueAt(it.firstRow, it.column) as String

                val clusterConfig =
                    (explorer.tableModel.currentNode as ElasticsearchClusterRootTreeNode).clusterProperties

                var clusterName = clusterConfig[CapCommonConstant.CLUSTER_NAME] as String

                if (key == CapCommonConstant.CLUSTER_NAME) {
                    // 集群名称
                    clusterConfig[CapCommonConstant.CLUSTER_NAME] = value
                    // 移除原有名称的集群
                    config.elasticsearchClusters.remove(clusterName)
                    // 新增现有名称的集群
                    config.addElasticsearchCluster(clusterConfig)
                    // 名称变更，重新赋值
                    clusterName = value
                }

                val clusterPersistentState = config.elasticsearchClusters[clusterName]!!
                arrayOf(clusterConfig, clusterPersistentState).forEach {
                    with(it) {
                        this[key] = value
                        if (key == ElasticsearchConstant.CLUSTER_PROPS_URL) {
                            refresh()
                        }
                    }
                }
                project.service<ElasticsearchClusterToolWindowExplorer>().treeModel.reload(explorer.tableModel.currentNode)
            }
        }
        set(value) {}

    override fun headers() = listOf("Property", "Value")

    override fun rows() =
        clusterProperties.map { (k, v) -> arrayOf(k, v) }.toMutableList()

    override fun readChildren(root: CapClusterTreeRootNode): List<DefaultMutableTreeNode> = listOf(
        object : CapClusterTreeFolderNode(ElasticsearchConstant.NODES) {
            override fun headers() = listOf(
                I18nResources.getMessageDefault("elasticsearch.node.name", ElasticsearchI18n.NODE_NAME),
                I18nResources.getMessageDefault("elasticsearch.node.ip", ElasticsearchI18n.NODE_IP),
                I18nResources.getMessageDefault("elasticsearch.node.master", ElasticsearchI18n.NODE_MASTER),
                I18nResources.getMessageDefault("elasticsearch.node.role", ElasticsearchI18n.NODE_ROLE),
                I18nResources.getMessageDefault("elasticsearch.node.load_1m", ElasticsearchI18n.NODE_LOAD_1M),
                I18nResources.getMessageDefault("elasticsearch.node.load_5m", ElasticsearchI18n.NODE_LOAD_5M),
                I18nResources.getMessageDefault("elasticsearch.node.load_15m", ElasticsearchI18n.NODE_LOAD_15M),
                I18nResources.getMessageDefault("elasticsearch.node.cpu", ElasticsearchI18n.NODE_CPU),
                I18nResources.getMessageDefault("elasticsearch.node.ram.percent", ElasticsearchI18n.NODE_RAM_PERCENT),
                I18nResources.getMessageDefault("elasticsearch.node.heap.percent", ElasticsearchI18n.NODE_HEAP_PERCENT)
            )

            override fun rows() = run {
                elasticsearchClient?.catNodes()?.map {
                    arrayOf(
                        it.name,
                        it.ip,
                        when (it.master) {
                            "*" -> "yes"
                            else -> "no"
                        },
                        it.role,
                        it.load_1m,
                        it.load_5m,
                        it.load_15m,
                        "${it.cpu}%",
                        "${it.ram}%",
                        "${it.heap}%"
                    )
                } ?: emptyList()
            }

            override fun readChildren(root: CapClusterTreeRootNode): List<DefaultMutableTreeNode> {
                return elasticsearchClient?.catNodes()?.sortedBy { it.name }?.map {
                    ElasticsearchNodeTreeNode(elasticsearchClient, it)
                } ?: emptyList()
            }
        },
        object : CapClusterTreeFolderNode(ElasticsearchConstant.PLUGINS) {
            override fun headers() = listOf(
                I18nResources.getMessageDefault("elasticsearch.plugin.name", ElasticsearchI18n.PLUGIN_NAME),
                I18nResources.getMessageDefault("elasticsearch.plugin.component", ElasticsearchI18n.PLUGIN_COMPONENT),
                I18nResources.getMessageDefault("elasticsearch.plugin.version", ElasticsearchI18n.PLUGIN_VERSION)
            )

            override fun rows() = run {
                elasticsearchClient?.catPlugins()?.map {
                    arrayOf(
                        it.name,
                        it.component,
                        it.version
                    )
                } ?: emptyList()
            }

            override fun readChildren(root: CapClusterTreeRootNode): List<DefaultMutableTreeNode> {
                return elasticsearchClient?.catPlugins()?.distinctBy { it.component }?.sortedBy { it.component }?.map {
                    ElasticsearchPluginTreeNode(elasticsearchClient, it)
                } ?: emptyList()
            }
        },
        object : CapClusterTreeFolderNode(ElasticsearchConstant.INDICES) {
            override fun headers() = listOf(
                I18nResources.getMessageDefault("elasticsearch.index.health", ElasticsearchI18n.INDEX_HEALTH),
                I18nResources.getMessageDefault("elasticsearch.index.status", ElasticsearchI18n.INDEX_STATUS),
                I18nResources.getMessageDefault("elasticsearch.index.index", ElasticsearchI18n.INDEX_INDEX),
                I18nResources.getMessageDefault("elasticsearch.index.uuid", ElasticsearchI18n.INDEX_UUID),
                I18nResources.getMessageDefault("elasticsearch.index.pri", ElasticsearchI18n.INDEX_PRI),
                I18nResources.getMessageDefault("elasticsearch.index.rep", ElasticsearchI18n.INDEX_REP),
                I18nResources.getMessageDefault("elasticsearch.index.docs.count", ElasticsearchI18n.INDEX_DOCS_COUNT),
                I18nResources.getMessageDefault(
                    "elasticsearch.index.docs.deleted",
                    ElasticsearchI18n.INDEX_DOCS_DELETED
                ),
                I18nResources.getMessageDefault("elasticsearch.index.store.size", ElasticsearchI18n.INDEX_STORE_SIZE),
                I18nResources.getMessageDefault(
                    "elasticsearch.index.pri.store.size",
                    ElasticsearchI18n.INDEX_PRI_STORE_SIZE
                )
            )

            override fun rows() = run {
                elasticsearchClient?.catIndices(null)?.map {
                    arrayOf(
                        it.health,
                        it.status,
                        it.index,
                        it.uuid,
                        it.primaries,
                        it.replicas,
                        it.documents,
                        it.deletedDocuments,
                        it.storeSize,
                        it.primaryStoreSize
                    )
                } ?: emptyList()
            }

            override fun readChildren(root: CapClusterTreeRootNode): List<DefaultMutableTreeNode> {
                return elasticsearchClient?.catIndices(null)?.sortedBy { it.name }?.map {
                    ElasticsearchIndexTreeNode(elasticsearchClient, clusterProperties, it)
                } ?: emptyList()
            }
        },
        object : CapClusterTreeFolderNode(ElasticsearchConstant.ALIASES) {
            override fun headers() = listOf(
                I18nResources.getMessageDefault("elasticsearch.alias.alias", ElasticsearchI18n.ALIAS_ALIAS),
                I18nResources.getMessageDefault("elasticsearch.alias.index", ElasticsearchI18n.ALIAS_INDEX),
                I18nResources.getMessageDefault("elasticsearch.alias.filter", ElasticsearchI18n.ALIAS_FILTER),
                I18nResources.getMessageDefault("elasticsearch.alias.routing.index", ElasticsearchI18n.ALIAS_ROUTING_INDEX),
                I18nResources.getMessageDefault("elasticsearch.alias.routing.search", ElasticsearchI18n.ALIAS_ROUTING_SEARCH),
                I18nResources.getMessageDefault("elasticsearch.alias.is_write_index", ElasticsearchI18n.ALIAS_IS_WRITE_INDEX)
            )

            override fun rows() = run {
                elasticsearchClient?.catAliases(null)?.map {
                    arrayOf(
                        it.alias,
                        it.index,
                        it.filter,
                        it.routing_index,
                        it.routing_search,
                        it.is_write_index
                    )
                } ?: emptyList()
            }

            override fun readChildren(root: CapClusterTreeRootNode): List<DefaultMutableTreeNode> {
                return elasticsearchClient?.catAliases(null)?.sortedBy { it.alias }?.map {
                    ElasticsearchAliasTreeNode(elasticsearchClient, clusterProperties, it)
                } ?: emptyList()
            }
        }
    )

    fun getIndices() = children.map { it as CapClusterTreeNode }.find { it.userObject == ElasticsearchConstant.INDICES }!!

    fun getAliases() = children.map { it as CapClusterTreeNode }.find { it.userObject == ElasticsearchConstant.ALIASES }!!

    override fun toString() = clusterProperties[CapCommonConstant.CLUSTER_NAME]!!
}