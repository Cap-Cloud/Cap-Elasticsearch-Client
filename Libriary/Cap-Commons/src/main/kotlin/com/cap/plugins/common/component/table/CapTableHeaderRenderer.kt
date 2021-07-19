package com.cap.plugins.common.component.table

import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.ui.CellRendererPanel
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBUI
import java.awt.*
import javax.swing.Icon
import javax.swing.JLabel
import javax.swing.JTable
import javax.swing.SwingConstants
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener


class CapTableColumnHeaderCellRenderer : DefaultTableCellHeaderRenderer {

    constructor() : super() {
        val colorsScheme = EditorColorsManager.getInstance().globalScheme
        font = colorsScheme.getFont(EditorFontType.PLAIN)
    }

    override fun getTableCellRendererComponent(
        table: JTable,
        value: Any?,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component {
        val label = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column) as JLabel
        val columnInfo = (table as CapTable).getColumnInfo(column)
        val panel = createColumnHeaderCellPanel(table, label, table.sortModel, columnInfo)
        var border = panel.border
        val indent = JBUI.Borders.empty(0, 8, 0, 0)
        border = JBUI.Borders.merge(border, indent, true)
        border = JBUI.Borders.merge(border, JBUI.Borders.customLine(CapTableUI.getTableGridColor(), 1, 0, 1, 1), true)
        panel.border = border
        if (table.isColumnSelected(column)) {
            panel.background = CapTableUI.getSelectedLineColor()
        }
        return panel
    }

    private fun createColumnHeaderCellPanel(
        table: JTable,
        nameLabel: JLabel,
        sortModel: CapSortModel,
        columnInfo: CapTableColumnInfo?
    ): CapTableColumnHeaderCellRendererPanel {
        val sortField = sortModel.getSortFields().find { it.field == columnInfo?.sortField?.fieldName }
        val sortIcon = when {
            columnInfo?.sortField?.fieldName == null -> {
                null
            }
            sortField == null -> {
                AllIcons.General.ArrowSplitCenterV
            }
            sortField.order == CapSortOrder.ASC -> {
                AllIcons.General.ArrowUp
            }
            else -> {
                AllIcons.General.ArrowDown
            }
        }
        val sortText = if (sortField != null) {
            (sortModel.getSortFields().indexOf(sortField) + 1).toString()
        } else {
            null
        }
        return CapTableColumnHeaderCellRendererPanel(
            table = table,
            text = nameLabel.text,
            tooltip = columnInfo?.tooltip,
            sortIcon = sortIcon,
            sortText = sortText
        )
    }
}

class CapTableColumnHeaderCellRendererPanel : CellRendererPanel {

    constructor(
        table: JTable,
        text: String,
        tooltip: String?,
        sortIcon: Icon?,
        sortText: String?
    ) : super() {
        val myNameLabel: JLabel = JBLabel()
        val mySortLabel: JLabel = JBLabel()
        myNameLabel.border = IdeBorderFactory.createEmptyBorder(NAME_LABEL_ROW_INSETS)
        mySortLabel.border = IdeBorderFactory.createEmptyBorder(SORT_LABEL_ROW_INSETS)
        mySortLabel.verticalAlignment = 0

        val font: Font = table.tableHeader.font
        val foreground: Color = table.foreground
        myNameLabel.font = font
        mySortLabel.font = font
        myNameLabel.foreground = foreground
        mySortLabel.foreground = foreground
        myNameLabel.text = text
        mySortLabel.icon = sortIcon
        mySortLabel.text = sortText

        tooltip?.let {
            toolTipText = it
        }

        this.layout = BorderLayout()
        this.add(myNameLabel, BorderLayout.CENTER)
        this.add(mySortLabel, BorderLayout.EAST)
    }

    companion object {
        private val NAME_LABEL_ROW_INSETS: Insets = JBUI.insets(0, 3, 0, 0)
        private val SORT_LABEL_ROW_INSETS: Insets = JBUI.insets(0, 2, 0, 3)
    }
}

class CapTableRowHeaderCellRenderer(
    private val refTable: CapTable,
    private val showTable: CapTableRowHeader,
    var indexFrom: Int? = 0
) : DefaultTableCellHeaderRenderer(), TableModelListener, ListSelectionListener {

    init {
        val colorsScheme = EditorColorsManager.getInstance().globalScheme
        font = colorsScheme.getFont(EditorFontType.PLAIN)

        refTable.model.addTableModelListener(this)
        refTable.selectionModel.addListSelectionListener(this)
    }

    override fun getTableCellRendererComponent(
        table: JTable?,
        value: Any?,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component? {
        val cmp: Component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column)
        if (cmp !is JLabel) return cmp
        cmp.horizontalAlignment = SwingConstants.CENTER
        var border = JBUI.Borders.customLine(CapTableUI.getTableBackground(), 1, 0, 0, 0)
        val indent = JBUI.Borders.empty(0, 8)
        border = JBUI.Borders.merge(border, indent, false)
        cmp.border = border
        if (table?.isRowSelected(row) == true) {
            cmp.background = CapTableUI.getSelectedLineColor()

            refTable.selectionModel.removeListSelectionListener(this)
            refTable.selectionModel.clearSelection()
            table?.selectedRows.forEach {
                refTable.selectionModel.addSelectionInterval(it, it)
            }
            refTable.selectionModel.addListSelectionListener(this)
        }
        if (refTable.isRowSelected(row)) {
            cmp.background = CapTableUI.getSelectedLineColor()
        }
        table?.setRowHeight(row, refTable.getRowHeight(row))
        cmp.text = "${(indexFrom ?: 0) + row + 1}"
        return cmp
    }

    override fun valueChanged(e: ListSelectionEvent?) {
        showTable.repaint()
    }

    override fun tableChanged(e: TableModelEvent?) {
        showTable.model = CapRowHeaderTableModel(refTable.rowCount, 1)
        showTable.repaint()
    }
}
