package com.cap.plugins.common.component.table

import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.ui.table.JBTable
import java.awt.Dimension
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent

class CapTableRowHeader(refTable: CapTable) :
    JBTable(CapRowHeaderTableModel(refTable.rowCount, 1)) {

    var indexFrom: Int = 0
        set(value) {
            field = value
            indexFromShow = value
        }
    var indexFromShow: Int = 0
        set(value) {
            field = value
            rowHeaderCellRenderer.indexFrom = value
        }


    private var rowHeaderCellRenderer: CapTableRowHeaderCellRenderer

    init {
        val colorsScheme = EditorColorsManager.getInstance().globalScheme
        font = colorsScheme.getFont(EditorFontType.PLAIN)

        emptyText.text = ""
        autoResizeMode = JBTable.AUTO_RESIZE_OFF
        cellSelectionEnabled = true

        rowHeaderCellRenderer = CapTableRowHeaderCellRenderer(refTable, this, indexFromShow)
        setDefaultRenderer(Any::class.java, rowHeaderCellRenderer)

        gridColor = CapTableUI.getTableGridColor()

        selectionForeground = null

        addFocusListener(object : FocusAdapter() {
            override fun focusLost(e: FocusEvent) {
                clearSelection()
            }
        })

        rowHeight = refTable.rowHeight

        preferredScrollableViewportSize = Dimension(columnModel.getColumn(0).preferredWidth, 0)
    }
}