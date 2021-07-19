package com.cap.plugins.elasticsearch.language.sql.lexer

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.language.sql.ElasticsearchSqlFile
import com.cap.plugins.elasticsearch.language.sql.ElasticsearchSqlLanguage
import com.cap.plugins.elasticsearch.language.sql.grammar.ElasticsearchSqlParser
import com.cap.plugins.elasticsearch.language.sql.grammar.ElasticsearchSqlParserUtil
import com.cap.plugins.elasticsearch.language.sql.grammar.psi.ElasticsearchSqlTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.ParserDefinition
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.stubs.PsiFileStub
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.ILightStubFileElementType
import com.intellij.psi.tree.TokenSet

@Keep
class ElasticsearchSqlParserDefinition : ParserDefinition {

    override fun getWhitespaceTokens() = TokenSet.create(TokenType.WHITE_SPACE)
    override fun getCommentTokens() = TokenSet.create(ElasticsearchSqlTypes.COMMENT)
    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY
    override fun spaceExistenceTypeBetweenTokens(p0: ASTNode, p1: ASTNode) = ParserDefinition.SpaceRequirements.MAY
    override fun createElement(node: ASTNode): PsiElement = ElasticsearchSqlParserUtil.createElement(node)
    override fun createParser(project: Project) = ElasticsearchSqlParser()

    override fun createFile(viewProvider: FileViewProvider): ElasticsearchSqlFile = ElasticsearchSqlFile(viewProvider)

    override fun getFileNodeType(): IFileElementType {
        return ILightStubFileElementType<PsiFileStub<ElasticsearchSqlFile>>(
            ElasticsearchSqlLanguage.INSTANCE
        )
    }

    override fun createLexer(p0: Project?): Lexer = ElasticsearchSqlLexerAdapter()

    fun getLanguage(): Language = ElasticsearchSqlLanguage.INSTANCE
}