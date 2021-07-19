package com.cap.plugins.elasticsearch.language.sql.highlighter

import com.cap.plugins.common.annotation.Keep
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

@Keep
class ElasticsearchSqlSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        return ElasticsearchSqlSyntaxHighlighter()
    }
}
