package com.cap.plugins.elasticsearch.ui.editor.rest.panel

import com.cap.plugins.common.component.table.CapTableItem
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.elasticsearch.ui.editor.model.ResponseContext
import com.cap.plugins.elasticsearch.ui.editor.model.RestResponseContext
import com.cap.plugins.elasticsearch.ui.editor.model.TableMode
import com.cap.plugins.elasticsearch.ui.editor.model.TextMode
import com.cap.plugins.elasticsearch.ui.editor.panel.*
import com.intellij.openapi.project.Project
import javax.swing.SwingConstants

class ElasticsearchRestResultPanel : ElasticsearchResultPanel {
    val project: Project
    val elasticsearchRestPanel: ElasticsearchRestPanel

    private var textPanel: ElasticsearchTextPanel
    private var jsonPanel: ElasticsearchJsonPanel
    private var treePanel: ElasticsearchTreePanel

    private var tablePanel: ElasticsearchRestTablePanel

    constructor(project: Project, elasticsearchRestPanel: ElasticsearchRestPanel) : super(elasticsearchRestPanel) {
        this.project = project
        this.elasticsearchRestPanel = elasticsearchRestPanel

        textPanel = ElasticsearchTextPanel(project)
        jsonPanel = ElasticsearchJsonPanel(project)
        treePanel = ElasticsearchTreePanel(project)

        tablePanel = ElasticsearchRestTablePanel(
            project,
            elasticsearchRestPanel
        )

        tabbedPane.tabPlacement = SwingConstants.BOTTOM
        add(tabbedPane)
    }

    override fun updateResult(responseContext: ResponseContext): Collection<out CapTableItem> {
        val title = if (tabbedPane.selectedIndex == -1) null else tabbedPane.getTitleAt(tabbedPane.selectedIndex)

        responseContext as RestResponseContext

        if (tabbedPane.indexOfTab(textTitle) > -1) {
            tabbedPane.removeTabAt(tabbedPane.indexOfTab(textTitle))
        }
        if (tabbedPane.indexOfTab(jsonTitle) > -1) {
            tabbedPane.removeTabAt(tabbedPane.indexOfTab(jsonTitle))
        }
        if (tabbedPane.indexOfTab(treeTitle) > -1) {
            tabbedPane.removeTabAt(tabbedPane.indexOfTab(treeTitle))
        }
        if (responseContext.response.tableMode == TableMode.NONE_TABLE &&
            tabbedPane.indexOfTab(tableTitle) > -1
        ) {
            tabbedPane.removeTabAt(tabbedPane.indexOfTab(tableTitle))
        }

        if (responseContext.response.textMode == TextMode.TEXT && responseContext.response.status.statusCode in 200..299) {
            tabbedPane.insertTab(
                textTitle,
                Icons.FileType.TEXT_ICON,
                textPanel,
                textTitle,
                0
            )
            textPanel.updateEditorText(responseContext)
        }

        if (responseContext.response.textMode == TextMode.JSON || responseContext.response.status.statusCode !in 200..299) {
            tabbedPane.insertTab(
                jsonTitle,
                Icons.FileType.JSON_ICON,
                jsonPanel,
                jsonTitle,
                0
            )
            jsonPanel.updateEditorText(responseContext)

            tabbedPane.insertTab(
                treeTitle,
                Icons.Actions.SHOW_AS_TREE_ICON,
                treePanel,
                treeTitle,
                1
            )
            treePanel.updateResultTree(responseContext)
        }

        val datalist = if (responseContext.response.tableMode != TableMode.NONE_TABLE) {
            if (tabbedPane.indexOfTab(tableTitle) == -1) {
                tabbedPane.insertTab(
                    tableTitle,
                    Icons.General.DATA_TABLE_ICON,
                    tablePanel,
                    tableTitle,
                    tabbedPane.tabCount
                )
            }
            tablePanel.updateResultTable(responseContext)
        } else {
            emptyList()
        }

        if (tabbedPane.indexOfTab(title ?: "") == -1) {
            tabbedPane.selectedIndex = 0
        } else {
            tabbedPane.selectedIndex = tabbedPane.indexOfTab(title ?: "")
        }

        return datalist
    }

    override fun dispose() {
    }
}