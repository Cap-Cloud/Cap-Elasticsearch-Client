package com.cap.plugins.elasticsearch.language.kql.formatter

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlLanguage
import com.intellij.json.JsonLanguage
import com.intellij.psi.PsiFile
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CommonCodeStyleSettings
import com.intellij.psi.codeStyle.FileIndentOptionsProvider

@Keep
class ElasticsearchKqlFileIndentOptionsProvider : FileIndentOptionsProvider() {
    private val myIndentOptions: CommonCodeStyleSettings.IndentOptions = CommonCodeStyleSettings.IndentOptions()

    init {
        this.myIndentOptions.INDENT_SIZE = 2
    }

    override fun getIndentOptions(settings: CodeStyleSettings, file: PsiFile): CommonCodeStyleSettings.IndentOptions? {
        if (file.language is ElasticsearchKqlLanguage) {
            return this.myIndentOptions
        }
        return null
    }
}