package com.cap.plugins.common.ui.explorer

import com.cap.plugins.common.component.table.CapTableWithPagination
import com.cap.plugins.common.ui.explorer.table.CapPropertiesTableModel
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import java.awt.BorderLayout

abstract class CapBrowserProperties(val project: Project) : SimpleToolWindowPanel(true, true), Disposable {
    val tableModel by lazy { defaultTableModel() }

    val table by lazy { CapTableWithPagination(tableModel) }

    init {
        add(table, BorderLayout.CENTER)
    }

    abstract fun defaultTableModel(): CapPropertiesTableModel

    override fun dispose() {
        this.components.forEach { component ->
            if (component is Disposable) {
                component.dispose()
            }
        }
        removeAll()
    }

    companion object {
        fun getInstance(serviceClass: Class<out CapBrowserProperties>? = null): CapBrowserProperties {
            return ApplicationManager.getApplication()
                .getService(serviceClass ?: CapBrowserProperties::class.java)
        }
    }
}