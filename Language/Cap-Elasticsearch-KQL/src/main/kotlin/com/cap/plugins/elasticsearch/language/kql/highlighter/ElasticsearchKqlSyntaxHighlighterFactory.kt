package com.cap.plugins.elasticsearch.language.kql.highlighter

import com.cap.plugins.common.annotation.Keep
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

@Keep
class ElasticsearchKqlSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        return ElasticsearchKqlSyntaxHighlighter()
    }
}
