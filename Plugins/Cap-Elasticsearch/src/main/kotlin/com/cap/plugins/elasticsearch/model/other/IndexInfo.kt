package com.cap.plugins.elasticsearch.model.other

class IndexInfo(
    val aliases: Map<String, Any>,
    val settings: Settings,
    val mappings: Map<String, Any>
) {
    class Settings(val index: Index)

    class Index(
        val creationDate: Long
    )
}