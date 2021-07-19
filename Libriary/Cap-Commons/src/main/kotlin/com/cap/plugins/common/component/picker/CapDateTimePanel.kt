package com.cap.plugins.common.component.picker

import com.cap.plugins.common.util.layoutNCS
import com.cap.plugins.common.util.layoutWCE
import org.jdesktop.swingx.JXMonthView
import java.awt.BorderLayout
import java.awt.Dimension
import java.util.*
import javax.swing.*
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

/**
 * 日期选择器
 */
abstract class CapDatePanel : JPanel {
    private val monthView: JXMonthView

    constructor(calendar: Calendar) : super(BorderLayout()) {
        monthView = JXMonthView(calendar.time)

        monthView.selectionDate = calendar.time
        monthView.isTraversable = true
        monthView.addPropertyChangeListener {
            if ("timeZone" == it.propertyName) {
                firePropertyChange("timeZone", it.oldValue, it.newValue)
            }
        }

        add(monthView)
    }

    fun getCalendar(): Calendar {
        var calendar: Calendar = Calendar.getInstance()
        calendar.time = monthView.selectionDate
        return calendar
    }
}

/**
 * 时间选择器
 */
abstract class CapTimePanel : JPanel, ChangeListener {
    private var hourSpinner: JSpinner? = null
    private var minuteSpinner: JSpinner? = null
    private var secondSpinner: JSpinner? = null

    private var _calendar: Calendar

    constructor(calendar: Calendar) : super(BorderLayout()) {
        _calendar = calendar.clone() as Calendar

        var width = 58
        var height = 30

        hourSpinner = JSpinner(SpinnerNumberModel(calendar[Calendar.HOUR_OF_DAY], 0, 23, 1))
        hourSpinner!!.name = "Hour"
        hourSpinner!!.addChangeListener(this)
        hourSpinner!!.preferredSize = Dimension(width, height)

        minuteSpinner = JSpinner(SpinnerNumberModel(calendar[Calendar.MINUTE], 0, 59, 1))
        minuteSpinner!!.name = "Minute"
        minuteSpinner!!.addChangeListener(this)
        minuteSpinner!!.preferredSize = Dimension(width, height)

        secondSpinner = JSpinner(SpinnerNumberModel(calendar[Calendar.SECOND], 0, 59, 1))
        secondSpinner!!.name = "Second"
        secondSpinner!!.addChangeListener(this)
        secondSpinner!!.preferredSize = Dimension(width, height)

        add(
            layoutWCE(
                layoutWCE(hourSpinner!!, JLabel("时")),
                layoutWCE(minuteSpinner!!, JLabel("分")),
                layoutWCE(secondSpinner!!, JLabel("秒"))
            )
        )
    }

    override fun stateChanged(e: ChangeEvent) {
        when ((e.source as JSpinner).name) {
            "Hour" -> {
                _calendar[Calendar.HOUR_OF_DAY] = (hourSpinner!!.value as Int).toInt()
            }
            "Minute" -> {
                _calendar[Calendar.MINUTE] = (minuteSpinner!!.value as Int).toInt()
            }
            "Second" -> {
                _calendar[Calendar.SECOND] = (secondSpinner!!.value as Int).toInt()
            }
            else -> {
            }
        }
    }

    fun getCalendar(): Calendar = _calendar
}

/**
 * 日期时间选择器
 */
abstract class CapDateTimePanel : JPanel {
    private val monthView: CapDatePanel
    private val timeView: CapTimePanel
    private val ok = JButton("OK")
    private val cancel = JButton("Cancel")

    constructor(calendar: Calendar) : super(BorderLayout()) {
        monthView = object : CapDatePanel(calendar) {}
        timeView = object : CapTimePanel(calendar) {}

        ok.addActionListener {
            onOk()
        }
        cancel.addActionListener {
            onCancel()
        }

        add(
            layoutNCS(
                monthView,
                timeView,
                layoutWCE(cancel, null, ok)
            )
        )
    }

    fun getCalendar(): Calendar {
        var calendar: Calendar = monthView.getCalendar().clone() as Calendar
        calendar[Calendar.HOUR_OF_DAY] = timeView.getCalendar()[Calendar.HOUR_OF_DAY]
        calendar[Calendar.MINUTE] = timeView.getCalendar()[Calendar.MINUTE]
        calendar[Calendar.SECOND] = timeView.getCalendar()[Calendar.SECOND]
        return calendar
    }

    abstract fun onOk()
    abstract fun onCancel()
}