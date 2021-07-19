package com.cap.plugins.common.ui.explorer

import com.cap.plugins.common.action.CapAction
import com.cap.plugins.common.constant.CapCommonI18n
import com.cap.plugins.common.i18n.I18nResources
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.common.ui.explorer.tree.CapClusterTreeRootNode
import com.cap.plugins.common.ui.explorer.tree.CapExplorerTreeNode
import com.cap.plugins.common.util.background
import com.cap.plugins.common.util.foreground
import com.cap.plugins.common.util.showErrorDialog
import com.intellij.ide.TreeExpander
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.progress.BackgroundTaskQueue
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.AnimatedIcon
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.SearchTextField
import com.intellij.ui.TreeSpeedSearch
import com.intellij.ui.treeStructure.Tree
import com.intellij.util.ui.tree.TreeUtil
import java.util.*
import javax.swing.BoxLayout
import javax.swing.JComponent
import javax.swing.ToolTipManager
import javax.swing.event.DocumentEvent
import javax.swing.event.TreeExpansionEvent
import javax.swing.event.TreeExpansionListener
import javax.swing.table.DefaultTableModel
import javax.swing.tree.*

abstract class CapClusterToolWindowExplorer(val project: Project, userObject: Any) : SimpleToolWindowPanel(true, true),
    Disposable {

    val taskQueue = BackgroundTaskQueue(project, "Cap intellij plugins task queue")
    val longTaskQueue = BackgroundTaskQueue(project, "Cap intellij plugins long task queue")

    val treeRoot by lazy { DefaultMutableTreeNode(userObject) }
    val treeModel by lazy { DefaultTreeModel(treeRoot) }
    val tree by lazy { Tree(treeModel) }

    val treeExpander: TreeExpander = object : TreeExpander {
        override fun expandAll() {
            TreeUtil.expandAll(tree)
        }

        override fun collapseAll() {
            TreeUtil.collapseAll(tree, 0)
        }

        override fun canExpand(): Boolean {
            return (treeModel.root as TreeNode).childCount > 0
        }

        override fun canCollapse(): Boolean {
            return (treeModel.root as TreeNode).childCount > 0
        }
    }

    val defaultTableModel by lazy { DefaultTableModel() }

    val removeClusterAction =
        CapAction(I18nResources.getMessageDefault("action.cluster.delete", CapCommonI18n.ACTION_CLUSTER_DELETE),
            I18nResources.getMessageDefault(
                "action.cluster.delete.description",
                CapCommonI18n.ACTION_CLUSTER_DELETE_DESCRIPTION
            ),
            Icons.Actions.REMOVE_ICON,
            {
                clusterTreeRootNodeSelected()
            }) {
            removeCluster(null)
        }

    val refreshAction = CapAction(
        I18nResources.getMessageDefault("action.cluster.refresh", CapCommonI18n.ACTION_CLUSTER_REFRESH),
        I18nResources.getMessageDefault(
            "action.cluster.refresh.description",
            CapCommonI18n.ACTION_CLUSTER_REFRESH_DESCRIPTION
        ),
        Icons.Actions.REFRESH_ICON,
        {
            clusterTreeRootNodeSelected()
        }
    ) {
        val node = tree.selectionPaths[0].path[1]
        if (node is CapExplorerTreeNode) {
            refreshNode(node)
        }
    }

    fun getSearchTextField(): SearchTextField {
        return SearchTextField().also {
            it.addDocumentListener(object : DocumentAdapter() {
                override fun textChanged(e: DocumentEvent) {
                    if (e != null) {
                        val pattern = e.document.getText(0, e.document.length).toLowerCase()
                        tree.selectionModel.selectionPaths = findNodes(treeRoot, pattern).map { leaf ->
                            generateSequence(leaf) { it.parent }.toList().reversed().toTypedArray()
                        }.map {
                            TreePath(it)
                        }.toTypedArray()
                        if (tree.selectionModel.selectionPaths.isNotEmpty()) {
                            tree.scrollPathToVisible(tree.selectionModel.selectionPaths[0])
                        }
                    }
                }
            })
        }
    }

    fun addSearchTextField(owner: JComponent) {
        getSearchTextField().let {
            owner.layout = BoxLayout(owner, BoxLayout.X_AXIS)
            owner.add(it)
        }
    }

    /**
     * 创建工具栏
     */
    abstract fun createToolbar()

    open fun removeCluster(task: (() -> Unit)? = null) {
        // 获取选中的第一个节点
        val clusterNode = tree.selectionPaths[0].lastPathComponent as CapClusterTreeRootNode
        if (Messages.OK == Messages.showOkCancelDialog(
                I18nResources.getMessageDefault(
                    "action.cluster.delete.confirm",
                    CapCommonI18n.ACTION_CLUSTER_DELETE_CONFIRM,
                    clusterNode.toString()
                ),
                I18nResources.getMessageDefault("cap.plugin.title", CapCommonI18n.CAP_PLUGIN_TITLE),
                I18nResources.getMessageDefault("button.text.ok", CapCommonI18n.BUTTON_TEXT_OK),
                I18nResources.getMessageDefault("button.text.cancel", CapCommonI18n.BUTTON_TEXT_CANCEL),
                Messages.getQuestionIcon()
            )
        ) {
            // 从树中移除
            treeRoot.remove(clusterNode)
            treeModel.reload(treeRoot)
            task?.invoke()
        }
    }

    /**
     * 创建树
     */
    open fun createTree() {
        tree.isRootVisible = false
        tree.showsRootHandles = true
        tree.selectionModel.selectionMode = TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION
        tree.putClientProperty(AnimatedIcon.ANIMATION_IN_RENDERER_ALLOWED, true)

        // 添加树展开监听器
        tree.addTreeExpansionListener(object : TreeExpansionListener {
            override fun treeExpanded(event: TreeExpansionEvent?) {
                val node = event!!.path.lastPathComponent
                if (node is CapExplorerTreeNode) {
                    background(
                        project,
                        I18nResources.getMessageDefault(
                            "background.progress.title.tree.node.expanding",
                            CapCommonI18n.BACKGROUND_PROGRESS_TITLE_TREE_NODE_EXPANDING,
                            node.toString()
                        ),
                        taskQueue
                    ) {
                        node.expand()
                        treeModel.reload(node)
                    }
                }
            }

            override fun treeCollapsed(event: TreeExpansionEvent?) {
            }
        })

        TreeUtil.installActions(tree)
        TreeSpeedSearch(tree)

        ToolTipManager.sharedInstance().registerComponent(tree)
    }

    fun findNodes(parent: TreeNode, text: String): Collection<TreeNode> {
        return (parent.children() as Enumeration<TreeNode>).toList()
            .mapNotNull {
                if (!text.isNullOrEmpty() && it.toString().toLowerCase().indexOf(text) >= 0) {
                    // 当前节点匹配，并且继续搜索子节点
                    listOf(it).plus(findNodes(it, text))
                } else {
                    // 当前节点不匹配，直接搜索子节点
                    findNodes(it, text)
                }
            }
            .flatten()
    }

    /**
     * 刷新节点
     */
    open fun refreshNode(node: CapExplorerTreeNode, task: (() -> Unit)? = null) {
        background(
            project,
            I18nResources.getMessageDefault(
                "background.progress.title.node.refresh",
                CapCommonI18n.BACKGROUND_PROGRESS_TITLE_NODE_REFRESH,
                "$node"
            ),
            taskQueue
        ) {
            try {
                node.refresh()
                foreground {
                    treeModel.reload(node)
                    task?.invoke()
                }
            } catch (e: Exception) {
                showErrorDialog("Unable to expand $node", e.cause)
            }
        }
    }

    fun hasClusterNode():Boolean{
        return treeRoot.childCount > 0
    }

    fun clusterTreeRootNodeSelected(): Boolean {
        if (tree.selectionPaths == null) {
            return false
        }
        return tree.selectionPaths.fold(true) { a, v ->
            val path = v.lastPathComponent
            a && path is CapClusterTreeRootNode
        }
    }

    companion object {
        fun getInstance(
            project: Project,
            serviceClass: Class<out CapClusterToolWindowExplorer>? = null
        ): CapClusterToolWindowExplorer {
            return ServiceManager.getService(project, serviceClass ?: CapClusterToolWindowExplorer::class.java)
        }
    }
}