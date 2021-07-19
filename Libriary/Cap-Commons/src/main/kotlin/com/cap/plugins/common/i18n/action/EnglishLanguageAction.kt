package com.cap.plugins.common.i18n.action

import com.cap.plugins.common.action.CapToggleOptionAction
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.service.capPersistentStateComponentI18n
import java.util.*

class EnglishLanguageAction : CapToggleOptionAction(object : Option {
    override fun getName(): String {
        return "English"
    }

    override fun setSelected(selected: Boolean) {
        I18nResources.changeLanguage(Locale.ENGLISH.toLanguageTag())
    }

    override fun isSelected(): Boolean {
        return Locale.ENGLISH.toLanguageTag() == capPersistentStateComponentI18n.locale
    }

}) {
}