package com.cap.plugins.elasticsearch.model.other.cluster.stats

class Shards(
    val total: Int,
    val successful: Int,
    val failed: Int
)