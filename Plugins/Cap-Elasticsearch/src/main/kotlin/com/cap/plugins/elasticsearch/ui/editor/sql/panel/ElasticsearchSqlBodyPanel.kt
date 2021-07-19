package com.cap.plugins.elasticsearch.ui.editor.sql.panel

import com.cap.plugins.elasticsearch.language.sql.ElasticsearchSqlFileType
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchBodyPanel
import com.intellij.lang.Language
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.testFramework.LightVirtualFile

class ElasticsearchSqlBodyPanel(project: Project) : ElasticsearchBodyPanel(
    project,
    LightVirtualFile("body.essql", ElasticsearchSqlFileType.INSTANCE, ""),
    null
) {
    init {
        val language = Language.findLanguageByID("ElasticsearchSql")!!
        val highlighter = LexerEditorHighlighter(
            SyntaxHighlighterFactory.getSyntaxHighlighter(language, null, null),
            editor.colorsScheme
        )
        editor.highlighter = highlighter
    }
}