package com.cap.plugins.common.ui.explorer.table

import com.cap.plugins.common.component.table.CapDefaultTableModel
import com.intellij.openapi.project.Project
import javax.swing.event.TableModelEvent
import javax.swing.tree.TreeNode

open class CapExplorerTableModel : CapDefaultTableModel {
    // open class CapExplorerTableModel : CapListTableModel() {
    var mute = false
    var currentNode: TreeNode? = null

    var listener: ((e: TableModelEvent) -> Unit)? = null

    constructor() : super() {
        // columnInfos = createColumns(listOf("Property", "Value"))
        clearAll()
        setColumnIdentifiers(arrayOf("Property", "Value"))
        // listOf("Property", "Value").forEach { addColumn(it) }
        addTableModelListener {
            if (!mute) {
                listener?.invoke(it)
            }
        }
    }

    open fun updateDetails(project: Project, node: TreeNode?, task: (() -> Unit)? = null) {
        mute = true
        currentNode = node
        clearAll()
        task?.invoke()
        mute = false
    }

    override fun isCellEditable(row: Int, column: Int): Boolean = false
}

//open class CapExplorerTableModel : DefaultTableModel() {
//    var mute = false
//    var currentNode: TreeNode? = null
//
//    var listener: ((e: TableModelEvent) -> Unit)? = null
//
//    init {
//        listOf("Property", "Value").forEach { addColumn(it) }
//        addTableModelListener {
//            if (!mute) {
//                listener?.invoke(it)
//            }
//        }
//    }
//
//    open fun updateDetails(project: Project, node: TreeNode?, task: (() -> Unit)? = null) {
//        mute = true
//        currentNode = node
//        dataVector.clear()
//        columnIdentifiers.clear()
//        task?.invoke()
//        mute = false
//    }
//
//    override fun isCellEditable(row: Int, column: Int): Boolean = false
//}