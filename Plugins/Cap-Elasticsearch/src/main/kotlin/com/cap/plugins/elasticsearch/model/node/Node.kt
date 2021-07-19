package com.cap.plugins.elasticsearch.model.node

data class Node(
    val name: String,
    val ip: String,
    val master: String,
    val role: String,
    val load_1m: String,
    val load_5m: String,
    val load_15m: String,
    val cpu: String,
    val ram: String,
    val heap: String
)