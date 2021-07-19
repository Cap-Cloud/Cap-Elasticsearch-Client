package com.cap.plugins.common.i18n

import com.cap.plugins.common.service.capPersistentStateComponentI18n
import java.util.*

class I18nResources {
    companion object {
        private const val baseName = "cap_messages"

        private var resourceBundle: ResourceBundle = if (capPersistentStateComponentI18n.locale.isNullOrEmpty()) {
            ResourceBundle.getBundle(baseName, Locale.getDefault())
        } else {
            ResourceBundle.getBundle(baseName, Locale.forLanguageTag(capPersistentStateComponentI18n.locale))
        }

        init {
            chooseLanguage()
        }

        private fun chooseLanguage() {
            resourceBundle = if (capPersistentStateComponentI18n.locale.isNullOrEmpty()) {
                ResourceBundle.getBundle(baseName, Locale.ROOT)
            } else {
                ResourceBundle.getBundle(baseName, Locale.forLanguageTag(capPersistentStateComponentI18n.locale))
            }
        }

        fun changeLanguage(lang: String) {
            capPersistentStateComponentI18n.locale = lang
            chooseLanguage()
        }

        fun changeLanguage(locale: Locale) {
            capPersistentStateComponentI18n.locale = locale.language
            chooseLanguage()
        }

        fun getMessage(key: String): String {
            if (key.isNullOrEmpty()) {
                return ""
            }
            return try {
                resourceBundle.getString(key)
            } catch (ex: Exception) {
                key
            }
        }

        fun getMessage(key: String, vararg params: Any?): String {
            val value = resourceBundle.getString(key)
            return if (params == null || params.size === 0) {
                value
            } else {
                value.format(*params)
            }
        }

        fun getMessageDefault(key: String, default: String): String {
            if (key.isNullOrEmpty()) {
                return ""
            }

            if (!capPersistentStateComponentI18n.i18nMap.containsKey("")) {
                capPersistentStateComponentI18n.i18nMap[""] = mutableMapOf<String, String>().also { m ->
                    val b = ResourceBundle.getBundle(baseName, Locale.ROOT)
                    b.keys.toList().forEach {
                        m[it] = b.getString(it)
                    }
                }
            }
            if (!capPersistentStateComponentI18n.i18nMap.containsKey("zh-CN")) {
                capPersistentStateComponentI18n.i18nMap["zh-CN"] = mutableMapOf<String, String>().also { m ->
                    val b = ResourceBundle.getBundle(baseName, Locale.SIMPLIFIED_CHINESE)
                    b.keys.toList().forEach {
                        m[it] = b.getString(it)
                    }
                }
            }
            if (!capPersistentStateComponentI18n.i18nMap.containsKey(capPersistentStateComponentI18n.locale)) {
                capPersistentStateComponentI18n.i18nMap[capPersistentStateComponentI18n.locale] =
                    mutableMapOf<String, String>().also { m ->
                        val b = ResourceBundle.getBundle(baseName, Locale.ROOT)
                        b.keys.toList().forEach {
                            m[it] = b.getString(it)
                        }
                    }
            }

            capPersistentStateComponentI18n.i18nMap[capPersistentStateComponentI18n.locale]!!.putIfAbsent(key, default)

            return capPersistentStateComponentI18n.i18nMap[capPersistentStateComponentI18n.locale]!![key]!!
        }

        fun getMessageDefault(key: String, default: String, vararg params: Any?): String {
            val value = getMessageDefault(key, default)
            return if (params == null || params.size === 0) {
                value
            } else {
                value.format(*params)
            }
        }

    }
}