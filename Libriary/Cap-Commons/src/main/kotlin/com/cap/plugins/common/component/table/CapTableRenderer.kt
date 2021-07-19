package com.cap.plugins.common.component.table

import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorFontType
import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.SimpleTextAttributes
import java.awt.Component
import javax.swing.JProgressBar
import javax.swing.JTable
import javax.swing.SwingConstants
import javax.swing.border.EmptyBorder
import javax.swing.table.TableCellRenderer

class CapTableColoredCellRenderer : ColoredTableCellRenderer {

    constructor() : super() {
        val colorsScheme = EditorColorsManager.getInstance().globalScheme
        font = colorsScheme.getFont(EditorFontType.PLAIN)
    }

    override fun customizeCellRenderer(
        table: JTable,
        value: Any?,
        selected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ) {
        border = EMPTY_BORDER
        background = when {
            table?.isCellSelected(row, column) == true -> CapTableUI.getSelectedCellColor()
            table?.isRowSelected(row) == true -> CapTableUI.getSelectedLineColor()
            else -> CapTableUI.getTableBackground()
        }

        if (value == null) {
            appendInternal("(null)", StyleAttributesProvider.getNullAttributes())
            return
        }
        if (value is Collection<Any?>) {
            if (value.size > 1) {
                appendInternal("[", StyleAttributesProvider.getBracesAttributes())
                value.asSequence().forEachIndexed { index, it ->
                    writeValue(it)
                    if (index != value.size - 1) {
                        appendInternal(", ", StyleAttributesProvider.getKeywordAttributes())
                    }
                }
                appendInternal("]", StyleAttributesProvider.getBracesAttributes())
            } else {
                writeValue(value.firstOrNull())
            }
        } else {
            writeValue(value)
        }
    }

    private fun writeValue(value: Any?) {
        when (value) {
            null -> {
                appendInternal("null", StyleAttributesProvider.getKeywordAttributes())
            }
            is Number -> {
                appendInternal(value.toString(), StyleAttributesProvider.getNumberAttributes())
            }
            is Boolean -> {
                appendInternal(value.toString(), StyleAttributesProvider.getKeywordAttributes())
            }
            else -> {
                appendInternal(value.toString(), StyleAttributesProvider.getIdentifierAttributes())
            }
        }
    }

    private fun appendInternal(fragment: String, attributes: SimpleTextAttributes) {
        foreground = attributes.fgColor
        setTextAlign(SwingConstants.LEFT)
        append(fragment, attributes)
    }

    companion object {
        val instance = CapTableColoredCellRenderer()
        private val EMPTY_BORDER = EmptyBorder(1, 1, 1, 1)
    }
}

class CapTableProgressBarCellRenderer(private val min: Int, private val max: Int) : TableCellRenderer {
    override fun getTableCellRendererComponent(
        table: JTable?,
        value: Any?,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component {
        val progressBar = JProgressBar(min, max)
        val stringValue = value.toString()
        progressBar.value = stringValue.split(" ").toMutableList().last().replace("%", "").toInt()
        progressBar.string = value.toString()
        progressBar.isStringPainted = true
        return progressBar
//        return panel {
//            row("$value") {
//            }
//            row {
//                component(progressBar)
//            }
//        }
    }
}