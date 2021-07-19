package com.cap.plugins.elasticsearch.ui.editor.kql

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.common.util.ProjectUtil
import com.cap.plugins.elasticsearch.ui.editor.ElasticsearchFileSystem
import com.cap.plugins.elasticsearch.ui.editor.kql.devtools.console.ElasticsearchKqlConsoleFileEditor
import com.cap.plugins.elasticsearch.ui.editor.kql.devtools.console.ElasticsearchKqlConsoleFileEditorProvider
import com.cap.plugins.elasticsearch.ui.editor.kql.devtools.console.panel.ElasticsearchKqlConsolePanel
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager

@Keep
class ElasticsearchKqlFileSystem : ElasticsearchFileSystem() {
    fun openDevTool(file: VirtualFile, cluster: String? = null) {
        val fileEditorManager = FileEditorManager.getInstance(ProjectUtil.getCurrProject())
        fileEditorManager.openFile(file, true)
        fileEditorManager.setSelectedEditor(file, ElasticsearchKqlConsoleFileEditorProvider.ID)
        cluster?.let {
            ((fileEditorManager.selectedEditor as ElasticsearchKqlConsoleFileEditor).panel as ElasticsearchKqlConsolePanel).clusters.selectedItem =
                cluster
        }
    }

    override fun getProtocol(): String {
        return PROTOCOL
    }

    companion object {
        private const val PROTOCOL = "eskql"

        val INSTANCE: ElasticsearchKqlFileSystem?
            get() = VirtualFileManager.getInstance().getFileSystem(PROTOCOL) as ElasticsearchKqlFileSystem?
    }
}