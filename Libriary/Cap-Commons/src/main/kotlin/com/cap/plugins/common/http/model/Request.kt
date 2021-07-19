package com.cap.plugins.common.http.model

data class Request(
    val host: String? = null,
    var path: String,
    val body: String = "",
    val method: Method
) {
    fun getUrl(): String {
        var h = host?.let {
            var h2 = it.trim()
            while (h2.endsWith("/")) {
                h2 = h2.substring(0, h2.length - 1)
            }
            h2
        } ?: ""
        var p = path.trim()
        while (p.startsWith("/")) {
            p = p.substring(1)
        }

        return "${h}/${p}"
    }
}