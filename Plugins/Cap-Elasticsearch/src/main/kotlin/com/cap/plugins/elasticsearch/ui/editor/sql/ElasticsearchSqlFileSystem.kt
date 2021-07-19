package com.cap.plugins.elasticsearch.ui.editor.sql

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.ui.editor.ElasticsearchFileSystem
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFileManager

@Keep
class ElasticsearchSqlFileSystem : ElasticsearchFileSystem() {
    fun openEditor(elasticsearchSqlFile: ElasticsearchSqlFile) {
        val fileEditorManager = FileEditorManager.getInstance(elasticsearchSqlFile.project)
        fileEditorManager.openFile(elasticsearchSqlFile, true)
        fileEditorManager.setSelectedEditor(elasticsearchSqlFile, "ElasticsearchSqlEditor")
    }

    override fun getProtocol(): String {
        return PROTOCOL
    }

    companion object {
        private const val PROTOCOL = "essql"

        val INSTANCE: ElasticsearchSqlFileSystem?
            get() = VirtualFileManager.getInstance().getFileSystem(PROTOCOL) as ElasticsearchSqlFileSystem?
    }
}