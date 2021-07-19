package com.cap.plugins.common.component

import com.cap.plugins.common.action.CapToggleOptionAction
import com.intellij.ui.components.JBMenu
import java.awt.event.ActionEvent
import javax.swing.AbstractAction
import javax.swing.Icon
import javax.swing.JMenuItem

/**
 * 菜单
 */
class CapMenu : JBMenu {
    constructor() : super() {

    }

    constructor(name: String, icon: Icon? = null) : this() {
        this.name = name
        this.text = name
        this.icon = icon
    }

    fun add(name: String, icon: Icon? = null, task: (title: String) -> Unit) {
        val item = JMenuItem(object : AbstractAction(name) {
            override fun actionPerformed(e: ActionEvent) {
                task(name)
            }
        })
        item.icon = icon
        add(item)
    }

    fun add(name: String, text: String, icon: Icon? = null, task: (title: String) -> Unit) {
        val item = JMenuItem(object : AbstractAction(name) {
            override fun actionPerformed(e: ActionEvent) {
                task(name)
            }
        })
        item.icon = icon
        add(item)
    }

    fun add(name: String, text: String, description: String, icon: Icon? = null, task: (title: String) -> Unit) {
        val item = JMenuItem(object : AbstractAction(name) {
            override fun actionPerformed(e: ActionEvent) {
                task(name)
            }
        })
        item.text = text
        item.toolTipText = description
        item.icon = icon
        add(item)
    }

    fun add(action: CapToggleOptionAction) {
        val item = JMenuItem(object : AbstractAction(action.option.name) {
            override fun actionPerformed(e: ActionEvent) {
                action.option.isSelected = true
            }
        })
        add(item)
    }
}