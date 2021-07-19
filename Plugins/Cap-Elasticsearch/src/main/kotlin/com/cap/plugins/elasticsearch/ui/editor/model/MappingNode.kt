package com.cap.plugins.elasticsearch.ui.editor.model

data class MappingNode(
    val name: String,
    val type: String?,
    val children: Map<String, MappingNode>,
    val fields: Map<String, MappingNode>,
    val nestedPath: String?
) {

    companion object {

        @Suppress("unchecked_cast")
        fun createNode(name: String, properties: Map<String, Any>, nestedPath: String?): MappingNode {
            val type = properties["type"] as String?
            val children = if (properties.contains("properties")) {
                (properties["properties"] as Map<String, Map<String, Any>>).map {
                    val node = when (type) {
                        null -> {
                            createNode(
                                name + "." + it.key,
                                it.value,
                                null
                            )
                        }
                        "nested" -> {
                            createNode(
                                name + "." + it.key,
                                it.value,
                                name
                            )
                        }
                        else -> {
                            createNode(
                                it.key,
                                it.value,
                                null
                            )
                        }
                    }
                    it.key to node
                }.toMap()
            } else {
                emptyMap()
            }
            val fields = if (properties.contains("fields")) {
                (properties["fields"] as Map<String, Map<String, Any>>).map {
                    it.key to createNode(
                        it.key,
                        it.value,
                        null
                    )
                }.toMap()
            } else {
                emptyMap()
            }
            return MappingNode(
                name,
                type,
                children,
                fields,
                nestedPath
            )
        }
    }
}