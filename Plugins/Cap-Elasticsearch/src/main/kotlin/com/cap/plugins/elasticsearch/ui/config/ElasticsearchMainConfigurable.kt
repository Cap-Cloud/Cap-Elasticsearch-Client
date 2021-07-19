package com.cap.plugins.elasticsearch.ui.config

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.common.ui.config.panel.I18nConfigPanel
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.intellij.openapi.options.Configurable
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

@Keep
class ElasticsearchMainConfigurable : Configurable, Configurable.Composite {
    private var mainSettingPanel = JPanel(BorderLayout())

    override fun isModified(): Boolean {
        return false
    }

    override fun getDisplayName(): String {
        return ElasticsearchI18n.PLUGIN_TITLE
    }

    override fun apply() {
    }

    override fun createComponent(): JComponent? {
        return mainSettingPanel
    }

    override fun getConfigurables(): Array<Configurable> {
        return arrayOf(
            I18nConfigPanel()
        )
    }
}