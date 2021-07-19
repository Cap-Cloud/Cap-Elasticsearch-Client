package com.cap.plugins.common.icon

import com.intellij.ui.IconManager
import javax.swing.Icon

object Icons {
    private fun load(path: String): Icon {
        return IconManager.getInstance().getIcon(path, Icons::class.java)
    }

    object Actions {
        val CLUSTER_CONNECT_ICON = load("/icons/cap/action/clusterConnect.png")
        val CLUSTER_DISCONNECT_ICON = load("/icons/cap/action/clusterDisconnect.png")
        val CLUSTER_RECONNECT_ICON = load("/icons/cap/action/clusterReconnect.png")

        val ADD_CLUSTER_ICON = load("/icons/cap/action/addCluster.png")
        val ADD_ICON = load("/icons/cap/action/add.png")

        val BACK_ICON = load("/icons/cap/action/back.png")

        val CANCEL_ICON = load("/icons/cap/action/cancel.png")
        val CLOSE_ICON = load("/icons/cap/action/close.png")
        val COMMIT_ICON = load("/icons/cap/action/commit.png")
        val COPY_ICON = load("/icons/cap/action/copy.png")

        val EDIT_SOURCE_ICON = load("/icons/cap/action/editSource.png")
        val EXECUTE_ICON = load("/icons/cap/action/execute.png")

        val FILTER_ICON = load("/icons/cap/action/filter.png")
        val FIND_ICON = load("/icons/cap/action/find.png")

        val GC_ICON = load("/icons/cap/action/gc.png")

        val SHOW_AS_TREE_ICON = load("/icons/cap/action/showAsTree.png")

        val PROFILE_ICON = load("/icons/cap/action/profile.png")

        val REFRESH_ICON = load("/icons/cap/action/refresh.png")
        val REMOVE_ICON = load("/icons/cap/action/remove.png")
        val RESET_ICON = load("/icons/cap/action/reset.png")

        val UPLOAD_ICON = load("/icons/cap/action/upload.png")
    }

    object FileType {
        val ANY_TYPE_ICON = load("/icons/cap/file/any_type.png")
        val AS_ICON = load("/icons/cap/file/as.png")
        val HTML_ICON = load("/icons/cap/file/html.png")
        val JSON_ICON = load("/icons/cap/file/json.png")
        val TEXT_ICON = load("/icons/cap/file/text.png")
    }

    object General {
        val ACTUAL_ZOOM_ICON = load("/icons/cap/general/actualZoom.png")
        val ADD_ICON = load("/icons/cap/general/add.png")
        val ALL_LEFT_ICON = load("/icons/cap/general/allLeft.png")
        val ALL_RIGHT_ICON = load("/icons/cap/general/allRight.png")

        val CALENDAR_ICON = load("/icons/cap/general/calendar.svg")

        val DATA_COLUMN_ICON = load("/icons/cap/general/dataColumn.png")
        val DATA_TABLE_ICON = load("/icons/cap/general/DataTables.png")

        val ERROR_ICON = load("/icons/cap/general/error.svg")

        val GRID_ICON = load("/icons/cap/general/grid.png")

        val FAVORITE_ICON = load("/icons/cap/general/favorite.png")

        val HORIZONTAL_VIEW_ICON = load("/icons/cap/general/horizontalView.svg")

        val LEAF_ICON = load("/icons/cap/general/leaf.png")

        val PLUGIN_ICON = load("/icons/cap/general/Plugins.png")

        val QUERY_EDITOR_ICON = load("/icons/cap/general/query_editor.svg")

        val SERVER_ICON = load("/icons/cap/general/server.png")
        val SUCCESS_ICON = load("/icons/cap/general/success.svg")

        val VERTICAL_VIEW_ICON = load("/icons/cap/general/verticalView.svg")

        val WEB_FOLDER_ICON = load("/icons/cap/general/webfolder.png")
        val WEB_LISTENER_ICON = load("/icons/cap/general/weblistener.png")
    }

    object MQ {

        val BROKER_ICON = load("/icons/cap/mq/broker.png")
        val CONSUMER_ICON = load("/icons/cap/mq/consumer.png")
        val TOPIC_ICON = load("/icons/cap/mq/topic.png")
    }
}