package com.cap.plugins.elasticsearch.ui.explorer.tree

import com.cap.plugins.common.component.RoundColorIcon
import com.cap.plugins.common.icon.Icons
import com.cap.plugins.common.ui.explorer.tree.CapClusterTreeFolderNode
import com.cap.plugins.elasticsearch.constant.ElasticsearchConstant
import com.cap.plugins.elasticsearch.icon.ElasticsearchIcons
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.SimpleTextAttributes
import java.awt.Color
import javax.swing.JTree

/**
 * 树节点渲染
 */
class CellRenderer : ColoredTreeCellRenderer() {
    override fun customizeCellRenderer(
        tree: JTree, node: Any?, selected: Boolean, expanded: Boolean,
        leaf: Boolean, row: Int, hasFocus: Boolean
    ) {
        icon = when {
            node == null -> Icons.General.LEAF_ICON

            node is CapClusterTreeFolderNode && node.userObject == ElasticsearchConstant.NODES -> Icons.General.SERVER_ICON
            node is CapClusterTreeFolderNode && node.userObject == ElasticsearchConstant.PLUGINS -> Icons.General.PLUGIN_ICON
            node is CapClusterTreeFolderNode && node.userObject == ElasticsearchConstant.ALIASES -> Icons.FileType.AS_ICON
            node is CapClusterTreeFolderNode -> Icons.General.WEB_FOLDER_ICON

            node is ElasticsearchClusterRootTreeNode -> ElasticsearchIcons.ELASTICSEARCH_ICON
            node is ElasticsearchNodeTreeNode -> Icons.General.SERVER_ICON
            node is ElasticsearchPluginTreeNode -> Icons.General.PLUGIN_ICON
            node is ElasticsearchAliasTreeNode -> Icons.FileType.AS_ICON

            node is ElasticsearchIndexTreeNode && node.index.health.equals("green", true) -> RoundColorIcon(
                size = 16,
                drawColor = Color.BLACK,
                fillColor = Color.GREEN
            )
            node is ElasticsearchIndexTreeNode && node.index.health.equals("yellow", true) -> RoundColorIcon(
                size = 16,
                drawColor = Color.BLACK,
                fillColor = Color.YELLOW
            )
            node is ElasticsearchIndexTreeNode && node.index.health.equals("red", true) -> RoundColorIcon(
                size = 16,
                drawColor = Color.BLACK,
                fillColor = Color.RED
            )

            else -> Icons.General.LEAF_ICON
        }
//        foreground = when {
//            node is ElasticsearchClusterRootTreeNode && (node.health?.status?.equals("green", true)
//                ?: false) -> Color.GREEN
//            node is ElasticsearchClusterRootTreeNode && (node.health?.status?.equals("yellow", true)
//                ?: false) -> Color.YELLOW
//            node is ElasticsearchClusterRootTreeNode && (node.health?.status?.equals("red", true)
//                ?: false) -> Color.RED
//
//            node is ElasticsearchIndexTreeNode && node.index.health.equals("green", true) -> Color.GREEN
//            node is ElasticsearchIndexTreeNode && node.index.health.equals("yellow", true) -> Color.YELLOW
//            node is ElasticsearchIndexTreeNode && node.index.health.equals("red", true) -> Color.RED
//
//            else -> null
//        }
        when {
            node is ElasticsearchClusterRootTreeNode && (node.health?.status?.equals("green", true)
                ?: false) -> append(
                "(green)",
                SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, Color.GREEN),
                true
            )
            node is ElasticsearchClusterRootTreeNode && (node.health?.status?.equals("yellow", true)
                ?: false) -> append(
                "(yellow)",
                SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, Color.YELLOW),
                true
            )
            node is ElasticsearchClusterRootTreeNode && (node.health?.status?.equals("red", true)
                ?: false) -> append(
                "(red)",
                SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, Color.RED),
                true
            )
        }
        append(node.toString(), SimpleTextAttributes.REGULAR_ATTRIBUTES)
    }
}