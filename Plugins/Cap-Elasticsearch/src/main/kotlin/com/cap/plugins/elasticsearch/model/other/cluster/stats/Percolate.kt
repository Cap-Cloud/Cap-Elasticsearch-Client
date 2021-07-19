package com.cap.plugins.elasticsearch.model.other.cluster.stats

class Percolate(
    val total: Int,
    val time_in_millis: Int,
    val current: Int,
    val memory_size_in_bytes: Long,
    val memory_size: String,
    val queries: Int
)