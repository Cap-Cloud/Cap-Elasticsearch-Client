package com.cap.plugins.elasticsearch.model.other

import com.cap.plugins.elasticsearch.model.HealthStatus

class ClusterStats(
    val clusterName: String,
    val status: HealthStatus,
    val indices: Indices,
    val nodes: Nodes
) {

    class Indices(
        val count: Int,
        val shards: Shards,
        val docs: Docs,
        val store: Store
    )

    class Shards(
        val total: Int,
        val primaries: Int
    )

    class Docs(
        val count: Long,
        val deleted: Long
    )

    class Store(
        val sizeInBytes: Long
    )

    class Nodes(
        val count: Count,
        val versions: List<String>
    )

    class Count(
        val total: Int
    )
}