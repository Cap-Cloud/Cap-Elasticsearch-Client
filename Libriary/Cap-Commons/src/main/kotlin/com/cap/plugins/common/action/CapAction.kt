package com.cap.plugins.common.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import javax.swing.Icon

open class CapAction : DumbAwareAction {
    private var task: ((e: AnActionEvent) -> Unit)
    private var enable: ((e: AnActionEvent) -> Boolean)? = null

    constructor(
        enable: ((e: AnActionEvent) -> Boolean)? = null,
        task: (e: AnActionEvent) -> Unit
    ) : super() {
        this.task = task
        this.enable = enable
    }

    constructor(
        text: String,
        enable: ((e: AnActionEvent) -> Boolean)? = null,
        task: (e: AnActionEvent) -> Unit
    ) : super(text) {
        this.enable = enable
        this.task = task
    }

    constructor(
        icon: Icon,
        enable: ((e: AnActionEvent) -> Boolean)? = null,
        task: (e: AnActionEvent) -> Unit
    ) : super(icon) {
        this.enable = enable
        this.task = task
    }

    constructor(
        text: String,
        icon: Icon,
        enable: ((e: AnActionEvent) -> Boolean)? = null,
        task: (e: AnActionEvent) -> Unit
    ) : super(text) {
        templatePresentation.icon = icon
        this.enable = enable
        this.task = task
    }

    constructor(
        text: String,
        description: String,
        icon: Icon? = null,
        enable: ((e: AnActionEvent) -> Boolean)? = null,
        task: (e: AnActionEvent) -> Unit
    ) : super(text, description, icon) {
        this.enable = enable
        this.task = task
    }

    override fun actionPerformed(e: AnActionEvent) {
        task(e)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = enable?.let { it(e) } ?: true
    }
}