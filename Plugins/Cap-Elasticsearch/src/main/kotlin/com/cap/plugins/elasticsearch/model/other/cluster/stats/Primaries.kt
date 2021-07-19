package com.cap.plugins.elasticsearch.model.other.cluster.stats

class Primaries(
    val docs: Docs,
    val store: Store,
    val indexing: Indexing,
    val get: Get,
    val search: Search,
    val merges: Merges,
    val refresh: Refresh,
    val flush: Flush,
    val warmer: Warmer,
    val query_cache: QueryCache,
    val fielddata: FieldData,
    val percolate: Percolate,
    val completion: Completion,
    val segments: Segments,
    val translog: TransLog,
    val suggest: Suggest,
    val request_cache: RequestCache,
    val recovery: Recovery
)