package com.cap.plugins.common.component

import com.intellij.openapi.ui.ComboBox
import java.awt.Color
import java.awt.Component
import java.awt.event.*
import java.util.*
import javax.swing.*
import javax.swing.plaf.basic.BasicComboPopup
import javax.swing.plaf.basic.ComboPopup
import javax.swing.plaf.metal.MetalComboBoxUI
import kotlin.collections.HashSet

class CapDefaultListSelectionModel<E> : DefaultListSelectionModel {
    var comboBox: CapJComboBox

    constructor(comboBox: JComboBox<E>) {
        this.comboBox = comboBox as CapJComboBox
    }

    override fun isSelectedIndex(index: Int): Boolean {
        return comboBox.set.contains(index)
    }
}

class CapMouseAdapter<E> : MouseAdapter {
    private var comboBox: CapJComboBox
    var list: JList<E>

    constructor(list: JList<E>, comboBox: JComboBox<E>) {
        this.list = list
        this.comboBox = comboBox as CapJComboBox
    }

    override fun mousePressed(anEvent: MouseEvent?) {
        var sb: StringBuilder
        var sb1: StringBuilder
        val sb2: StringBuilder
        var k: Int
        val index: Int = list.selectedIndex
        val jTextField = comboBox.editor.editorComponent as JTextField
        sb = StringBuilder(jTextField.text)
        if (sb.isNotEmpty() && ',' != sb[0]) sb.insert(0, ",")
        if (sb.isNotEmpty() && ',' != sb[sb.length - 1]) sb.append(",")
        if (sb.isNotEmpty() && "," == sb.toString()) sb = StringBuilder()
        if (list.selectionMode === ListSelectionModel.MULTIPLE_INTERVAL_SELECTION) {
            if (comboBox.set.contains(index)) {
                comboBox.set.remove(index)
                sb1 = StringBuilder()
                sb1.append(",").append(comboBox.model.getElementAt(index)).append(",")
                k = sb.indexOf(sb1.toString())
                while (k != -1) {
                    sb.replace(k, k + sb1.length, ",")
                    k = sb.indexOf(sb1.toString())
                }
            } else {
                comboBox.set.add(index)
                if (sb.isEmpty()) sb.append(",").append(comboBox.model.getElementAt(index))
                    .append(",") else sb.append(comboBox.model.getElementAt(index)).append(",")
            }
        } else {
            if (!comboBox.set.contains(index)) {
                comboBox.set.clear()
                comboBox.set.add(index)
            }
        }
        var obj: Any
        sb2 = StringBuilder(sb)
        //替换完正常的可选值
        for (i in 0 until list.model.size) {
            obj = list.model.getElementAt(i)!!
            sb1 = StringBuilder()
            sb1.append(",").append(obj).append(",")
            k = sb2.indexOf(sb1.toString())
            while (k != -1) {
                sb2.replace(k, k + sb1.length, ",")
                k = sb2.indexOf(sb1.toString())
            }
        }
        val list1: MutableList<*> = ArrayList<Any?>(comboBox.set.sorted())
        for (i in list1.indices) {
            obj = comboBox.model.getElementAt(list1[i].toString().toInt())!!
            sb1 = StringBuilder()
            sb1.append(",").append(obj).append(",")
            k = sb.indexOf(sb1.toString())
            if (k != -1 && sb2.indexOf(sb1.toString()) == -1) {
                sb2.append(obj).append(",")
            }
        }
        sb = StringBuilder(sb2)
        if (sb.isNotEmpty() && ',' == sb[0]) sb.deleteCharAt(0)
        if (sb.isNotEmpty() && ',' == sb[sb.length - 1]) sb.deleteCharAt(sb.length - 1)
        if (sb.isNotEmpty() && "," == sb.toString()) sb = StringBuilder()
        jTextField.text = sb.toString()
        comboBox.repaint()
        list.repaint()
    }
}

class CapBasicComboPopup(comboBox: JComboBox<Any>) : BasicComboPopup(comboBox) {

    override fun configureList() {
        super.configureList()
        list.selectionModel =
            CapDefaultListSelectionModel(comboBox)
    }

    override fun createListMouseListener(): MouseListener {
        return CapMouseAdapter(list, comboBox)
    }
}

class CapMetalComboBoxUI : MetalComboBoxUI {

    constructor(comboBox: CapJComboBox) {
        this.comboBox = comboBox
    }

    fun clearSelection(){
        this.popup.list.clearSelection()
    }

    override fun createPopup(): ComboPopup {
        return CapBasicComboPopup(comboBox)
    }
}

class CapDefaultComboBoxModel<E> : DefaultComboBoxModel<E> {
    constructor(elements: Collection<E>) : super() {
        elements.forEach {
            addElement(it)
        }
    }
}

class CapJCheckBox<E> : JCheckBox(), ListCellRenderer<E> {
    override fun getListCellRendererComponent(
        list: JList<out E>,
        value: E,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        componentOrientation = list.componentOrientation
        if (isSelected) {
            background = list.selectionBackground
            setForeground(list.selectionForeground)
        } else {
            background = list.background
            setForeground(list.foreground)
        }
        isEnabled = list.isEnabled
        setSelected(isSelected)
        text = value?.toString() ?: ""
        font = list.font
        return this
    }
}

class CapJComboBox : ComboBox<Any>, FocusListener {
    val set = HashSet<Int>()

    constructor(elements: Collection<Any>) : super() {
        init(elements)
    }

    fun init(elements: Collection<Any>) {
        setUI(CapMetalComboBoxUI(this))
        setRenderer(CapJCheckBox())
        border = BorderFactory.createLineBorder(Color.LIGHT_GRAY)
        setItems(elements)
    }

    fun setItems(elements: Collection<Any>) {
        (model as DefaultComboBoxModel)?.removeAllElements()
        model = CapDefaultComboBoxModel(elements)
        model.selectedItem = null
        selectedIndex = -1
    }

    fun clearSelection(){
        (getUI() as CapMetalComboBoxUI).clearSelection()
    }

    override fun focusLost(e: FocusEvent?) {
    }

    override fun focusGained(e: FocusEvent?) {
    }
}
