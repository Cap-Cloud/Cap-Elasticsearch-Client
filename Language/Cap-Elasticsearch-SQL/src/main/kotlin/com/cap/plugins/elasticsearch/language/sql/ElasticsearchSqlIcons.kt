package com.cap.plugins.elasticsearch.language.sql

import com.intellij.ui.IconManager
import javax.swing.Icon

object ElasticsearchSqlIcons {
    private fun load(path: String): Icon {
        return IconManager.getInstance().getIcon(path, ElasticsearchSqlIcons::class.java)
    }

    val ELASTICSEARCH_ICON = load("/icons/elasticsearch-13.svg")
}