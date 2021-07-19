package com.cap.plugins.common.component

import com.intellij.util.ui.ColorIcon
import com.intellij.util.ui.GraphicsUtil
import java.awt.Color
import java.awt.Component
import java.awt.Graphics

open class RoundColorIcon(
    size: Int,
    private val fillColor: Color?,
    private val drawColor: Color?,
) : ColorIcon(size, Color.WHITE) {

    override fun paintIcon(component: Component?, g: Graphics, i: Int, j: Int) {
        doPaint(g, i, j)
    }

    fun doPaint(g: Graphics, i: Int, j: Int) {
        val config = GraphicsUtil.setupAAPainting(g)
        val dim = this.iconWidth - 2
        fillColor?.let {
            g.color = it
            g.fillOval(i + 1, j + 1, dim, dim)
        }
        drawColor?.let {
            g.color = it
            g.drawOval(i + 1, j + 1, dim, dim)
        }
        config.restore()
    }
}