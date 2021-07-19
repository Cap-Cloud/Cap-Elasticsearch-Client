package com.cap.plugins.elasticsearch.icon

import com.intellij.ui.IconManager
import javax.swing.Icon

object ElasticsearchIcons {
    private fun load(path: String): Icon {
        return IconManager.getInstance().getIcon(path, ElasticsearchIcons::class.java)
    }

    val ELASTICSEARCH_ICON = load("/icons/cap/elasticsearch/elasticsearch-13.svg")
    val KIBANA_ICON = load("/icons/cap/elasticsearch/kibana.svg")
    val X_PACK_ICON = load("/icons/cap/elasticsearch/enterprise-search.svg")
}