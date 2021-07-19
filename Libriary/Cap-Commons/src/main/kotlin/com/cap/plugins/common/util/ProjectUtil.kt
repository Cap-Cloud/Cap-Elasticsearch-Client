package com.cap.plugins.common.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.wm.WindowManager

object ProjectUtil {

    /**
     * 获取当前项目对象
     *
     * @return 当前项目对象
     */
    fun getCurrProject(): Project {
        val projectManager = ProjectManager.getInstance()
        val openProjects = projectManager.openProjects
        if (openProjects.isEmpty()) {
            return projectManager.defaultProject
        } else if (openProjects.size == 1) {
            // 只存在一个打开的项目则使用打开的项目
            return openProjects[0]
        }

        //如果有项目窗口处于激活状态
        try {
            val wm = WindowManager.getInstance()
            for (project in openProjects) {
                val window = wm.suggestParentWindow(project)
                if (window != null && window.isActive) {
                    return project
                }
            }
        } catch (ignored: Exception) {
        }

        // 否则使用默认项目
        return projectManager.defaultProject
    }
}