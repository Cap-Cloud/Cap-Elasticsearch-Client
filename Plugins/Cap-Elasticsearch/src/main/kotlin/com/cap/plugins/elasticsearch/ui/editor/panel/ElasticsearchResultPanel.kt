package com.cap.plugins.elasticsearch.ui.editor.panel

import com.cap.plugins.common.component.table.CapTableItem
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.ui.editor.model.ResponseContext
import com.intellij.openapi.Disposable
import com.intellij.ui.components.JBLoadingPanel
import java.awt.BorderLayout
import javax.swing.JTabbedPane
import javax.swing.SwingConstants

abstract class ElasticsearchResultPanel : JBLoadingPanel, Disposable {
    val tabbedPane = JTabbedPane()
    val textTitle = I18nResources.getMessageDefault("elasticsearch.result.tab.text", ElasticsearchI18n.RESULT_TAB_TEXT)
    val jsonTitle = I18nResources.getMessageDefault("elasticsearch.result.tab.json", ElasticsearchI18n.RESULT_TAB_JSON)
    val tableTitle =
        I18nResources.getMessageDefault("elasticsearch.result.tab.table", ElasticsearchI18n.RESULT_TAB_TABLE)
    val treeTitle = I18nResources.getMessageDefault("elasticsearch.result.tab.tree", ElasticsearchI18n.RESULT_TAB_TREE)

    constructor(parent: Disposable) : super(BorderLayout(), parent, 100) {
        tabbedPane.tabPlacement = SwingConstants.BOTTOM
    }

    abstract fun updateResult(responseContext: ResponseContext): Collection<out CapTableItem>

    override fun dispose() {
    }
}