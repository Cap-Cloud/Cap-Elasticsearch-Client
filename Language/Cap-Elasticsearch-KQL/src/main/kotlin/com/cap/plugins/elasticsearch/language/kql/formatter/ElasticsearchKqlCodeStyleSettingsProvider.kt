package com.cap.plugins.elasticsearch.language.kql.formatter

import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlLanguage
import com.intellij.application.options.CodeStyleAbstractConfigurable
import com.intellij.application.options.CodeStyleAbstractPanel
import com.intellij.application.options.TabbedLanguageCodeStylePanel
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.CodeStyleConfigurable
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.codeStyle.CodeStyleSettingsProvider
import com.intellij.psi.codeStyle.CustomCodeStyleSettings

class ElasticsearchKqlCodeStyleSettingsProvider : CodeStyleSettingsProvider() {

    override fun createConfigurable(
        settings: CodeStyleSettings,
        originalSettings: CodeStyleSettings
    ): CodeStyleConfigurable {
        return object : CodeStyleAbstractConfigurable(settings, originalSettings, "settings.display.name.json") {
            override fun createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel {
                val eDQLLanguage: ElasticsearchKqlLanguage = ElasticsearchKqlLanguage.INSTANCE
                val currentSettings: CodeStyleSettings = currentSettings
                return object : TabbedLanguageCodeStylePanel(eDQLLanguage as Language, currentSettings, settings) {
                    override fun initTabs(settings: CodeStyleSettings) {
                        addIndentOptionsTab(settings)
                        // addSpacesTab(settings)
                        // addBlankLinesTab(settings)
                        // addWrappingAndBracesTab(settings)
                    }
                }
            }

            override fun getHelpTopic(): String = "reference.settingsdialog.codestyle.json"
        }
    }

    override fun getConfigurableDisplayName(): String? {
        return ElasticsearchKqlLanguage.INSTANCE.displayName
    }

    override fun createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings {
        return ElasticsearchKqlCodeStyleSettings(settings)
    }
}