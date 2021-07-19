package com.cap.plugins.elasticsearch.language.kql.completion

import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlTypes
import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext

class ElasticsearchKqlRequestMethodPatternCondition : PatternCondition<PsiElement>("RequestMethod") {
    override fun accepts(t: PsiElement, context: ProcessingContext): Boolean {
        return when {
            t?.elementType == ElasticsearchKqlTypes.REQUEST_METHOD -> true
            t?.parent?.elementType == ElasticsearchKqlTypes.REQUEST_METHOD -> true
            t?.parent?.prevSibling?.elementType == ElasticsearchKqlTypes.REQUEST_FILE -> true
            t?.parent?.prevSibling?.prevSibling?.elementType == ElasticsearchKqlTypes.REQUEST_FILE -> true
            else -> false
        }
    }
}