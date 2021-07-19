package com.cap.plugins.elasticsearch.ui.explorer.dialog

import com.cap.plugins.common.component.CapHintTextField
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.elasticsearch.config.ElasticsearchPersistentStateSetting
import com.cap.plugins.elasticsearch.constant.ElasticsearchI18n
import com.cap.plugins.elasticsearch.ui.editor.rest.panel.ElasticsearchRestBodyPanel
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.ui.layout.panel
import javax.swing.JTextField

class ElasticsearchIndexDialog(val project: Project) : DialogWrapper(false) {
    private val config = service<ElasticsearchPersistentStateSetting>()

    val indexName: CapHintTextField =
        CapHintTextField(
            I18nResources.getMessageDefault(
                "action.index.create.index.name",
                ElasticsearchI18n.ACTION_INDEX_CREATE_INDEX_NAME
            )
        )

    val bodyPanel: ElasticsearchRestBodyPanel = ElasticsearchRestBodyPanel(project)

    init {
        title = I18nResources.getMessageDefault("action.index.create.title", ElasticsearchI18n.ACTION_INDEX_CREATE_TITLE)
        bodyPanel.setText(
            "{\n" +
                    "  \"settings\": {\n" +
                    "    \"number_of_shards\": 1,\n" +
                    "    \"number_of_replicas\": 3\n" +
                    "  },\n" +
                    "  \"mappings\": {\n" +
                    "    \"properties\": {\n" +
                    "    }\n" +
                    "  }\n" +
                    "}"
        )
        init()
    }

    override fun createCenterPanel() = panel {
        row(
            I18nResources.getMessageDefault(
                "action.index.create.index.name",
                ElasticsearchI18n.ACTION_INDEX_CREATE_INDEX_NAME
            )
        ) {
            indexName()
                .withValidationOnInput(validateName())
                .withValidationOnApply(validateName())
                .focused()
        }
        row(
            I18nResources.getMessageDefault(
                "action.index.create.index.body",
                ElasticsearchI18n.ACTION_INDEX_CREATE_INDEX_BODY
            )
        ) {
        }
        row() {
            component(JBScrollPane(bodyPanel))
        }
    }

    private fun validateName(): ValidationInfoBuilder.(JTextField) -> ValidationInfo? {
        return {
            when {
                it.text.isNullOrBlank() -> {
                    this.error(
                        I18nResources.getMessageDefault(
                            "action.index.create.index.name.cannot.null",
                            ElasticsearchI18n.ACTION_INDEX_CREATE_INDEX_NAME_CANNOT_NULL
                        )
                    )
                }
                !Regex("""[a-zA-Z0-9.\-_]+""").matches(it.text) -> {
                    this.error(
                        I18nResources.getMessageDefault(
                            "action.index.create.index.name.must.identifier",
                            ElasticsearchI18n.ACTION_INDEX_CREATE_INDEX_NAME_MUST_IDENTIFIER
                        )
                    )
                }
                else -> {
                    null
                }
            }
        }
    }
}