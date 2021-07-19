package com.cap.plugins.elasticsearch.language.rest

import com.intellij.json.JsonFileType
import javax.swing.Icon

class ElasticsearchRestFileType : JsonFileType(ElasticsearchRestLanguage.INSTANCE) {
    companion object {
        val INSTANCE = ElasticsearchRestFileType()
    }

    override fun getIcon(): Icon? {
        return null
    }

    override fun getName(): String {
        return "Elasticsearch REST"
    }

    override fun getDefaultExtension(): String {
        return "elasticsearch.rest"
    }

    override fun getDescription(): String {
        return "Elasticsearch REST"
    }
}