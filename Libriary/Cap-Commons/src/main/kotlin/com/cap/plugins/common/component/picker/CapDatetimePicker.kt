package com.cap.plugins.common.component.picker

import com.cap.plugins.common.component.CapDateVerifier
import com.cap.plugins.common.icon.Icons
import com.intellij.openapi.keymap.KeymapUtil
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.ui.popup.JBPopupFactory
import java.awt.Point
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class CapDatetimePicker : TextFieldWithBrowseButton, FocusListener {
    val DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"

    private var popup: Popup? = null

    var datetime: Date? = null

    private var pattern = DEFAULT_DATE_PATTERN
        set(value) {
            if (value.isNullOrEmpty()) {
                field = DEFAULT_DATE_PATTERN
                textField.inputVerifier =
                    CapDateVerifier(DEFAULT_DATE_PATTERN)
                dateFormat = SimpleDateFormat(DEFAULT_DATE_PATTERN)
            } else {
                field = value
                textField.inputVerifier =
                    CapDateVerifier(value)
                dateFormat = SimpleDateFormat(value)
            }
        }

    var dateFormat: SimpleDateFormat

    constructor() : this(null, null, null) {

    }

    constructor(pattern: String) : this(null, null, pattern) {

    }

    constructor(selected: Date?) : this(selected, null, null) {

    }

    constructor(locale: Locale?) : this(null, locale, null) {

    }

    constructor(selection: Date?, locale: Locale?, pattern: String?) : super() {
        dateFormat = SimpleDateFormat(pattern ?: DEFAULT_DATE_PATTERN)

        selection?.let {
            datetime = selection
            updateText()
        }

        textField.inputVerifier =
            CapDateVerifier(pattern ?: DEFAULT_DATE_PATTERN)
        textField.document.addDocumentListener(object : DocumentListener {
            override fun changedUpdate(e: DocumentEvent) {
                updateDate()
            }

            override fun insertUpdate(e: DocumentEvent) {
                updateDate()
            }

            override fun removeUpdate(e: DocumentEvent) {
                updateDate()
            }

        })

        addFocusListener(this)

        addActionListener {
            hidePopup()

            val calendar = Calendar.getInstance()
            datetime?.let {
                calendar.time = it
            }

            if (!textField.text.isNullOrEmpty()) {
                try {
                    calendar.time = dateFormat.parse(textField.text)
                } catch (ex: Exception) {
                }
            }

            var dateTimePanel = object : CapDateTimePanel(calendar) {
                override fun onOk() {
                    datetime = getCalendar().time
                    updateText()
                    hidePopup()
                }

                override fun onCancel() {
                    hidePopup()
                }
            }

            val show = Point(0, this.height)
            SwingUtilities.convertPointToScreen(show, this)
            JBPopupFactory.getInstance().createBalloonBuilder(this.textField).createBalloon()
            popup = PopupFactory.getSharedInstance().getPopup(this.textField, dateTimePanel, show.x, show.y)
            popup?.show()
        }
    }

    fun updateText() {
        datetime?.let {
            textField.text = try {
                dateFormat.format(datetime)
            } catch (ex: Exception) {
                ""
            }
        }
    }

    fun updateDate() {
        try {
            datetime = dateFormat.parse(textField.text)
        } catch (ex: Exception) {

        }
    }

    fun hidePopup() {
        popup?.hide()
        popup = null
    }

    override fun getDefaultIcon(): Icon = Icons.General.GRID_ICON
    override fun getHoveredIcon(): Icon = Icons.General.GRID_ICON
    override fun getIconTooltip(): String = "Choose Datetime (${KeymapUtil.getKeystrokeText(
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