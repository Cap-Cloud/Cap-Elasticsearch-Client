package com.cap.plugins.elasticsearch.ui.editor.sql.panel

import com.cap.plugins.common.component.table.CapTableItem
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.elasticsearch.ui.editor.model.ResponseContext
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchJsonPanel
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchResultPanel
import com.intellij.openapi.project.Project
import javax.swing.SwingConstants

class ElasticsearchSqlResultPanel : ElasticsearchResultPanel {
    val project: Project

    private var jsonPanel: ElasticsearchJsonPanel
    private var tablePanel: ElasticsearchSqlTablePanel

    constructor(project: Project, elasticsearchSqlPanel: ElasticsearchSqlPanel) : super(elasticsearchSqlPanel) {
        this.project = project

        jsonPanel = ElasticsearchJsonPanel(project)
        tablePanel =
            ElasticsearchSqlTablePanel(project)

        tabbedPane.tabPlacement = SwingConstants.BOTTOM
        tabbedPane.insertTab(
            jsonTitle,
            Icons.FileType.JSON_ICON,
            jsonPanel,
            jsonTitle,
            tabbedPane.tabCount
        )
        tabbedPane.insertTab(
            tableTitle,
            Icons.General.DATA_TABLE_ICON,
            tablePanel,
            tableTitle,
            tabbedPane.tabCount
        )
        add(tabbedPane)
        tabbedPane.selectedIndex = 0
    }

    override fun updateResult(responseContext: ResponseContext): Collection<CapTableItem> {
        jsonPanel.updateEditorText(responseContext)
        tabbedPane.selectedIndex = 0
        return tablePanel.updateResultTable(responseContext)
    }

    override fun dispose() {
    }
}