package com.cap.plugins.elasticsearch.language.sql

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class ElasticsearchSqlFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, ElasticsearchSqlLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return ElasticsearchSqlFileType.INSTANCE
    }
}