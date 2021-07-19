package com.cap.plugins.elasticsearch.language.rest.api.current

import com.cap.plugins.common.http.model.Method
import com.cap.plugins.elasticsearch.language.rest.api.RequestApi
import com.cap.plugins.elasticsearch.language.rest.api.RequestApiTarget

class ClusterApi {
    companion object {
        val API_LIST = mutableListOf(
            // Cluster APIs
            RequestApi(
                documentation = "cluster.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cluster"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cluster-allocation-explain.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cluster/allocation/explain"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cluster-get-settings.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cluster/settings"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cluster-health.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cluster/health/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "cluster-reroute.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_cluster/reroute"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cluster-state.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cluster/state/@metrics@/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "cluster-stats.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cluster/stats",
                    "/_cluster/stats/nodes/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.NODES)
            ),
            RequestApi(
                documentation = "cluster-update-settings.html",
                methods = mutableListOf(Method.PUT),
                patterns = mutableListOf(
                    "/_cluster/settings"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cluster-nodes-usage.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_nodes/usage",
                    "/_nodes/usage/@metrics@",
                    "/_nodes/@target@/usage",
                    "/_nodes/@target@/usage/@metrics@"
                ),
                targets = mutableListOf(RequestApiTarget.NODES)
            ),
            RequestApi(
                documentation = "cluster-nodes-hot-threads.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_nodes/hot_threads",
                    "/_nodes/@target@/hot_threads"
                ),
                targets = mutableListOf(RequestApiTarget.NODES)
            ),
            RequestApi(
                documentation = "cluster-nodes-info.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_nodes",
                    "/_nodes/@metrics@",
                    "/_nodes/@target@",
                    "/_nodes/@target@/@metrics@"
                ),
                targets = mutableListOf(RequestApiTarget.NODES)
            ),
            RequestApi(
                documentation = "cluster-nodes-reload-secure-settings.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_nodes/reload_secure_settings",
                    "/_nodes/@target@/reload_secure_settings"
                ),
                targets = mutableListOf(RequestApiTarget.NODES)
            ),
            RequestApi(
                documentation = "cluster-nodes-stats.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_nodes/stats",
                    "/_nodes/stats/@metrics@",
                    "/_nodes/stats/@metrics@/@index_metric@",
                    "/_nodes/@target@/stats",
                    "/_nodes/@target@/stats/@metrics@",
                    "/_nodes/@target@/stats/@metrics@/@index_metric@"
                ),
                targets = mutableListOf(RequestApiTarget.NODES)
            ),
            RequestApi(
                documentation = "cluster-pending.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cluster/pending_tasks"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cluster-remote-info.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_remote/info"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "tasks.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_tasks",
                    "/_tasks/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.TASKS)
            ),
            RequestApi(
                documentation = "voting-config-exclusions.html",
                methods = mutableListOf(Method.POST, Method.DELETE),
                patterns = mutableListOf(
                    "/_cluster/voting_config_exclusions"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            )
        )
    }
}