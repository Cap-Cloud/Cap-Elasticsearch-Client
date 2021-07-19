package com.cap.plugins.common.component.table

import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.treeStructure.treetable.TreeTable
import javax.swing.JTable
import javax.swing.JTree

class CapTreeTableJsonKeyCellRenderer : ColoredTreeCellRenderer() {
    override fun customizeCellRenderer(
        tree: JTree,
        value: Any?,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ) {
        val descriptor: CapTreeTableJsonNodeDescriptor = (value as CapJsonTreeNode).descriptor

        descriptor.renderNode(this)
    }
}


class CapTreeTableJsonValueCellRenderer : ColoredTableCellRenderer() {
    override fun customizeCellRenderer(
        table: JTable,
        value: Any?,
        selected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ) {
        val tree = (table as TreeTable).tree
        val pathForRow = tree.getPathForRow(row)

        val node: CapJsonTreeNode = pathForRow.lastPathComponent as CapJsonTreeNode

        val descriptor: CapTreeTableJsonNodeDescriptor = node.descriptor
        descriptor.renderValue(this, tree.isExpanded(pathForRow))
    }
}