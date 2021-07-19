package com.cap.plugins.common.action

import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.DumbAware

open class CapToggleOptionActionGroup : DefaultActionGroup, DumbAware {

    constructor(name: String, popup: Boolean, actions: () -> Collection<CapToggleOptionAction>) : super(name, popup) {
        setActions(actions())
    }

    fun setActions(actions: Collection<CapToggleOptionAction>) {
        removeAll()
        actions.forEach {
            add(it)
        }
    }
}