package com.cap.plugins.elasticsearch.ui.editor.panel

import com.cap.plugins.elasticsearch.config.ElasticsearchPersistentStateSetting
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.openapi.ui.Splitter
import com.intellij.util.ui.UIUtil
import java.awt.BorderLayout
import javax.swing.JPanel

abstract class ElasticsearchPanel : JPanel, Disposable {
    private val config = service<ElasticsearchPersistentStateSetting>()

    abstract var bodyPanel: ElasticsearchBodyPanel
    abstract var resultPanel: ElasticsearchResultPanel

    private val splitter: Splitter = Splitter(true, 0.3f)

    constructor():super(BorderLayout()) {
        splitter.divider.background = UIUtil.SIDE_PANEL_BACKGROUND

        splitter.firstComponent = bodyPanel
        splitter.secondComponent = resultPanel
    }

    abstract fun executeRequest()
}