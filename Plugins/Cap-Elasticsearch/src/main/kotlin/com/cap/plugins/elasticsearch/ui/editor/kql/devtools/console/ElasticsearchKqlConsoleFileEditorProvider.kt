package com.cap.plugins.elasticsearch.ui.editor.kql.devtools.console

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlFileType
import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlLanguage
import com.cap.plugins.elasticsearch.ui.editor.ElasticsearchPhysicalFileEditorProvider
import com.intellij.ide.scratch.ScratchUtil
import com.intellij.lang.LanguageUtil
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

@Keep
class ElasticsearchKqlConsoleFileEditorProvider : ElasticsearchPhysicalFileEditorProvider() {
    override fun getEditorTypeId(): String {
        return ID
    }

    override fun createTextEditor(project: Project, file: VirtualFile, fileEditor: FileEditor): FileEditor {
        return ElasticsearchKqlConsoleFileEditor(
            project,
            file,
            fileEditor as TextEditor
        )
    }

    override fun accept(project: Project, file: VirtualFile): Boolean {
        val fileType = file.fileType
        return fileType === ElasticsearchKqlFileType.INSTANCE ||
                ScratchUtil.isScratch(file) && LanguageUtil.getLanguageForPsi(
            project,
            file
        ) === ElasticsearchKqlLanguage.INSTANCE
    }

    companion object {
        const val ID = "ElasticsearchKqlConsoleFileEditor"
    }
}