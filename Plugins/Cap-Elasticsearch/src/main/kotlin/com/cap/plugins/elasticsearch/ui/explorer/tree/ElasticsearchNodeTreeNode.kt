package com.cap.plugins.elasticsearch.ui.explorer.tree

import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.ui.explorer.tree.CapClusterTreeLeafNode
import com.cap.plugins.elasticsearch.client.ElasticsearchClient
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.model.node.Node

class ElasticsearchNodeTreeNode(
    val elasticsearchClient: ElasticsearchClient?,
    val node: Node
) : CapClusterTreeLeafNode("${node.name}(${node.ip})") {

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
        elasticsearchClient?.catNodes()?.filter {
            it.name == node.name
        }?.map {
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
}