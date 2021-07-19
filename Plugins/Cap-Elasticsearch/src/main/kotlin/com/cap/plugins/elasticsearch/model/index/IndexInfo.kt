package com.cap.plugins.elasticsearch.model.index

import com.fasterxml.jackson.databind.JsonNode

class IndexInfo(var name: String) {
    var aliases: Aliases? = null
    var mappings: Mappings? = null
    var settings: Settings? = null

    class Aliases(val origin: JsonNode) {

    }

    class Mappings(val origin: JsonNode) {

    }

    class Settings(settingsNode: JsonNode) {
        var index: Index = Index(settingsNode.get("index"))

        class Index(indexNode: JsonNode) {
//            var routing: JsonNode = indexNode.get("routing")
//            var search: JsonNode = indexNode.get("search")
//            var number_of_shards: Int = indexNode.get("number_of_shards").asInt()
//            var provided_name: String = indexNode.get("provided_name").asText()
            var frozen: Boolean = if(indexNode.has("frozen")) indexNode.get("frozen").asBoolean() else false
//            var creation_date: Long = indexNode.get("creation_date").asLong()
//            var number_of_replicas: Int = indexNode.get("number_of_replicas").asInt()
//            var uuid: String = indexNode.get("uuid").asText()
//            var version: JsonNode = indexNode.get("version")
        }
    }
}