package com.cap.plugins.elasticsearch.model.other.cluster.stats

class Merges(
    val current: Int,
    val current_docs: Int,
    val current_size_in_bytes: Long,
    val total: Int,
    val total_time_in_millis: Int,
    val total_docs: Int,
    val total_size_in_bytes: Long,
    val total_stopped_time_in_millis: Int,
    val total_throttled_time_in_millis: Int,
    val total_auto_throttle_in_bytes: Long
)