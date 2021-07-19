package com.cap.plugins.elasticsearch.ui.editor.rest

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.ui.editor.ElasticsearchFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

@Keep
class ElasticsearchRestFileEditorProvider : ElasticsearchFileEditorProvider() {
    override fun getEditorTypeId(): String {
        return "ElasticsearchRestEditor"
    }

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return file is ElasticsearchRestFile
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        val elasticsearchRestFile = file as ElasticsearchRestFile
        return ElasticsearchRestFileEditor(project, elasticsearchRestFile)
    }
}