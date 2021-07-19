package com.cap.plugins.elasticsearch.language.kql.grammar.psi.impl

import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlMessageBody
import com.intellij.lang.ASTNode
import com.intellij.psi.ElementManipulators
import com.intellij.psi.LiteralTextEscaper
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost

open class ElasticsearchKqlMessageBodyBaseImpl(node: ASTNode) : ElasticsearchKqlJsonObjectImpl(node),
    ElasticsearchKqlMessageBody {
    override fun updateText(text: String): PsiLanguageInjectionHost {
        return ElementManipulators.handleContentChange(this as PsiElement, text) as PsiLanguageInjectionHost
    }

    override fun createLiteralTextEscaper(): LiteralTextEscaper<out PsiLanguageInjectionHost> {
        return LiteralTextEscaper.createSimple(this as PsiLanguageInjectionHost)
    }

    override fun isValidHost(): Boolean = true
}