package com.cap.plugins.elasticsearch.ui.editor.model

import com.cap.plugins.common.util.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class Mapping(
    val index: String,
    val indexType: String?,
    val nodes: Map<String, MappingNode>
) {

    companion object {

        @Suppress("unchecked_cast")
        fun parseMappings(mappingsJson: String): List<Mapping> {
            val jsonMap = ObjectMapper.jsonMapper.readValue<Map<String, Any>>(mappingsJson)
            val mappings = ArrayList<Mapping>()
            jsonMap.forEach { (index, indexProperties) ->
                val indexMapping = (indexProperties as Map<String, Map<String, Any>>).getValue("mappings")
                if (indexMapping.contains("properties")) {
                    val nodes =
                        parseMappingNodes(
                            indexMapping
                        )
                    mappings.add(
                        Mapping(
                            index,
                            null,
                            nodes
                        )
                    )
                } else if (indexMapping.isNotEmpty()) {
                    indexMapping.forEach { (type, typeMapping) ->
                        val nodes =
                            parseMappingNodes(
                                typeMapping as Map<String, Any>
                            )
                        mappings.add(
                            Mapping(
                                index,
                                type,
                                nodes
                            )
                        )
                    }
                }
            }
            return mappings
        }

        @Suppress("unchecked_cast")
        private fun parseMappingNodes(mapping: Map<String, Any>): Map<String, MappingNode> {
            val mappingProperties = (mapping as Map<String, Map<String, Map<String, Any>>>).getValue("properties")
            return mappingProperties.map {
                it.key to MappingNode.createNode(
                    it.key,
                    it.value,
                    null
                )
            }.toMap()
        }
    }
}

