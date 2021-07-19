package com.cap.plugins.common.i18n.action

import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.action.CapToggleOptionAction
import com.cap.plugins.common.service.capPersistentStateComponentI18n
import java.util.*

class ChineseLanguageAction : CapToggleOptionAction(object : Option {
    override fun getName(): String {
        return "简体中文"
    }

    override fun setSelected(selected: Boolean) {
        I18nResources.changeLanguage(Locale.SIMPLIFIED_CHINESE.toLanguageTag())
    }

    override fun isSelected(): Boolean {
        return Locale.SIMPLIFIED_CHINESE.toLanguageTag() == capPersistentStateComponentI18n.locale
    }

}) {
}