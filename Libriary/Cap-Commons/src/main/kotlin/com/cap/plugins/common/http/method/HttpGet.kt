package com.cap.plugins.common.http.method

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase
import java.net.URI

class HttpGet : HttpEntityEnclosingRequestBase {

    constructor(uri: URI) : super() {
        setURI(uri)
    }

    constructor(uri: String) : this(URI.create(uri))

    override fun getMethod(): String? {
        return "GET"
    }

}