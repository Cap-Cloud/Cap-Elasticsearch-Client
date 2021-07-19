package com.cap.plugins.elasticsearch.ui.editor.rest.panel

import com.cap.plugins.elasticsearch.language.rest.ElasticsearchRestFileType
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchBodyPanel
import com.intellij.lang.Language
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.testFramework.LightVirtualFile

class ElasticsearchRestBodyPanel(project: Project) : ElasticsearchBodyPanel(
    project,
    LightVirtualFile("body.elasticsearch.rest", ElasticsearchRestFileType.INSTANCE, ""),
    null
) {
    init {
        val language = Language.findLanguageByID("ElasticsearchRest")!!
        val highlighter = LexerEditorHighlighter(
            SyntaxHighlighterFactory.getSyntaxHighlighter(language, null, null),
            editor.colorsScheme
        )
        editor.highlighter = highlighter
    }
}