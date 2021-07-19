package com.cap.plugins.common.http.response

import org.apache.http.HttpResponse
import org.apache.http.client.ResponseHandler

open class ConvertingResponseHandler<T>(private val converter: (String) -> T) :
    ResponseHandler<T> {

    override fun handleResponse(response: HttpResponse): T {
        ResponseUtil.checkResponse(response)
        val content = ResponseUtil.readContent(response)
        return converter.invoke(content)
    }
}