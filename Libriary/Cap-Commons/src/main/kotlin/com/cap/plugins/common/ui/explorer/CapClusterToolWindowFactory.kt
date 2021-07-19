package com.cap.plugins.common.ui.explorer

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

open class CapClusterToolWindowFactory : ToolWindowFactory {
    private var serviceClass: Class<out CapClusterToolWindowExplorer>? = null

    constructor() {
        this.serviceClass = null
    }

    constructor(serviceClass: Class<out CapClusterToolWindowExplorer>) {
        this.serviceClass = serviceClass
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {

        val toolWindowExplorer = CapClusterToolWindowExplorer.getInstance(project, serviceClass)

        // 获得ContentManager
        val contentManager = toolWindow.contentManager
        // 创建一个Content
        val content = contentManager.factory.createContent(toolWindowExplorer, null, false)
        // 将新建的Content加入到ContentManager
        contentManager.addContent(content)
        // 父子关系链中增加一个子项
        Disposer.register(project, toolWindowExplorer!!)
    }
}