package com.cap.plugins.elasticsearch.ui.editor

import com.cap.plugins.common.http.model.Method
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.elasticsearch.client.ElasticsearchClient
import com.cap.plugins.elasticsearch.client.ElasticsearchClients
import com.cap.plugins.elasticsearch.config.ElasticsearchConfiguration
import com.cap.plugins.elasticsearch.config.ElasticsearchPersistentStateSetting
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchPanel
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EnumComboBoxModel
import com.intellij.ui.components.JBCheckBox
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField

abstract class ElasticsearchPhysicalFileEditor(
    val project: Project,
    val myFile: VirtualFile,
    val myEditor: TextEditor,
    val panel: ElasticsearchPanel
) :
    UserDataHolderBase(),
    FileEditor {
    val config: ElasticsearchPersistentStateSetting = service()
    val clusters: ComboBox<String> = ComboBox(config.elasticsearchClusters.keys.toTypedArray())
    val methodCombo: ComboBox<Method> = ComboBox(EnumComboBoxModel(Method::class.java))
    val model: ComboBox<String> = ComboBox(arrayOf("NLPchina", "Opendistro", "X-Pack"))
    val fetchSize = JTextField()
    val submitSelectedOnly = JBCheckBox(
        I18nResources.getMessageDefault(
            "elasticsearch.sql.submit.selected.only",
            ElasticsearchI18n.SQL_SUBMIT_SELECTED_ONLY
        )
    )

    var client: ElasticsearchClient? = null
        get() {
            if (!ElasticsearchClients.clients.containsKey(clusters.selectedItem.toString())) {
                val configuration = ElasticsearchConfiguration()
                configuration.putAll(config.elasticsearchClusters[clusters.selectedItem]!!)
                ElasticsearchClients.clients[clusters.selectedItem.toString()] =
                    ElasticsearchClient(configuration)
            }
            return ElasticsearchClients.clients[clusters.selectedItem.toString()]
        }

    val myComponent: JComponent = createComponent()

    fun setText(text: String) {
        ApplicationManager.getApplication().runWriteAction {
            (myEditor as TextEditor).editor.document.setText(text)
        }
    }

    fun getSelectedText(): String? {
        return (myEditor as TextEditor).editor.selectionModel.selectedText
    }

    fun getText(): String {
        return (myEditor as TextEditor).editor.document.text
    }

    private fun createComponent(): JComponent {
        return panel
    }

    override fun isModified(): Boolean {
        return myEditor.isModified
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
        myEditor.addPropertyChangeListener(listener)
    }

    override fun setState(state: FileEditorState) {
    }

    override fun getComponent(): JComponent {
        return myComponent
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return myEditor.preferredFocusedComponent
    }

    override fun getCurrentLocation(): FileEditorLocation? {
        return myEditor.currentLocation
    }

    override fun isValid(): Boolean {
        return myEditor.isValid
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
        myEditor.removePropertyChangeListener(listener)
    }

    override fun getFile(): VirtualFile? {
        return myFile
    }

    override fun dispose() {
        myEditor.dispose()
    }
}

abstract class ElasticsearchFileEditor : UserDataHolderBase(), FileEditor {
    abstract var panel: ElasticsearchPanel
    var isDisposed = false

    override fun isModified(): Boolean {
        return false
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
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