package com.cap.plugins.elasticsearch.language.kql.formatter

import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlLanguage
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

class ElasticsearchKqlCodeStyleSettings(container: CodeStyleSettings) :
    CustomCodeStyleSettings(ElasticsearchKqlLanguage.INSTANCE.id, container) {
}