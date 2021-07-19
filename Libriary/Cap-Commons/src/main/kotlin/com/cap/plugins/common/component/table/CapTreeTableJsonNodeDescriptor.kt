package com.cap.plugins.common.component.table

import com.intellij.ui.ColoredTableCellRenderer
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.SimpleTextAttributes

abstract class CapTreeTableJsonNodeDescriptor {
    var key: String? = null
    var value: Any? = null
    var valueTextAttributes: SimpleTextAttributes? = null
    abstract fun getFormattedValue(): String

    abstract fun renderNode(cellRenderer: ColoredTreeCellRenderer?)

    abstract fun renderValue(cellRenderer: ColoredTableCellRenderer?, isNodeExpanded: Boolean)
}

open class CapTreeTableJsonValueDescriptor : CapTreeTableJsonNodeDescriptor {
    constructor(key: String?, value: Any?, valueTextAttributes: SimpleTextAttributes) : super() {
        this.key = key
        this.value = value
        this.valueTextAttributes = valueTextAttributes
    }

    companion object {
        fun createDescriptor(key: String, value: Any?): CapTreeTableJsonValueDescriptor {
            if (value == null) {
                return CapTreeTableJsonNullValueDescriptor()
            }
            if (value is String) {
                return CapTreeTableJsonStringValueDescriptor(key, value)
            }

            if (value is Boolean) {
                return CapTreeTableJsonValueDescriptor(key, value, StyleAttributesProvider.getKeywordValueAttributes())
            }

            if (value is Int) {
                return CapTreeTableJsonValueDescriptor(key, value, StyleAttributesProvider.getNumberAttributes())
            }

            if (value is Double) {
                return CapTreeTableJsonValueDescriptor(key, value, StyleAttributesProvider.getNumberAttributes())
            }

            return CapTreeTableJsonValueDescriptor(key, value, StyleAttributesProvider.getStringAttributes())
        }
    }

    override fun getFormattedValue(): String {
        return value.toString()
    }

    override fun renderNode(cellRenderer: ColoredTreeCellRenderer?) {
        cellRenderer!!.append(key!!, StyleAttributesProvider.getStringAttributes())
    }

    override fun renderValue(cellRenderer: ColoredTableCellRenderer?, isNodeExpanded: Boolean) {
        if (!isNodeExpanded) {
            cellRenderer!!.append(getFormattedValue(), valueTextAttributes!!)
        }
    }
}

class CapTreeTableJsonNullValueDescriptor :
    CapTreeTableJsonValueDescriptor("null", null, StyleAttributesProvider.getKeywordValueAttributes()) {
    override fun getFormattedValue(): String {
        return "null"
    }

    override fun toString(): String {
        return "null"
    }
}

class CapTreeTableJsonStringValueDescriptor(key: String, value: String) :
    CapTreeTableJsonValueDescriptor(key, value, StyleAttributesProvider.getStringAttributes()) {
    override fun getFormattedValue(): String {
        return value.toString()
    }
}
