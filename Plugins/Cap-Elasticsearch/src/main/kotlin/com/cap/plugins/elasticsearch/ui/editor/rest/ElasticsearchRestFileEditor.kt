package com.cap.plugins.elasticsearch.ui.editor.rest

import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.util.background
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.ui.editor.ElasticsearchFileEditor
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchPanel
import com.cap.plugins.elasticsearch.ui.editor.rest.panel.ElasticsearchRestPanel
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel

class ElasticsearchRestFileEditor : ElasticsearchFileEditor {

    val project: Project
    val elasticsearchRestFile: ElasticsearchRestFile

    override var panel: ElasticsearchPanel

    constructor(project: Project, elasticsearchRestFile: ElasticsearchRestFile) : super() {
        this.project = project
        this.elasticsearchRestFile = elasticsearchRestFile
        this.panel = ElasticsearchRestPanel(project, elasticsearchRestFile)

        background(
            I18nResources.getMessageDefault(
                "background.progress.title.execute.request",
                ElasticsearchI18n.BACKGROUND_EXECUTE_REQUEST
            )
        ) {
            panel.executeRequest()
        }
    }

    override fun isModified(): Boolean {
        return false
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun getName(): String {
        return "Elasticsearch Rest Editor"
    }

    override fun setState(state: FileEditorState) {
    }

    override fun getComponent(): JComponent {
        return (if (isDisposed) JPanel() else panel)!!
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return panel.resultPanel
    }

    override fun getCurrentLocation(): FileEditorLocation? {
        return null
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun dispose() {
        if (!isDisposed) {
            panel!!.dispose()
            isDisposed = true
        }
    }
}