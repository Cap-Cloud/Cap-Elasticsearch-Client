package com.cap.plugins.common.component.table

import com.cap.plugins.common.util.ObjectMapper
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.intellij.ui.TreeTableSpeedSearch
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns
import com.intellij.ui.treeStructure.treetable.TreeTable
import com.intellij.ui.treeStructure.treetable.TreeTableModel
import com.intellij.util.containers.Convertor
import com.intellij.util.ui.ColumnInfo
import javax.swing.table.TableCellRenderer
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeNode
import javax.swing.tree.TreePath

abstract class CapTreeTable : TreeTable {
    var rootNode: TreeNode
    var columnInfos: Array<ColumnInfo<out Any?, out Any?>>

    constructor(rootNode: TreeNode, columnInfos: Array<ColumnInfo<out Any?, out Any?>>) : super(
        ListTreeTableModelOnColumns(rootNode, columnInfos)
    ) {
        this.rootNode = rootNode
        this.columnInfos = columnInfos
    }
}

class CapJsonTreeNode : DefaultMutableTreeNode {
    val descriptor: CapTreeTableJsonNodeDescriptor

    constructor(descriptor: CapTreeTableJsonNodeDescriptor) : super(descriptor) {
        this.descriptor = descriptor
    }
}

class CapJsonTreeTable : CapTreeTable {

    constructor(
        json: String
    ) : super(
        convertJsonToTreeNode(json),
        COLUMNS_FOR_READING
    ) {
        tree.showsRootHandles = true
        tree.isRootVisible = false

        setTreeCellRenderer(CapTreeTableJsonKeyCellRenderer())
        TreeTableSpeedSearch(
            this,
            Convertor { path: TreePath ->
                val node: CapJsonTreeNode = path.lastPathComponent as CapJsonTreeNode
                val descriptor: CapTreeTableJsonNodeDescriptor = node.descriptor
                descriptor.key
            }
        )
        // UIUtil.invokeAndWaitIfNeeded((Runnable { TreeUtil.expand(tree, 2) })!!)
    }

    fun updateResultTree(json: String) {
        val treeNode = convertJsonToTreeNode(json)
        val model = ListTreeTableModelOnColumns(treeNode, columnInfos)
        tree.treeTable.tableModel = model
    }

    override fun getCellRenderer(row: Int, column: Int): TableCellRenderer? {
        val treePath = tree.getPathForRow(row) ?: return super.getCellRenderer(row, column)

        val node: CapJsonTreeNode = treePath.lastPathComponent as CapJsonTreeNode
        val renderer: TableCellRenderer? =
            (columnInfos[column] as ColumnInfo<CapJsonTreeNode?, CapTreeTableJsonNodeDescriptor?>).getRenderer(node)
        return renderer ?: super.getCellRenderer(row, column)
    }

    companion object {
        val KEY: ColumnInfo<out Any?, out Any?> =
            object : ColumnInfo<CapJsonTreeNode?, CapTreeTableJsonNodeDescriptor?>("Key") {

                override fun getColumnClass(): Class<out Any>? {
                    return TreeTableModel::class.java
                }

                override fun valueOf(item: CapJsonTreeNode?): CapTreeTableJsonNodeDescriptor? {
                    return item?.descriptor
                }

                override fun isCellEditable(item: CapJsonTreeNode?): Boolean {
                    return false
                }
            }

        val READONLY_VALUE: ColumnInfo<out Any?, out Any?> =
            object : ColumnInfo<CapJsonTreeNode, CapTreeTableJsonNodeDescriptor>("Value") {
                val renderer = CapTreeTableJsonValueCellRenderer()

                override fun valueOf(item: CapJsonTreeNode?): CapTreeTableJsonNodeDescriptor? {
                    return item?.descriptor
                }

                override fun isCellEditable(item: CapJsonTreeNode?): Boolean {
                    return false
                }

                override fun getRenderer(item: CapJsonTreeNode?): TableCellRenderer? {
                    return renderer
                }

            }

        val COLUMNS_FOR_READING = arrayOf(KEY, READONLY_VALUE)

        fun convertJsonToTreeNode(json: String): CapJsonTreeNode {
            val jsonNode = ObjectMapper.jsonMapper.readTree(json)
            val rootNode = CapJsonTreeNode(CapTreeTableJsonValueDescriptor.createDescriptor("Root", "Root"))
            processSubNode(rootNode, jsonNode)
            return rootNode
        }

        fun processSubNode(parent: CapJsonTreeNode, jsonNode: JsonNode): CapJsonTreeNode {
            if (jsonNode is ArrayNode) {
                jsonNode.forEachIndexed { index, node ->
                    parent.add(
                        processSubNode(
                            CapJsonTreeNode(
                                CapTreeTableJsonValueDescriptor.createDescriptor(index.toString(), node)
                            ), node
                        )
                    )
                }
            }

            if (jsonNode is ObjectNode) {
                jsonNode.fieldNames().forEach {
                    parent.add(
                        processSubNode(
                            CapJsonTreeNode(
                                CapTreeTableJsonValueDescriptor.createDescriptor(it, jsonNode.get(it))
                            ), jsonNode.get(it)
                        )
                    )
                }
            }

            return parent
        }
    }
}