package com.cap.plugins.elasticsearch.model.other.cluster.stats

class Indexing(
    val index_total: Int,
    val index_time_in_millis: Int,
    val index_current: Int,
    val index_failed: Int,
    val delete_total: Int,
    val delete_time_in_millis: Int,
    val delete_current: Int,
    val noop_update_total: Int,
    val is_throttled: Boolean,
    val throttle_time_in_millis: Int
)