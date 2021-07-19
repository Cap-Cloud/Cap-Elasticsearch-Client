package com.cap.plugins.elasticsearch.ui.editor.sql

import com.cap.plugins.elasticsearch.ui.editor.ElasticsearchFileEditor
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchPanel
import com.cap.plugins.elasticsearch.ui.editor.sql.panel.ElasticsearchSqlPanel
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel

class ElasticsearchSqlFileEditor(
    val project: Project,
    file: ElasticsearchSqlFile
) : ElasticsearchFileEditor() {

    override var panel: ElasticsearchPanel = ElasticsearchSqlPanel(project, file)

    override fun isModified(): Boolean {
        return false
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
    }

    override fun getName(): String {
        return "Elasticsearch Sql Editor"
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