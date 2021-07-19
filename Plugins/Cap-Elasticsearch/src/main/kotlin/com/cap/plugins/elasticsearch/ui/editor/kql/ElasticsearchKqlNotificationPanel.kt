package com.cap.plugins.elasticsearch.ui.editor.kql

import com.cap.plugins.elasticsearch.ui.editor.kql.ElasticsearchKqlDataKeys
import com.intellij.codeInsight.intention.IntentionActionWithOptions
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.DataProvider
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.EditorNotificationPanel
import com.intellij.ui.HyperlinkAdapter
import com.intellij.ui.HyperlinkLabel
import java.awt.Color
import java.awt.Component
import javax.swing.event.HyperlinkEvent
import javax.swing.event.HyperlinkListener

class ElasticsearchKqlNotificationPanel(val virtualFile: VirtualFile) : EditorNotificationPanel(), DataProvider {
    private var myRunAllRequestsHyperlinkLabel: HyperlinkLabel? = null

    init {
        this.myRunAllRequestsHyperlinkLabel = createRunAllActionLabel();
    }

    private fun createRunAllActionLabel(): HyperlinkLabel? {
        val label = HyperlinkLabel("RunAll", background)

        label.addHyperlinkListener(object : HyperlinkAdapter() {
            override fun hyperlinkActivated(e: HyperlinkEvent?) {
                this@ElasticsearchKqlNotificationPanel.executeAction("ElasticsearchKql.RunAll")
            }
        } as HyperlinkListener)
        label.toolTipText = "RunAll"
        label.setIcon(AllIcons.Actions.RunAll)
        add("West", label as Component)
        return label
    }

    override fun getBackground(): Color {
        return getToolbarBackground()
    }

    override fun getActionPlace(): String {
        return "ElasticsearchKql.NotificationPanel"
    }

    override fun getIntentionAction(): IntentionActionWithOptions? {
        return null
    }

    override fun getData(dataId: String): Any? {
        if (CommonDataKeys.VIRTUAL_FILE.`is`(dataId)) {
            return this.virtualFile
        }
        if (ElasticsearchKqlDataKeys.RUN_ALL_TOOLBAR_HYPERLINK_LABEL.`is`(dataId)) {
            return this.myRunAllRequestsHyperlinkLabel
        }
        return null
    }
}