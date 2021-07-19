package com.cap.plugins.elasticsearch.model.cluster

data class ClusterInfo(
    val nodeName: String,
    val clusterName: String,
    val clusterUuid: String,
    val version: Version,
    val tagline: String
) {
    data class Version(
        val number: String,
        val buildFlavor: String,
        val buildType: String,
        val buildHash: String,
        val buildDate: String,
        val isSnapshot: Boolean,
        val luceneVersion: String,
        val minimumWireCompatibilityVersion: String,
        val minimumIndexCompatibilityVersion: String
    )
}