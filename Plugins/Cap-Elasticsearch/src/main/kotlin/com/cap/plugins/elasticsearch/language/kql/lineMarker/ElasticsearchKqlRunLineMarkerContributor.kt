package com.cap.plugins.elasticsearch.language.kql.lineMarker

import com.cap.plugins.common.action.CapAction
import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.common.http.model.Method
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlTypes
import com.cap.plugins.elasticsearch.language.rest.api.RequestApi
import com.cap.plugins.elasticsearch.ui.editor.kql.devtools.console.ElasticsearchKqlConsoleFileEditor
import com.cap.plugins.elasticsearch.ui.editor.kql.devtools.console.panel.ElasticsearchKqlConsolePanel
import com.intellij.execution.lineMarker.RunLineMarkerContributor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import org.apache.http.client.utils.URIBuilder

@Keep
class ElasticsearchKqlRunLineMarkerContributor : RunLineMarkerContributor() {
    private fun getDocumentation(element: PsiElement): String {
        val method =
            Method.valueOf(element.parent.children.first { it.elementType == ElasticsearchKqlTypes.REQUEST_METHOD }.text)
        val url =
            element.parent.children.first { it.elementType == ElasticsearchKqlTypes.REQUEST_URL }.text

        return if (url == "/") {
            "index.html"
        } else {
            val builder = URIBuilder(url)
            val pathSegments = builder.pathSegments

            val patterns = RequestApi.REQUEST_API_MAPS["current"]!!.asSequence().filter {
                it.methods.contains(method)
            }.map { api ->
                api.patterns.map { pattern ->
                    Pair(pattern, api.documentation)
                }
            }.fold(mutableListOf<Pair<String, String>>()) { a, b ->
                a.addAll(b)
                a
            }.filter { pattern ->
                val apiSegments = URIBuilder(pattern.first).pathSegments
                apiSegments.size >= pathSegments.size && pathSegments.zip(apiSegments).none { pair ->
                    pair.second.indexOf("@") < 0 && pair.first != pair.second
                }
            }.map { pattern ->
                val apiSegments = URIBuilder(pattern.first).pathSegments
                var weight = 0
                Pair(apiSegments.mapIndexed { i, s ->
                    if (s.indexOf("@") != -1 && pathSegments.size > i) {
                        weight++
                        pathSegments[i] ?: s
                    } else {
                        s
                    }
                }.joinToString("/", "/"), Pair(weight, pattern.second))
            }

            val minWeight = patterns.map {
                it.second.first
            }.minOrNull() ?: 0

            patterns.filter {
                it.first.startsWith(url) && it.second.first == minWeight
            }.minByOrNull {
                it.first.length
            }?.second?.second ?: "index.html"
        }
    }

    override fun getInfo(element: PsiElement): Info? {
        if (isHttpRequestRunElement(element)) {
            val runRequest = CapAction("Run Request", Icons.Actions.EXECUTE_ICON) {
                val method =
                    element.parent.children.first { it.elementType == ElasticsearchKqlTypes.REQUEST_METHOD }.text
                val url =
                    element.parent.children.first { it.elementType == ElasticsearchKqlTypes.REQUEST_URL }.text
                val body =
                    element.parent.children.firstOrNull { it.elementType == ElasticsearchKqlTypes.REQUEST_BODY }?.text?.let {
                        "$it\n"
                    }

                val fileEditorManager = FileEditorManager.getInstance(element.project)
                if (fileEditorManager.selectedEditor is ElasticsearchKqlConsoleFileEditor) {
                    val panel =
                        (fileEditorManager.selectedEditor as ElasticsearchKqlConsoleFileEditor).panel as ElasticsearchKqlConsolePanel
                    panel.executeRequest(method, url, body)
                }
            }

            val openDocumentation = CapAction("Open Documentation", Icons.FileType.HTML_ICON) {
                val documentation = getDocumentation(element)

                val fileEditorManager = FileEditorManager.getInstance(element.project)
                if (fileEditorManager.selectedEditor is ElasticsearchKqlConsoleFileEditor) {
                    val panel =
                        (fileEditorManager.selectedEditor as ElasticsearchKqlConsoleFileEditor).panel as ElasticsearchKqlConsolePanel
                    panel.openDocumentation("https://www.elastic.co/guide/en/elasticsearch/reference/${"current"}/${documentation}")
                }
            }

            return Info(
                Icons.Actions.EXECUTE_ICON,
                arrayOf(
                    runRequest,
                    openDocumentation
                ),
                null
            )
        }
        return null
    }

    private fun isHttpRequestRunElement(element: PsiElement): Boolean {
        return element.node.elementType == ElasticsearchKqlTypes.REQUEST_METHOD
    }
}