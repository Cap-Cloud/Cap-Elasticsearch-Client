package com.cap.plugins.elasticsearch.ui.editor.kql

import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlFileType
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotifications

class ElasticsearchKqlNotificationPanelProvider : EditorNotifications.Provider<ElasticsearchKqlNotificationPanel>(),
    DumbAware {

    val PANEL_KEY: Key<ElasticsearchKqlNotificationPanel> =
        Key.create<ElasticsearchKqlNotificationPanel>("ElasticsearchKqlNotificationPanel")

    override fun getKey(): Key<ElasticsearchKqlNotificationPanel> {
        return PANEL_KEY
    }

    override fun createNotificationPanel(
        file: VirtualFile,
        fileEditor: FileEditor,
        project: Project
    ): ElasticsearchKqlNotificationPanel? {
        return if (!FileTypeRegistry.getInstance().isFileOfType(file, ElasticsearchKqlFileType.INSTANCE as FileType)) {
            null
        } else {
            ElasticsearchKqlNotificationPanel(file)
        }
    }
}