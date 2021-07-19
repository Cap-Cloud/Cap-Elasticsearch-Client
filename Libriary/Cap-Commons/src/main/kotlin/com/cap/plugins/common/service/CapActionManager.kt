package com.cap.plugins.common.service

import com.cap.plugins.common.action.CapActionGroup
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.ServiceManager
import com.intellij.util.ui.JBUI
import javax.swing.JComponent

@Service
class CapActionManager {
    companion object {
        fun getInstance(): CapActionManager {
            return ServiceManager.getService(CapActionManager::class.java)
        }
    }

    fun createActionToolbar(place: String, horizontal: Boolean, actions: () -> Collection<AnAction>): JComponent {
        val actionGroup = DefaultActionGroup()
        actionGroup.addAll(actions())
        val actionToolBar = ActionManager.getInstance().createActionToolbar(place, actionGroup, horizontal)
        return JBUI.Panels.simplePanel(actionToolBar.component)
    }

    fun createActionToolbar(place: String, group: ActionGroup, horizontal: Boolean) =
        ActionManager.getInstance().createActionToolbar(place, group, horizontal)

    fun createActionGroup(popup: Boolean, actions: () -> Collection<AnAction>): ActionGroup {
        return CapActionGroup(popup, actions)
    }
}