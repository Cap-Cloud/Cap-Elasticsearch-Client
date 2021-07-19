package com.cap.plugins.elasticsearch.language.sql.completion

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.client.ElasticsearchClients
import com.cap.plugins.elasticsearch.ui.editor.sql.ElasticsearchSqlFileEditor
import com.cap.plugins.elasticsearch.ui.editor.sql.panel.ElasticsearchSqlPanel
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

@Keep
class ElasticsearchSqlCompletionContributor : CompletionContributor {
    constructor() {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(PsiElement::class.java).with(ElasticsearchSqlTableNamePatternCondition()),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    resultSet: CompletionResultSet
                ) {
                    val psiElement: PsiElement? = parameters.originalPosition

                    val fileEditorManager = FileEditorManager.getInstance(psiElement!!.project)
                    if (fileEditorManager.selectedEditor is ElasticsearchSqlFileEditor) {
                        val clusters =
                            ((fileEditorManager.selectedEditor as ElasticsearchSqlFileEditor).panel as ElasticsearchSqlPanel).clusters
                        ElasticsearchClients.clients[clusters.selectedItem.toString()]?.catIndices(null)?.filter {
                            it.status.equals("open", true)
                        }?.map {
                            it.name
                        }?.forEach {
                            resultSet.addElement(LookupElementBuilder.create(it))
                        }
                    }
                }
            }
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(PsiElement::class.java).with(ElasticsearchSqlColumnNamePatternCondition()),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    resultSet: CompletionResultSet
                ) {
                    val psiElement: PsiElement? = parameters.originalPosition

                    val fileEditorManager = FileEditorManager.getInstance(psiElement!!.project)
                }
            }
        )
    }
}