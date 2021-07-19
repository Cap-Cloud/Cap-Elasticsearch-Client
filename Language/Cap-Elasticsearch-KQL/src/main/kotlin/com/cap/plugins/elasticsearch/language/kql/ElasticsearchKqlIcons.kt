package com.cap.plugins.elasticsearch.language.kql

import com.intellij.ui.IconManager
import javax.swing.Icon

object ElasticsearchKqlIcons {
    private fun load(path: String): Icon {
        return IconManager.getInstance().getIcon(path, ElasticsearchKqlIcons::class.java)
    }

    val ELASTICSEARCH_ICON = load("/icons/elasticsearch-13.svg")
}