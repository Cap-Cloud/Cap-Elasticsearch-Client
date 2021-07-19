package com.cap.plugins.common.component.picker

import com.cap.plugins.common.icon.Icons
import com.cap.plugins.common.util.LONG_VERIFIER
import com.intellij.openapi.keymap.KeymapUtil
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import java.awt.Point
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.util.*
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class CapTimestampPicker : TextFieldWithBrowseButton,FocusListener {
    private var popup: Popup? = null

    var timestamp: Long = 0

    constructor() : this(null, null) {

    }

    constructor(selected: Date?) : this(selected, null) {

    }

    constructor(locale: Locale?) : this(null, locale) {

    }

    constructor(selection: Date?, locale: Locale?) : super() {
        selection?.let {
            val calendar = Calendar.getInstance()
            timestamp = calendar.timeInMillis
            updateText()
        }

        addFocusListener(this)

        addActionListener {
            hidePopup()

            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp

            if (!textField.text.isNullOrEmpty()) {
                try {
                    calendar.timeInMillis = textField.text.toLong()
                } catch (ex: Exception) {
                }
            }

            var dateTimePanel = object : CapDateTimePanel(calendar) {
                override fun onOk() {
                    timestamp = getCalendar().timeInMillis
                    updateText()
                    hidePopup()
                }

                override fun onCancel() {
                    hidePopup()
                }
            }

            val show = Point(0, this.height)
            SwingUtilities.convertPointToScreen(show, this)
            popup = PopupFactory.getSharedInstance().getPopup(this.textField, dateTimePanel, show.x, show.y)
            popup?.show()
        }

        textField.inputVerifier = LONG_VERIFIER

        textField.document.addDocumentListener(object:DocumentListener{
            override fun changedUpdate(e: DocumentEvent?) {
                updateTime()
            }

            override fun insertUpdate(e: DocumentEvent?) {
                updateTime()
            }

            override fun removeUpdate(e: DocumentEvent?) {
                updateTime()
            }

        })
    }

    fun updateText() {
        timestamp?.let {
            textField.text = "$timestamp"
        }
    }

    fun updateTime() {
        try {
            timestamp = textField.text.toLong()
        } catch (ex: Exception) {

        }
    }

    fun hidePopup(){
        popup?.hide()
        popup = null
    }

    override fun getDefaultIcon(): Icon = Icons.General.ACTUAL_ZOOM_ICON
    override fun getHoveredIcon(): Icon = Icons.General.ACTUAL_ZOOM_ICON
    override fun getIconTooltip(): String = "Choose Timestamp (${KeymapUtil.getKeystrokeText(
        KeyStroke.getKeyStroke(
            KeyEvent.VK_ENTER,
            InputEvent.SHIFT_DOWN_MASK
        )
    )})"

    override fun focusLost(e: FocusEvent?) {
        hidePopup()
    }

    override fun focusGained(e: FocusEvent?) {
    }
}