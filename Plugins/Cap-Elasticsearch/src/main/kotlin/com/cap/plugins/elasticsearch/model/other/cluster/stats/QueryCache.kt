package com.cap.plugins.elasticsearch.model.other.cluster.stats

class QueryCache(
    val memory_size_in_bytes: Long,
    val total_count: Int,
    val hit_count: Int,
    val miss_count: Int,
    val cache_size: Int,
    val cache_count: Int,
    val evictions: Int
)