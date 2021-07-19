package com.cap.plugins.common.ui.explorer.tree

import javax.swing.tree.MutableTreeNode

interface CapExplorerTreeNode : MutableTreeNode {
    fun refresh()
    fun expand()
}