package com.cap.plugins.elasticsearch.ui.editor.rest.panel

import com.cap.plugins.common.component.table.*
import com.cap.plugins.common.util.ObjectMapper
import com.cap.plugins.elasticsearch.ui.editor.model.*
import com.cap.plugins.elasticsearch.ui.editor.panel.ElasticsearchTablePanel
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.collect.ArrayListMultimap
import com.intellij.openapi.project.Project

class ElasticsearchRestTablePanel(
    project: Project,
    val elasticsearchRestPanel: ElasticsearchRestPanel
) : ElasticsearchTablePanel(project) {

    private fun initAsSearchTable() {
        removeAll()
        tableWithPagination = CapTableWithPagination(tableModel)
        // 分页操作
        tableWithPagination.registerPageActionListener(object : CapPageActionListener {
            override fun firstPage(pageParameter: CapPageParameter): Collection<out CapTableItem> {
                return elasticsearchRestPanel.updateAndExecuteLastSearchRequest(pageParameter)
            }

            override fun previousPage(pageParameter: CapPageParameter): Collection<out CapTableItem> {
                return elasticsearchRestPanel.updateAndExecuteLastSearchRequest(pageParameter)
            }

            override fun nextPage(pageParameter: CapPageParameter): Collection<out CapTableItem> {
                return elasticsearchRestPanel.updateAndExecuteLastSearchRequest(pageParameter)
            }

            override fun lastPage(pageParameter: CapPageParameter): Collection<out CapTableItem> {
                return elasticsearchRestPanel.updateAndExecuteLastSearchRequest(pageParameter)
            }

        })
        // 分页大小
        tableWithPagination.registerSizeChangeActionListener(object : CapPageSizeChangeListener {
            override fun pageSizeChanged(pageParameter: CapPageParameter): Collection<out CapTableItem> {
                return elasticsearchRestPanel.updateAndExecuteLastSearchRequest(pageParameter)
            }

        })
        // 改变排序
        tableWithPagination.registerHeaderMouseListener {
            elasticsearchRestPanel.updateAndExecuteLastSearchRequest(it)
        }
        add(tableWithPagination)
    }

    override fun updateResultTable(context: ResponseContext): Collection<out CapTableItem> {
        // for error
        if (context.response.status.statusCode !in 200..299) {
            tableModel.createStringColumns(emptyList())
            tableWithPagination.tempList = emptyList()
            return tableWithPagination.tempList
        }

        context as RestResponseContext

        // for _search
        if (context.response.tableMode == TableMode.HIT_MAPPING_TABLE && context.isValidSearchRequest()) {
            val columns = createHitColumns(context.getMappings())
            if (!tableModel.isColumnEquals(columns.map { it.columnName })) {
                initAsSearchTable()
                tableModel.createTableColumns(columns)
            }
            val dataList = createHitItems(context)
            tableWithPagination.setPageSize(ObjectMapper.jsonMapper.readTree(context.request.body)?.get("size")?.asInt() ?: 10)
            tableWithPagination.setRowIndexFrom(
                ObjectMapper.jsonMapper.readTree(context.request.body)?.get("from")?.asInt() ?: 0
            )
            // 用tempList是为了使用分页按钮功能
            tableWithPagination.tempList = dataList
            return tableWithPagination.tempList
        }

        if (context.response.tableMode == TableMode.JSON_PROPERTY_TABLE) {
            initAsNormalTable()
            val columns = listOf("Property", "Value")
            if (!tableModel.isColumnEquals(columns)) {
                tableModel.createStringColumns(columns)
            }
            tableWithPagination.dataList = createPropertyItems(context)
            return tableWithPagination.dataList
        }

        if (context.response.tableMode == TableMode.CAT_TEXT_TABLE) {
            val responseList = readCatTextList(context.response.content)
            val columns = responseList.first()
            val items = responseList.subList(1, responseList.size)
            if (!tableModel.isColumnEquals(columns)) {
                initAsNormalTable()
                tableModel.createStringColumns(columns)
            }
            tableWithPagination.dataList = items.mapIndexed { index, list ->
                CapTableArrayItem(index, list.toTypedArray())
            }
            return tableWithPagination.dataList
        }

        if (context.response.tableMode == TableMode.CAT_JSON_TABLE) {
            val responseList = readCatJsonList(context.response.content)
            val columns = if (responseList.isEmpty()) emptySet() else responseList.first().keys
            if (!tableModel.isColumnEquals(columns)) {
                initAsNormalTable()
                tableModel.createStringColumns(columns)
            }
            tableWithPagination.dataList = responseList.mapIndexed { index, list ->
                CapTableMapItem(index, list)
            }
            return tableWithPagination.dataList
        }

        return emptyList()
    }

    override fun dispose() {
    }
}