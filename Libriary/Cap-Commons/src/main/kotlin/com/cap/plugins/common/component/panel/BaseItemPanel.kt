package com.cap.plugins.common.component.panel

import com.cap.plugins.common.action.CapAction
import com.cap.plugins.common.annotation.KeepAll
import com.cap.plugins.common.constant.CapCommonI18n
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.common.service.CapActionManager
import com.cap.plugins.common.util.ProjectUtil
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.MessageDialogBuilder
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.CollectionListModel
import com.intellij.ui.border.CustomLineBorder
import com.intellij.ui.components.JBList
import java.awt.BorderLayout
import java.util.*
import java.util.function.Consumer
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.ListSelectionModel

@KeepAll
interface BaseItem {
    /**
     * 名称
     */
    var name: String
}

abstract class BaseItemPanel<T : BaseItem> : JPanel {
    /**
     * 可选面板集合
     */
    private var itemList: List<T>? = null

    /**
     * 列表元素是否可上下移动
     */
    private var movable = false

    /**
     * 左边面板
     */
    private var leftPanel = JPanel(BorderLayout())

    /**
     * 列表面板
     */
    private var listPanel: JBList<String>? = null

    constructor(itemList: List<T>) : this(itemList, false)

    constructor(itemList: List<T>, movable: Boolean) : this(itemList, movable, null)

    constructor(itemList: List<T>, movable: Boolean, rightComponent: JComponent?) {
        this.itemList = itemList
        this.movable = movable

        init(rightComponent)
    }

    /**
     * 新增元素
     *
     * @param name 元素名称
     */
    abstract fun addItem(name: String)

    /**
     * 复制元素
     *
     * @param newName 新名称
     * @param item    元素对象
     */
    abstract fun copyItem(newName: String, item: T)

    /**
     * 删除多个元素
     *
     * @param item 元素对象
     */
    abstract fun deleteItem(item: T)

    /**
     * 选中元素
     *
     * @param item 元素对象
     */
    abstract fun selectedItem(item: T)

    fun init(rightComponent: JComponent?) {
        layout = BorderLayout()

        val actionToolbar = CapActionManager.getInstance().createActionToolbar("Item Toolbar", true) {
            listOf(
                CapAction(Icons.Actions.ADD_ICON) {
                    val itemName = inputItemName("")
                    itemName?.let { addItem(it) }
                },
                CapAction(Icons.Actions.REMOVE_ICON) {

                    val selectedItem = getSelectedItem()
                    if (MessageDialogBuilder.yesNo(
                            I18nResources.getMessageDefault("cap.plugin.title", CapCommonI18n.CAP_PLUGIN_TITLE),
                            I18nResources.getMessageDefault(
                                "item.name.delete.confirm",
                                CapCommonI18n.ITEM_DELETE_CONFIRM,
                                selectedItem!!.name
                            )
                        ).ask(ProjectUtil.getCurrProject())
                    ) {
                        deleteItem(selectedItem!!)
                    }
                }
            )
        }

        // 添加边框
        actionToolbar.border = CustomLineBorder(1, 1, 1, 1)
        leftPanel.add(actionToolbar, BorderLayout.NORTH)

        listPanel = JBList(dataConvert()!!)
        listPanel!!.selectionMode = ListSelectionModel.SINGLE_SELECTION
        listPanel!!.addListSelectionListener {
            val item = getSelectedItem()
            item?.let { selectedItem(it) }
        }
        listPanel!!.border = CustomLineBorder(0, 1, 1, 1)

        leftPanel.add(listPanel, BorderLayout.CENTER)

        // 左右分割面板并添加至主面板
        val splitter = Splitter(false, 0.3f)
        splitter.firstComponent = leftPanel
        rightComponent.let {
            splitter.secondComponent = it
        }

        add(splitter)
    }

    /**
     * 输入元素名称
     *
     * @param initValue 初始值
     * @return 获得的名称，为null表示取消输入
     */
    fun inputItemName(initValue: String): String? {
        return Messages.showInputDialog(
            I18nResources.getMessageDefault("item.name.label", CapCommonI18n.ITEM_NAME),
            I18nResources.getMessageDefault("cap.plugin.title", CapCommonI18n.CAP_PLUGIN_TITLE),
            Messages.getQuestionIcon(),
            initValue,
            object : InputValidator {
                override fun checkInput(inputString: String): Boolean {
                    // 非空校验
                    if (inputString.isNullOrEmpty()) {
                        return false
                    }
                    // 不能出现同名
                    for (item in itemList!!) {
                        if (item.name == inputString) {
                            return false
                        }
                    }
                    return true
                }

                override fun canClose(inputString: String): Boolean {
                    return checkInput(inputString)
                }
            })
    }

    /**
     * 重置方法
     *
     * @param itemList      元素列表
     * @param selectedIndex 选中的元素下标
     */
    open fun reset(itemList: List<T>, selectedIndex: Int) {
        this.itemList = itemList
        listPanel!!.model = CollectionListModel(dataConvert()!!)

        // 存在元素时，默认选中第一个元素
        if (itemList.isNotEmpty()) {
            listPanel!!.selectedIndex = selectedIndex
            selectedItem(itemList[selectedIndex])
        }

    }

    /**
     * 数据转换
     *
     * @return 转换结果
     */
    private fun dataConvert(): List<String>? {
        val data: MutableList<String> = ArrayList()
        itemList!!.forEach(Consumer { item: T -> data.add(item.name) })
        return data
    }

    /**
     * 获取选中元素
     *
     * @return 选中元素
     */
    fun getSelectedItem(): T? {
        val selectedName = listPanel!!.selectedValue ?: return null
        for (t in itemList!!) {
            if (t.name == selectedName) {
                return t
            }
        }
        return null
    }
}