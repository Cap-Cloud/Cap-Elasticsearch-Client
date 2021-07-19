package com.cap.plugins.elasticsearch.config

import com.cap.plugins.common.annotation.KeepAllMembers
import com.cap.plugins.common.config.CapPersistentStateComponentI18n
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
    name = "CapPluginsElasticsearchI18nSetting",
    storages = [Storage("$APP_CONFIG/cap-elasticsearch-client-i18n.xml")]
)
class ElasticsearchI18nSetting : PersistentStateComponent<ElasticsearchI18nSetting>, CapPersistentStateComponentI18n {

    override var locale: String = ""

    override var i18nMap: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

    override fun loadState(setting: ElasticsearchI18nSetting) {
        XmlSerializerUtil.copyBean(setting, this);
    }

    override fun getState(): ElasticsearchI18nSetting {
        return this
    }

}