package com.cap.plugins.common.config

interface CapPersistentStateComponentI18n {
    var locale: String
    var i18nMap: MutableMap<String, MutableMap<String, String>>
}

class CapPersistentStateComponentI18nAdapter : CapPersistentStateComponentI18n {
    override var locale: String = ""
    override var i18nMap: MutableMap<String, MutableMap<String, String>> = mutableMapOf()
}