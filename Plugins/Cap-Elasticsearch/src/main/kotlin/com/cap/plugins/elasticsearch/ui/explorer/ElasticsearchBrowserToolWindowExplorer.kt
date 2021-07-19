package com.cap.plugins.elasticsearch.ui.explorer

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.common.ui.explorer.CapBrowserToolWindowExplorer
import com.cap.plugins.common.ui.explorer.table.CapExplorerTableModel
import com.cap.plugins.elasticsearch.ui.explorer.table.ElasticsearchTableModel
import com.intellij.openapi.project.Project

@Keep
class ElasticsearchBrowserToolWindowExplorer(project: Project) : CapBrowserToolWindowExplorer(project) {
    override fun defaultTableModel(): CapExplorerTableModel = ElasticsearchTableModel()
}