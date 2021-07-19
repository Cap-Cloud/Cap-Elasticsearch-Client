package com.cap.plugins.elasticsearch.ui.editor.kql.devtools.console.panel

import com.cap.plugins.common.component.table.CapTableItem
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.elasticsearch.ui.editor.kql.panel.ElasticsearchKqlTablePanel
import com.cap.plugins.elasticsearch.ui.editor.model.ResponseContext
import com.cap.plugins.elasticsearch.ui.editor.model.RestResponseContext
import com.cap.plugins.elasticsearch.ui.editor.model.TableMode
import com.cap.plugins.elasticsearch.ui.editor.model.TextMode
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchJsonPanel
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchResultPanel
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchTextPanel
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchTreePanel
import com.intellij.openapi.project.Project
import com.intellij.ui.jcef.JBCefBrowser
import javax.swing.SwingConstants


class ElasticsearchKqlConsoleResultPanel : ElasticsearchResultPanel {
    val project: Project
    private val consolePanel: ElasticsearchKqlConsolePanel

    private var textPanel: ElasticsearchTextPanel
    private var jsonPanel: ElasticsearchJsonPanel
    private var treePanel: ElasticsearchTreePanel

    private var apiTitle = "API Documentation"
    private val jbCefBrowser: JBCefBrowser by lazy { JBCefBrowser() }

    private var tablePanel: ElasticsearchKqlTablePanel

    constructor(project: Project, consolePanel: ElasticsearchKqlConsolePanel) : super(consolePanel) {
        this.project = project
        this.consolePanel = consolePanel

        textPanel = ElasticsearchTextPanel(project)
        jsonPanel = ElasticsearchJsonPanel(project)
        treePanel = ElasticsearchTreePanel(project)

        tablePanel = ElasticsearchKqlTablePanel(
            project,
            consolePanel
        )

        tabbedPane.tabPlacement = SwingConstants.BOTTOM
        add(tabbedPane)
    }

    override fun updateResult(responseContext: ResponseContext): Collection<CapTableItem> {
        val title = if (tabbedPane.selectedIndex == -1) null else tabbedPane.getTitleAt(tabbedPane.selectedIndex)

        responseContext as RestResponseContext

        if (tabbedPane.indexOfTab(textTitle) > -1) {
            tabbedPane.removeTabAt(tabbedPane.indexOfTab(textTitle))
        }
        if (responseContext.response.textMode == TextMode.TEXT && responseContext.response.status.statusCode in 200..299) {
            tabbedPane.insertTab(
                textTitle,
                Icons.FileType.TEXT_ICON,
                textPanel,
                textTitle,
                tabbedPane.tabCount
            )
            textPanel.updateEditorText(responseContext)
        }

        if (tabbedPane.indexOfTab(jsonTitle) > -1) {
            tabbedPane.removeTabAt(tabbedPane.indexOfTab(jsonTitle))
        }
        if (tabbedPane.indexOfTab(treeTitle) > -1) {
            tabbedPane.removeTabAt(tabbedPane.indexOfTab(treeTitle))
        }
        if (responseContext.response.textMode == TextMode.JSON || responseContext.response.status.statusCode !in 200..299) {
            tabbedPane.insertTab(
                jsonTitle,
                Icons.FileType.JSON_ICON,
                jsonPanel,
                jsonTitle,
                tabbedPane.tabCount
            )
            jsonPanel.updateEditorText(responseContext)

            tabbedPane.insertTab(
                treeTitle,
                Icons.Actions.SHOW_AS_TREE_ICON,
                treePanel,
                treeTitle,
                tabbedPane.tabCount
            )
            treePanel.updateResultTree(responseContext)
        }

        if (responseContext.response.tableMode == TableMode.NONE_TABLE &&
            tabbedPane.indexOfTab(tableTitle) > -1
        ) {
            tabbedPane.removeTabAt(tabbedPane.indexOfTab(tableTitle))
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

    fun openDocumentation(documentation: String) {
//        if (!JBCefApp.isSupported()) {
//            return
//        }
        try {
            jbCefBrowser.loadURL(documentation)
            if (tabbedPane.indexOfTab(apiTitle) == -1) {
                tabbedPane.insertTab(
                    apiTitle,
                    Icons.FileType.HTML_ICON,
                    jbCefBrowser.component,
                    apiTitle,
                    tabbedPane.tabCount
                )
            }
            tabbedPane.selectedIndex = tabbedPane.indexOfTab(apiTitle)
        } catch (e:Throwable){
            // ignore
        }
    }

    override fun dispose() {
    }
}