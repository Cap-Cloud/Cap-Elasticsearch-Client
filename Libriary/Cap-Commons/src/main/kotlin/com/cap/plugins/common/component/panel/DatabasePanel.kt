package com.cap.plugins.common.component.panel

import com.cap.plugins.common.action.CapAction
import com.cap.plugins.common.component.CapHintTextField
import com.cap.plugins.common.component.table.CapDefaultTableModel
import com.cap.plugins.common.component.table.CapTableArrayItem
import com.cap.plugins.common.component.table.CapTableItem
import com.cap.plugins.common.component.table.CapTableWithPagination
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.common.util.FileChooserUtil
import com.cap.plugins.common.util.layoutNCS
import com.cap.plugins.common.util.layoutWCE
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.CollectionComboBoxModel
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.layout.PropertyBinding
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.panel
import com.intellij.ui.layout.withTextBinding
import java.awt.BorderLayout
import java.awt.Dimension
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLClassLoader
import java.sql.Driver
import java.sql.DriverManager
import java.util.*
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class DatabasePanel(var originConfig: DatabaseConfig) : JComponent() {
    private val databaseConfig: DatabaseConfig = originConfig.copy()

    private val tabledPanel: JTabbedPane = JTabbedPane()

    private val databaseTypeTableModel: CapDefaultTableModel = object : CapDefaultTableModel() {
        override fun isCellEditable(row: Int, column: Int): Boolean = false
    }

    private val databaseTypeTable: CapTableWithPagination = CapTableWithPagination(databaseTypeTableModel)

    private val databaseDriverTableModel: CapDefaultTableModel = object : CapDefaultTableModel() {
        override fun isCellEditable(row: Int, column: Int): Boolean = false
    }

    private val databaseDriverTable: CapTableWithPagination = CapTableWithPagination(databaseDriverTableModel)

    private var databaseComboBox = ComboBox<String>()
    private var driverClassComboBox = ComboBox<String>()
    private var connectionTextField = JTextField()

    private var dbhostTextField: JTextField = JTextField()
    private var dbportTextField: JTextField = JTextField()
    private var dbnameTextField: JTextField = JTextField()
    private var dbuserTextField: JTextField = JTextField()
    private var dbpassPasswordField: JPasswordField = JPasswordField()

    private var dbhost = databaseConfig.dbhost ?: "<dbhost>"
    private var dbport = databaseConfig.dbport ?: "<dbport>"
    private var dbname = databaseConfig.dbname ?: "<dbname>"

    private var dbuser = ""
    private var dbpass = ""

    private var typePanel = object : JComponent() {
        init {
            layout = BorderLayout()
            databaseTypeTableModel.setColumnIdentifiers(arrayOf("Database Type", "Connection Template"))
            databaseTypeTable.withAction(CapAction("Add Database Type", Icons.General.ADD_ICON) {
                val dialog = DatabaseTypeDialog(databaseConfig.typeMap, null)
                dialog.show()
                if (dialog.isOK) {
                    val config = dialog.getConfig()
                    databaseTypeTable.dataList =
                        databaseTypeTable.dataList.plus(
                            CapTableArrayItem(
                                null,
                                arrayOf(config.first, config.second)
                            )
                        )
                    loadDatabaseType()
                }
            })
            databaseTypeTable.withAction(CapAction("Edit Database Type", Icons.Actions.EDIT_SOURCE_ICON, {
                databaseTypeTable.getSelectedRowCount() == 1
            }) {
                val vector: Vector<Any> =
                    databaseTypeTableModel.dataVector.elementAt(databaseTypeTable.getSelectedRow()) as Vector<Any>
                val dialog =
                    DatabaseTypeDialog(databaseConfig.typeMap, Pair(vector[1].toString(), vector[2].toString()))
                dialog.show()
                if (dialog.isOK) {
                    val config = dialog.getConfig()
                    databaseConfig.typeMap[config.first] = config.second
                    val item = databaseTypeTable.dataList.first {
                        (it as CapTableArrayItem).value[0] == config.first
                    }
                    val index = databaseTypeTable.dataList.indexOf(item)

                    databaseTypeTable.dataList = databaseTypeTable.dataList.toMutableList().also {
                        it.removeAt(index)
                    }.also {
                        it.add(index, CapTableArrayItem(null, arrayOf(config.first, config.second)))
                    }
                }
            })
            databaseTypeTable.withAction(CapAction("Delete Database Type", Icons.Actions.REMOVE_ICON, {
                databaseTypeTable.getSelectedRowCount() > 0
            }) {
                databaseTypeTable.dataList = databaseTypeTable.dataList.toMutableList().also { list ->
                    val selectedItems = databaseTypeTable.getSelectedRows()
                    selectedItems?.sortDescending()
                    selectedItems?.forEach { row ->
                        val vector: Vector<Any> =
                            databaseTypeTableModel.dataVector.elementAt(row) as Vector<Any>
                        val config = Pair(vector[1].toString(), vector[2].toString())

                        val item = list.first {
                            (it as CapTableArrayItem).value[0] == config.first
                        }
                        val index = list.indexOf(item)

                        list.removeAt(index)
                    }
                }
                loadDatabaseType()
            })
            add(databaseTypeTable)
        }
    }

    private var driverPanel = object : JComponent() {
        init {
            layout = BorderLayout()

            databaseComboBox.addActionListener {
                buildConnectionString(true)
            }
            databaseDriverTableModel.setColumnIdentifiers(arrayOf("Driver Jar Path"))
            databaseDriverTable.withAction(CapAction("Add Driver Jar", Icons.Actions.ADD_ICON) {
                val files: Array<VirtualFile> = FileChooserUtil.chooseDriverJars()
                val items = files.map { file ->
                    object : CapTableItem(null, file.path) {}
                }
                databaseDriverTable.dataList = databaseDriverTable.dataList.toMutableList().also {
                    it.addAll(items)
                }
                loadDriverClass()
            })
            databaseDriverTable.withAction(CapAction("Delete Driver Jar", Icons.Actions.REMOVE_ICON, {
                databaseDriverTable.getSelectedRowCount() > 0
            }) { _ ->
                databaseDriverTable.dataList = databaseDriverTable.dataList.toMutableList().also { list ->
                    val selectedItems = databaseDriverTable.getSelectedRows()
                    selectedItems?.sortDescending()
                    selectedItems?.forEach {
                        val vector: Vector<Any> = databaseDriverTableModel.dataVector.elementAt(it) as Vector<Any>
                        val path = vector[1].toString()
                        val item = list.first { item ->
                            item.value == path
                        }
                        val index = list.indexOf(item)

                        list.removeAt(index)
                    }
                }
                loadDriverClass()
            })

            val label1 = JLabel("Database Type:")
            label1.preferredSize.width = 100
            val label2 = JLabel("Driver Jar:")
            label2.preferredSize.width = 100
            val label3 = JLabel("Driver Class:")
            label3.preferredSize.width = 100
            val label4 = JLabel("URL:")
            label4.preferredSize.width = 100

            add(
                layoutNCS(
                    layoutWCE(label1, databaseComboBox),
                    layoutNCS(label2, databaseDriverTable),
                    layoutWCE(
                        layoutNCS(label3, label4),
                        layoutNCS(driverClassComboBox, connectionTextField)
                    )
                )
            )
        }
    }

    private var propertyPanel = object : JComponent() {
        private val feedbackLabel: JBLabel = JBLabel()

        private fun setFailureMessage(message: String?) {
            feedbackLabel.isVisible = true
            feedbackLabel.icon = Icons.General.ERROR_ICON
            feedbackLabel.foreground = JBColor.RED
            feedbackLabel.text = message
            feedbackLabel.toolTipText = "<html><p width=\"500\">$message</p></html>"
        }

        private fun setSuccessMessage(message: String) {
            feedbackLabel.isVisible = true
            feedbackLabel.icon = Icons.General.SUCCESS_ICON
            feedbackLabel.foreground = JBColor.GRAY
            feedbackLabel.text = message
            feedbackLabel.toolTipText = ""
        }

        init {
            layout = BorderLayout()

            feedbackLabel.isVisible = false
            feedbackLabel.maximumSize = Dimension(400, 20)

            val documentListener: DocumentListener = object : DocumentListener {
                override fun insertUpdate(e: DocumentEvent) {
                    buildConnectionString(false)
                }

                override fun removeUpdate(e: DocumentEvent) {
                    buildConnectionString(false)
                }

                override fun changedUpdate(e: DocumentEvent) {
                    buildConnectionString(false)
                }
            }

            dbhostTextField.document.addDocumentListener(documentListener)
            dbportTextField.document.addDocumentListener(documentListener)
            dbnameTextField.document.addDocumentListener(documentListener)

            dbhostTextField.text = databaseConfig.dbhost ?: ""
            dbportTextField.text = databaseConfig.dbport ?: ""
            dbnameTextField.text = databaseConfig.dbname ?: ""
            dbuserTextField.text = databaseConfig.dbuser ?: ""
            dbpassPasswordField.text = databaseConfig.dbpass ?: ""

            val content = panel {
                row {
                    label("")
                }
                row("Database host:") {
                    dbhostTextField().withTextBinding(PropertyBinding({ dbhost }, { dbhost = it }))
                }
                row("Database port:") {
                    dbportTextField().withTextBinding(PropertyBinding({ dbport }, { dbport = it }))
                }
                row("Database name:") {
                    dbnameTextField().withTextBinding(PropertyBinding({ dbname }, { dbname = it }))
                }
                row("Database user:") {
                    dbuserTextField().withTextBinding(PropertyBinding({ dbuser }, { dbuser = it }))
                }
                row("Database pass:") {
                    dbpassPasswordField().withTextBinding(PropertyBinding({ dbpass }, { dbpass = it }))
                }
                row("") {
                    feedbackLabel()
                }
                row("") {
                    button("Test Connection") {
                        val classLoader = Thread.currentThread().contextClassLoader
                        val addURL = classLoader.javaClass.getDeclaredMethod("addURL", URL::class.java)
                        databaseDriverTable.dataList.map {
                            it.value.toString()
                        }.forEach {
                            try {
                                addURL.invoke(classLoader, File(it).toURI().toURL())
                                Class.forName(driverClassComboBox.selectedItem?.toString() ?: "")
                                val connection = DriverManager.getConnection(
                                    connectionTextField.text,
                                    dbuserTextField.text,
                                    String(dbpassPasswordField.password)
                                )
                                if (connection != null) {
                                    setSuccessMessage("Connect success.")
                                } else {
                                    setFailureMessage("Connect failure.")
                                }
                            } catch (e: IllegalAccessException) {
                                setFailureMessage(e.message)
                            } catch (e: InvocationTargetException) {
                                setFailureMessage(e.message)
                            } catch (e: MalformedURLException) {
                                setFailureMessage(e.message)
                            } catch (e: ClassNotFoundException) {
                                setFailureMessage(e.message)
                            } catch (e: Exception) {
                                setFailureMessage(e.message)
                            }
                        }
                    }
                }
            }

            add(content)
        }
    }

    init {
        layout = BorderLayout()

        initData()

        tabledPanel.add("Database Type", typePanel)
        tabledPanel.add("Database Driver", driverPanel)
        tabledPanel.add("Database Property", propertyPanel)

        add(tabledPanel)
    }

    private fun initData() {
        if (databaseConfig.typeMap.isNotEmpty()) {
            // 初始化数据库类型表
            databaseTypeTable.dataList = databaseConfig.typeMap.map {
                CapTableArrayItem(null, arrayOf(it.key, it.value))
            }

            loadDatabaseType()
        }

        if (databaseConfig.databaseDrivers.isNotEmpty()) {
            // 初始化驱动包表
            databaseDriverTable.dataList = databaseConfig.databaseDrivers.map {
                object : CapTableItem(null, it) {}
            }

            loadDriverClass()
        }
    }

    private fun loadDatabaseType() {
        val typeItem = databaseComboBox.selectedItem?.toString()

        databaseConfig.typeMap.clear()
        databaseTypeTable.dataList.forEach {
            it as CapTableArrayItem
            databaseConfig.typeMap[it.value[0].toString()] = it.value[1].toString()
        }
        databaseComboBox.model = CollectionComboBoxModel(databaseConfig.typeMap.keys.toList())

        databaseComboBox.selectedItem = typeItem
        if (databaseComboBox.itemCount > 0 && databaseComboBox.selectedItem?.toString().isNullOrEmpty()) {
            databaseComboBox.selectedIndex = 0
        }

        buildConnectionString(true)
    }

    /**
     * 生成连接字符串
     *
     * @param flag
     */
    private fun buildConnectionString(flag: Boolean) {
        var template = if (flag) {
            databaseConfig.typeMap[databaseComboBox.selectedItem] ?: ""
        } else {
            connectionTextField.text
        }
        val oldConn = (databaseConfig.typeMap[databaseComboBox.selectedItem] ?: "")
            .replace("<dbhost>", dbhost)
            .replace("<dbport>", dbport)
            .replace("<dbname>", dbname)
        val newConn = (databaseConfig.typeMap[databaseComboBox.selectedItem] ?: "")
            .replace("<dbhost>", readHost())
            .replace("<dbport>", readPort())
            .replace("<dbname>", readName())
        template = template.replace(oldConn, newConn)
            .replace("<dbhost>", readHost())
            .replace("<dbport>", readPort())
            .replace("<dbname>", readName())

        connectionTextField.text = template
        dbhost = readHost()
        dbport = readPort()
        dbname = readName()
    }

    private fun readHost(): String {
        return if ("" == dbhostTextField.text) "<dbhost>" else dbhostTextField.text
    }

    private fun readPort(): String {
        return if ("" == dbportTextField.text) "<dbport>" else dbportTextField.text
    }

    private fun readName(): String {
        return if ("" == dbnameTextField.text) "<dbname>" else dbnameTextField.text
    }

    /**
     * 加载驱动类
     */
    private fun loadDriverClass() {
        val driverItem = driverClassComboBox.selectedItem?.toString()

        driverClassComboBox.removeAllItems()

        val urls = databaseDriverTable.dataList.map {
            val path = it.value.toString()
            File(path).toURI().toURL()
        }
        val classloader = URLClassLoader.newInstance(urls.toTypedArray())
        val sl =
            ServiceLoader.load(Driver::class.java, classloader)
        val iterator: Iterator<*> = sl.iterator()
        while (iterator.hasNext()) {
            driverClassComboBox.addItem(iterator.next()!!.javaClass.name)
        }
        driverClassComboBox.selectedItem = driverItem
        if (driverClassComboBox.itemCount > 0 && driverClassComboBox.selectedItem?.toString().isNullOrEmpty()) {
            driverClassComboBox.selectedIndex = 0
        }
    }
}

data class DatabaseConfig(var typeMap: MutableMap<String, String> = mutableMapOf()) {
    var databaseType: String? = null
    var databaseDrivers: Collection<String> = emptyList()
    var driverClass: String? = null
    var connectionUrl: String? = null
    var dbhost: String? = null
    var dbport: String? = null
    var dbname: String? = null
    var dbuser: String? = null
    var dbpass: String? = null
}

class DatabaseTypeDialog :
    DialogWrapper {
    private val typeMap: MutableMap<String, String>
    var pair: Pair<String, String>?

    private val typeField: CapHintTextField = CapHintTextField("Database Type")
    private val templateField: CapHintTextField = CapHintTextField("jdbc:...")

    private var typeValue: String
    private var templateValue: String

    constructor(typeMap: MutableMap<String, String>, pair: Pair<String, String>? = null) : super(false) {
        this.typeMap = typeMap
        this.pair = pair

        typeValue = pair?.first ?: ""
        templateValue = pair?.second ?: "jdbc:"

        title = "Database Template Define"

        typeField.text = typeValue
        templateField.text = templateValue

        pair?.let {
            typeField.isEnabled = false
        }

        init()
    }

    override fun createCenterPanel(): JComponent? {
        return panel {
            row("Database type:") {
                typeField()
                    .withTextBinding(PropertyBinding({ typeValue }, { typeValue = it }))
                    .withValidationOnInput(validateType())
                    .withValidationOnApply(validateType())
            }
            row("URL template:") {
                templateField()
                    .withTextBinding(PropertyBinding({ templateValue }, { templateValue = it }))
                    .withValidationOnInput(validateTemplate())
                    .withValidationOnApply(validateTemplate())
            }
        }
    }

    private fun validateType(): ValidationInfoBuilder.(JTextField) -> ValidationInfo? {
        return {
            when {
                it.text.isNullOrBlank() -> {
                    this.error("Database type name can not be null.")
                }
                pair == null && typeMap.containsKey(it.text) -> {
                    this.error("Database type name already exists.")
                }
                else -> {
                    null
                }
            }
        }
    }

    private fun validateTemplate(): ValidationInfoBuilder.(JTextField) -> ValidationInfo? {
        return {
            when {
                it.text.isNullOrBlank() -> {
                    this.error("URL template can not be null.")
                }
                !it.text.startsWith("jdbc:") -> {
                    this.error("URL template should start with 'jdbc:'")
                }
                else -> {
                    null
                }
            }
        }
    }

    fun getConfig(): Pair<String, String> {
        return Pair(typeValue, templateValue)
    }
}
