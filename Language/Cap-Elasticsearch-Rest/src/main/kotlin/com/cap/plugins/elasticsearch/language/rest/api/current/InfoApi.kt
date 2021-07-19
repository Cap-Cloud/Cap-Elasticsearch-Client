package com.cap.plugins.elasticsearch.language.rest.api.current

import com.cap.plugins.common.http.model.Method
import com.cap.plugins.elasticsearch.language.rest.api.RequestApi
import com.cap.plugins.elasticsearch.language.rest.api.RequestApiTarget

class InfoApi {
    companion object {
        val API_LIST = mutableListOf(
            // Info API
            RequestApi(
                documentation = "info-api.html",
                methods = mutableListOf(Method.GET),
                patterns = mutableListOf(
                    "/_xpack"
                ),
                targets = mutableListOf(RequestApiTarget.NOT_NEEDED)
            ),
        )
    }
}