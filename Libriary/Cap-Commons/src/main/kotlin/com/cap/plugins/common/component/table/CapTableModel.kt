package com.cap.plugins.common.component.table

import java.util.*
import javax.swing.table.DefaultTableModel

open class CapDefaultTableModel : DefaultTableModel {

    var columnInfos: MutableList<CapTableColumnInfo> = mutableListOf()

    constructor() : super()

    constructor(rowCount: Int, columnCount: Int) : super(rowCount, columnCount)

    constructor(columnNames: Vector<Any>, rowCount: Int) : super(columnNames, rowCount) {
        columnNames?.forEach { columnName ->
            columnInfos.add(CapTableColumnInfo(columnName.toString(), CapSortType.STRING, columnName.toString()))
        }
    }

    constructor(data: Vector<out Vector<*>>, columnNames: Vector<Any>) : super(data, columnNames) {
        columnNames?.forEach { columnName ->
            columnInfos.add(CapTableColumnInfo(columnName.toString(), CapSortType.STRING, columnName.toString()))
        }
    }

    fun clearAll() {
        dataVector.clear()
        columnIdentifiers.clear()
        columnInfos.clear()
    }

    fun createStringColumns(names: Collection<String>) {
        clearAll()
        setColumnIdentifiers(names.toTypedArray())
    }

    fun createTableColumns(names: Collection<CapTableColumnInfo>) {
        clearAll()
        setColumnIdentifiers(names)
    }

    fun setColumnIdentifiers(columnIdentifiers: Collection<CapTableColumnInfo>?) {
        setColumnIdentifiers(columnIdentifiers?.map { it.columnName }?.toTypedArray())
        setColumnInfos(columnIdentifiers)
    }

    private fun setColumnInfos(columnInfos: Collection<CapTableColumnInfo>?) {
        this.columnInfos.clear()
        columnInfos?.forEach {
            this.columnInfos.add(it)
        }
    }

    override fun setColumnIdentifiers(columnIdentifiers: Vector<*>?) {
        super.setColumnIdentifiers(columnIdentifiers)
        setColumnInfos(columnIdentifiers?.map {
            CapTableColumnInfo(it.toString(), CapSortType.STRING, it.toString())
        })
    }

    override fun addColumn(column: Any?, columnData: Vector<*>?) {
        if (column is CapTableColumnInfo) {
            super.addColumn(column.columnName, columnData)
            columnInfos.add(column)
        } else {
            super.addColumn(column, columnData)
            columnInfos.add(CapTableColumnInfo(column.toString(), CapSortType.STRING, column.toString()))
        }
    }


    fun isColumnEquals(columns: Collection<String>): Boolean {
        if (columns.size != columnCount) {
            return false
        }

        for (i in columns.indices) {
            if (columnInfos[i].columnName != columns.elementAt(i)) {
                return false
            }
        }

        return true
    }
}

open class CapRowHeaderTableModel(rowCount: Int, columnCount: Int) : DefaultTableModel(rowCount, columnCount) {
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        return rowIndex
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }
}