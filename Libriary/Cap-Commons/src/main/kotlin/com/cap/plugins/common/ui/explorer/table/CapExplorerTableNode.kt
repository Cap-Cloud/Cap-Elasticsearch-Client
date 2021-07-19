package com.cap.plugins.common.ui.explorer.table

import javax.swing.event.TableModelEvent

interface CapExplorerTableNode {
    fun headers() = listOf("Property", "Value")
    fun rows() = emptyList<Array<String>>()
    var listener: ((e: TableModelEvent) -> Unit)?
}