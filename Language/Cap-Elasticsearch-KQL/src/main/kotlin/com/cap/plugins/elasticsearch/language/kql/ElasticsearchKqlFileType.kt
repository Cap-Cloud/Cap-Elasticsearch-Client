package com.cap.plugins.elasticsearch.language.kql

import com.cap.plugins.common.annotation.Keep
import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

@Keep
class ElasticsearchKqlFileType : LanguageFileType(ElasticsearchKqlLanguage.INSTANCE) {
    companion object {
        val INSTANCE = ElasticsearchKqlFileType()
    }

    override fun getIcon(): Icon? {
        return ElasticsearchKqlIcons.ELASTICSEARCH_ICON
    }

    override fun getName(): String {
        return "Elasticsearch KQL"
    }

    override fun getDefaultExtension(): String {
        return "eskql"
    }

    override fun getDescription(): String {
        return "Elasticsearch KQL"
    }
}