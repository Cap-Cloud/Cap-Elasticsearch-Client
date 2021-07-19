package com.cap.plugins.elasticsearch.model.other.cluster.stats

class Recovery(
    val current_as_source: Int,
    val current_as_target: Int,
    val throttle_time_in_millis: Int
)