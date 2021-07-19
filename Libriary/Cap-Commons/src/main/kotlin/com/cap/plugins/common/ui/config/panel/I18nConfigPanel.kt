package com.cap.plugins.common.ui.config.panel

import com.cap.plugins.common.component.panel.BaseItemPanel
import com.cap.plugins.common.component.table.CapDefaultTableModel
import com.cap.plugins.common.component.table.CapTable
import com.cap.plugins.common.constant.CapCommonI18n
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.service.capPersistentStateComponentI18n
import com.cap.plugins.common.ui.config.model.I18nItem
import com.cap.plugins.common.util.*
import com.google.common.collect.Iterators
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.options.Configurable
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.layout.panel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.event.TableModelEvent

class I18nConfigPanel : Configurable {
    private val project = ProjectUtil.getCurrProject()

    private var mute = true

    private val mainPanel = JPanel(BorderLayout())

    private var baseItemPanel: BaseItemPanel<I18nItem>

    private var currItemName: String? = null

    private val displayLanguage = JBCheckBox()

    private var tableModel = object : CapDefaultTableModel() {
        override fun isCellEditable(row: Int, column: Int): Boolean {
            return currItemName != "zh-CN" && column == 2
        }
    }
    private var table = CapTable(tableModel)

    private var i18nMap: MutableMap<String, MutableMap<String, String>>
    private var localeString: String = ""

    constructor() {
        this.localeString = "${capPersistentStateComponentI18n.locale}"
        this.i18nMap =
            CloneUtil.cloneByJson(
                capPersistentStateComponentI18n.i18nMap,
                MapMapStringTypeReference()
            )!!

        tableModel.setColumnIdentifiers(arrayOf("Key", "Default"))
        tableModel.addTableModelListener {
            if (mute && it.type == TableModelEvent.UPDATE) {
                val key = tableModel.getValueAt(it.firstRow, 0) as String
                val value = tableModel.getValueAt(it.firstRow, it.column) as String

                i18nMap[currItemName]!![key] = value
            }
        }
        table.adjustColumnsBySize()

        displayLanguage.addActionListener {
            if (mute) {
                if (displayLanguage.isSelected) {
                    currItemName?.let { name ->
                        localeString = name
                    }
                } else {
                    localeString = ""
                }
            }
        }

        val localPanel = layoutNCS(panel {
            row(I18nResources.getMessageDefault("cap.plugin.display.language", CapCommonI18n.CAP_DISPLAY_LANGUAGE)) {
                displayLanguage()
            }
        }, ScrollPaneFactory.createScrollPane(table))

        baseItemPanel = object : BaseItemPanel<I18nItem>(listOf(), true, localPanel) {
            override fun addItem(name: String) {
                mute = false
                currItemName = name
                i18nMap[name] =
                    CloneUtil.cloneByJson(i18nMap[""]!!, MapStringTypeReference())!!
                clearInput()
                reset(i18nMap.keys.filter { it != "" }.map { I18nItem(it) }, i18nMap.keys.filter { it != "" }.size - 1)
                mute = true
            }

            override fun copyItem(newName: String, item: I18nItem) {
            }

            override fun deleteItem(item: I18nItem) {
                mute = false
                i18nMap.remove(item.name)
                if (localeString == item.name) {
                    localeString = ""
                }
                currItemName = null
                clearInput()
                reset(i18nMap.keys.filter { it != "" }.map { I18nItem(it) }, 0)
                mute = true
            }

            override fun selectedItem(item: I18nItem) {
                mute = false
                currItemName = item.name
                showInput(item)
                mute = true
            }
        }

        mainPanel.add(baseItemPanel, BorderLayout.CENTER)
    }

    private fun createEditor(document: Document): EditorEx {
        val editor = EditorFactory.getInstance().createEditor(document, project) as EditorEx
        editor.settings.isLineMarkerAreaShown = false
        editor.settings.isLineNumbersShown = false
        editor.settings.isRightMarginShown = false
        editor.component.preferredSize = Dimension(500, 300)
        return editor
    }

    private fun clearInput() {
        displayLanguage.isSelected = false

        tableModel.clearAll()
        tableModel.setColumnIdentifiers(arrayOf("Key", "Default"))
        table.adjustColumnsBySize()
    }

    private fun showInput(item: I18nItem) {
        clearInput()

        displayLanguage.isSelected = localeString == item.name

        i18nMap[item.name]?.toSortedMap()?.let {
            tableModel.setColumnIdentifiers(arrayOf("Key", "Default", item.name))
            it.keys.forEach { key ->
                tableModel.addRow(
                    arrayOf(key, i18nMap[""]!![key], i18nMap[item.name]!![key])
                )
            }
        }
        table.adjustColumnsBySize()
    }

    override fun isModified(): Boolean {
        // key判断
        return if (!Iterators.elementsEqual(
                i18nMap.keys.stream().sorted().iterator(),
                capPersistentStateComponentI18n.i18nMap.keys.stream().sorted().iterator()
            )
        ) {
            true
        } else {
            !i18nMap.keys
                .map { key: Any? -> i18nMap[key]?.equals(capPersistentStateComponentI18n.i18nMap[key]) ?: false }
                .fold(true) { a, b -> a && b }
        } || "$localeString" != "${capPersistentStateComponentI18n.locale}"
    }

    override fun getDisplayName(): String {
        return I18nResources.getMessageDefault("cap.plugin.internationalization", CapCommonI18n.CAP_PLUGIN_I18N)
    }

    override fun apply() {
        val saveGroup =
            CloneUtil.cloneByJson(
                i18nMap,
                MapMapStringTypeReference()
            )!!
        capPersistentStateComponentI18n.locale = "$localeString"
        capPersistentStateComponentI18n.i18nMap = saveGroup
    }

    override fun createComponent(): JComponent? {
        return mainPanel
    }

    override fun reset() {
        mute = false
        this.currItemName = null
        this.localeString = "${capPersistentStateComponentI18n.locale}"
        this.i18nMap =
            CloneUtil.cloneByJson(
                capPersistentStateComponentI18n.i18nMap,
                MapMapStringTypeReference()
            )!!
        baseItemPanel.reset(i18nMap.keys.filter { it != "" }.map { I18nItem(it) }, 0)
        mute = true
    }
}