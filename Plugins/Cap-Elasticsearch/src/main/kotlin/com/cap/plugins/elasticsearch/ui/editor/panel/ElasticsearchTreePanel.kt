package com.cap.plugins.elasticsearch.ui.editor.panel

import com.cap.plugins.common.component.table.CapJsonTreeTable
import com.cap.plugins.common.util.ObjectMapper
import com.cap.plugins.elasticsearch.ui.editor.model.ResponseContext
import com.fasterxml.jackson.databind.JsonNode
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeNode

class ElasticsearchTreePanel : JPanel, Disposable {

    val project: Project
    private val defaultTreeNode = ObjectMapper.jsonMapper.readTree("{}")

    var treeTable = CapJsonTreeTable("{}")

    constructor(project: Project) : super(BorderLayout()) {
        this.project = project
        background = EditorColorsManager.getInstance().globalScheme.defaultBackground
        add(JBScrollPane(treeTable))
    }

    fun jsonToTree(jsonNode: JsonNode): TreeNode {
        return DefaultMutableTreeNode()
    }

    fun updateResultTree(responseContext: ResponseContext) {

        val search = responseContext.request.path.contains("/_search")
        val context = ObjectMapper.jsonMapper.readTree(responseContext.response.content)
        if (search && context.has("hits")) {
            treeTable = CapJsonTreeTable(ObjectMapper.jsonMapper.writeValueAsString(context.get("hits").get("hits")))
        } else {
            treeTable = CapJsonTreeTable(responseContext.response.content)
        }

        invalidate()
        removeAll()
        add(JBScrollPane(treeTable))
        validate()
    }

    override fun dispose() {
    }
}