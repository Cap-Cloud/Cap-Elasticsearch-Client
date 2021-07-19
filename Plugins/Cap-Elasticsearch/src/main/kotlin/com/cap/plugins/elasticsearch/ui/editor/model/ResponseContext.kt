package com.cap.plugins.elasticsearch.ui.editor.model

import com.cap.plugins.common.http.model.Request
import com.cap.plugins.common.http.model.Response
import com.cap.plugins.common.util.ObjectMapper
import com.cap.plugins.elasticsearch.model.ElasticsearchResponse
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.http.NameValuePair
import org.apache.http.client.utils.URIBuilder

open class ResponseContext(
    open val request: Request,
    open val response: Response
) {

    private var responseJsonNode: JsonNode? = null
    private var requestBodyJsonNode: JsonNode? = null

    fun getResponseJsonNode(): JsonNode {
        if (responseJsonNode == null) {
            responseJsonNode = ObjectMapper.jsonMapper.readValue(response.content)
        }
        return responseJsonNode!!
    }

    fun getRequestBodyJsonNode(): JsonNode {
        if (requestBodyJsonNode == null) {
            requestBodyJsonNode = ObjectMapper.jsonMapper.readValue(if (request.body.isBlank()) "{}" else request.body)
        }
        return requestBodyJsonNode!!
    }

    fun isValidRequestAndResponse(): Boolean {
        return isResponseValid() && isRequestBodyValid()
    }

    fun isRequestBodyValid(): Boolean {
        return try {
            getRequestBodyJsonNode()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun isResponseValid(): Boolean {
        return try {
            getResponseJsonNode()
            true
        } catch (e: Exception) {
            false
        }
    }
}


class RestResponseContext(
    override val request: Request,
    override val response: ElasticsearchResponse
) : ResponseContext(request, response) {
    var mappingResponse: String? = null
    private var hits: List<Hit>? = null
    private var mappings: List<Mapping>? = null
    private var queryParams: Map<String, NameValuePair>? = null

    fun getHitsNode(): JsonNode? {
        return getResponseJsonNode().get("hits")
    }

    fun getTotal(): Long {
        val totalNode = getHitsNode()?.get("total") ?: return 0
        return when {
            totalNode.isInt -> {
                totalNode.asLong()
            }
            totalNode.isObject -> {
                totalNode.get("value").asLong()
            }
            else -> {
                0
            }
        }
    }

    fun getHits(): List<Hit> {
        if (hits == null) {
            hits = if (getHitsNode() == null) {
                emptyList()
            } else {
                (getHitsNode()!!.get("hits") as ArrayNode).asIterable().asSequence()
                    .map { Hit.create(it as ObjectNode) }
                    .toList()
            }
        }
        return hits!!
    }

    fun getMappings(): List<Mapping> {
        if (mappings == null) {
            mappings = if (mappingResponse == null) {
                return emptyList()
            } else {
                Mapping.parseMappings(mappingResponse!!)
            }
        }
        return mappings!!
    }

    private fun getQueryParams(): Map<String, NameValuePair> {
        if (queryParams == null) {
            queryParams = URIBuilder(request.path)
                .queryParams.associateBy { it.name }
        }
        return queryParams!!
    }

    fun getFrom(): Int {
        var from = getQueryParams()["from"]?.value?.toIntOrNull()
            ?: getRequestBodyJsonNode().get("from")?.asInt() ?: 0
        if (from < 0) {
            from = 0
        }
        return from
    }

    fun getSize(): Long {
        var size = getQueryParams()["size"]?.value?.toLongOrNull()
            ?: getRequestBodyJsonNode().get("size")?.asLong() ?: getHits().size.toLong()
        if (size < 0) {
            size = getHits().size.toLong()
        }
        return size
    }

    fun getSortFields(): List<SortFieldNode> {
        val sortNode = getRequestBodyJsonNode().get("sort") ?: return emptyList()
        return when {
            sortNode.isValueNode || sortNode.isObject -> listOf(
                SortFieldNode(
                    sortNode
                )
            )
            sortNode.isArray -> (sortNode as ArrayNode).elements().asSequence()
                .map { SortFieldNode(it) }
                .toList()
            else -> emptyList()
        }
    }

    fun isValidSearchRequest(): Boolean {
        return mappingResponse != null && hasHits()
    }

    private fun hasHits(): Boolean {
        if (!isResponseValid() || getHitsNode() == null) {
            return false
        }
        return getHitsNode()!!.has("hits")
    }
}

class SqlResponseContext(
    override val request: Request,
    override val response: Response
) : ResponseContext(request, response) {
}