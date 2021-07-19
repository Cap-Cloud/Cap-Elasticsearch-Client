package com.cap.plugins.elasticsearch.ui.editor

import com.cap.plugins.elasticsearch.icon.ElasticsearchIcons
import com.intellij.openapi.fileTypes.ex.FakeFileType
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

object ElasticsearchFakeFileType : FakeFileType() {
    
    override fun getIcon(): Icon {
        return ElasticsearchIcons.ELASTICSEARCH_ICON
    }

    override fun isMyFileType(file: VirtualFile): Boolean {
        return false
    }

    override fun getName(): String {
        return "Elasticsearch"
    }

    override fun getDescription(): String {
        return "Elasticsearch"
    }
}