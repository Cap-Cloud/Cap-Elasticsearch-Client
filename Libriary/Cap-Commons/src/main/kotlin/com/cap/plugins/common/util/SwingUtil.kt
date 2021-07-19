package com.cap.plugins.common.util

import com.intellij.ui.components.JBLabel
import java.awt.BorderLayout
import java.awt.Component
import javax.swing.JComponent
import javax.swing.JPanel

fun layoutWCE(west: JComponent, center: JComponent? = null, east: JComponent? = null): JPanel {
    val panel = JPanel(BorderLayout())
    panel.add(west, BorderLayout.WEST)
    center?.let { panel.add(it, BorderLayout.CENTER) }
    east?.let { panel.add(it, BorderLayout.EAST) }
    return panel
}

fun layoutNCS(north: JComponent, center: JComponent? = null, south: JComponent? = null): JPanel {
    val panel = JPanel(BorderLayout())
    panel.add(north, BorderLayout.NORTH)
    center?.let { panel.add(it, BorderLayout.CENTER) }
    south?.let { panel.add(it, BorderLayout.SOUTH) }
    return panel
}

/**
 * 自定义方法扩展
 */
fun JPanel.addLabelled(label: String, field: Component) {
    add(JBLabel("$label "))
    add(field)
}
