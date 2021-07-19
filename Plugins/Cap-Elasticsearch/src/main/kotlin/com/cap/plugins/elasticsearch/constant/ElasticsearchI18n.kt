package com.cap.plugins.elasticsearch.constant

object ElasticsearchI18n {

    /* for Elasticsearch */

    const val ACTION_INDEX_CREATE = "Create index"
    const val ACTION_INDEX_CREATE_TITLE = "Create index"
    const val ACTION_INDEX_CREATE_INDEX_NAME = "Index name"
    const val ACTION_INDEX_CREATE_INDEX_NAME_CANNOT_NULL = "Index name cannot be null"
    const val ACTION_INDEX_CREATE_INDEX_NAME_MUST_IDENTIFIER = "Index name must be a valid identifier"
    const val ACTION_INDEX_CREATE_INDEX_BODY = "Body"

    const val ACTION_INDEX_NEW_ALIAS_MESSAGE = "Alias name"
    const val ACTION_INDEX_CLONE_MESSAGE = "Target index name"
    const val ACTION_INDEX_FORCE_MERGE_SEGMENTS = "Maximum number of segments per shard"
    const val ACTION_INDEX_OPERATE_CONFIRM = "Are you sure to do that: %s?"

    const val PLUGIN_TITLE = "Cap Elasticsearch Client"

    const val CLUSTER_ADD_ELASTICSEARCH = "Elasticsearch cluster"

    const val CLUSTER = "Cluster"

    const val BACKGROUND_EXECUTE_REQUEST = "Execute request"

    const val REQUEST_METHOD = "Request Method"
    const val REQUEST_PATH = "Request Path"
    const val REQUEST_AUTO_COMMIT = "Auto Commit"
    const val REQUEST_BODY = "Request Body"
    const val REQUEST_DESCRIPTION = "Description"

    const val REQUEST_EXECUTE_TEXT = "Execute query"
    const val REQUEST_EXECUTE_DESCRIPTION = "Execute query"

    const val REQUEST_EXPLAIN_TEXT = "Explain query"
    const val REQUEST_EXPLAIN_DESCRIPTION = "Explain query"

    const val RESULT_TAB_TEXT = "Text"
    const val RESULT_TAB_JSON = "Json"
    const val RESULT_TAB_TREE = "Tree"
    const val RESULT_TAB_TABLE = "Table"

    const val NODE_NAME = "name"
    const val NODE_IP = "ip"
    const val NODE_MASTER = "master"
    const val NODE_ROLE = "node.role"
    const val NODE_LOAD_1M = "load_1m"
    const val NODE_LOAD_5M = "load_5m"
    const val NODE_LOAD_15M = "load_15m"
    const val NODE_CPU = "cpu"
    const val NODE_RAM_PERCENT = "ram"
    const val NODE_HEAP_PERCENT = "heap"

    const val PLUGIN_NAME = "name"
    const val PLUGIN_COMPONENT = "component"
    const val PLUGIN_VERSION = "version"

    const val INDEX_HEALTH = "health"
    const val INDEX_STATUS = "status"
    const val INDEX_INDEX = "index"
    const val INDEX_UUID = "uuid"
    const val INDEX_PRI = "pri"
    const val INDEX_REP = "rep"
    const val INDEX_DOCS_COUNT = "docs.count"
    const val INDEX_DOCS_DELETED = "docs.deleted"
    const val INDEX_STORE_SIZE = "store.size"
    const val INDEX_PRI_STORE_SIZE = "pri.store.size"

    const val ALIAS_ALIAS = "alias"
    const val ALIAS_INDEX = "index"
    const val ALIAS_FILTER = "filter"
    const val ALIAS_ROUTING_INDEX = "routing.index"
    const val ALIAS_ROUTING_SEARCH = "routing.search"
    const val ALIAS_IS_WRITE_INDEX = "is_write_index"

    /* for Kibana */
    const val KIBANA_DEV_TOOLS = "Dev Tools"
    const val KIBANA_DEV_TOOLS_CONSOLE = "Console"


    /* for X-Pack */
    const val SQL_OPEN_EDITOR = "Open query editor"
    const val SQL_FETCH_SIZE = "Fetch size"
    const val SQL_SUBMIT_SELECTED_ONLY = "Submit selected only"

    /* for plugins */
    const val PLUGIN_SQL_SITE = "Elasticsearch sql site"
}