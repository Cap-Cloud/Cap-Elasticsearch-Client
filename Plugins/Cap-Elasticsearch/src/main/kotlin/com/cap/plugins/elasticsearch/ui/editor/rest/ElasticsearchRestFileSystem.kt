package com.cap.plugins.elasticsearch.ui.editor.rest

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.ui.editor.ElasticsearchFileSystem
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.*

@Keep
class ElasticsearchRestFileSystem : ElasticsearchFileSystem() {

    fun openEditor(elasticsearchRestFile: ElasticsearchRestFile) {
        val fileEditorManager = FileEditorManager.getInstance(elasticsearchRestFile.project)
        fileEditorManager.openFile(elasticsearchRestFile, true)
        fileEditorManager.setSelectedEditor(elasticsearchRestFile, "ElasticsearchRestEditor")
    }

    override fun getProtocol(): String {
        return PROTOCOL
    }

    companion object {
        private const val PROTOCOL = "elasticsearch.rest"

        val INSTANCE: ElasticsearchRestFileSystem?
            get() = VirtualFileManager.getInstance().getFileSystem(PROTOCOL) as ElasticsearchRestFileSystem?
    }
}