package com.cap.plugins.elasticsearch.ui.explorer.dialog

import com.cap.plugins.common.component.CapHintTextField
import com.cap.plugins.common.constant.CapCommonConstant
import com.cap.plugins.common.constant.CapCommonI18n
import com.cap.plugins.common.http.response.JsonResponseHandler
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.common.task.CapTaskExecution
import com.cap.plugins.common.util.background
import com.cap.plugins.elasticsearch.client.ElasticsearchClient
import com.cap.plugins.elasticsearch.config.ElasticsearchConfiguration
import com.cap.plugins.elasticsearch.config.ElasticsearchPersistentStateSetting
import com.cap.plugins.elasticsearch.constant.ElasticsearchConstant
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.fileChooser.PathChooserDialog
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.PropertyBinding
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.panel
import com.intellij.ui.layout.withTextBinding
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpUriRequest
import java.awt.Dimension
import javax.swing.JPasswordField
import javax.swing.JTextField

class ElasticsearchClusterDialog(private val configuration: ElasticsearchConfiguration? = null) : DialogWrapper(false) {
    private val config = service<ElasticsearchPersistentStateSetting>()

    private val tabbedPane: JBTabbedPane = JBTabbedPane(1)

    private val generalPanel: DialogPanel
    private val clusterName: CapHintTextField =
        CapHintTextField(
            I18nResources.getMessageDefault(
                "action.cluster.add.cluster.name",
                CapCommonI18n.ACTION_CLUSTER_ADD_CLUSTER_NAME
            )
        )
    private val urlField: CapHintTextField = CapHintTextField("http://localhost:9200")
    private val passwordField: JPasswordField = JPasswordField()

    private val sslPanel: DialogPanel
    private val truststorePasswordField: JPasswordField = JPasswordField()
    private val keystorePasswordField: JPasswordField = JPasswordField()

    private val awsPanel: DialogPanel
    private val regionField: JBTextField = JBTextField()
    private val accessKeyField: JPasswordField = JPasswordField()
    private val secretKeyField: JPasswordField = JPasswordField()

    private val feedbackLabel: JBLabel = JBLabel()

    private var name = configuration?.get(CapCommonConstant.CLUSTER_NAME) ?: "@localhost"
    private var url = configuration?.get(ElasticsearchConstant.CLUSTER_PROPS_URL) ?: "http://localhost:9200"
    private var user = configuration?.get(ElasticsearchConstant.CLUSTER_PROPS_USER) ?: ""
    private var password = configuration?.get(ElasticsearchConstant.CLUSTER_PROPS_PASSWORD) ?: ""

    private var trustStorePath = configuration?.get(ElasticsearchConstant.CLUSTER_PROPS_TRUST_STORE_PATH) ?: ""
    private var trustStorePassword = configuration?.get(ElasticsearchConstant.CLUSTER_PROPS_TRUST_STORE_PASSWORD) ?: ""
    private var keyStorePath = configuration?.get(ElasticsearchConstant.CLUSTER_PROPS_KEY_STORE_PATH) ?: ""
    private var keyStorePassword = configuration?.get(ElasticsearchConstant.CLUSTER_PROPS_KEY_STORE_PASSWORD) ?: ""
    private var selfSigned = configuration?.get(ElasticsearchConstant.CLUSTER_PROPS_SELF_SIGNED) == "true"

    private var region = ""
    private var accessKey = ""
    private var secretKey = ""

    init {
        title = I18nResources.getMessageDefault("action.cluster.add.title", CapCommonI18n.ACTION_CLUSTER_ADD_TITLE)
        configuration?.let {
            title =
                I18nResources.getMessageDefault("action.cluster.edit.title", CapCommonI18n.ACTION_CLUSTER_EDIT_TITLE)
            clusterName.text = name
            clusterName.isEnabled = false
            urlField.text = url
            passwordField.text = password
            truststorePasswordField.text = trustStorePassword
            keystorePasswordField.text = keyStorePassword
        }

        feedbackLabel.isVisible = false
        feedbackLabel.maximumSize = Dimension(400, 20)

        generalPanel = createGeneralPanel()
        sslPanel = createSSLPanel()
        awsPanel = createAwsPanel()

        tabbedPane.addTab("General", generalPanel)
        tabbedPane.addTab("SSL", sslPanel)
        // tabbedPane.addTab("AWS", awsPanel)

        init()
    }

    override fun createCenterPanel() = panel {
        row {
            component(tabbedPane)
                .withValidationOnApply {
                    validateTabs()
                }
        }
        row {
            button(
                I18nResources.getMessageDefault(
                    "action.cluster.add.button.test",
                    CapCommonI18n.ACTION_CLUSTER_ADD_BUTTON_TEST
                )
            ) {
                testConnection()
            }
        }
        row {
            feedbackLabel()
        }
        onGlobalApply {
            generalPanel.apply()
            sslPanel.apply()
            awsPanel.apply()
        }
    }

    private fun validateTabs(): ValidationInfo? {
        sequenceOf(generalPanel, sslPanel, awsPanel)
            .forEachIndexed { index, panel ->
                panel.apply()
                val valInfo: ValidationInfo? = panel.componentValidateCallbacks.values.asSequence()
                    .map { it() }
                    .filterNotNull()
                    .firstOrNull()
                if (valInfo != null) {
                    tabbedPane.selectedIndex = index
                    return valInfo
                }
            }
        return null
    }

    private fun validateName(): ValidationInfoBuilder.(JTextField) -> ValidationInfo? {
        return {
            when {
                it.text.isNullOrBlank() -> {
                    this.error(
                        I18nResources.getMessageDefault(
                            "action.cluster.add.cluster.name.cannot.null",
                            CapCommonI18n.ACTION_CLUSTER_ADD_CLUSTER_NAME_CANNOT_NULL
                        )
                    )
                }
                config.elasticsearchClusters.containsKey(it.text) -> {
                    this.error(
                        I18nResources.getMessageDefault(
                            "action.cluster.add.cluster.already.exists",
                            CapCommonI18n.ACTION_CLUSTER_ADD_CLUSTER_ALREADY_EXISTS,
                            it.text
                        )
                    )
                }
                else -> {
                    null
                }
            }
        }
    }

    private fun validateAwsField(fieldName: String): ValidationInfoBuilder.(JTextField) -> ValidationInfo? {
        return {
            if ((region.isNotBlank() || accessKey.isNotBlank() || secretKey.isNotBlank()) && it.text.isNullOrBlank()) {
                this.error(
                    I18nResources.getMessageDefault(
                        "validation.input.field.required",
                        CapCommonI18n.VALIDATION_INPUT_FIELD_REQUIRED,
                        fieldName
                    )
                )
            } else {
                null
            }
        }
    }

    private fun createGeneralPanel() = panel {
        row("Name:") {
            clusterName()
                .withBinding({
                    it.text
                }, { it, value ->
                    it.text = value
                }, PropertyBinding({
                    name
                }, {
                    name = it
                })).also {
                    if (configuration == null) {
                        it.withValidationOnInput(validateName())
                            .withValidationOnApply(validateName())
                            .focused()
                    }
                }
        }
        row("URL:") {
            urlField().withTextBinding(PropertyBinding({ url }, { url = it })).also {
                if (configuration != null) {
                    it.focused()
                }
            }
        }
        row("User:") {
            textField({ user }, { user = it })
        }
        row("Password:") {
            passwordField()
                .withTextBinding(PropertyBinding({ password }, { password = it }))
        }
    }

    private fun createSSLPanel() = panel {
        row("Truststore:") {
            textFieldWithBrowseButton(PropertyBinding({ trustStorePath }, { trustStorePath = it }),
                fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor()
                    .also { it.putUserData(PathChooserDialog.PREFER_LAST_OVER_EXPLICIT, false) }
            )
        }
        row("Truststore password:") {
            truststorePasswordField().withTextBinding(
                PropertyBinding(
                    { trustStorePassword },
                    { trustStorePassword = it })
            )
        }
        row("Keystore:") {
            textFieldWithBrowseButton(PropertyBinding({ keyStorePath }, { keyStorePath = it }),
                fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor()
                    .also { it.putUserData(PathChooserDialog.PREFER_LAST_OVER_EXPLICIT, false) }
            )
        }
        row("Keystore password:") {
            keystorePasswordField().withTextBinding(PropertyBinding({ keyStorePassword }, { keyStorePassword = it }))
        }
        row("") {
            checkBox("self-signed", { selfSigned }, { selfSigned = it })
        }
    }

    private fun createAwsPanel() = panel {
        row("Region:") {
            regionField().withTextBinding(PropertyBinding({ region }, { region = it }))
                .withValidationOnInput(validateAwsField("Region"))
                .withValidationOnApply(validateAwsField("Region"))
        }
        row("Access key:") {
            accessKeyField().withTextBinding(PropertyBinding({ accessKey }, { accessKey = it }))
                .withValidationOnInput(validateAwsField("Access key"))
                .withValidationOnApply(validateAwsField("Access key"))
        }
        row("Secret key:") {
            secretKeyField().withTextBinding(PropertyBinding({ secretKey }, { secretKey = it }))
                .withValidationOnInput(validateAwsField("Secret key"))
                .withValidationOnApply(validateAwsField("Secret key"))
        }
    }

    private fun testConnection() {
        generalPanel.apply()
        sslPanel.apply()
        awsPanel.apply()

        background("Connecting to Elasticsearch cluster...") {
            try {
                val client = ElasticsearchClient(getCluster())
                val httpClient = client.httpClient
                val request: HttpUriRequest = HttpGet(client.httpHost.toURI())

                CapTaskExecution({
                    httpClient.execute(
                        request,
                        JsonResponseHandler(Map::class.java)
                    )
                }).success {
                    setSuccessMessage("Connection success.")
                }.failure {
                    setErrorMessage("Connection failure: $it")
                }.finally { _, _ ->
                    client.close()
                }.execute()
            } catch (e: Exception) {
                setErrorMessage("Connection failure: $e")
            }
        }
    }

    private fun setErrorMessage(message: String?) {
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

    fun getCluster(): ElasticsearchConfiguration {
        val configuration = ElasticsearchConfiguration()
        configuration[CapCommonConstant.CLUSTER_NAME] = name
        configuration[ElasticsearchConstant.CLUSTER_PROPS_URL] = url
        configuration[ElasticsearchConstant.CLUSTER_PROPS_USER] = user
        configuration[ElasticsearchConstant.CLUSTER_PROPS_PASSWORD] = password
        configuration[ElasticsearchConstant.CLUSTER_PROPS_TRUST_STORE_PATH] = trustStorePath
        configuration[ElasticsearchConstant.CLUSTER_PROPS_TRUST_STORE_PASSWORD] = trustStorePassword
        configuration[ElasticsearchConstant.CLUSTER_PROPS_KEY_STORE_PATH] = keyStorePath
        configuration[ElasticsearchConstant.CLUSTER_PROPS_KEY_STORE_PASSWORD] = keyStorePassword
        configuration[ElasticsearchConstant.CLUSTER_PROPS_SELF_SIGNED] = "$selfSigned"
//        configuration[ElasticsearchConstant.CLUSTER_PROPS_REGION] = region
//        configuration[ElasticsearchConstant.CLUSTER_PROPS_ACCESS_KEY] = accessKey
//        configuration[ElasticsearchConstant.CLUSTER_PROPS_SECRET_KEY] = secretKey
        return configuration
    }
}