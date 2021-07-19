package com.cap.plugins.common.component.picker

import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.event.ChangeEvent
import javax.swing.event.ChangeListener

class DateSelector(preLabel: String?, date: Date?) : JButton() {
    private var dateChooser: DateChooser? = null
    private var preLabel = ""

    constructor(df: SimpleDateFormat, dateString: String?) : this() {
        setText(df, dateString)
    }

    @JvmOverloads
    constructor(date: Date? = nowDate) : this("", date) {
    }

    override fun setText(s: String) {
        var date: Date? = null
        date = try {
            defaultDateFormat.parse(s)
        } catch (e: ParseException) {
            nowDate
        }
        date = date
    }

    private fun setText(df: SimpleDateFormat, s: String?) {
        var date: Date? = null
        date = try {
            df.parse(s)
        } catch (e: ParseException) {
            nowDate
        }
        date = date
    }

    var date: Date?
        get() {
            val dateString = text.substring(preLabel.length)
            return try {
                defaultDateFormat.parse(dateString)
            } catch (e: ParseException) {
                nowDate
            }
        }
        set(date) {
            super.setText(preLabel + defaultDateFormat.format(date))
        }

    // 覆盖父类的方法使之无效
    override fun addActionListener(listener: ActionListener) {}

    inner class DateChooser : JPanel(), ActionListener,
        ChangeListener {
        var startYear = 1980 // 默认【最小】显示年份
        var lastYear = 2050 // 默认【最大】显示年份

        var chooserWidth = 250 // 界面宽度
        var chooserHeight = 250 // 界面高度

        var backGroundColor: Color = Color.gray // 底色

        // 月历表格配色----------------//
        var palletTableColor: Color = Color.white // 日历表底色
        var todayBackColor: Color = Color.orange // 今天背景色
        var weekFontColor: Color = Color.blue // 星期文字色
        var dateFontColor: Color = Color.black // 日期文字色
        var weekendFontColor: Color = Color.red // 周末文字色

        // 控制条配色------------------//
        var controlLineColor: Color = Color.blue // 控制条底色
        var controlTextColor: Color = Color.white // 控制条标签文字色
        var rbFontColor: Color = Color.white // RoundBox文字色
        var rbBorderColor: Color = Color.red // RoundBox边框色
        var rbButtonColor: Color = Color.pink // RoundBox按钮色
        var rbBtFontColor: Color = Color.red // RoundBox按钮文字色
        var dialog: JDialog? = null
        var yearSpin: JSpinner? = null
        var monthSpin: JSpinner? = null
        var hourSpin: JSpinner? = null
        var minuteSpin: JSpinner? = null
        var secondSpin: JSpinner? = null
        var daysButton = Array(6) { arrayOfNulls<JButton>(7) }

        private fun createYearAndMonthPanal(): JPanel {
            val c = calendar
            val currentYear = c[Calendar.YEAR] //年
            val currentMonth = c[Calendar.MONTH] + 1 //月
            val result = JPanel()
            result.layout = FlowLayout()
            result.background = controlLineColor
            yearSpin = JSpinner(
                SpinnerNumberModel(
                    currentYear,
                    startYear, lastYear, 1
                )
            )
            yearSpin!!.preferredSize = Dimension(48, 20)
            yearSpin!!.name = "Year"
            yearSpin!!.editor = JSpinner.NumberEditor(yearSpin, "####")
            yearSpin!!.addChangeListener(this)
            result.add(yearSpin)
            val yearLabel = JLabel("年")
            yearLabel.foreground = controlTextColor
            result.add(yearLabel)
            monthSpin = JSpinner(
                SpinnerNumberModel(
                    currentMonth, 1,
                    12, 1
                )
            )
            monthSpin!!.preferredSize = Dimension(35, 20)
            monthSpin!!.name = "Month"
            monthSpin!!.addChangeListener(this)
            result.add(monthSpin)
            val monthLabel = JLabel("月")
            monthLabel.foreground = controlTextColor
            result.add(monthLabel)
            return result
        }

        private fun createWeekAndDayPanal(): JPanel {
            val colname = arrayOf("日", "一", "二", "三", "四", "五", "六")
            val result = JPanel()
            // 设置固定字体，以免调用环境改变影响界面美观
            result.font = Font("宋体", Font.PLAIN, 12)
            result.layout = GridLayout(7, 7)
            result.background = Color.white
            var cell: JLabel
            for (i in 0..6) {
                cell = JLabel(colname[i])
                cell.horizontalAlignment = JLabel.RIGHT
                if (i == 0 || i == 6) cell.foreground = weekendFontColor else cell.foreground = weekFontColor
                result.add(cell)
            }
            var actionCommandId = 0
            for (i in 0..5) for (j in 0..6) {
                val numberButton = JButton()
                numberButton.border = null
                numberButton.horizontalAlignment = SwingConstants.RIGHT
                numberButton.actionCommand = actionCommandId.toString()
                numberButton.addActionListener(this)
                numberButton.background = palletTableColor
                numberButton.foreground = dateFontColor
                if (j == 0 || j == 6) numberButton.foreground = weekendFontColor else numberButton.foreground =
                    dateFontColor
                daysButton[i][j] = numberButton
                result.add(numberButton)
                actionCommandId++
            }
            return result
        }

        private fun createMinuteAndsecondPanal(): JPanel {
            val c = calendar
            val currentHour = c[Calendar.HOUR_OF_DAY] //时
            val currentMin = c[Calendar.MINUTE] //分
            val currentSec = c[Calendar.SECOND] //秒
            val result = JPanel()
            result.layout = FlowLayout()
            result.background = controlLineColor
            hourSpin = JSpinner(SpinnerNumberModel(currentHour, 0, 23, 1))
            hourSpin!!.preferredSize = Dimension(35, 20)
            hourSpin!!.name = "Hour"
            hourSpin!!.addChangeListener(this)
            result.add(hourSpin)
            val hourLabel = JLabel("时")
            hourLabel.foreground = controlTextColor
            result.add(hourLabel)
            minuteSpin = JSpinner(SpinnerNumberModel(currentMin, 0, 59, 1))
            minuteSpin!!.preferredSize = Dimension(35, 20)
            minuteSpin!!.name = "Minute"
            minuteSpin!!.addChangeListener(this)
            result.add(minuteSpin)
            val minuteLabel = JLabel("分")
            minuteLabel.foreground = controlTextColor
            result.add(minuteLabel)
            secondSpin = JSpinner(SpinnerNumberModel(currentSec, 0, 59, 1))
            secondSpin!!.preferredSize = Dimension(35, 20)
            secondSpin!!.name = "Second"
            secondSpin!!.addChangeListener(this)
            result.add(secondSpin)
            val secondLabel = JLabel("秒")
            secondLabel.foreground = controlTextColor
            result.add(secondLabel)
            return result
        }

        private fun createDialog(owner: Frame): JDialog {
            val result = JDialog(owner, "日期时间选择", true)
            result.defaultCloseOperation = JDialog.HIDE_ON_CLOSE
            result.contentPane.add(this, BorderLayout.CENTER)
            result.pack()
            result.setSize(chooserWidth, chooserHeight)
            return result
        }

        fun showDateChooser(position: Point) {
            val owner = SwingUtilities
                .getWindowAncestor(this@DateSelector) as Frame
            if (dialog == null || dialog!!.owner !== owner) dialog = createDialog(owner)
            dialog!!.location = getAppropriateLocation(owner, position)
            flushWeekAndDay()
            dialog!!.show()
        }

        fun getAppropriateLocation(owner: Frame, position: Point): Point {
            val result = Point(position)
            val p = owner.location
            val offsetX = position.x + chooserWidth - (p.x + owner.width)
            val offsetY = position.y + chooserHeight - (p.y + owner.height)
            if (offsetX > 0) {
                result.x -= offsetX
            }
            if (offsetY > 0) {
                result.y -= offsetY
            }
            return result
        }

        private val calendar: Calendar
            private get() {
                val result = Calendar.getInstance()
                result.time = date
                return result
            }

        private val selectedYear: Int
            private get() = (yearSpin!!.value as Int).toInt()

        private val selectedMonth: Int
            private get() = (monthSpin!!.value as Int).toInt()

        private val selectedHour: Int
            private get() = (hourSpin!!.value as Int).toInt()

        private val selectedMinute: Int
            private get() = (minuteSpin!!.value as Int).toInt()

        private val selectedSecond: Int
            private get() = (secondSpin!!.value as Int).toInt()

        private fun dayColorUpdate(isOldDay: Boolean) {
            val c = calendar
            val day = c[Calendar.DAY_OF_MONTH]
            c[Calendar.DAY_OF_MONTH] = 1
            val actionCommandId = day - 2 + c[Calendar.DAY_OF_WEEK]
            val i = actionCommandId / 7
            val j = actionCommandId % 7
            if (isOldDay) {
                daysButton[i][j]!!.foreground = dateFontColor
            } else {
                daysButton[i][j]!!.foreground = todayBackColor
            }
        }

        private fun flushWeekAndDay() {
            val c = calendar
            c[Calendar.DAY_OF_MONTH] = 1
            val maxDayNo = c.getActualMaximum(Calendar.DAY_OF_MONTH)
            var dayNo = 2 - c[Calendar.DAY_OF_WEEK]
            for (i in 0..5) {
                for (j in 0..6) {
                    var s = ""
                    if (dayNo in 1..maxDayNo) s = dayNo.toString()
                    daysButton[i][j]!!.text = s
                    dayNo++
                }
            }
            dayColorUpdate(false)
        }

        override fun stateChanged(e: ChangeEvent) {
            val source = e.source as JSpinner
            val c = calendar
            if (source.name == "Hour") {
                c[Calendar.HOUR_OF_DAY] = selectedHour
                date = c.time
                return
            } else if (source.name == "Minute") {
                c[Calendar.MINUTE] = selectedMinute
                date = c.time
                return
            } else if (source.name == "Second") {
                c[Calendar.SECOND] = selectedSecond
                date = c.time
                return
            }
            dayColorUpdate(true)
            if (source.name == "Year") c[Calendar.YEAR] = selectedYear else  // (source.getName().equals("Month"))
                c[Calendar.MONTH] = selectedMonth - 1
            date = c.time
            flushWeekAndDay()
        }

        override fun actionPerformed(e: ActionEvent) {
            val source = e.source as JButton
            if (source.text.length == 0) return
            dayColorUpdate(true)
            source.foreground = todayBackColor
            val newDay = source.text.toInt()
            val c = calendar
            c[Calendar.DAY_OF_MONTH] = newDay
            date = c.time
        }

        init {
            layout = BorderLayout()
            border = LineBorder(backGroundColor, 2)
            background = backGroundColor


            /*上中下布局*/
            val topYearAndMonth = createYearAndMonthPanal()
            add(topYearAndMonth, BorderLayout.NORTH)
            val centerWeekAndDay = createWeekAndDayPanal()
            add(centerWeekAndDay, BorderLayout.CENTER)
            val southMinAndSec = createMinuteAndsecondPanal()
            add(southMinAndSec, BorderLayout.SOUTH)
        }
    }

    companion object {
        private val nowDate: Date
            private get() = Calendar.getInstance().time

        private val defaultDateFormat: SimpleDateFormat
            private get() = SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒")
    }

    init {
        if (preLabel != null) this.preLabel = preLabel
        this.date = date
        border = null
        cursor = Cursor(Cursor.HAND_CURSOR)
        super.addActionListener {
            if (dateChooser == null) dateChooser = DateChooser()
            val p = locationOnScreen
            p.y = p.y + 30
            dateChooser!!.showDateChooser(p)
        }
    }
}
