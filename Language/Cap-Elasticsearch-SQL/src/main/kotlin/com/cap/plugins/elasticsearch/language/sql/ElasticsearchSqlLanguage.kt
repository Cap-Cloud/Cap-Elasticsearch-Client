package com.cap.plugins.elasticsearch.language.sql

import com.intellij.lang.Language

class ElasticsearchSqlLanguage : Language(ID) {
    companion object {
        const val ID = "ElasticsearchSql"
        val INSTANCE = ElasticsearchSqlLanguage()
    }
}