package com.cap.plugins.elasticsearch.language.rest.lexer

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.language.rest.ElasticsearchRestLanguage
import com.intellij.json.JsonParser
import com.intellij.json.JsonParserDefinition
import com.intellij.json.psi.impl.JsonFileImpl
import com.intellij.lang.PsiParser
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType

@Keep
class ElasticsearchRestParserDefinition : JsonParserDefinition() {
    private val elementType = IFileElementType(ElasticsearchRestLanguage.INSTANCE)

    override fun createFile(fileViewProvider: FileViewProvider): PsiFile {
        return JsonFileImpl(fileViewProvider,
            ElasticsearchRestLanguage.INSTANCE
        )
    }

    override fun getFileNodeType(): IFileElementType {
        return elementType
    }

    override fun createParser(project: Project): PsiParser {
        return JsonParser()
    }
}