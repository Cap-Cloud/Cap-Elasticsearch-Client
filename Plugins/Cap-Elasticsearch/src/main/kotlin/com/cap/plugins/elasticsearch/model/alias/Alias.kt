package com.cap.plugins.elasticsearch.model.alias

data class Alias(
    var alias: String,
    var index: String,
    var filter: String,
    var routing_index: String,
    var routing_search: String,
    var is_write_index: String
)