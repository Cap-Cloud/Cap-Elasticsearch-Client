package com.cap.plugins.common.ui.explorer.tree

import com.cap.plugins.common.ui.explorer.table.CapExplorerTableNode
import javax.swing.event.TableModelEvent
import javax.swing.tree.DefaultMutableTreeNode

abstract class CapClusterTreeLeafNode(userObject: Any) : DefaultMutableTreeNode(userObject), CapExplorerTreeNode, CapExplorerTableNode {

    override fun refresh() {
    }

    override fun expand() {
    }

    override var listener: ((e: TableModelEvent) -> Unit)? = null

    fun getClusterTreeRoot(): CapClusterTreeRootNode {
        var ancestor: DefaultMutableTreeNode? = this
        while (ancestor !is CapClusterTreeRootNode) {
            ancestor = ancestor?.parent as DefaultMutableTreeNode?
        }
        return ancestor
    }
}