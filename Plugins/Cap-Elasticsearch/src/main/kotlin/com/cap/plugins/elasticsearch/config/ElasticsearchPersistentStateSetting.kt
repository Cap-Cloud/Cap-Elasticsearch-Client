package com.cap.plugins.elasticsearch.config

import com.cap.plugins.common.annotation.KeepAllMembers
import com.cap.plugins.common.config.CapPersistentStateComponent
import com.cap.plugins.common.constant.CapCommonConstant
import com.intellij.configurationStore.APP_CONFIG
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * CapPluginsElasticsearchClientSetting
 * @author cap_cloud
 */
@KeepAllMembers
@Service
@State(
    name = "CapPluginsElasticsearchClientSetting",
    storages = [Storage("$APP_CONFIG/cap-elasticsearch-client.xml")]
)
class ElasticsearchPersistentStateSetting : PersistentStateComponent<ElasticsearchPersistentStateSetting>,
    CapPersistentStateComponent {

    override var configs: MutableMap<String, String> = mutableMapOf()

    var elasticsearchClusters: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

    /**
     * 添加集群
     */
    fun addElasticsearchCluster(cluster: ElasticsearchConfiguration) {
        elasticsearchClusters[cluster[CapCommonConstant.CLUSTER_NAME]!!] = cluster
    }

    /**
     * 移除集群
     */
    fun removeElasticsearchCluster(cluster: ElasticsearchConfiguration) {
        elasticsearchClusters.remove(cluster[CapCommonConstant.CLUSTER_NAME])
    }

    override fun loadState(setting: ElasticsearchPersistentStateSetting) {
        XmlSerializerUtil.copyBean(setting, this);
    }

    override fun getState(): ElasticsearchPersistentStateSetting? {
        return this
    }
}