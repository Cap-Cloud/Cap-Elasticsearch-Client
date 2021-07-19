package com.cap.plugins.elasticsearch.language.rest.api.current

import com.cap.plugins.common.http.model.Method
import com.cap.plugins.elasticsearch.language.rest.api.RequestApi
import com.cap.plugins.elasticsearch.language.rest.api.RequestApiTarget

class DataStreamApi {
    companion object {
        val API_LIST = mutableListOf(
            // Data stream APIs
            RequestApi(
                documentation = "indices-create-data-stream.html",
                methods = mutableListOf(Method.PUT),
                patterns = mutableListOf(
                    "/_data_stream/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.DATA_STREAMS)
            ),
            RequestApi(
                documentation = "indices-delete-data-stream.html",
                methods = mutableListOf(Method.DELETE),
                patterns = mutableListOf(
                    "/_data_stream/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.DATA_STREAMS)
            ),
            RequestApi(
                documentation = "indices-get-data-stream.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_data_stream/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.DATA_STREAMS)
            ),
            RequestApi(
                documentation = "indices-migrate-to-data-stream.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_data_stream/_migrate/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INDEX_ALIAS)
            ),
            RequestApi(
                documentation = "data-stream-stats-api.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_data_stream/@target@/_stats"
                ),
                targets = mutableListOf(RequestApiTarget.DATA_STREAMS)
            ),
            RequestApi(
                documentation = "promote-data-stream-api.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_data_stream/_promote/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.DATA_STREAMS)
            )
        )
    }
}