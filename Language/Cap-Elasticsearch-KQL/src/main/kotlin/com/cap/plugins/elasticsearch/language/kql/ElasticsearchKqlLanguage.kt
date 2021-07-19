package com.cap.plugins.elasticsearch.language.kql

import com.intellij.lang.Language

class ElasticsearchKqlLanguage : Language("ElasticsearchKql") {
    companion object {
        val INSTANCE = ElasticsearchKqlLanguage()
    }
}