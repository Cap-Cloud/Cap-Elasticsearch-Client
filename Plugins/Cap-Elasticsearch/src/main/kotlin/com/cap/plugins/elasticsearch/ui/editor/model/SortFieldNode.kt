package com.cap.plugins.elasticsearch.ui.editor.model

import com.cap.plugins.common.component.table.CapSortOrder
import com.cap.plugins.common.util.ObjectMapper
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode

class SortFieldNode {
    val jsonNode: JsonNode
    val field: String
    val order: CapSortOrder

    constructor(jsonNode: JsonNode) {
        this.jsonNode = jsonNode

        if (jsonNode.isValueNode) {
            field = jsonNode.textValue()
            order = CapSortOrder.ASC
        } else {
            val sortField = jsonNode.fields().next()
            order = extractOrder(sortField.value)
            field = sortField.key
        }
    }

    fun changeOrder(order: CapSortOrder): SortFieldNode {
        if (jsonNode.isValueNode) {
            val objectNode = ObjectMapper.jsonMapper.createObjectNode()
            objectNode.put(field, order.value)
            return SortFieldNode(objectNode)
        } else {
            val sortField = jsonNode.fields().next()
            val resultNode = jsonNode.deepCopy<ObjectNode>()
            if (sortField.value.isValueNode) {
                resultNode.put(sortField.key, order.value)
                return SortFieldNode(resultNode)
            }
            val sortPropertiesNode = sortField.value as ObjectNode
            sortPropertiesNode.put("order", order.value)
            resultNode.set<JsonNode>(sortField.key, sortPropertiesNode)
            return SortFieldNode(resultNode)
        }
    }


    private fun extractOrder(node: JsonNode): CapSortOrder {
        if (node.isValueNode) {
            return CapSortOrder.valueOf(node.textValue().toUpperCase())
        }
        if (node.isObject) {
            val orderNode = (node as ObjectNode).get("order")
            if (orderNode != null) {
                return CapSortOrder.valueOf(orderNode.textValue().toUpperCase())
            }
        }
        return CapSortOrder.ASC
    }

    private fun extractNestedPath(node: JsonNode): String? {
        if (node.isObject) {
            val objectNode = node as ObjectNode
            return if (objectNode.has("nested")) {
                val nestedNode = objectNode.get("nested")
                if (nestedNode.isObject && nestedNode.has("path")) {
                    nestedNode.get("path").textValue()
                } else {
                    null
                }
            } else if (objectNode.has("nested_path")) {
                objectNode.get("nested_path").textValue()
            } else {
                null
            }
        }
        return null
    }

    companion object {
        fun create(fieldName: String, order: CapSortOrder, nestedPath: String?): SortFieldNode {
            val node = ObjectMapper.jsonMapper.createObjectNode()
            if (nestedPath == null) {
                node.put(fieldName, order.value)
            } else {
                val sortPropertiesNode = ObjectMapper.jsonMapper.createObjectNode()
                sortPropertiesNode.put("order", order.value)
                sortPropertiesNode.put("nested_path", nestedPath)
                node.set(fieldName, sortPropertiesNode)
            }
            return SortFieldNode(node)
        }
    }
}