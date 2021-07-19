package com.cap.plugins.elasticsearch.model.other.cluster

/**
{
"name": "node-1",
"cluster_name": "es-cluster",
"version": {
"number": "2.3.3",
"build_hash": "218bdf10790eef486ff2c41a3df5cfa32dadcfde",
"build_timestamp": "2016-05-17T15:40:04Z",
"build_snapshot": false,
"lucene_version": "5.5.0"
},
"tagline": "You Know, for Search"
}*/
class ClusterInfo(
    val name: String,
    val cluster_name: String,
    val tagline: String
) {
    class Version(
        val number: String,
        val build_hash: String,
        val build_timestamp: String,
        val build_snapshot: Boolean,
        val lucene_version: String
    )
}
