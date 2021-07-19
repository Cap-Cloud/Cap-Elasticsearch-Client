package com.cap.plugins.elasticsearch.ui.editor.sql

import com.cap.plugins.elasticsearch.config.ElasticsearchConfiguration
import com.cap.plugins.elasticsearch.ui.editor.ElasticsearchFile
import com.cap.plugins.elasticsearch.ui.editor.sql.model.ElasticsearchSqlQueryType
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileSystem
import com.intellij.openapi.vfs.ex.dummy.DummyFileSystem

class ElasticsearchSqlFile(
    val project: Project,
    val clusterProperties: ElasticsearchConfiguration? = null,
    val queryType: ElasticsearchSqlQueryType? = null
) : ElasticsearchFile() {

    override fun getFileSystem(): VirtualFileSystem {
        return ElasticsearchSqlFileSystem.INSTANCE ?: DummyFileSystem.getInstance()
    }

    override fun getName(): String {
        return "Elasticsearch SQL"
    }
}