package com.cap.plugins.elasticsearch.ui.editor.model

import com.cap.plugins.common.util.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.google.common.collect.ArrayListMultimap

class Hit(
    val id: String,
    val index: String,
    val score: Double,
    val type: String,
    val values: Map<String, Any?>
) {

    companion object {

        fun create(objectNode: ObjectNode): Hit {
            val id = objectNode.get("_id").asText()
            val index = objectNode.get("_index").asText()
            val score = objectNode.get("_score").asDouble()
            val type = objectNode.get("_type").asText()
            val source = objectNode.get("_source")
            val values = HashMap<String, Any?>()
            if (source != null) {
                val map = ObjectMapper.jsonMapper.convertValue<Map<String, Any?>>(source)
                map.forEach { (key, value) ->
                    collectValues(
                        key,
                        value,
                        values
                    )
                }
            }
            return Hit(id, index, score, type, values)
        }

        private fun collectValues(field: String, value: Any?, values: MutableMap<String, Any?>) {
            values[field] = value
            if (value is List<*>) {
                val objects = value.asSequence().mapNotNull {
                    if (it is Map<*, *>) {
                        it
                    } else {
                        null
                    }
                }.toList()
                if (objects.isNotEmpty()) {
                    val multiMap = ArrayListMultimap.create<String, Any?>()
                    objects.forEach {
                        it.forEach { entry ->
                            multiMap.put("$field.${entry.key as String}", entry.value)
                        }
                    }
                    multiMap.asMap().forEach { entry ->
                        if (entry.value.size == 1) {
                            collectValues(
                                entry.key as String,
                                entry.value.first(),
                                values
                            )
                        } else if (entry.value.size > 1) {
                            collectValues(
                                entry.key as String,
                                entry.value,
                                values
                            )
                        }
                    }
                }
            } else if (value is Map<*, *>) {
                value.forEach {
                    collectValues(
                        "$field.${it.key as String}",
                        it.value,
                        values
                    )
                }
            }
        }
    }
}