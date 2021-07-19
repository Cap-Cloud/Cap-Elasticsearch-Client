package com.cap.plugins.common.http.response

import org.apache.http.StatusLine

class ResponseException(
    status: StatusLine,
    content: String
) : RuntimeException("$status $content")