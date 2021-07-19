package com.cap.plugins.elasticsearch.language.kql.completion

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.common.http.model.Method
import com.cap.plugins.elasticsearch.client.ElasticsearchClients
import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlTypes
import com.cap.plugins.elasticsearch.language.rest.api.RequestApi
import com.cap.plugins.elasticsearch.language.rest.api.RequestApiTarget
import com.cap.plugins.elasticsearch.ui.editor.kql.devtools.console.ElasticsearchKqlConsoleFileEditor
import com.cap.plugins.elasticsearch.ui.editor.kql.devtools.console.panel.ElasticsearchKqlConsolePanel
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext
import org.apache.http.client.utils.URIBuilder

@Keep
class ElasticsearchKqlCompletionContributor : CompletionContributor {
    constructor() {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(PsiElement::class.java).with(ElasticsearchKqlRequestMethodPatternCondition()),
            object : CompletionProvider<CompletionParameters>() {
                override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    resultSet: CompletionResultSet
                ) {
                    Method.values().forEach {
                        resultSet.addElement(LookupElementBuilder.create(it).withTypeText("Request method", true))
                    }
                }
            }
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(ElasticsearchKqlTypes.URL_PATH),
            ElasticsearchKqlRequestUrlCompletionProvider()
        )
    }
}

class ElasticsearchKqlRequestUrlCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val psiElement: PsiElement? = parameters.originalPosition
        val urlText = psiElement?.parent?.text ?: ""

        if (urlText.startsWith("/")) {
            addCompletions1(parameters, context, resultSet)
        } else if (urlText.startsWith("http://", true) || urlText.startsWith("https://", true)) {
            addCompletions2(parameters, context, resultSet)
        }
    }

    private fun addCompletions1(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val psiElement: PsiElement = parameters.originalPosition!!
        val fileEditorManager = FileEditorManager.getInstance(psiElement!!.project)
        if (fileEditorManager.selectedEditor is ElasticsearchKqlConsoleFileEditor) {
            val clusters =
                ((fileEditorManager.selectedEditor as ElasticsearchKqlConsoleFileEditor).panel as ElasticsearchKqlConsolePanel).clusters
            val client = ElasticsearchClients.clients[clusters.selectedItem.toString()]
            val nodes by lazy {
                client?.catNodes()?.map {
                    it.name
                } ?: emptyList()
            }

            fun addNodes() {
                nodes.forEach { node ->
                    resultSet.addElement(
                        LookupElementBuilder.create(node).withTypeText("Node", true)
                    )
                }
            }

            val indices by lazy {
                client?.catIndices(null)?.filter {
                    it.status.equals("open", true)
                }?.map {
                    it.name
                } ?: emptyList()
            }

            fun addIndices() {
                indices.forEach { index ->
                    resultSet.addElement(
                        LookupElementBuilder.create(index).withTypeText("Index", true)
                    )
                }
            }

            val aliases by lazy {
                client?.catAliases(null)?.map {
                    it.alias
                } ?: emptyList()
            }

            fun addAliases() {
                aliases.forEach { alias ->
                    resultSet.addElement(
                        LookupElementBuilder.create(alias).withTypeText("Alias", true)
                    )
                }
            }

            val method = Method.valueOf(parameters.originalPosition!!.parent.parent.firstChild.text)
            var i = 0
            var tempElement = parameters.originalPosition!!.parent.firstChild
            while (tempElement != psiElement) {
                tempElement = tempElement.nextSibling
                if (tempElement.elementType == ElasticsearchKqlTypes.URL_PATH) {
                    i++
                }
            }
            val builder = URIBuilder(psiElement?.parent?.text ?: "")

            val urlSegments = builder.pathSegments.subList(0, i + 1)
            val compareSegments = urlSegments.dropLast(1)
            val preUrl = urlSegments.joinToString(separator = "/", prefix = "/")

            val apis = RequestApi.REQUEST_API_MAPS["current"]!!.filter {
                it.methods.contains(method)
            }
            val patterns = apis.map { api ->
                api.patterns.map { pattern ->
                    Pair(pattern, api.targets)
                }
            }.fold(mutableListOf<Pair<String, MutableList<RequestApiTarget>>>()) { a, b ->
                a.addAll(b)
                a
            }

            patterns.filter { pattern ->
                val apiSegments = URIBuilder(pattern.first).pathSegments
                apiSegments.size > compareSegments.size && compareSegments.zip(apiSegments).none { pair ->
                    pair.second.indexOf("@") < 0 && pair.first != pair.second
                }
            }.map {
                Pair(URIBuilder(it.first).pathSegments[i], it.second)
            }.forEach {
                when {
                    it.first == RequestApi.TARGET -> {
                        it.second.forEach { target ->
                            when (target) {
                                RequestApiTarget.NODES -> {
                                    addNodes()
                                }
                                RequestApiTarget.INDICES -> {
                                    addIndices()
                                }
                                RequestApiTarget.INDEX_ALIAS -> {
                                    addAliases()
                                }
                            }
                        }
                    }
                    it.first == RequestApi.INDEX -> {
                        addIndices()
                    }
                    it.first == RequestApi.ALIAS -> {
                        addAliases()
                    }
                    it.first == RequestApi.METRICS -> {
                    }
                    it.first == RequestApi.TARGET_INDEX -> {
                    }
                    it.first == RequestApi.INDEX_METRICS -> {
                    }
                    it.first == RequestApi.INDEX_TEMPLATE -> {
                    }
                    it.first == RequestApi.INDEX_UUID -> {
                    }
                    it.first == RequestApi.COMPONENT_TEMPLATE -> {
                    }
                    it.first == RequestApi.ID -> {
                    }
                    it.first == RequestApi.QUERY -> {
                    }
                    it.first.contains("@") -> {

                    }
                    else -> {
                        resultSet.addElement(
                            LookupElementBuilder.create(it.first).withTypeText("Endpoint", true)
                        )
                    }
                }
            }
        }
    }

    private fun addCompletions2(
        parameters: CompletionParameters,
        context: ProcessingContext,
        resultSet: CompletionResultSet
    ) {
        val psiElement: PsiElement = parameters.originalPosition!!

    }
}