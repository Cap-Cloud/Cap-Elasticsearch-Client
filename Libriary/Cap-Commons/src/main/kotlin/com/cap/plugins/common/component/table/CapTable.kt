package com.cap.plugins.common.component.table

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.ui.JBColor
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Font
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.*
import javax.swing.JComponent
import javax.swing.event.ListSelectionEvent
import javax.swing.table.*
import kotlin.math.max
import kotlin.math.min

class CapTable : JBTable {
    val sortModel: CapSortModel = CapSortModel()
    val headerMouseListeners: MutableList<((sortModel: CapSortModel) -> Unit)> = mutableListOf()

    constructor() : super() {
        initComponent()
    }

    constructor(model: CapDefaultTableModel) : super(model) {
        initComponent()
    }

    constructor(model: TableModel, columnModel: TableColumnModel) : super(model, columnModel) {
        initComponent()
    }

    private fun initComponent() {
        val colorsScheme = EditorColorsManager.getInstance().globalScheme
        val font: Font = colorsScheme.getFont(EditorFontType.PLAIN)
        setFont(font)

        autoResizeMode = JBTable.AUTO_RESIZE_OFF
        cellSelectionEnabled = true

        setDefaultRenderer(Any::class.java, CapTableColoredCellRenderer.instance)

        tableHeader.defaultRenderer = CapTableColumnHeaderCellRenderer()
        // JetBrains Mono 字体不支持中文展示
        // tableHeader.font = font
        tableHeader.background = CapTableUI.getResultTableHeaderColor()
        tableHeader.foreground = colorsScheme.defaultForeground
        tableHeader.border = JBUI.Borders.customLine(JBColor.border(), 1, 0, 0, 0)
        tableHeader.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                if (rowAtPoint(e.point) == 0 || rowCount == 0) {
                    val columnViewIndex: Int = columnAtPoint(e.point)
                    if (columnViewIndex == -1) return
                    val columnModelIndex: Int = convertColumnIndexToModel(columnViewIndex)
                    val columnInfo = getColumnInfo(columnViewIndex)

                    if (columnInfo?.sortField != null) {
                        sortModel.changeSort(columnInfo.sortField)
                        val tableModel: CapDefaultTableModel = model as CapDefaultTableModel
                        // 客户端排序
                        var vector = tableModel.dataVector.map {
                            (it as Vector<Any>)
                        }
                        sortModel.getSortFields().reversed().forEach { field ->
                            vector = when (columnInfo.sortType) {
                                CapSortType.DOUBLE -> when (field.order) {
                                    CapSortOrder.ASC -> when (columnModelIndex) {
                                        else -> vector.sortedBy {
                                            it[columnModelIndex].toString().toDouble()
                                        }
                                    }
                                    CapSortOrder.DESC -> when (columnModelIndex) {
                                        else -> vector.sortedByDescending {
                                            it[columnModelIndex].toString().toDouble()
                                        }
                                    }
                                }
                                CapSortType.LONG -> when (field.order) {
                                    CapSortOrder.ASC -> when (columnModelIndex) {
                                        else -> vector.sortedBy {
                                            it[columnModelIndex].toString().toLong()
                                        }
                                    }
                                    CapSortOrder.DESC -> when (columnModelIndex) {
                                        else -> vector.sortedByDescending {
                                            it[columnModelIndex].toString().toLong()
                                        }
                                    }
                                }
                                else -> when (field.order) {
                                    CapSortOrder.ASC -> when (columnModelIndex) {
                                        else -> vector.sortedBy {
                                            it[columnModelIndex].toString()
                                        }
                                    }
                                    CapSortOrder.DESC -> when (columnModelIndex) {
                                        else -> vector.sortedByDescending {
                                            it[columnModelIndex].toString()
                                        }
                                    }
                                }
                            }
                        }
                        tableModel.dataVector.clear()
                        vector.forEach {
                            tableModel.addRow(it)
                        }
                        // 额外操作
                        headerMouseListeners.forEach {
                            it.invoke(sortModel)
                        }
                    }
                }
            }
        })

        gridColor = CapTableUI.getTableGridColor()

        selectionForeground = null

        columnModel.addColumnModelListener(object : CapTableColumnModelListener() {
            override fun columnSelectionChanged(e: ListSelectionEvent?) {
                if (e == null) {
                    return
                }
                tableHeader.repaint(tableHeader.getHeaderRect(e.firstIndex))
                tableHeader.repaint(tableHeader.getHeaderRect(e.lastIndex))
            }
        })

        addFocusListener(object : FocusAdapter() {
            override fun focusLost(e: FocusEvent) {
                clearSelection()
            }
        })
    }

    fun registerHeaderMouseListener(listener: (sortModel: CapSortModel) -> Unit) {
        headerMouseListeners.add(listener)
    }

    fun getColumnInfo(column: Int): CapTableColumnInfo? {
        return try {
            (model as CapDefaultTableModel).columnInfos[convertColumnIndexToModel(column)]
        } catch (e: Exception) {
            null
        }
    }

    fun getColumnModelIndex(column: Int): Int {
        return convertColumnIndexToModel(column)
    }

    fun getRowModelIndex(row: Int): Int {
        return convertRowIndexToModel(row)
    }

    fun adjustColumnsBySize() {
        for (column in 0 until columnCount) {
            val tableColumn: TableColumn = getColumnModel().getColumn(column)
            val renderer = tableHeader.defaultRenderer as CapTableColumnHeaderCellRenderer
            val header: Component =
                renderer.getTableCellRendererComponent(
                    table = this,
                    value = tableColumn.headerValue,
                    isSelected = false,
                    hasFocus = false,
                    row = 0,
                    column = column
                )
            var preferredWidth = header.preferredSize.width + 10
            val maxWidth = max(500, preferredWidth)
            for (row in 0 until rowCount) {
                val cellRenderer: TableCellRenderer = getCellRenderer(row, column)
                val c: Component = prepareRenderer(cellRenderer, row, column)
                val width: Int = c.preferredSize.width + intercellSpacing.width
                preferredWidth = min(max(preferredWidth, width), maxWidth)

                if (preferredWidth == maxWidth) {
                    break
                }
            }
            tableColumn.preferredWidth = preferredWidth
        }
    }
}

open class CapTableWithPagination : JComponent {
    private var table: CapTable
    private var tableModel: DefaultTableModel
    private var capPageToolbar: CapPageToolbar
    var rowHeader: CapTableRowHeader

    var dataList: Collection<out CapTableItem> = emptyList()
        set(value) {
            field = value
            capPageToolbar.dataList = value
        }

    var tempList: Collection<out CapTableItem> = emptyList()
        set(value) {
            field = value
            capPageToolbar.tempList = value
        }

    constructor(tableModel: CapDefaultTableModel) {
        this.tableModel = tableModel
        this.table = CapTable(tableModel)
        this.rowHeader = CapTableRowHeader(table)
        this.capPageToolbar = CapPageToolbar(this, tableModel).withTable(table)
        initComponent()
    }

    private fun initComponent() {
        layout = BorderLayout()

        val panel = ScrollPaneFactory.createScrollPane(table)
        panel.setRowHeaderView(rowHeader)

        add(panel, BorderLayout.CENTER)

        add(capPageToolbar, BorderLayout.SOUTH)
    }

    fun refreshRowHeader() {
        (rowHeader.getDefaultRenderer(Any::class.java) as CapTableRowHeaderCellRenderer).tableChanged(null)
    }

    fun withAction(action: AnAction): CapTableWithPagination {
        capPageToolbar.withAction(action)
        return this
    }

    fun withActions(vararg actions: AnAction): CapTableWithPagination {
        capPageToolbar.withActions(*actions)
        return this
    }

    fun registerPageStatusListener(listener: CapPageStatusListener) {
        capPageToolbar.registerPageStatusListener(listener)
    }

    fun registerPageActionListener(listener: CapPageActionListener) {
        capPageToolbar.registerPageActionListener(listener)
    }

    fun registerSizeChangeActionListener(listener: CapPageSizeChangeListener) {
        capPageToolbar.registerSizeChangeActionListener(listener)
    }

    fun registerHeaderMouseListener(listener: (sortModel: CapSortModel) -> Unit) {
        table.registerHeaderMouseListener(listener)
    }

    fun refreshCurrentPage() {
        capPageToolbar.refreshCurrentPage()
    }

    fun getColumnModel(): TableColumnModel {
        return table.columnModel
    }

    fun setPageSize(size: Int) {
        capPageToolbar.setPageSize(size)
    }

    fun setRowIndexFrom(indexFrom: Int) {
        rowHeader.indexFrom = indexFrom
    }

    fun getSelectedRows(): IntArray? {
        return table.selectedRows
    }

    fun getSelectedRowCount(): Int {
        return table.selectedRowCount
    }

    fun getSelectedRow(): Int {
        return table.selectedRow
    }
}
