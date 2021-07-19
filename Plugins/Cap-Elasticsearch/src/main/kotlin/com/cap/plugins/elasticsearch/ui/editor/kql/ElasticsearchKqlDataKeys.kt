package com.cap.plugins.elasticsearch.ui.editor.kql

import com.intellij.openapi.actionSystem.DataKey
import com.intellij.ui.HyperlinkLabel

object ElasticsearchKqlDataKeys {
    val RUN_ALL_TOOLBAR_HYPERLINK_LABEL: DataKey<HyperlinkLabel> =
        DataKey.create("elasticsearch.kql.run.all.toolbar.hyperlink.label")
}