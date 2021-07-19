package com.cap.plugins.elasticsearch.model.other.cluster.stats

class Search(
    val open_contexts: Int,
    val query_total: Int,
    val query_time_in_millis: Int,
    val query_current: Int,
    val fetch_total: Int,
    val fetch_time_in_millis: Int,
    val fetch_current: Int,
    val scroll_total: Int,
    val scroll_time_in_millis: Int,
    val scroll_current: Int
)