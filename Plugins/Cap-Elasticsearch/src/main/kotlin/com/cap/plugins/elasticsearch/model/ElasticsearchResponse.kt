package com.cap.plugins.elasticsearch.model

import com.cap.plugins.common.http.model.Response
import com.cap.plugins.elasticsearch.ui.editor.model.TableMode
import com.cap.plugins.elasticsearch.ui.editor.model.TextMode
import org.apache.http.StatusLine

class ElasticsearchResponse(
    content: String,
    status: StatusLine,
    var tableMode: TableMode = TableMode.NONE_TABLE,
    var textMode: TextMode = TextMode.TEXT
) : Response(content, status) {

}