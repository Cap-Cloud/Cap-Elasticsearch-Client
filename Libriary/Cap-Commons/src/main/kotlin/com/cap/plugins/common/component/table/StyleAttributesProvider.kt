package com.cap.plugins.common.component.table

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.ui.JBColor
import com.intellij.ui.SimpleTextAttributes
import java.awt.Color
import java.awt.Font

object StyleAttributesProvider {
    fun getNullAttributes() = getSimpleTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT, Font.ITALIC)

    fun getNumberAttributes() = getSimpleTextAttributes(DefaultLanguageHighlighterColors.NUMBER)
    fun getKeywordAttributes() = getSimpleTextAttributes(DefaultLanguageHighlighterColors.KEYWORD)
    fun getKeywordValueAttributes() = getSimpleTextAttributes(DefaultLanguageHighlighterColors.KEYWORD)
    fun getStringAttributes() = getSimpleTextAttributes(DefaultLanguageHighlighterColors.STRING)
    fun getIdentifierAttributes() = getSimpleTextAttributes(DefaultLanguageHighlighterColors.IDENTIFIER)
    fun getBracesAttributes() = getSimpleTextAttributes(DefaultLanguageHighlighterColors.BRACES)

    private fun getSimpleTextAttributes(
        textAttributesKey: TextAttributesKey,
        fontStyle: Int = Font.PLAIN
    ): SimpleTextAttributes {
        val textAttributes =
            EditorColorsManager.getInstance().globalScheme.getAttributes(textAttributesKey)
        return SimpleTextAttributes(fontStyle, textAttributes.foregroundColor)
    }

    private fun getSimpleTextAttributes(
        color: Color,
        fontStyle: Int = Font.PLAIN
    ): SimpleTextAttributes {
        return SimpleTextAttributes(fontStyle, color)
    }
}