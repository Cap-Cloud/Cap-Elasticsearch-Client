package com.cap.plugins.common.http.response

import com.cap.plugins.common.util.ObjectMapper

class JsonResponseHandler<T>(private val clazz: Class<T>) : ConvertingResponseHandler<T>({
    ObjectMapper.jsonMapper.readValue(it, clazz)
})