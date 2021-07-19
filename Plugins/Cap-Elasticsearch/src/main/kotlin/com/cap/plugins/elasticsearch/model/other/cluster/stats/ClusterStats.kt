package com.cap.plugins.elasticsearch.model.other.cluster.stats

class ClusterStats(
    val _all: All?,
    val _shards: Shards?,
    val indices: Map<String, All>
)
