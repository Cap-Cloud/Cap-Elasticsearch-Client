package com.cap.plugins.elasticsearch.ui.editor.panel

import com.cap.plugins.common.component.panel.CapEditorPanel
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

open class ElasticsearchBodyPanel(
    project: Project,
    file: VirtualFile,
    highlighter: LexerEditorHighlighter? = null
) : CapEditorPanel(project, file, highlighter)