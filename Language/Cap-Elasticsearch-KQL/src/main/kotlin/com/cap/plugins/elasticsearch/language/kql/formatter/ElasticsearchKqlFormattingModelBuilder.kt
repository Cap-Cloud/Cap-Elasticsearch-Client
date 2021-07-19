package com.cap.plugins.elasticsearch.language.kql.formatter

import com.cap.plugins.common.annotation.Keep
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings

@Keep
class ElasticsearchKqlFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        val block: ElasticsearchKqlBaseBlock =
            ElasticsearchKqlFileBlock(element.containingFile.node.firstChildNode, settings)
        return FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            block,
            settings
        )
    }
}