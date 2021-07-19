package com.cap.plugins.elasticsearch.ui.editor.rest

import com.cap.plugins.common.constant.CapCommonConstant
import com.cap.plugins.common.http.model.Request
import com.cap.plugins.elasticsearch.client.ElasticsearchClient
import com.cap.plugins.elasticsearch.config.ElasticsearchConfiguration
import com.cap.plugins.elasticsearch.ui.editor.ElasticsearchFile
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileSystem
import com.intellij.openapi.vfs.ex.dummy.DummyFileSystem

class ElasticsearchRestFile(
    val project: Project,
    val clusterProperties: ElasticsearchConfiguration,
    val elasticsearchClient: ElasticsearchClient,
    val request: Request
) : ElasticsearchFile() {
    var autoCommit = false
    override fun getFileSystem(): VirtualFileSystem {
        return ElasticsearchRestFileSystem.INSTANCE ?: DummyFileSystem.getInstance()
    }

    override fun getName(): String {
        return clusterProperties[CapCommonConstant.CLUSTER_NAME]!!
    }
}