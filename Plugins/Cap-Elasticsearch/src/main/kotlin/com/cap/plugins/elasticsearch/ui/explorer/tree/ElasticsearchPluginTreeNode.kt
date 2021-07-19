package com.cap.plugins.elasticsearch.ui.explorer.tree

import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.ui.explorer.tree.CapClusterTreeLeafNode
import com.cap.plugins.elasticsearch.client.ElasticsearchClient
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.model.plugin.Plugin

class ElasticsearchPluginTreeNode(
    val elasticsearchClient: ElasticsearchClient?,
    val plugin: Plugin
) : CapClusterTreeLeafNode("${plugin.component}(${plugin.version})") {

    override fun headers() = listOf(
        I18nResources.getMessageDefault("elasticsearch.plugin.name", ElasticsearchI18n.PLUGIN_NAME),
        I18nResources.getMessageDefault("elasticsearch.plugin.component", ElasticsearchI18n.PLUGIN_COMPONENT),
        I18nResources.getMessageDefault("elasticsearch.plugin.version", ElasticsearchI18n.PLUGIN_VERSION)
    )

    override fun rows() = run {
        elasticsearchClient?.catPlugins()?.filter {
            it.name == plugin.name && it.component == plugin.component && it.version == plugin.version
        }?.map {
            arrayOf(
                it.name,
                it.component,
                it.version
            )
        } ?: emptyList()
    }
}