package com.cap.plugins.common.component

import com.cap.plugins.common.action.CapToggleOptionAction
import com.intellij.openapi.ui.JBPopupMenu
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.Icon
import javax.swing.JMenuItem

/**
 * 弹出菜单
 */
class CapPopupMenu: JBPopupMenu() {
    fun add(name: String, icon: Icon? = null, task: (title: String) -> Unit) {
        val item = JMenuItem(object : AbstractAction(name) {
            override fun actionPerformed(e: ActionEvent) {
                task(name)
            }
        })
        item.icon = icon
        add(item)
    }

    fun add(action: CapToggleOptionAction){
        val item = JMenuItem(object : AbstractAction(action.option.name) {
            override fun actionPerformed(e: ActionEvent) {
                action.option.isSelected = true
            }
        })
        add(item)
    }
}