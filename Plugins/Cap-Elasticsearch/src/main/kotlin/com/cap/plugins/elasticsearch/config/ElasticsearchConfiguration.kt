package com.cap.plugins.elasticsearch.config

import com.cap.plugins.elasticsearch.constant.ElasticsearchConstant
import org.apache.http.HttpHost
import java.util.*

class ElasticsearchConfiguration : LinkedHashMap<String, String>() {
    val credentials: Credentials? by lazy {
        if (!get(ElasticsearchConstant.CLUSTER_PROPS_USER).isNullOrEmpty()) {
            Credentials(
                get(ElasticsearchConstant.CLUSTER_PROPS_USER) ?: "",
                get(ElasticsearchConstant.CLUSTER_PROPS_PASSWORD) ?: ""
            )
        } else {
            null
        }
    }

    val sslConfig: SSLConfig? by lazy {
        if (
            !get(ElasticsearchConstant.CLUSTER_PROPS_TRUST_STORE_PATH).isNullOrEmpty() ||
            !get(ElasticsearchConstant.CLUSTER_PROPS_KEY_STORE_PATH).isNullOrEmpty()
        ) {
            SSLConfig(
                get(ElasticsearchConstant.CLUSTER_PROPS_TRUST_STORE_PATH) ?: "",
                get(ElasticsearchConstant.CLUSTER_PROPS_TRUST_STORE_PASSWORD) ?: "",
                get(ElasticsearchConstant.CLUSTER_PROPS_KEY_STORE_PATH) ?: "",
                get(ElasticsearchConstant.CLUSTER_PROPS_KEY_STORE_PASSWORD) ?: "",
                (get(ElasticsearchConstant.CLUSTER_PROPS_SELF_SIGNED) ?: "").equals("true",true)
            )
        } else {
            null
        }
    }

    class Credentials(
        val user: String,
        val password: String
    ) {
        fun toBasicAuthHeader(): String {
            return "Basic " + Base64.getEncoder().encodeToString("$user:$password".toByteArray())
        }
    }

    class SSLConfig(
        val trustStorePath: String?,
        val trustStorePassword: String?,
        val keyStorePath: String?,
        val keyStorePassword: String?,
        val selfSigned: Boolean
    )
}