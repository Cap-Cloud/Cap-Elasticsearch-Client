package com.cap.plugins.elasticsearch.model.other.cluster.stats

class RequestCache(
    val memory_size_in_bytes: Long,
    val evictions: Int,
    val hit_count: Int,
    val miss_count: Int
)