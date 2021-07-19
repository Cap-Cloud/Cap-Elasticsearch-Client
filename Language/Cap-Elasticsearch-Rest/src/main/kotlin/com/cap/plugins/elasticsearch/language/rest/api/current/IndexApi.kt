package com.cap.plugins.elasticsearch.language.rest.api.current

import com.cap.plugins.common.http.model.Method
import com.cap.plugins.elasticsearch.language.rest.api.RequestApi
import com.cap.plugins.elasticsearch.language.rest.api.RequestApiTarget

class IndexApi {
    companion object {
        val API_LIST = mutableListOf(
            // Index APIs
            RequestApi(
                documentation = "indices-analyze.html",
                methods = mutableListOf(Method.GET, Method.POST),
                patterns = mutableListOf(
                    "/_analyze",
                    "/@target@/_analyze"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "indices-aliases.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_aliases"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
            RequestApi(
                documentation = "indices-clearcache.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_cache/clear",
                    "/@target@/_cache/clear"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-clone-index.html",
                methods = mutableListOf(Method.POST, Method.PUT),
                patterns = mutableListOf(
                    "/@index@/_clone/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_LIMIT)
            ),
            RequestApi(
                documentation = "indices-close.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/@target@/_close"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "indices-create-index.html",
                methods = mutableListOf(Method.PUT),
                patterns = mutableListOf(
                    "/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_LIMIT)
            ),
            RequestApi(
                documentation = "indices-component-template.html",
                methods = mutableListOf(Method.PUT),
                patterns = mutableListOf(
                    "/_component_template/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_LIMIT)
            ),
            RequestApi(
                documentation = "indices-add-alias.html",
                methods = mutableListOf(Method.PUT, Method.POST),
                patterns = mutableListOf(
                    "/@index@/_alias/@target@",
                    "/@index@/_aliases/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_LIMIT)
            ),
            RequestApi(
                documentation = "indices-put-template.html",
                methods = mutableListOf(Method.PUT),
                patterns = mutableListOf(
                    "/_index_template/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_LIMIT)
            ),
            RequestApi(
                documentation = "indices-templates-v1.html",
                methods = mutableListOf(Method.PUT),
                patterns = mutableListOf(
                    "/_template/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_LIMIT)
            ),
            RequestApi(
                documentation = "indices-delete-component-template.html",
                methods = mutableListOf(Method.DELETE),
                patterns = mutableListOf(
                    "/_component_template/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.COMPONENT_TEMPLATE)
            ),
            RequestApi(
                documentation = "dangling-index-delete.html",
                methods = mutableListOf(Method.DELETE),
                patterns = mutableListOf(
                    "/_dangling/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INDEX_UUID)
            ),
            RequestApi(
                documentation = "indices-delete-index.html",
                methods = mutableListOf(Method.DELETE),
                patterns = mutableListOf(
                    "/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "indices-delete-alias.html",
                methods = mutableListOf(Method.DELETE),
                patterns = mutableListOf(
                    "/@index@/_alias/@target@",
                    "/@index@/_aliases/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INDEX_ALIAS)
            ),
            RequestApi(
                documentation = "indices-delete-template-v1.html",
                methods = mutableListOf(Method.DELETE),
                patterns = mutableListOf(
                    "/_template/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.LEGACY_INDEX_TEMPLATES)
            ),
            RequestApi(
                documentation = "indices-flush.html",
                methods = mutableListOf(Method.GET, Method.POST),
                patterns = mutableListOf(
                    "/_flush",
                    "/@target@/_flush"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-forcemerge.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_forcemerge",
                    "/@target@/_forcemerge"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "freeze-index-api.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/@target@/_freeze"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "getting-component-templates.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_component_template/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.COMPONENT_TEMPLATE)
            ),
            RequestApi(
                documentation = "indices-get-field-mapping.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_mapping/field/@field@",
                    "/@target@/_mapping/field/@field@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-get-index.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-get-alias.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_alias",
                    "/_alias/@target@",
                    "/@index@/_alias/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INDEX_ALIAS)
            ),
            RequestApi(
                documentation = "indices-get-settings.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@/_settings",
                    "/@target@/_settings/@setting@",
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-get-template.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_index_template/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INDEX_TEMPLATE)
            ),
            RequestApi(
                documentation = "indices-get-template-v1.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_template/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INDEX_TEMPLATE)
            ),
            RequestApi(
                documentation = "indices-get-mapping.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_mapping",
                    "/@target@/_mapping"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "dangling-index-import.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_dangling/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INDEX_UUID)
            ),
            RequestApi(
                documentation = "indices-alias-exists.html",
                methods = mutableListOf(Method.HEAD),
                patterns = mutableListOf(
                    "/_alias/@target@",
                    "/@index@/_alias/@target@",
                ),
                targets = mutableListOf(RequestApiTarget.INDEX_ALIAS)
            ),
            RequestApi(
                documentation = "indices-exists.html",
                methods = mutableListOf(Method.HEAD),
                patterns = mutableListOf(
                    "/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-recovery.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@/_recovery",
                    "/_recovery"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-segments.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@/_segments",
                    "/_segments"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-shards-stores.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@/_shard_stores",
                    "/_shard_stores"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-stats.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@/_stats/@index_metric@",
                    "/@target@/_stats",
                    "/_stats"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-template-exists-v1.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_template/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.INDEX_TEMPLATE
                )
            ),
            RequestApi(
                documentation = "dangling-indices-list.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_dangling"
                ),
                targets = mutableListOf(
                    RequestApiTarget.NOT_NEEDED
                )
            ),
            RequestApi(
                documentation = "indices-open-close.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/@target@/_open"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-refresh.html",
                methods = mutableListOf(Method.GET, Method.POST),
                patterns = mutableListOf(
                    "/_refresh",
                    "/@target@/_refresh"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "indices-resolve-index-api.html",
                methods = mutableListOf(Method.GET, Method.POST),
                patterns = mutableListOf(
                    "/_resolve/index/@target@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-rollover-index.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/@target@/_rollover",
                    "/@target@/_rollover/@target_index@"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-shrink-index.html",
                methods = mutableListOf(Method.POST, Method.PUT),
                patterns = mutableListOf(
                    "/@target@/_shrink/@target_index@"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "indices-simulate-index.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_index_template/_simulate_index/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "indices-simulate-template.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/_index_template/_simulate/@target@"
                ),
                targets = mutableListOf(RequestApiTarget.INDEX_TEMPLATE)
            ),
            RequestApi(
                documentation = "indices-split-index.html",
                methods = mutableListOf(Method.POST, Method.PUT),
                patterns = mutableListOf(
                    "/@target@/_split/@target_index@"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "indices-synced-flush-api.html",
                methods = mutableListOf(Method.POST, Method.GET),
                patterns = mutableListOf(
                    "/_flush/synced",
                    "/@target@/_flush/synced"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "unfreeze-index-api.html",
                methods = mutableListOf(Method.POST),
                patterns = mutableListOf(
                    "/@target@/_unfreeze"
                ),
                targets = mutableListOf(RequestApiTarget.INDICES)
            ),
            RequestApi(
                documentation = "indices-update-settings.html",
                methods = mutableListOf(Method.PUT),
                patterns = mutableListOf(
                    "/@target@/_settings"
                ),
                targets = mutableListOf(
                    RequestApiTarget.DATA_STREAMS,
                    RequestApiTarget.INDICES,
                    RequestApiTarget.INDEX_ALIAS
                )
            ),
            RequestApi(
                documentation = "indices-put-mapping.html",
                methods = mutableListOf(Method.PUT),
                patterns = mutableListOf(
                    "/_mapping",
                    "/@target@/_mapping"
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