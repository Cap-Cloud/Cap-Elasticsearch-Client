package com.cap.plugins.elasticsearch.ui.explorer.table

import com.cap.plugins.common.ui.explorer.table.CapExplorerTableModel
import com.cap.plugins.common.ui.explorer.table.CapExplorerTableNode
import com.cap.plugins.common.ui.explorer.tree.CapClusterTreeRootNode
import com.cap.plugins.common.component.table.CapTableArrayItem
import com.cap.plugins.elasticsearch.ui.explorer.ElasticsearchBrowserToolWindowExplorer
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import javax.swing.tree.TreeNode

class ElasticsearchTableModel : CapExplorerTableModel() {

    override fun updateDetails(project: Project, node: TreeNode?, task: (() -> Unit)?) {
        super.updateDetails(project, node) {
            if (node != null) {
                if (node is CapExplorerTableNode) {
                    // 准备列
                    // columnInfos = createColumns(node.headers())
                    // node.headers().forEach { addColumn(it) }
                    // setColumnIdentifiers(node.headers().toTypedArray())
                    createStringColumns(node.headers())
                    // 准备行
                    project.service<ElasticsearchBrowserToolWindowExplorer>().table.dataList =
                        node.rows().mapIndexed { index, strings -> CapTableArrayItem(index, strings) }
                }
                if(node is CapClusterTreeRootNode){
                }
            }
        }
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        if (currentNode is CapClusterTreeRootNode) {
            return column == 1
        }
        return false
    }
}