package com.cap.plugins.elasticsearch.language.rest.api.current

import com.cap.plugins.common.http.model.Method
import com.cap.plugins.elasticsearch.language.rest.api.RequestApi
import com.cap.plugins.elasticsearch.language.rest.api.RequestApiTarget

class IndexLifecycleApi {
    companion object {
        val API_LIST = mutableListOf(
            // Index lifecycle management APIs
            RequestApi(
                documentation = "ilm-put-lifecycle.html",
                methods = mutableListOf(Method.PUT),
                patterns = mutableListOf(
                    "/_ilm/policy/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.POLICY)
            ),
            RequestApi(
                documentation = "ilm-get-lifecycle.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_ilm/policy",
                    "/_ilm/policy/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.POLICY)
            ),
            RequestApi(
                documentation = "ilm-delete-lifecycle.html",
                methods = mutableListOf(Method.DELETE),
                patterns = mutableListOf(
                    "/_ilm/policy/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.POLICY)
            ),
            RequestApi(
                documentation = "ilm-move-to-step.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_ilm/move/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "ilm-remove-policy.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/@target@/_ilm/remove"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "ilm-retry-policy.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/@target@/_ilm/retry"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "ilm-get-status.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_ilm/status"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "ilm-explain-lifecycle.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@/_ilm/explain"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "ilm-start.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_ilm/start"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "ilm-stop.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_ilm/stop"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
        )
    }
}