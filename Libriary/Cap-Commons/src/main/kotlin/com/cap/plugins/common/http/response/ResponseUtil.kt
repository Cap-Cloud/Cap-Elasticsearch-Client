package com.cap.plugins.common.http.response

import com.intellij.openapi.util.text.StringUtil
import com.intellij.util.LineSeparator
import org.apache.commons.text.translate.UnicodeUnescaper
import org.apache.http.HttpResponse
import java.io.BufferedReader

class ResponseUtil {
    companion object {
        fun checkResponse(response: HttpResponse) {
            if (response.statusLine.statusCode !in 200..299) {
                val content =
                    readContent(response)
                throw ResponseException(
                    response.statusLine,
                    content
                )
            }
        }

        fun readContent(response: HttpResponse): String {
            return response.entity.content.use {
                it.bufferedReader().use(BufferedReader::readText)
            }
                .let { UnicodeUnescaper().translate(it) }
                .let { StringUtil.convertLineSeparators(it, LineSeparator.LF.separatorString) }
        }
    }
}