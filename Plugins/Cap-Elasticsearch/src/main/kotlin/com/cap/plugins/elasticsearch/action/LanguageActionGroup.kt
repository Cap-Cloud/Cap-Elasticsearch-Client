package com.cap.plugins.elasticsearch.action

import com.cap.plugins.common.action.CapToggleOptionAction
import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.i18n.action.ChineseLanguageAction
import com.cap.plugins.common.i18n.action.DefaultLanguageAction
import com.cap.plugins.common.service.capPersistentStateComponentI18n
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleOptionAction

@Keep
class LanguageActionGroup : ActionGroup() {
    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return mutableListOf<AnAction>(
            DefaultLanguageAction(),
            ChineseLanguageAction()
        ).also {
            it.addAll(capPersistentStateComponentI18n.i18nMap.keys.filter {
                it != "" && it != "zh-CN" && it != "und"
            }.map {
                CapToggleOptionAction(object : ToggleOptionAction.Option {
                    override fun getName(): String {
                        return it
                    }

                    override fun setSelected(selected: Boolean) {
                        I18nResources.changeLanguage(it)
                    }

                    override fun isSelected(): Boolean {
                        return capPersistentStateComponentI18n.locale == it
                    }
                })
            })
        }.toTypedArray()
    }
}