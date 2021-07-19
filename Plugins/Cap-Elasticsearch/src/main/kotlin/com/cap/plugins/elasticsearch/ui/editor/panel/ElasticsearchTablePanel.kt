package com.cap.plugins.elasticsearch.ui.editor.panel

import com.cap.plugins.common.component.table.*
import com.cap.plugins.common.util.ObjectMapper
import com.cap.plugins.elasticsearch.ui.editor.model.Mapping
import com.cap.plugins.elasticsearch.ui.editor.model.MappingNode
import com.cap.plugins.elasticsearch.ui.editor.model.ResponseContext
import com.cap.plugins.elasticsearch.ui.editor.model.RestResponseContext
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.collect.ArrayListMultimap
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import java.awt.BorderLayout
import javax.swing.JPanel

abstract class ElasticsearchTablePanel : JPanel, Disposable {
    val project: Project
    val tableModel = CapDefaultTableModel()
    var table = CapTable(tableModel)
    var tableWithPagination = CapTableWithPagination(tableModel)

    constructor(project: Project) : super(BorderLayout()) {
        this.project = project
        background = EditorColorsManager.getInstance().globalScheme.defaultBackground
    }

    fun initAsNormalTable() {
        removeAll()
        tableWithPagination = CapTableWithPagination(tableModel)
        add(tableWithPagination)
    }

    abstract fun updateResultTable(context: ResponseContext): Collection<out CapTableItem>

    fun readCatTextList(response: String): List<List<String>> {
        val lines = response.lines().filter { line -> line.isNotBlank() }
        val header = lines[0]
        val columnStarts = ArrayList<Int>()
        for (i in header.indices) {
            if (i == 0 || (header[i - 1] == ' ' && header[i] != ' ')) {
                columnStarts.add(i)
            }
        }
        columnStarts.add(Int.MAX_VALUE)
        val columnRanges = columnStarts.zipWithNext()
        return lines.asSequence()
            .map { line ->
                columnRanges.asSequence()
                    .map { range -> line.substring(range.first, minOf(range.second, line.length)).trim() }
                    .toList()
            }.toList()
    }

    fun readCatJsonList(response: String): List<Map<String, out Any>> {
        return ObjectMapper.jsonMapper.readValue(response, List::class.java) as List<Map<String, out Any>>
    }

    fun createPropertyItems(responseContext: RestResponseContext): List<CapTableItem> {
        val jsonMap = ObjectMapper.jsonMapper.readValue<Map<String, Any>>(responseContext.response.content)
        val itemMap = mutableMapOf<String, Any?>()
        collectPropertyItems(jsonMap, itemMap)
        return itemMap.entries.mapIndexed { index, entry ->
            CapTableArrayItem(index, arrayOf(entry.key, entry.value))
        }
    }

    fun collectPropertyItems(from: Map<String, Any?>?, to: MutableMap<String, Any?>, parent: String? = null) {
        from?.forEach { (key, value) ->
            if (from[key] is Map<*, *>) {
                collectPropertyItems(value as Map<String, Any?>?, to, key)
            } else {
                if (parent.isNullOrEmpty()) {
                    to[key] = from[key]
                } else {
                    to["${parent}.${key}"] = from[key]
                }
            }
        }
    }

    fun createHitItems(responseContext: RestResponseContext): List<CapTableItem> {
        return responseContext.getHits()
            .mapIndexed { index, hit ->
                CapTableMapItem(
                    index + responseContext.getFrom(),
                    mutableMapOf<String,Any>(
                        "_index" to hit.index, "_type" to hit.type, "_id" to hit.id, "_score" to hit.score
                    ).also {
                        hit.values.forEach { (k, v) -> it[k] = v ?: "" }
                    }
                )
            }.toMutableList()
    }

    fun createHitColumns(mappings: List<Mapping>): Collection<CapTableColumnInfo> {
        return mutableListOf(
            CapTableColumnInfo("_index", CapSortType.STRING, "_index"),
            CapTableColumnInfo("_type", CapSortType.STRING, "_type"),
            CapTableColumnInfo("_id", CapSortType.STRING, "_id"),
            CapTableColumnInfo("_score", CapSortType.DOUBLE, "_score")
        ).also {
            val columns = ArrayListMultimap.create<String, Pair<Mapping, MappingNode>>()
            mappings.forEach { mapping ->
                mapping.nodes.forEach {
                    collectHitColumns(it.key, it.value, mapping, columns)
                }
            }
            columns.forEach { k, v ->
                it.add(
                    CapTableColumnInfo(
                        k,
                        when (v.second.type) {
                            "date" -> CapSortType.DATE
                            "text" -> CapSortType.STRING
                            "long" -> CapSortType.LONG
                            else -> CapSortType.STRING
                        },
                        """
                        <html>
                            ${v.first.index}.${v.second.name}<br/>
                            type: ${v.second.type}<br/>
                        </html>
                        """.trimIndent()
                    )
                )
            }
        }
    }

    fun collectHitColumns(
        field: String,
        node: MappingNode,
        mapping: Mapping,
        columns: ArrayListMultimap<String, Pair<Mapping, MappingNode>>
    ) {
        if (node.children.isEmpty()) {
            columns.put(field, Pair(mapping, node))
        } else {
            node.children.forEach {
                collectHitColumns("$field.${it.key}", it.value, mapping, columns)
            }
        }
    }

    override fun dispose() {
    }
}