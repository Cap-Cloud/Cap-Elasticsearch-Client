package com.cap.plugins.common.component

import com.cap.plugins.common.icon.Icons
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.components.fields.ExtendableTextComponent
import com.intellij.ui.components.fields.ExtendableTextField
import org.jdesktop.swingx.JXMonthView
import java.awt.BorderLayout
import java.awt.Point
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.Icon
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.SpinnerDateModel
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class CapDateTimeChoosePanel(
    calendar: Calendar,
    private val callback: ((Date) -> Unit)? = null
) :
    JPanel(BorderLayout()) {
    private val monthView: JXMonthView = JXMonthView(calendar.time)

    private val timeSpinner: JSpinner

    init {
        monthView.selectionDate = calendar.time
        monthView.isTraversable = true
        monthView.addActionListener {
            commitTime()
        }
        monthView.addPropertyChangeListener {
            if ("timeZone" == it.propertyName) {
                firePropertyChange("timeZone", it.oldValue, it.newValue)
            }
        }


        timeSpinner = JSpinner(SpinnerDateModel())
        val editor = JSpinner.DateEditor(timeSpinner, "HH:mm:ss")
        timeSpinner.editor = editor
        timeSpinner.value = calendar.time

        timeSpinner.addChangeListener {
            commitTime()
        }

        add(monthView, BorderLayout.CENTER)
        add(timeSpinner, BorderLayout.SOUTH)
    }

    private fun commitTime() {
        val date = monthView.selectionDate
        val time = timeSpinner.value as Date

        val timeCalendar = GregorianCalendar()
        timeCalendar.time = time

        val calendar = GregorianCalendar()
        calendar.time = date

        calendar[Calendar.HOUR_OF_DAY] = timeCalendar[Calendar.HOUR_OF_DAY]
        calendar[Calendar.MINUTE] = timeCalendar[Calendar.MINUTE]
        calendar[Calendar.SECOND] = timeCalendar[Calendar.SECOND]
        calendar[Calendar.MILLISECOND] = 0

        callback?.invoke(calendar.time)
    }
}

class CapDateTimeChooseExtension(
    private val textField: CapDateTimeChooseTextField,
    private val calendar: Calendar,
    private val callback: ((Date) -> Unit)? = null
) : ExtendableTextComponent.Extension {
    override fun getIcon(hovered: Boolean): Icon {
        return Icons.General.CALENDAR_ICON
    }

    override fun getActionOnClick(): Runnable {
        return Runnable { perform() }
    }

    private fun perform() {
        textField.datetime?.let {
            calendar.time = it
        }

        val popup = JBPopupFactory.getInstance()
            .createComponentPopupBuilder(
                CapDateTimeChoosePanel(
                    calendar,
                    callback
                ), null
            ).createPopup()

        val point = Point(0, textField.height)

        popup.show(RelativePoint(textField, point))
    }
}

class CapDateTimeChooseTextField : ExtendableTextField {
    val DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss"

    var datetime: Date? = null

    private var pattern = DEFAULT_DATE_PATTERN
        set(value) {
            if (value.isNullOrEmpty()) {
                field = DEFAULT_DATE_PATTERN
                inputVerifier =
                    CapDateVerifier(DEFAULT_DATE_PATTERN)
                dateFormat = SimpleDateFormat(DEFAULT_DATE_PATTERN)
            } else {
                field = value
                inputVerifier =
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

        inputVerifier =
            CapDateVerifier(pattern ?: DEFAULT_DATE_PATTERN)
        document.addDocumentListener(object : DocumentListener {
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

        val calendar = GregorianCalendar()
        datetime?.let {
            calendar.time = it
        }

        if (!text.isNullOrEmpty()) {
            try {
                calendar.time = dateFormat.parse(text)
            } catch (ex: Exception) {
            }
        }

        val extension = CapDateTimeChooseExtension(this, calendar) {
            datetime = it
            updateText()
        }

        addExtension(extension)
    }


    fun updateText() {
        datetime?.let {
            text = try {
                dateFormat.format(datetime)
            } catch (ex: Exception) {
                ""
            }
        }
    }

    fun updateDate() {
        try {
            datetime = dateFormat.parse(text)
        } catch (ex: Exception) {
        }
    }
}
