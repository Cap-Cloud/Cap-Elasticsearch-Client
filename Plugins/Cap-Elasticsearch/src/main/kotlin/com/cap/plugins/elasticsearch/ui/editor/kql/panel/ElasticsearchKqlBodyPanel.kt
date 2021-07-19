package com.cap.plugins.elasticsearch.ui.editor.kql.panel

import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlFileType
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchBodyPanel
import com.intellij.lang.Language
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.testFramework.LightVirtualFile

class ElasticsearchKqlBodyPanel(project: Project) : ElasticsearchBodyPanel(
    project,
    LightVirtualFile("body.eskql", ElasticsearchKqlFileType.INSTANCE, ""),
    null
) {
    init {
        val language = Language.findLanguageByID("ElasticsearchKql")!!
        val highlighter = LexerEditorHighlighter(
            SyntaxHighlighterFactory.getSyntaxHighlighter(language, null, null),
            editor.colorsScheme
        )
        editor.highlighter = highlighter
    }
}