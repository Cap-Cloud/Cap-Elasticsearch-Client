package com.cap.plugins.elasticsearch.ui.editor

import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

abstract class ElasticsearchPhysicalFileEditorProvider(val fileEditorProvider: FileEditorProvider = PsiAwareTextEditorProvider()) :
    AsyncFileEditorProvider, DumbAware {
    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        return createEditorAsync(project, file).build()
    }

    abstract fun createTextEditor(project: Project, file: VirtualFile, fileEditor: FileEditor): FileEditor

    override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder {
        val builder: AsyncFileEditorProvider.Builder =
            getBuilderFromEditorProvider(
                fileEditorProvider,
                project,
                file
            )

        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor {
                return createTextEditor(project, file, builder.build())
            }
        }
    }

    private fun getBuilderFromEditorProvider(
        provider: FileEditorProvider,
        project: Project,
        file: VirtualFile
    ): AsyncFileEditorProvider.Builder {
        return if (provider is AsyncFileEditorProvider) {
            provider.createEditorAsync(project, file)
        } else {
            object : AsyncFileEditorProvider.Builder() {
                override fun build(): FileEditor {
                    return provider.createEditor(project, file)
                }
            }
        }
    }
}

abstract class ElasticsearchFileEditorProvider : FileEditorProvider, DumbAware {
    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR
    }
}