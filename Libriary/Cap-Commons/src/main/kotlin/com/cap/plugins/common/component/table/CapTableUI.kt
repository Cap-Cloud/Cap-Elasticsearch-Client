package com.cap.plugins.common.component.table

import com.intellij.openapi.editor.colors.EditorColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import com.intellij.ui.FileColorManager
import com.intellij.util.ObjectUtils
import com.intellij.util.ui.UIUtil
import java.awt.Color

object CapTableUI {
    fun getResultTableHeaderColor(): Color {
        val color = EditorColorsManager.getInstance().globalScheme.defaultBackground
        return if (EditorColorsManager.getInstance().isDarkEditor) {
            Color(Integer.min(color.red + 16, 255), Integer.min(color.green + 16, 255), Integer.min(color.blue + 16, 255))
        } else {
            Color(Integer.max(0, color.red - 16), Integer.max(0, color.green - 16), Integer.max(0, color.blue - 16))
        }
    }

    fun getTableGridColor(): Color {
        return ObjectUtils.chooseNotNull(
            EditorColorsManager.getInstance().globalScheme.getColor(EditorColors.INDENT_GUIDE_COLOR),
            UIUtil.getTableGridColor()
        )
    }

    fun getPropertiesTableHeaderColor(): Color {
        val color = ObjectUtils.chooseNotNull(
            EditorColorsManager.getInstance().globalScheme.getColor(EditorColors.GUTTER_BACKGROUND),
            getResultTableHeaderColor()
        )
        return if (EditorColorsManager.getInstance().isDarkEditor) {
            color.brighter()
        } else {
            color
        }
    }

    fun getSelectedLineColor(): Color {
        return ObjectUtils.chooseNotNull(
            EditorColorsManager.getInstance().globalScheme.getColor(EditorColors.CARET_ROW_COLOR),
            UIUtil.getDecoratedRowColor()
        )
    }

    fun getSelectedCellColor(): Color {
        return ObjectUtils.chooseNotNull(
            EditorColorsManager.getInstance().globalScheme.getColor(EditorColors.SELECTION_BACKGROUND_COLOR),
            UIUtil.getTableSelectionBackground(true)
        )
    }

    fun getTableBackground(): Color {
        return ObjectUtils.chooseNotNull(
            EditorColorsManager.getInstance().globalScheme.defaultBackground,
            UIUtil.getTableBackground()
        )
    }

    fun getColorByName(colorName: String?, project: Project): Color? {
        return colorName?.let { FileColorManager.getInstance(project).getColor(it) }
    }
}