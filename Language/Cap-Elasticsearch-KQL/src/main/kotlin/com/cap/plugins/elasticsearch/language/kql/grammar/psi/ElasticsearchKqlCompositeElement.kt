package com.cap.plugins.elasticsearch.language.kql.grammar.psi

import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.psi.tree.IElementType

interface ElasticsearchKqlCompositeElement: NavigatablePsiElement, PsiLanguageInjectionHost {
    fun getTokenType(): IElementType
}