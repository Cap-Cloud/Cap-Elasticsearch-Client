package com.cap.plugins.elasticsearch.ui.explorer.tree

import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.ui.explorer.tree.CapClusterTreeLeafNode
import com.cap.plugins.elasticsearch.client.ElasticsearchClient
import com.cap.plugins.elasticsearch.config.ElasticsearchConfiguration
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.model.alias.Alias

class ElasticsearchAliasTreeNode(
    val elasticsearchClient: ElasticsearchClient?,
    val clusterProperties: ElasticsearchConfiguration,
    var alias: Alias
) : CapClusterTreeLeafNode(alias.alias) {

    override fun headers() = listOf(
        I18nResources.getMessageDefault("elasticsearch.alias.alias", ElasticsearchI18n.ALIAS_ALIAS),
        I18nResources.getMessageDefault("elasticsearch.alias.index", ElasticsearchI18n.ALIAS_INDEX),
        I18nResources.getMessageDefault("elasticsearch.alias.filter", ElasticsearchI18n.ALIAS_FILTER),
        I18nResources.getMessageDefault("elasticsearch.alias.routing.index", ElasticsearchI18n.ALIAS_ROUTING_INDEX),
        I18nResources.getMessageDefault("elasticsearch.alias.routing.search", ElasticsearchI18n.ALIAS_ROUTING_SEARCH),
        I18nResources.getMessageDefault("elasticsearch.alias.is_write_index", ElasticsearchI18n.ALIAS_IS_WRITE_INDEX)
    )

    override fun rows() = run {
        elasticsearchClient?.catAliases(userObject.toString())?.map {
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
}