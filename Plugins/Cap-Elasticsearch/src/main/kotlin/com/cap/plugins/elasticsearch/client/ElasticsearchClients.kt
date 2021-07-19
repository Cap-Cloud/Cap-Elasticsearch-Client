package com.cap.plugins.elasticsearch.client

import com.intellij.openapi.Disposable
import java.util.concurrent.ConcurrentHashMap

object ElasticsearchClients : Disposable {

    val clients = ConcurrentHashMap<String, ElasticsearchClient?>()

    override fun dispose() {
        clients.values.forEach { it?.close() }
    }
}