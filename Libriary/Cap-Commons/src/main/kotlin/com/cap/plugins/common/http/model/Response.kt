package com.cap.plugins.common.http.model

import org.apache.http.StatusLine

open class Response(
    val content: String,
    val status: StatusLine
)