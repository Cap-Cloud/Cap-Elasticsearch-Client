package com.cap.plugins.elasticsearch.language.sql.completion

import com.cap.plugins.elasticsearch.language.sql.grammar.psi.ElasticsearchSqlTypes
import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext

class ElasticsearchSqlTableNamePatternCondition : PatternCondition<PsiElement>("TABLE_NAME") {
    override fun accepts(t: PsiElement, context: ProcessingContext): Boolean {
        return t.parent.elementType == ElasticsearchSqlTypes.TABLE_NAME
    }
}