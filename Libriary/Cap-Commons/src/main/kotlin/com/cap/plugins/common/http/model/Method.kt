package com.cap.plugins.common.http.model

import com.cap.plugins.common.annotation.KeepAll

@KeepAll
enum class Method(
    val hasBody: Boolean
) {
    GET(true),
    POST(true),
    PUT(true),
    HEAD(false),
    DELETE(false)
}