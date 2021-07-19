package com.cap.plugins.elasticsearch.language.rest.api.current

import com.cap.plugins.common.http.model.Method
import com.cap.plugins.elasticsearch.language.rest.api.RequestApi
import com.cap.plugins.elasticsearch.language.rest.api.RequestApiTarget

class SearchApi {
    companion object {
        val API_LIST = mutableListOf(
            // Search APIs
            RequestApi(
                documentation = "search-search.html",
                methods = mutableListOf(Method.GET, Method.POST),
                patterns = mutableListOf(
                    "/_search",
                    "/@target@/_search"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                ),
                queryParameters = mutableMapOf(
                    "allow_no_indices" to RequestParamValueOption.BOOLEAN_LIST,
                    "allow_partial_search_results" to RequestParamValueOption.BOOLEAN_LIST,
                )
            ),
            RequestApi(
                documentation = "async-search.html",
                methods = mutableListOf(Method.GET, Method.POST, Method.DELETE),
                patterns = mutableListOf(
                    "/_async_search",
                    "/@target@/_async_search"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "scroll-api.html",
                methods = mutableListOf(Method.GET, Method.POST),
                patterns = mutableListOf(
                    "/_search/scroll"
                ),
                targets = mutableListOf(
                    RequestApiTarget.NOT_NEEDED
                ),
                queryParameters = mutableMapOf(
                    "scroll" to mutableListOf(),
                    "rest_total_hits_as_int" to RequestParamValueOption.BOOLEAN_LIST,
                )
            ),
            RequestApi(
                documentation = "clear-scroll-api.html",
                methods = mutableListOf(Method.DELETE),
                patterns = mutableListOf(
                    "/_search/scroll"
                ),
                targets = mutableListOf(
                    RequestApiTarget.NOT_NEEDED
                )
            ),
            RequestApi(
                documentation = "search-template.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_search/template"
                ),
                targets = mutableListOf(
                    RequestApiTarget.NOT_NEEDED
                )
            ),
            RequestApi(
                documentation = "multi-search-template.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_msearch/template"
                ),
                targets = mutableListOf(
                    RequestApiTarget.NOT_NEEDED
                )
            ),
            RequestApi(
                documentation = "search-shards.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@/_search_shards"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "search-multi-search.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@/_msearch"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "eql-search-api.html",
                methods = mutableListOf(Method.GET, Method.POST),
                patterns = mutableListOf(
                    "/@target@/_eql/search"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "get-async-eql-search-api.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_eql/search/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.NOT_LIMIT
                )
            ),
            RequestApi(
                documentation = "delete-async-eql-search-api.html",
                methods = mutableListOf(Method.DELETE),
                patterns = mutableListOf(
                    "/_eql/search/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.NOT_LIMIT
                )
            ),
            RequestApi(
                documentation = "search-count.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@/_count"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "search-validate.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@/_validate/@query@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "search-explain.html",
                methods = mutableListOf(Method.GET, Method.POST),
                patterns = mutableListOf(
                    "/@target@/_explain/@id@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.INDICES
                )
            ),
            RequestApi(
                documentation = "search-field-caps.html",
                methods = mutableListOf(Method.GET, Method.POST),
                patterns = mutableListOf(
                    "/_field_caps?fields=",
                    "/@target@/_field_caps?fields="
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "search-rank-eval.html",
                methods = mutableListOf(Method.GET, Method.POST),
                patterns = mutableListOf(
                    "/@target@/_rank_eval"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
        )
    }
}