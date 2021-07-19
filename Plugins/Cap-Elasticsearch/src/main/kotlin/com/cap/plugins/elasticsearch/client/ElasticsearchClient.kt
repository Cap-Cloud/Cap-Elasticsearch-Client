package com.cap.plugins.elasticsearch.client

import com.cap.plugins.common.http.SSLUtil
import com.cap.plugins.common.http.method.HttpGet
import com.cap.plugins.common.http.model.Method
import com.cap.plugins.common.http.model.Request
import com.cap.plugins.common.http.model.Response
import com.cap.plugins.common.http.response.FixedLengthTableResponseHandler
import com.cap.plugins.common.http.response.JsonResponseHandler
import com.cap.plugins.common.http.response.ResponseUtil
import com.cap.plugins.common.task.CapTaskExecution
import com.cap.plugins.common.util.ObjectMapper
import com.cap.plugins.elasticsearch.config.ElasticsearchConfiguration
import com.cap.plugins.elasticsearch.constant.ElasticsearchConstant
import com.cap.plugins.elasticsearch.model.ElasticsearchResponse
import com.cap.plugins.elasticsearch.model.alias.Alias
import com.cap.plugins.elasticsearch.model.cluster.ClusterHealth
import com.cap.plugins.elasticsearch.model.cluster.ClusterInfo
import com.cap.plugins.elasticsearch.model.index.Index
import com.cap.plugins.elasticsearch.model.index.IndexInfo
import com.cap.plugins.elasticsearch.model.node.Node
import com.cap.plugins.elasticsearch.model.plugin.Plugin
import com.cap.plugins.elasticsearch.ui.editor.model.TableMode
import com.cap.plugins.elasticsearch.ui.editor.model.TextMode
import org.apache.http.Header
import org.apache.http.HttpHeaders
import org.apache.http.HttpHost
import org.apache.http.client.ResponseHandler
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.*
import org.apache.http.client.utils.URIBuilder
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.message.BasicHeader
import org.apache.http.ssl.SSLContextBuilder
import java.io.Closeable

class ElasticsearchClient(val clusterProperties: ElasticsearchConfiguration) : Closeable {
    val httpHost: HttpHost = clusterProperties[ElasticsearchConstant.CLUSTER_PROPS_URL].let {
        HttpHost.create(it)
    }

    val httpClient: CloseableHttpClient by lazy {
        val headers = if (clusterProperties.credentials == null) {
            emptyList<Header>()
        } else {
            listOf(BasicHeader(HttpHeaders.AUTHORIZATION, clusterProperties.credentials!!.toBasicAuthHeader()))
        }

        val config = RequestConfig.custom()
            .setConnectTimeout(5000)
            .setConnectionRequestTimeout(5000)
            .setSocketTimeout(5000)
            .build()

        val sslContext = SSLUtil.createSSLContext(
            clusterProperties.sslConfig?.trustStorePath,
            clusterProperties.sslConfig?.keyStorePath,
            clusterProperties.sslConfig?.trustStorePassword,
            clusterProperties.sslConfig?.keyStorePassword,
            clusterProperties.sslConfig?.selfSigned
        ) ?: SSLContextBuilder().loadTrustMaterial(null) { _, _ -> true }.build()

        HttpClientBuilder.create()
            .setDefaultHeaders(headers)
            .setSSLContext(sslContext)
            .setSSLHostnameVerifier(NoopHostnameVerifier())
            .setDefaultRequestConfig(config)
            .build()
    }

    val config = mutableMapOf<String, Any>()
        get() {
            try {
                if (!field.containsKey(ELASTICSEARCH_VERSION) || field[ELASTICSEARCH_VERSION] == null) {
                    field[ELASTICSEARCH_VERSION] = clusterInfo().version.number
                }
                if (!field.containsKey(ELASTICSEARCH_SQL_PLUGIN_VERSION) || field[ELASTICSEARCH_SQL_PLUGIN_VERSION] == null) {
                    field[ELASTICSEARCH_SQL_PLUGIN_VERSION] =
                        catPlugins()?.firstOrNull { it.component.equals("sql", true) }?.version ?: ""
                }
            } catch (e: Exception) {
            }
            return field
        }

    /**
     * 集群信息
     */
    fun clusterInfo(): ClusterInfo {
        val url = "$httpHost"
        val request = HttpGet(url)
        return CapTaskExecution({
            httpClient.execute(request, JsonResponseHandler(Map::class.java))
        }).map {
            val version = it["version"] as Map<*, *>
            ClusterInfo(
                nodeName = it["name"].toString(),
                clusterName = it["cluster_name"].toString(),
                clusterUuid = it["cluster_uuid"].toString(),
                version = ClusterInfo.Version(
                    number = version["number"].toString(),
                    buildFlavor = version["build_flavor"].toString(),
                    buildType = version["build_type"].toString(),
                    buildHash = version["build_hash"].toString(),
                    buildDate = version["build_date"].toString(),
                    isSnapshot = version["build_snapshot"].toString().equals("true", true),
                    luceneVersion = version["lucene_version"].toString(),
                    minimumWireCompatibilityVersion = version["minimum_wire_compatibility_version"].toString(),
                    minimumIndexCompatibilityVersion = version["minimum_index_compatibility_version"].toString()
                ),
                tagline = it["tagline"].toString()
            )
        }.execute()
    }

    /**
     * 集群健康状态
     */
    fun clusterHealth(): ClusterHealth? {
        val url = "$httpHost/_cluster/health"
        val request = HttpGet(url)
        return CapTaskExecution({
            httpClient.execute(request, JsonResponseHandler(Map::class.java))
        }).map {
            ClusterHealth(
                cluster_name = it["cluster_name"].toString(),
                status = it["status"].toString(),
                timed_out = it["timed_out"].toString() == "true",
                number_of_nodes = it["number_of_nodes"].toString().toInt(),
                number_of_data_nodes = it["number_of_data_nodes"].toString().toInt(),
                active_primary_shards = it["active_primary_shards"].toString().toInt(),
                active_shards = it["active_shards"].toString().toInt(),
                relocating_shards = it["relocating_shards"].toString().toInt(),
                initializing_shards = it["initializing_shards"].toString().toInt(),
                unassigned_shards = it["unassigned_shards"].toString().toInt(),
                delayed_unassigned_shards = it["delayed_unassigned_shards"].toString().toInt(),
                number_of_pending_tasks = it["number_of_pending_tasks"].toString().toInt(),
                number_of_in_flight_fetch = it["number_of_in_flight_fetch"].toString().toInt(),
                task_max_waiting_in_queue_millis = it["task_max_waiting_in_queue_millis"].toString().toLong(),
                active_shards_percent_as_number = it["active_shards_percent_as_number"].toString().toDouble()
            )
        }.execute()
    }

    /**
     * 节点列表
     */
    fun catNodes(): List<Node> {
        val url = "$httpHost/_cat/nodes?v"
        val request = HttpGet(url)
        return CapTaskExecution({
            httpClient.execute(request, FixedLengthTableResponseHandler())
        }).map { table ->
            val header = table[0].asSequence()
                .mapIndexed { i, value -> Pair(value, i) }
                .associate { it }
            table.asSequence()
                .drop(1)
                .map {
                    Node(
                        name = it[header.getValue("name")],
                        ip = it[header.getValue("ip")],
                        master = it[header.getValue("master")],
                        role = it[header.getValue("node.role")],
                        load_1m = it[header.getValue("load_1m")],
                        load_5m = it[header.getValue("load_5m")],
                        load_15m = it[header.getValue("load_15m")],
                        cpu = it[header.getValue("cpu")],
                        ram = it[header.getValue("ram.percent")],
                        heap = it[header.getValue("heap.percent")]
                    )
                }.toList()
        }.executeQuietly() ?: emptyList()
    }

    /**
     * 插件列表
     */
    fun catPlugins(): List<Plugin> {
        val url = "$httpHost/_cat/plugins?v"
        val request = HttpGet(url)
        return CapTaskExecution({
            httpClient.execute(request, FixedLengthTableResponseHandler())
        }).map { table ->
            val header = table[0].asSequence()
                .mapIndexed { i, value -> Pair(value, i) }
                .associate { it }
            table.asSequence()
                .drop(1)
                .map {
                    Plugin(
                        name = it[header.getValue("name")],
                        component = it[header.getValue("component")],
                        version = it[header.getValue("version")]
                    )
                }.toList()
        }.executeQuietly() ?: emptyList()
    }

    /**
     * 索引列表
     */
    fun catIndices(index: String?): List<Index> {
        val url = "$httpHost/_cat/indices/${index ?: ""}?v"
        val request = HttpGet(url)
        return CapTaskExecution({
            httpClient.execute(request, FixedLengthTableResponseHandler())
        }).map { table ->
            val header = table[0].asSequence()
                .mapIndexed { i, value -> Pair(value, i) }
                .associate { it }
            table.asSequence()
                .drop(1)
                .map {
                    Index(
                        name = it[header.getValue("index")],
                        health = it[header.getValue("health")],
                        status = it[header.getValue("status")],
                        index = it[header.getValue("index")],
                        uuid = it[header.getValue("uuid")],
                        primaries = it[header.getValue("pri")],
                        replicas = it[header.getValue("rep")],
                        documents = it[header.getValue("docs.count")],
                        deletedDocuments = it[header.getValue("docs.deleted")],
                        storeSize = it[header.getValue("store.size")],
                        primaryStoreSize = it[header.getValue("pri.store.size")]
                    )
                }.toList()
        }.executeQuietly() ?: emptyList()
    }

    /**
     * 索引信息
     */
    fun indexInfo(index: String): IndexInfo? {
        val url = "$httpHost/${index}"
        val request = HttpGet(url)
        return CapTaskExecution({
            httpClient.execute(request) { response ->
                val content = ResponseUtil.readContent(response)
                Response(content, response.statusLine)
            }
        }).map {
            if (it.status.statusCode !in 200..299) {
                null
            } else {
                val indexInfo = IndexInfo(index)

                val content = ObjectMapper.jsonMapper.readTree(it.content).get(index)
                indexInfo.aliases = IndexInfo.Aliases(content.get("aliases"))
                indexInfo.mappings = IndexInfo.Mappings(content.get("mappings"))
                indexInfo.settings = IndexInfo.Settings(content.get("settings"))

                indexInfo
            }
        }.execute()
    }

    /**
     * 别名列表
     */
    fun catAliases(alias: String?): List<Alias> {
        val url = "$httpHost/_cat/aliases/${alias ?: ""}?v"
        val request = HttpGet(url)
        return CapTaskExecution({
            httpClient.execute(request, FixedLengthTableResponseHandler())
        }).map { table ->
            val header = table[0].asSequence()
                .mapIndexed { i, value -> Pair(value, i) }
                .associate { it }
            table.asSequence()
                .drop(1)
                .map {
                    Alias(
                        alias = it[header.getValue("alias")],
                        index = it[header.getValue("index")],
                        filter = it[header.getValue("filter")],
                        routing_index = it[header.getValue("routing.index")],
                        routing_search = it[header.getValue("routing.search")],
                        is_write_index = it[header.getValue("is_write_index")],
                    )
                }.toList()
        }.executeQuietly() ?: emptyList()
    }

    private fun <T> prepareExecution(
        request: HttpUriRequest,
        responseHandler: ResponseHandler<T>
    ): CapTaskExecution<T> {
        return CapTaskExecution({ httpClient.execute(request, responseHandler) })
    }

    fun <T> prepareExecution(request: Request, responseHandler: ResponseHandler<T>): CapTaskExecution<T> {
        val httpRequest = when (request.method) {
            Method.GET -> {
                val get = HttpGet(request.getUrl())
                get.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.mimeType)
                get.entity = StringEntity(request.body, ContentType.APPLICATION_JSON)
                get
            }
            Method.POST -> {
                val post = HttpPost(request.getUrl())
                post.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.mimeType)
                post.entity = StringEntity(request.body, ContentType.APPLICATION_JSON)
                post
            }
            Method.PUT -> {
                val put = HttpPut(request.getUrl())
                put.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.mimeType)
                put.entity = StringEntity(request.body, ContentType.APPLICATION_JSON)
                put
            }
            Method.HEAD -> HttpHead(request.getUrl())
            Method.DELETE -> HttpDelete(request.getUrl())
        }
        return prepareExecution(httpRequest, responseHandler)
    }

    fun prepareExecutionSpaceTable(request: Request): CapTaskExecution<List<List<String>>> {
        return prepareExecution(request, FixedLengthTableResponseHandler())
    }

    fun prepareExecutionResponse(request: Request, check: Boolean): CapTaskExecution<Response> {
        return prepareExecution(request,
            ResponseHandler<Response> { response ->
                if (check) {
                    ResponseUtil.checkResponse(response)
                }
                val content = ResponseUtil.readContent(response)
                Response(content, response.statusLine)
            })
    }

    fun prepareExecutionRestResponse(request: Request, check: Boolean): CapTaskExecution<ElasticsearchResponse> {
        var tableMode = TableMode.NONE_TABLE
        var textMode = TextMode.JSON

        val requestUrl = request.getUrl()
        val builder = URIBuilder(requestUrl)
        var path = builder.path
        while (path.startsWith("/")) {
            path = path.substring(1)
        }
        path = "/${path}"
        // 属性值表格，JSON深度最好是在2以内
        var propertyTableRequest = listOf(
            "/_cluster/health"
        )
        when {
            // 根，特殊处理
            path == "/" -> {
                tableMode = TableMode.JSON_PROPERTY_TABLE
                textMode = TextMode.JSON
            }
            // 属性值表格
            propertyTableRequest.map {
                path.startsWith(it)
            }.fold(false) { a, b -> a || b } -> {
                tableMode = TableMode.JSON_PROPERTY_TABLE
                textMode = TextMode.JSON
            }
            // _cat 尽可能转换成表格
            path.startsWith("/_cat") -> {
                tableMode = TableMode.CAT_TEXT_TABLE
                textMode = TextMode.TEXT
                builder.queryParams.filter { it.name == "format" }.forEach { format ->
                    when (format.value) {
                        "json" -> {
                            tableMode = TableMode.CAT_JSON_TABLE
                            textMode = TextMode.JSON
                        }
                        "yaml" -> {
                            tableMode = TableMode.CAT_YAML_TABLE
                            textMode = TextMode.YAML
                        }
                        else -> {
                            tableMode = TableMode.CAT_TEXT_TABLE
                            textMode = TextMode.TEXT
                        }
                    }
                }

                // if (path != "/_cat" && !requestUrl.contains("v=true")) {
                if (path != "/_cat" && builder.queryParams.none { it.name == "v" }) {
                    if (builder.queryParams.size == 0) {
                        request.path = "${request.path}?v=true"
                    } else {
                        request.path = "${request.path}&v=true"
                    }
                }
            }
            path.contains("/_search") -> {
                tableMode = TableMode.HIT_MAPPING_TABLE
            }
        }

        return prepareExecution(request,
            ResponseHandler<ElasticsearchResponse> { response ->
                if (check) {
                    ResponseUtil.checkResponse(response)
                }
                val content = ResponseUtil.readContent(response)
                ElasticsearchResponse(
                    content,
                    response.statusLine,
                    tableMode,
                    textMode
                )
            })
    }

    override fun close() {
        try {
            httpClient?.close()
        } catch (ignore: Exception) {
        }
    }

    companion object {
        const val ELASTICSEARCH_VERSION = "elasticsearch.version"
        const val ELASTICSEARCH_SQL_PLUGIN_VERSION = "elasticsearch.sql.plugin.version"
    }
}