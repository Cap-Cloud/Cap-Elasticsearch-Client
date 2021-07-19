package com.cap.plugins.elasticsearch.ui.explorer

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.common.service.capPersistentStateComponent
import com.cap.plugins.common.service.capPersistentStateComponentI18n
import com.cap.plugins.elasticsearch.config.ElasticsearchI18nSetting
import com.cap.plugins.elasticsearch.config.ElasticsearchPersistentStateSetting
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

@Keep
class ElasticsearchBrowserToolWindowFactory : ToolWindowFactory, DumbAware {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        capPersistentStateComponent = service<ElasticsearchPersistentStateSetting>()
        capPersistentStateComponentI18n = service<ElasticsearchI18nSetting>()

        // 创建一个Explorer
        // 直接创建
        // val clusterExplorer = ElasticsearchBrowserToolWindowExplorer(project)
        // 通过Service获取
        val explorer = ServiceManager.getService(project, ElasticsearchBrowserToolWindowExplorer::class.java)

        // 获得ContentManager
        val contentManager = toolWindow.contentManager
        // 创建一个Content
        val content = contentManager.factory.createContent(explorer, null, false)
        // 将新建的Content加入到ContentManager
        contentManager.addContent(content)
        // 父子关系链中增加一个子项
        Disposer.register(project, explorer!!)
    }
}