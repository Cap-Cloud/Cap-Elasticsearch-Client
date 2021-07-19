package com.cap.plugins.elasticsearch.ui.editor.sql

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.ui.editor.ElasticsearchFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

@Keep
class ElasticsearchSqlFileEditorProvider : ElasticsearchFileEditorProvider() {
    // class ElasticsearchSqlFileEditorProvider : FileEditorProvider, DumbAware {
    // class ElasticsearchSqlFileEditorProvider : PsiAwareTextEditorProvider(), DumbAware {
    // class ElasticsearchSqlFileEditorProvider : DatabaseTableFileEditorProvider(), DumbAware {
    override fun getEditorTypeId(): String {
        return "ElasticsearchSqlEditor"
    }

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return file is ElasticsearchSqlFile
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        return ElasticsearchSqlFileEditor(project, file as ElasticsearchSqlFile)
    }
}