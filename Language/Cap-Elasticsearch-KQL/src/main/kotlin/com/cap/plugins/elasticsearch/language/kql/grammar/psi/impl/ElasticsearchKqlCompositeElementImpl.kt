package com.cap.plugins.elasticsearch.language.kql.grammar.psi.impl

import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlCompositeElement
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.*
import com.intellij.psi.scope.PsiScopeProcessor
import com.intellij.psi.tree.IElementType


open class ElasticsearchKqlCompositeElementImpl(node: ASTNode) : ASTWrapperPsiElement(node),
    ElasticsearchKqlCompositeElement {
    override fun getTokenType(): IElementType {
        return node.elementType
    }

    override fun toString(): String {
        return getTokenType().toString()
    }

    override fun processDeclarations(
        processor: PsiScopeProcessor,
        state: ResolveState,
        lastParent: PsiElement?,
        place: PsiElement
    ): Boolean {
        return super.processDeclarations(processor, state, lastParent, place)
    }

    // PsiLanguageInjectionHost

    override fun updateText(text: String): PsiLanguageInjectionHost {
        return ElementManipulators.handleContentChange(this as PsiElement, text) as PsiLanguageInjectionHost
    }

    override fun createLiteralTextEscaper(): LiteralTextEscaper<out PsiLanguageInjectionHost> {
        return LiteralTextEscaper.createSimple(this as PsiLanguageInjectionHost)
    }

    override fun isValidHost(): Boolean = true
}