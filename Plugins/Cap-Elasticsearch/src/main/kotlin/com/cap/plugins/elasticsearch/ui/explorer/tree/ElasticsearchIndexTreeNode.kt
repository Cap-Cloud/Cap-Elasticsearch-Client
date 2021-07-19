package com.cap.plugins.elasticsearch.ui.explorer.tree

import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.ui.explorer.tree.CapClusterTreeLeafNode
import com.cap.plugins.elasticsearch.client.ElasticsearchClient
import com.cap.plugins.elasticsearch.config.ElasticsearchConfiguration
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.model.index.Index
import com.cap.plugins.elasticsearch.model.index.IndexInfo

class ElasticsearchIndexTreeNode(
    val elasticsearchClient: ElasticsearchClient?,
    val clusterProperties: ElasticsearchConfiguration,
    var index: Index
) : CapClusterTreeLeafNode(index.name) {

    var indexInfo: IndexInfo? = elasticsearchClient?.indexInfo(index.name)

    override fun refresh() {
        index = elasticsearchClient?.catIndices(userObject.toString())!!.first()
        indexInfo = elasticsearchClient?.indexInfo(index.name)
    }

    override fun headers() = listOf(
        I18nResources.getMessageDefault("elasticsearch.index.health", ElasticsearchI18n.INDEX_HEALTH),
        I18nResources.getMessageDefault("elasticsearch.index.status", ElasticsearchI18n.INDEX_STATUS),
        I18nResources.getMessageDefault("elasticsearch.index.index", ElasticsearchI18n.INDEX_INDEX),
        I18nResources.getMessageDefault("elasticsearch.index.uuid", ElasticsearchI18n.INDEX_UUID),
        I18nResources.getMessageDefault("elasticsearch.index.pri", ElasticsearchI18n.INDEX_PRI),
        I18nResources.getMessageDefault("elasticsearch.index.rep", ElasticsearchI18n.INDEX_REP),
        I18nResources.getMessageDefault("elasticsearch.index.docs.count", ElasticsearchI18n.INDEX_DOCS_COUNT),
        I18nResources.getMessageDefault("elasticsearch.index.docs.deleted", ElasticsearchI18n.INDEX_DOCS_DELETED),
        I18nResources.getMessageDefault("elasticsearch.index.store.size", ElasticsearchI18n.INDEX_STORE_SIZE),
        I18nResources.getMessageDefault("elasticsearch.index.pri.store.size", ElasticsearchI18n.INDEX_PRI_STORE_SIZE)
    )

    override fun rows() = run {
        elasticsearchClient?.catIndices(userObject.toString())?.map {
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
}