package com.cap.plugins.common.ui.explorer.tree

abstract class CapClusterTreeRootNode(open val clusterProperties: MutableMap<String, String>) : CapClusterTreeNode(clusterProperties) {
    /**
     * 连接集群
     */
    abstract fun connect()

    /**
     * 判断集群是否已连接
     */
    abstract fun isconnect(): Boolean

    /**
     * 断开连接
     */
    abstract fun disconnect()

    /**
     * 重新连接
     */
    abstract fun reconnect()
}