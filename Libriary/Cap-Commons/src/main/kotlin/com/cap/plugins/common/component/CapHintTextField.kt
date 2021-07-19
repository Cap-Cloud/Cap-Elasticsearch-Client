package com.cap.plugins.common.component

import com.intellij.ui.components.JBTextField
import java.awt.*

/**
 * 带提示内容的文本输入框
 */
class CapHintTextField(private val _hint: String) : JBTextField() {
    override fun paint(g: Graphics) {
        super.paint(g)
        if (text.isEmpty()) {
            val h = height
            (g as Graphics2D).setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            )
            val fm: FontMetrics = g.getFontMetrics()
            val c0 = background.rgb
            val c1 = foreground.rgb
            val m = -0x1010102
            val c2 = (c0 and m ushr 1) + (c1 and m ushr 1)
            g.setColor(Color(c2, true))
            g.drawString(_hint, insets.left + 5, h / 2 + fm.ascent / 2 - 2)
        }
    }
}