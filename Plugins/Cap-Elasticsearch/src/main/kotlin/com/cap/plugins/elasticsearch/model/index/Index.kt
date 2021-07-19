package com.cap.plugins.elasticsearch.model.index

data class Index(
    val name: String,
    val health: String,
    val status: String,
    val index: String,
    val uuid: String,
    val primaries: String,
    val replicas: String,
    val documents: String,
    val deletedDocuments: String,
    val storeSize: String,
    val primaryStoreSize: String
)