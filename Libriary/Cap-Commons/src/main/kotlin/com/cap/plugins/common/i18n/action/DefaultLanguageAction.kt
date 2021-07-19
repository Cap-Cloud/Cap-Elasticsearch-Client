package com.cap.plugins.common.i18n.action

import com.cap.plugins.common.action.CapToggleOptionAction
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.service.capPersistentStateComponentI18n
import java.util.*

class DefaultLanguageAction : CapToggleOptionAction(object : Option {
    override fun getName(): String {
        return "Default Language"
    }

    override fun setSelected(selected: Boolean) {
        I18nResources.changeLanguage("")
    }

    override fun isSelected(): Boolean {
        return "" == capPersistentStateComponentI18n.locale
    }

}) {
}