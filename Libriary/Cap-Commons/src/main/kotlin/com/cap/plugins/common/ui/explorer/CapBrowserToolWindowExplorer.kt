package com.cap.plugins.common.ui.explorer

import com.cap.plugins.common.ui.explorer.table.CapExplorerTableModel
import com.cap.plugins.common.component.table.CapTableWithPagination
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import javax.swing.JTabbedPane

abstract class CapBrowserToolWindowExplorer(val project:Project): SimpleToolWindowPanel(true, true), Disposable {
    val tableModel by lazy { defaultTableModel() }

    abstract fun defaultTableModel(): CapExplorerTableModel

    // val table by lazy { CapTableViewWithPagination(tableModel) }
    val table by lazy { CapTableWithPagination(tableModel) }

    override fun dispose() {
        this.components.forEach { component ->
            if (component is JTabbedPane) {
                (0 until component.componentCount).forEach { index ->
                    val panel = component.getComponentAt(index)
                    if (panel is Disposable) {
                        panel.dispose()
                    }
                }
            }
        }
        removeAll()
    }

    companion object {
        fun getInstance(serviceClass: Class<out CapBrowserToolWindowExplorer>? = null): CapBrowserToolWindowExplorer {
            return ApplicationManager.getApplication()
                .getService(serviceClass ?: CapBrowserToolWindowExplorer::class.java)
        }
    }
}