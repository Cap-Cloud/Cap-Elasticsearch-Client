package com.cap.plugins.common.ui.explorer.tree

import com.cap.plugins.common.constant.CapCommonConstant
import com.cap.plugins.common.ui.explorer.table.CapExplorerTableNode
import com.cap.plugins.common.util.showErrorDialog
import java.util.concurrent.ExecutionException
import javax.swing.event.TableModelEvent
import javax.swing.tree.DefaultMutableTreeNode

abstract class CapClusterTreeNode : DefaultMutableTreeNode, CapExplorerTreeNode, CapExplorerTableNode {
    val loadingNode = DefaultMutableTreeNode(CapCommonConstant.TREE_NODE_LOADING)

    constructor(userObject: Any) : super(userObject) {
        add(loadingNode)
    }

    override fun refresh() {
        removeAllChildren()
        add(loadingNode)
        expand()
    }

    override fun expand() {
        if (childCount > 0 && getChildAt(0) == loadingNode) {
            getClusterTreeRoot().let { it1 ->
                try {
                    readChildren(it1).let { it2 ->
                        removeAllChildren()
                        it2.forEach { c -> add(c) }
                    }
                } catch (e: ExecutionException) {
                    showErrorDialog(e.message ?: "", e)
                }
            }
        }
    }

    override var listener: ((e: TableModelEvent) -> Unit)? = null

    private fun getClusterTreeRoot(): CapClusterTreeRootNode {
        var ancestor: DefaultMutableTreeNode? = this
        while (ancestor !is CapClusterTreeRootNode) {
            ancestor = ancestor?.parent as DefaultMutableTreeNode?
        }
        return ancestor
    }

    abstract fun readChildren(root: CapClusterTreeRootNode): List<DefaultMutableTreeNode>
}