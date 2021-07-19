package com.cap.plugins.elasticsearch.model.cluster

data class ClusterHealth(
    val cluster_name: String,
    val status: String,
    val timed_out: Boolean,
    val number_of_nodes: Int,
    val number_of_data_nodes: Int,
    val active_primary_shards: Int,
    val active_shards: Int,
    val relocating_shards: Int,
    val initializing_shards: Int,
    val unassigned_shards: Int,
    val delayed_unassigned_shards: Int,
    val number_of_pending_tasks: Int,
    val number_of_in_flight_fetch: Int,
    val task_max_waiting_in_queue_millis: Long,
    val active_shards_percent_as_number: Double
)