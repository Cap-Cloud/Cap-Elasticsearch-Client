package com.cap.plugins.elasticsearch.language.kql

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class ElasticsearchKqlFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, ElasticsearchKqlLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return ElasticsearchKqlFileType.INSTANCE
    }
}