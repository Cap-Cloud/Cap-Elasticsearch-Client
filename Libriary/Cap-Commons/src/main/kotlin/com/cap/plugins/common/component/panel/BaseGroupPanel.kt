package com.cap.plugins.common.component.panel

import com.cap.plugins.common.action.CapAction
import com.cap.plugins.common.annotation.KeepAll
import com.cap.plugins.common.constant.CapCommonI18n
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.common.service.CapActionManager
import com.cap.plugins.common.util.ProjectUtil
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.MessageDialogBuilder
import com.intellij.openapi.ui.Messages
import com.intellij.ui.CollectionComboBoxModel
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.Label
import javax.swing.ComboBoxModel
import javax.swing.JPanel

@KeepAll
interface BaseGroup<E> {

    /**
     * 分组名称
     */
    var name: String

    /**
     * 元素集合
     */
    var elementList: MutableList<E>
}

abstract class BaseGroupPanel : JPanel {
    private var keepGroupName: String

    /**
     * 分组名称
     */
    private var groupNameList: List<String>? = null

    /**
     * 分组下拉框
     */
    private var comboBox: ComboBox<String>? = null

    constructor(groupNameList: List<String>, keepGroupName: String, defaultGroupName: String = keepGroupName) {
        layout = BorderLayout()
        this.keepGroupName = keepGroupName
        this.groupNameList = groupNameList
        init(defaultGroupName)
    }

    /**
     * 创建分组
     *
     * @param name 分组名称
     */
    abstract fun createGroup(name: String)

    /**
     * 删除分组
     *
     * @param name 分组名称
     */
    abstract fun deleteGroup(name: String)

    /**
     * 复制分组
     *
     * @param name 分组名称
     */
    abstract fun copyGroup(name: String)

    /**
     * 切换分组
     *
     * @param name 分组名称
     */
    abstract fun changeGroup(name: String)


    /**
     * 初始化方法
     *
     * @param defaultGroupName 默认选中分组
     */
    private fun init(defaultGroupName: String) {
        // 创建一个内容面板
        val contentPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        val comboBoxModel: ComboBoxModel<String> = CollectionComboBoxModel(groupNameList!!)
        comboBox = ComboBox(comboBoxModel)

        // 添加下拉框
        try {
            contentPanel.add(Label(I18nResources.getMessageDefault("group.name.label", CapCommonI18n.GROUP_NAME)))
        } catch (t: Throwable) {
        }
        contentPanel.add(comboBox)

        // 添加分组选中事件
        comboBox!!.addItemListener {
            changeGroup(comboBox!!.selectedItem as String)
        }

        // 选择默认分组
        comboBox!!.selectedItem = defaultGroupName
        val actionToolbar = CapActionManager.getInstance().createActionToolbar("Group Toolbar", true) {
            listOf(
                CapAction(Icons.Actions.COPY_ICON) {
                    val groupName = comboBox!!.selectedItem as String
                    val newGroupName = inputItemName(groupName + "Copy")
                    newGroupName?.let { copyGroup(it) }
                },
                CapAction(Icons.Actions.ADD_ICON) {
                    val newGroupName = inputItemName("")
                    newGroupName?.let { createGroup(it) }
                },
                CapAction(Icons.Actions.REMOVE_ICON, {
                    comboBox!!.selectedItem != keepGroupName
                }) {
                    val groupName = comboBox!!.selectedItem as String
                    // 默认分组不允许删除
                    if (groupName != keepGroupName) {
                        // 确认删除？
                        if (MessageDialogBuilder.yesNo(
                                I18nResources.getMessageDefault("cap.plugin.title", CapCommonI18n.CAP_PLUGIN_TITLE),
                                I18nResources.getMessageDefault(
                                    "group.name.delete.confirm",
                                    CapCommonI18n.GROUP_DELETE_CONFIRM,
                                    groupName
                                )
                            ).ask(ProjectUtil.getCurrProject())
                        ) {
                            deleteGroup(groupName)
                        }
                    }
                }
            )
        }
        contentPanel.add(actionToolbar)
        this.add(contentPanel, BorderLayout.WEST)
    }

    /**
     * 重置方法
     *
     * @param groupNameList    分组列表
     * @param defaultGroupName 默认选中分组
     */
    fun reset(groupNameList: List<String>, defaultGroupName: String) {
        this.groupNameList = groupNameList
        comboBox!!.model = CollectionComboBoxModel(groupNameList)
        if (this.groupNameList!!.isNotEmpty()) {
            comboBox!!.selectedItem = defaultGroupName
        }
        // 回调一波
        changeGroup(defaultGroupName)
    }


    /**
     * 输入元素名称
     *
     * @param initValue 初始值
     * @return 获得的名称，为null表示取消输入
     */
    private fun inputItemName(initValue: String): String? {
        return Messages.showInputDialog(
            I18nResources.getMessageDefault("group.name.label", CapCommonI18n.GROUP_NAME),
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
                    for (name in groupNameList!!) {
                        if (name == inputString) {
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
}