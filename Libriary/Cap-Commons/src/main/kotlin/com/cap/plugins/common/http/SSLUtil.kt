package com.cap.plugins.common.http

import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.ssl.SSLContexts
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.KeyStore
import javax.net.ssl.SSLContext

class SSLUtil {
    companion object {
        fun createSSLContext(
            trustStorePath: String?,
            keyStorePath: String?,
            trustStorePass: String?,
            keyStorePass: String?,
            selfSigned: Boolean?
        ): SSLContext? {
            val trustStore: KeyStore? = loadKeyStore(trustStorePath, trustStorePass)
            val keyStore: KeyStore? = loadKeyStore(keyStorePath, keyStorePass)
            if (trustStore == null && keyStore == null) {
                return null
            }
            val builder = SSLContexts.custom()
            if (trustStore != null) {
                val trustStrategy = if (selfSigned == true) TrustSelfSignedStrategy.INSTANCE else null
                builder.loadTrustMaterial(trustStore, trustStrategy)
            }
            if (keyStore != null) {
                builder.loadKeyMaterial(keyStore, keyStorePass?.toCharArray())
            }
            return builder.build()
        }

        private fun loadKeyStore(keyStorePath: String?, keyStorePass: String?): KeyStore? {
            val keyStorePath: Path = if (keyStorePath.isNullOrBlank()) {
                return null
            } else {
                Paths.get(keyStorePath)
            }
            val keyStore: KeyStore = if (keyStorePath.endsWith(".jks")) {
                KeyStore.getInstance("jks")
            } else {
                KeyStore.getInstance("pkcs12")
            }
            Files.newInputStream(keyStorePath)
                .use { inputStream -> keyStore.load(inputStream, keyStorePass?.toCharArray()) }
            return keyStore
        }
    }
}