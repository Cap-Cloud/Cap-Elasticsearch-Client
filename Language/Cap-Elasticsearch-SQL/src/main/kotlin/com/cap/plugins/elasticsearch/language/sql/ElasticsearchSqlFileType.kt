package com.cap.plugins.elasticsearch.language.sql

import com.cap.plugins.common.annotation.Keep
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

@Keep
class ElasticsearchSqlFileType : LanguageFileType(ElasticsearchSqlLanguage.INSTANCE) {
    companion object {
        const val NAME = "Elasticsearch SQL"
        const val EXTENSION = "essql"
        val INSTANCE = ElasticsearchSqlFileType()
    }

    override fun getIcon(): Icon? {
        return ElasticsearchSqlIcons.ELASTICSEARCH_ICON
    }

    override fun getName(): String {
        return NAME
    }

    override fun getDefaultExtension(): String {
        return EXTENSION
    }

    override fun getDescription(): String {
        return NAME
    }
}