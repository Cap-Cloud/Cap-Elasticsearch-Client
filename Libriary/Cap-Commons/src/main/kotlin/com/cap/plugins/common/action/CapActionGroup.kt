package com.cap.plugins.common.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.DumbAware

class CapActionGroup : DefaultActionGroup, DumbAware {
    constructor(popup: Boolean, actions: () -> Collection<AnAction>) {

    }
}