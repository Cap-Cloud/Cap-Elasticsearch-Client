package com.cap.plugins.elasticsearch.language.rest

import com.intellij.json.JsonLanguage

class ElasticsearchRestLanguage : JsonLanguage("ElasticsearchRest") {
    companion object {
        val INSTANCE = ElasticsearchRestLanguage()
    }
}