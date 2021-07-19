package com.cap.plugins.elasticsearch.language.sql.grammar.psi.impl

import com.cap.plugins.elasticsearch.language.sql.grammar.psi.ElasticsearchSqlNamedElement
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode

abstract class ElasticsearchSqlNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node),
    ElasticsearchSqlNamedElement {
}