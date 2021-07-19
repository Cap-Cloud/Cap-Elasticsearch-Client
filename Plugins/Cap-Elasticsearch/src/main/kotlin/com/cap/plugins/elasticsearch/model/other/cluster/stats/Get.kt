package com.cap.plugins.elasticsearch.model.other.cluster.stats

class Get(
    val total: Int,
    val time_in_millis: Int,
    val exists_total: Int,
    val exists_time_in_millis: Int,
    val missing_total: Int,
    val missing_time_in_millis: Int,
    val current: Int
)