package com.cap.plugins.elasticsearch.language.rest.api.current

import com.cap.plugins.common.http.model.Method
import com.cap.plugins.elasticsearch.language.rest.api.RequestApi
import com.cap.plugins.elasticsearch.language.rest.api.RequestApiTarget

class CatApi {
    companion object {
        val API_LIST = mutableListOf(
            // Compact and aligned text (CAT) APIs
            RequestApi(
                documentation = "cat.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cat-alias.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/aliases",
                    "/_cat/aliases/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INDEX_ALIAS)
            ),
            RequestApi(
                documentation = "cat-allocation.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/allocation",
                    "/_cat/allocation/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.NODES)
            ),
            RequestApi(
                documentation = "cat-anomaly-detectors.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/ml/anomaly_detectors",
                    "/_cat/ml/anomaly_detectors/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.JOBS)
            ),
            RequestApi(
                documentation = "cat-count.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/count",
                    "/_cat/count/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "cat-dfanalytics.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/ml/data_frame/analytics",
                    "/_cat/ml/data_frame/analytics/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.DATA_FRAME_ANALYTICS_JOBS)
            ),
            RequestApi(
                documentation = "cat-datafeeds.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/ml/datafeeds",
                    "/_cat/ml/datafeeds/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.DATAFEEDS)
            ),
            RequestApi(
                documentation = "elastic.co/guide/en/elasticsearch/reference/current/cat-fielddata.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/fielddata",
                    "/_cat/fielddata/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.FIELDS)
            ),
            RequestApi(
                documentation = "cat-health.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/health"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cat-indices.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/indices",
                    "/_cat/indices/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "cat-master.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/master"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cat-nodeattrs.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/nodeattrs"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cat-nodes.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/nodes"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cat-pending-tasks.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/pending_tasks"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cat-plugins.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/plugins"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cat-recovery.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/recovery",
                    "/_cat/recovery/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "cat-repositories.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/repositories"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cat-segments.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/segments",
                    "/_cat/segments/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "cat-shards.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/shards",
                    "/_cat/shards/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "cat-snapshots.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/snapshots/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.REPOSITORIES)
            ),
            RequestApi(
                documentation = "cat-tasks.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/tasks"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cat-templates.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/templates",
                    "/_cat/templates/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.TEMPLATES)
            ),
            RequestApi(
                documentation = "cat-thread-pool.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/thread_pool",
                    "/_cat/thread_pool/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.THREAD_POOLS)
            ),
            RequestApi(
                documentation = "cat-trained-model.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/ml/trained_models"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "cat-transforms.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_cat/transforms",
                    "/_cat/transforms/*",
                    "/_cat/transforms/_all",
                    "/_cat/transforms/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.TRANSFORMS)
            )
        )
    }
}