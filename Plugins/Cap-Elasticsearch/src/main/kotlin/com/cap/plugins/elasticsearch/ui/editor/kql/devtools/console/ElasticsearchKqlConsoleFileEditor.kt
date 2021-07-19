package com.cap.plugins.elasticsearch.ui.editor.kql.devtools.console

import com.cap.plugins.elasticsearch.ui.editor.ElasticsearchPhysicalFileEditor
import com.cap.plugins.elasticsearch.ui.editor.kql.devtools.console.panel.ElasticsearchKqlConsolePanel
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class ElasticsearchKqlConsoleFileEditor(
    project: Project,
    file: VirtualFile,
    textEditor: TextEditor
) : ElasticsearchPhysicalFileEditor(project, file, textEditor, ElasticsearchKqlConsolePanel(project, file, textEditor)) {

    override fun getName(): String {
        return "Console"
    }
}