package com.cap.plugins.elasticsearch.language.rest.api.current

import com.cap.plugins.common.http.model.Method
import com.cap.plugins.elasticsearch.language.rest.api.RequestApi
import com.cap.plugins.elasticsearch.language.rest.api.RequestApiTarget

class IngestApi {
    companion object {
        val API_LIST = mutableListOf(
            // Ingest APIs
            RequestApi(
                documentation = "put-pipeline-api.html",
                methods = mutableListOf(Method.PUT),
                patterns = mutableListOf(
                    "/_ingest/pipeline/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INGEST_PIPELINE)
            ),
            RequestApi(
                documentation = "get-pipeline-api.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_ingest/pipeline",
                    "/_ingest/pipeline/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INGEST_PIPELINE)
            ),
            RequestApi(
                documentation = "delete-pipeline-api.html",
                methods = mutableListOf(Method.DELETE),
                patterns = mutableListOf(
                    "/_ingest/pipeline/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INGEST_PIPELINE)
            ),
            RequestApi(
                documentation = "simulate-pipeline-api.html",
                methods = mutableListOf(Method.POST, Method.PUT),
                patterns = mutableListOf(
                    "/_ingest/pipeline/_simulate",
                    "/_ingest/pipeline/@target@/_simulate"
                ),
                targets = mutableListOf(RequestApiTarget.INGEST_PIPELINE)
            ),
        )
    }
}