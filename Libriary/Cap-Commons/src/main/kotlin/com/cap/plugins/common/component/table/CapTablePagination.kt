package com.cap.plugins.common.component.table

import com.cap.plugins.common.action.CapAction
import com.cap.plugins.common.action.CapToggleOptionAction
import com.cap.plugins.common.action.CapToggleOptionActionGroup
import com.intellij.find.editorHeaderActions.Utils
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.*
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.border.EmptyBorder

enum class CapPageMode {
    SPECIAL, ACTIONS
}

class CapPageParameter {
    var pageSize: Int = 10
        internal set(value) {
            if (value >= 0) {
                field = value
                currPage = if (value == 0) 0 else (pageStart / value)
                pageStart = currPage * value
            }
        }

    var currPage: Int = 0
        internal set(value) {
            if (value >= 0) {
                field = value
                pageStart = value * pageSize
            }
        }

    var pageStart: Int = 0
        internal set(value) {
            if (value >= 0) {
                field = value
            }
        }

    internal var totalPage: Int = 0
        get() {
            return when {
                pageSize == 0 -> {
                    0
                }
                total % pageSize == 0 -> {
                    total / pageSize
                }
                else -> {
                    total / pageSize + 1
                }
            }
        }

    var pageEnd: Int = 10
        get() {
            return when {
                total == 0 -> {
                    pageSize
                }
                (pageStart + pageSize > total) -> {
                    total
                }
                else -> {
                    pageStart + pageSize
                }
            }
        }
        internal set(value) {
            if (value >= 0) {
                field = value
            }
        }

    internal var total: Int = 0
}

class CapPageToolbar : JComponent {
    internal var table: CapTable? = null
    private var tableModel: CapDefaultTableModel
    private var parent: CapTableWithPagination
    private var mode: CapPageMode = CapPageMode.SPECIAL

    private val defaultPageSizeList = listOf(10, 20, 50, 100)

    private var pageParameter: CapPageParameter = CapPageParameter()

    private var pageStatusListener: CapPageStatusListener = CapPageStatusAdapter.instance
    private var pageActionListener: CapPageActionListener = CapPageActionAdapter.instance
    private var pageSizeChangeListener: CapPageSizeChangeListener = CapPageSizeChangeAdapter.instance

    private var firstPageButton =
        CapAction("First Page", AllIcons.Actions.Play_first, {
            pageStatusListener.firstPageEnabled()
        }) {
            pageParameter.currPage = 0
            doPageAction(PageAction.FIRST)
        }

    private var previousPageButton =
        CapAction("Previous Page", AllIcons.Actions.Play_back, {
            pageStatusListener.previousPageEnabled() && when (mode) {
                CapPageMode.SPECIAL -> pageParameter.currPage > 0
                CapPageMode.ACTIONS -> true
            }
        }) {
            pageParameter.currPage = maxOf(0, pageParameter.currPage - 1)
            doPageAction(PageAction.PREVIOUS)
        }

    private var pageSizeButton = object : CapToggleOptionActionGroup("Change Page Size", true, {
        getPageSizeActions()
    }) {
        override fun displayTextInToolbar(): Boolean {
            return true
        }

        override fun useSmallerFontForTextInToolbar(): Boolean {
            return true
        }

        override fun update(e: AnActionEvent) {
            e.presentation.text =
                "Page Size: ${pageParameter.pageSize}. (Displayed ${pageParameter.pageStart + 1} - ${pageParameter.pageStart + pageParameter.pageSize})"
        }
    }

    private fun setPageSizeActions() {
        pageSizeButton.setActions(getPageSizeActions())
        pageSizeButton.update(
            AnActionEvent.createFromAnAction(
                pageSizeButton,
                null,
                "",
                actionToolbar.toolbarDataContext
            )
        )
        parent.repaint()
    }

    private fun getPageSizeActions(): Collection<CapToggleOptionAction> {
        return defaultPageSizeList.toMutableList().also {
            it.add(pageParameter.pageSize / 2)
            it.add(pageParameter.pageSize)
            it.add(pageParameter.pageSize * 2)
            // }.map {
            //     minOf(it, pageParameter.total)
            // }.filter {
            //     it in 1..pageParameter.total
        }.distinct().sorted().map { size ->
            CapToggleOptionAction(object : ToggleOptionAction.Option {
                override fun getName(): String {
                    return size.toString()
                }

                override fun setSelected(selected: Boolean) {
                    pageParameter.pageSize = size
                    doPageAction(PageAction.PAGESIZE)
                }

                override fun isSelected(): Boolean {
                    return pageParameter.pageSize == size
                }

            })
        }
    }

    private var nextPageButton =
        CapAction("Next Page", AllIcons.Actions.Play_forward, {
            pageStatusListener.nextPageEnabled() && when (mode) {
                CapPageMode.SPECIAL -> pageParameter.currPage + 1 < pageParameter.totalPage
                CapPageMode.ACTIONS -> true
            }
        }) {
            pageParameter.currPage = if (mode == CapPageMode.ACTIONS) {
                pageParameter.currPage + 1
            } else {
                minOf(pageParameter.currPage + 1, maxOf(pageParameter.totalPage - 1, 0))
            }
            doPageAction(PageAction.NEXT)
        }

    private var lastPageButton =
        CapAction("Last Page", AllIcons.Actions.Play_last, {
            pageStatusListener.lastPageEnabled()
        }) {
            pageParameter.currPage = if (mode == CapPageMode.ACTIONS) {
                pageParameter.currPage + 1
            } else {
                maxOf(pageParameter.totalPage - 1, 0)
            }
            doPageAction(PageAction.LAST)
        }

    /**
     * 对外提供，数据列表
     */
    var dataList: Collection<out CapTableItem> = emptyList()
        set(value) {
            field = value
            mode = CapPageMode.SPECIAL
            pageParameter.total = dataList.size
            pageParameter.currPage = 0
            doPageAction(PageAction.FIRST)
        }

    var tempList: Collection<out CapTableItem> = emptyList()
        set(value) {
            field = value
            showData(value)
        }

    private val group = DefaultActionGroup()
    private val actionToolbar: ActionToolbar =
        ActionManager.getInstance().createActionToolbar("Table Foot Pagination", group, true)

    private var lastAction: PageAction? = null

    constructor(parent: CapTableWithPagination, tableModel: CapDefaultTableModel) {
        this.parent = parent
        this.tableModel = tableModel
        initActionToolbar()
    }

    fun withTable(table: CapTable): CapPageToolbar {
        this.table = table
        return this
    }

    fun withAction(action: AnAction): CapPageToolbar {
        group.add(action)
        return this
    }

    fun withActions(vararg actions: AnAction): CapPageToolbar {
        group.addAll(*actions)
        return this
    }

    fun registerPageStatusListener(listener: CapPageStatusListener) {
        pageStatusListener = listener
    }

    fun registerPageActionListener(listener: CapPageActionListener) {
        mode = CapPageMode.ACTIONS
        pageActionListener = listener
    }

    fun registerSizeChangeActionListener(listener: CapPageSizeChangeListener) {
        pageSizeChangeListener = listener
    }

    fun refreshCurrentPage() {
        doPageAction(PageAction.REFRESH)
    }

    fun setPageSize(size: Int) {
        pageParameter.pageSize = size
        setPageSizeActions()
    }


    private fun initActionToolbar() {
        layout = BorderLayout()

        group.addAll(firstPageButton, previousPageButton, pageSizeButton, nextPageButton, lastPageButton)
        group.addSeparator()

        actionToolbar.setTargetComponent(this)
        actionToolbar.layoutPolicy = ActionToolbar.AUTO_LAYOUT_POLICY
        Utils.setSmallerFontForChildren(actionToolbar.component)

        actionToolbar.component.border = EmptyBorder(0, 0, 0, 0)
        border = JBUI.Borders.customLine(JBColor.border(), 1, 0, 0, 0)

        add(actionToolbar!!.component, BorderLayout.SOUTH)
    }

    private fun doPageAction(action: PageAction) {
        when (mode) {
            CapPageMode.ACTIONS -> {
                if (action != PageAction.REFRESH) {
                    lastAction = action
                }
                when (lastAction) {
                    null -> {
                    }
                    PageAction.FIRST -> showData(pageActionListener.firstPage(pageParameter))
                    PageAction.PREVIOUS -> showData(pageActionListener.previousPage(pageParameter))
                    PageAction.NEXT -> showData(pageActionListener.nextPage(pageParameter))
                    PageAction.LAST -> showData(pageActionListener.lastPage(pageParameter))
                    PageAction.PAGESIZE -> showData(pageSizeChangeListener.pageSizeChanged(pageParameter))
                    else -> {
                    }
                }
            }
            CapPageMode.SPECIAL -> {
                val subList: Collection<out CapTableItem> = when {
                    pageParameter.pageStart >= pageParameter.pageEnd -> {
                        emptyList()
                    }
                    pageParameter.total <= pageParameter.pageStart -> {
                        emptyList()
                    }
                    pageParameter.total < pageParameter.pageEnd -> {
                        dataList.toList().subList(pageParameter.pageStart, pageParameter.total)
                    }
                    else -> {
                        dataList.toList().subList(pageParameter.pageStart, pageParameter.pageEnd)
                    }
                }
                parent.rowHeader.indexFromShow = parent.rowHeader.indexFrom + pageParameter.pageStart
                showData(subList)
            }
        }
        setPageSizeActions()
    }

    @Deprecated("")
    private fun showDataBackup(dataCollection: Collection<out CapTableItem>) {
        tableModel.dataVector.clear()
        dataCollection.forEachIndexed { index, item ->
            when (item) {
                is CapTableArrayItem -> {
                    mutableListOf<Any?>((item.index ?: index) + 1).also {
                        it.addAll(item.value)
                    }.toTypedArray().let {
                        tableModel.addRow(it)
                    }
                }
                is CapTableMapItem -> {
                    mutableListOf<Any?>((item.index ?: index) + 1).also {
                        for (i in 1..tableModel.columnCount) {
                            it.add(item.value[tableModel.getColumnName(i)])
                        }
                    }.toTypedArray().let {
                        tableModel.addRow(it)
                    }
                }
                is CapTableItem -> {
                    mutableListOf<Any?>((item.index ?: index) + 1).also {
                        it.add(item.value)
                    }.toTypedArray().let {
                        tableModel.addRow(it)
                    }
                }
            }
        }
        table?.adjustColumnsBySize()
    }

    private fun showData(dataCollection: Collection<out CapTableItem>) {
        tableModel.dataVector.clear()
        dataCollection.forEach { item ->
            when (item) {
                is CapTableArrayItem -> {
                    mutableListOf<Any?>().also {
                        it.addAll(item.value)
                    }.toTypedArray().let {
                        tableModel.addRow(it)
                    }
                }
                is CapTableMapItem -> {
                    mutableListOf<Any?>().also {
                        for (i in 0 until tableModel.columnCount) {
                            it.add(item.value[tableModel.getColumnName(i)])
                        }
                    }.toTypedArray().let {
                        tableModel.addRow(it)
                    }
                }
                is CapTableItem -> {
                    mutableListOf<Any?>().also {
                        it.add(item.value)
                    }.toTypedArray().let {
                        tableModel.addRow(it)
                    }
                }
            }
        }
        table?.adjustColumnsBySize()
        if (dataCollection.isEmpty()) {
            parent.refreshRowHeader()
        }
    }

    enum class PageAction {
        FIRST, PREVIOUS, NEXT, LAST, PAGESIZE, REFRESH
    }
}

interface CapPageStatusListener {
    fun firstPageEnabled(): Boolean
    fun previousPageEnabled(): Boolean
    fun nextPageEnabled(): Boolean
    fun lastPageEnabled(): Boolean
}

abstract class CapPageStatusAdapter : CapPageStatusListener {
    override fun firstPageEnabled(): Boolean = true
    override fun previousPageEnabled(): Boolean = true
    override fun nextPageEnabled(): Boolean = true
    override fun lastPageEnabled(): Boolean = true

    companion object {
        val instance: CapPageStatusAdapter = object : CapPageStatusAdapter() {}
    }
}

interface CapPageActionListener {
    fun firstPage(pageParameter: CapPageParameter): Collection<out CapTableItem>
    fun previousPage(pageParameter: CapPageParameter): Collection<out CapTableItem>
    fun nextPage(pageParameter: CapPageParameter): Collection<out CapTableItem>
    fun lastPage(pageParameter: CapPageParameter): Collection<out CapTableItem>
}

abstract class CapPageActionAdapter : CapPageActionListener {
    override fun firstPage(pageParameter: CapPageParameter): Collection<out CapTableItem> = emptyList()
    override fun previousPage(pageParameter: CapPageParameter): Collection<out CapTableItem> = emptyList()
    override fun nextPage(pageParameter: CapPageParameter): Collection<out CapTableItem> = emptyList()
    override fun lastPage(pageParameter: CapPageParameter): Collection<out CapTableItem> = emptyList()

    companion object {
        val instance: CapPageActionAdapter = object : CapPageActionAdapter() {}
    }
}

interface CapPageSizeChangeListener {
    fun pageSizeChanged(pageParameter: CapPageParameter): Collection<out CapTableItem>
}

abstract class CapPageSizeChangeAdapter : CapPageSizeChangeListener {
    override fun pageSizeChanged(pageParameter: CapPageParameter): Collection<out CapTableItem> = emptyList()

    companion object {
        val instance: CapPageSizeChangeAdapter = object : CapPageSizeChangeAdapter() {}
    }
}