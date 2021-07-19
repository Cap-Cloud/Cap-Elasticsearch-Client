package com.cap.plugins.common.component

import com.intellij.util.ui.ColorIcon
import com.intellij.util.ui.GraphicsUtil
import java.awt.Color
import java.awt.Component
import java.awt.Graphics

open class SquareNumberColorIcon(
    size: Int,
    private val number: Int,
    private val fillColor: Color? = null,
    private val drawColor: Color? = null,
    private val textColor: Color? = null
) : ColorIcon(size, Color.WHITE) {

    override fun paintIcon(component: Component?, g: Graphics, i: Int, j: Int) {
        doPaint(g, i, j)
    }

    fun doPaint(g: Graphics, i: Int, j: Int) {
        val config = GraphicsUtil.setupAAPainting(g)
        val dim = this.iconWidth - 2

        drawColor?.let {
            g.color = it
            g.drawRect(i + 1, j + 1, dim, dim)
        }
        fillColor?.let {
            g.color = it
            g.fillRect(i + 1, j + 1, dim, dim)
        }

        g.color = textColor ?: Color.BLACK
        g.drawString("$number", dim / 2, height - 1)

        config.restore()
    }
}