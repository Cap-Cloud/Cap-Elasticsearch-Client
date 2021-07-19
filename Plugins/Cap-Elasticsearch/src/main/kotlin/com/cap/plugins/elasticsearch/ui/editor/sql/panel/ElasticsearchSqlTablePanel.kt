package com.cap.plugins.elasticsearch.ui.editor.sql.panel

import com.cap.plugins.common.component.table.CapTableArrayItem
import com.cap.plugins.common.component.table.CapTableItem
import com.cap.plugins.common.component.table.CapTableMapItem
import com.cap.plugins.common.util.ObjectMapper
import com.cap.plugins.elasticsearch.ui.editor.model.Hit
import com.cap.plugins.elasticsearch.ui.editor.model.ResponseContext
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchTablePanel
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.readValue
import com.intellij.openapi.project.Project

class ElasticsearchSqlTablePanel(
    project: Project
) : ElasticsearchTablePanel(project) {

    init {
        add(tableWithPagination)
    }

    override fun updateResultTable(context: ResponseContext): Collection<out CapTableItem> {
        // for error
        if (context.response.status.statusCode !in 200..299) {
            tableModel.createStringColumns(emptyList())
            tableWithPagination.tempList = emptyList()
            return tableWithPagination.tempList
        }

        val jsonMap = ObjectMapper.jsonMapper.readValue<Map<String, Any>>(context.response.content)

        // for nlpchina explain
        if (jsonMap.containsKey("from")) {
            val columnList = jsonMap.keys
            val dataList = mutableListOf(CapTableMapItem(0, jsonMap))

            if (!tableModel.isColumnEquals(columnList)) {
                tableModel.createStringColumns(columnList)
            }
            tableWithPagination.dataList = dataList
            return tableWithPagination.dataList
        }
        // for nlpchina query
        if (jsonMap.containsKey("hits")) {
            val hits = (context.getResponseJsonNode().get("hits").get("hits") as ArrayNode).asIterable().asSequence()
                .map { Hit.create(it as ObjectNode) }
                .toList()
            val columnList = mutableListOf("_index", "_type", "_id", "_score")
            val dataList = hits.mapIndexed { index, hit ->
                CapTableMapItem(
                    index,
                    mutableMapOf<String, Any>(
                        "_index" to hit.index, "_type" to hit.type, "_id" to hit.id, "_score" to hit.score
                    ).also {
                        hit.values.forEach { (k, v) ->
                            if (!columnList.contains(k)) {
                                columnList.add(k)
                            }
                            it[k] = v ?: ""
                        }
                    }
                )
            }.toMutableList()

            if (!tableModel.isColumnEquals(columnList)) {
                tableModel.createStringColumns(columnList)
            }
            tableWithPagination.dataList = dataList
            return tableWithPagination.dataList
        }

        // for opendistro
        if (jsonMap.containsKey("schema") && jsonMap.containsKey("datarows")) {
            if (jsonMap.containsKey("schema")) {
                val columns = (jsonMap["schema"] as ArrayList<Map<String, Any>>).map {
                    it["name"].toString()
                }
                if (!tableModel.isColumnEquals(columns)) {
                    tableModel.createStringColumns(columns)
                }
            }
            val rows = (jsonMap["datarows"] as ArrayList<ArrayList<Any?>>).mapIndexed { index, arrayList ->
                CapTableArrayItem(index, arrayList.toArray())
            }
            tableWithPagination.dataList = rows
            return tableWithPagination.dataList
        }

        // for x-pack
        if (jsonMap.containsKey("rows")) {
            if (jsonMap.containsKey("columns")) {
                val columns = (jsonMap["columns"] as ArrayList<Map<String, Any>>).map {
                    it["name"].toString()
                }
                if (!tableModel.isColumnEquals(columns)) {
                    tableModel.createStringColumns(columns)
                }
            }
            val rows = (jsonMap["rows"] as ArrayList<ArrayList<Any?>>).mapIndexed { index, arrayList ->
                CapTableArrayItem(index, arrayList.toArray())
            }
            tableWithPagination.dataList = rows
            return tableWithPagination.dataList
        }
        return emptyList()
    }

}