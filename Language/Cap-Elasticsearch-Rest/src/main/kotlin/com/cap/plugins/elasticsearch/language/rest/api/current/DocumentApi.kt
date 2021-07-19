package com.cap.plugins.elasticsearch.language.rest.api.current

import com.cap.plugins.common.http.model.Method
import com.cap.plugins.elasticsearch.language.rest.api.RequestApi
import com.cap.plugins.elasticsearch.language.rest.api.RequestApiTarget

class DocumentApi {
    companion object {
        val API_LIST = mutableListOf(
            // Document APIs
            RequestApi(
                documentation = "docs-index_.html",
                methods = mutableListOf(Method.POST, Method.PUT),
                patterns = mutableListOf(
                    "/@target@/_doc",
                    "/@target@/_doc/@id@",
                    "/@target@/_create/@id@"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_LIMIT)
            ),
            RequestApi(
                documentation = "docs-get.html",
                methods = mutableListOf(Method.GET, Method.HEAD),
                patterns = mutableListOf(
                    "/@target@/_doc/@id@",
                    "/@target@/_source/@id@"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "docs-delete.html",
                methods = mutableListOf(Method.DELETE),
                patterns = mutableListOf(
                    "/@target@/_doc/@id@"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "docs-delete-by-query.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/@target@/_delete_by_query"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "docs-update.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/@target@/_update/@id@"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "docs-update-by-query.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/@target@/_update_by_query"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "docs-multi-get.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_mget",
                    "/@target@/_mget"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "docs-bulk.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_bulk",
                    "/@target@/_bulk"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "docs-reindex.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_reindex"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "docs-termvectors.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@/_termvectors/@id@"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "docs-multi-termvectors.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_mtermvectors",
                    "/@target@/_mtermvectors"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            )
        )
    }
}