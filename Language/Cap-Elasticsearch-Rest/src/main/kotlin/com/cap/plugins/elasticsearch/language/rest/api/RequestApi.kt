package com.cap.plugins.elasticsearch.language.rest.api

import com.cap.plugins.common.http.model.Method

class RequestApi(
    var documentation: String,
    var methods: MutableList<Method>,
    var patterns: MutableList<String>,
    var targets: MutableList<RequestApiTarget> = mutableListOf(),
    var queryParameters: MutableMap<String, MutableList<out Any>>? = null,
    var requestBody: MutableMap<String, MutableList<out Any>>? = null,
    var bothParameters: MutableMap<String, MutableList<out Any>>? = null
) {
    companion object {
        val REQUEST_API_MAPS = mutableMapOf(
            "current" to mutableListOf<RequestApi>().also {
                it.addAll(com.cap.plugins.elasticsearch.language.rest.api.current.CatApi.API_LIST)
                it.addAll(com.cap.plugins.elasticsearch.language.rest.api.current.ClusterApi.API_LIST)
                it.addAll(com.cap.plugins.elasticsearch.language.rest.api.current.DataStreamApi.API_LIST)
                it.addAll(com.cap.plugins.elasticsearch.language.rest.api.current.DocumentApi.API_LIST)
                it.addAll(com.cap.plugins.elasticsearch.language.rest.api.current.IndexApi.API_LIST)
                it.addAll(com.cap.plugins.elasticsearch.language.rest.api.current.IndexLifecycleApi.API_LIST)
                it.addAll(com.cap.plugins.elasticsearch.language.rest.api.current.InfoApi.API_LIST)
                it.addAll(com.cap.plugins.elasticsearch.language.rest.api.current.IngestApi.API_LIST)
                it.addAll(com.cap.plugins.elasticsearch.language.rest.api.current.SearchApi.API_LIST)
            }
        )

        const val TARGET = "@target@"
        const val INDEX = "@index@"
        const val ALIAS = "@alias@"
        const val METRICS = "@metrics@"
        const val TARGET_INDEX = "@target_index@"
        const val INDEX_METRICS = "@index_metric@"
        const val INDEX_TEMPLATE = "@index_template@"
        const val INDEX_UUID = "@index_uuid@"
        const val COMPONENT_TEMPLATE = "@component_template@"
        const val ID = "@id@"
        const val QUERY = "@query@"
    }
}