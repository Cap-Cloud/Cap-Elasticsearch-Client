package com.cap.plugins.elasticsearch.language.kql.lexer

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlFile
import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlLanguage
import com.cap.plugins.elasticsearch.language.kql.grammar.ElasticsearchKqlParser
import com.cap.plugins.elasticsearch.language.kql.grammar.ElasticsearchKqlParserUtil
import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlTypes
import com.intellij.lang.ASTNode
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
class ElasticsearchKqlParserDefinition : ParserDefinition {

    override fun getWhitespaceTokens() = TokenSet.create(TokenType.WHITE_SPACE)
    override fun getCommentTokens() = TokenSet.create(ElasticsearchKqlTypes.COMMENT)
    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY
    override fun spaceExistenceTypeBetweenTokens(p0: ASTNode, p1: ASTNode) = ParserDefinition.SpaceRequirements.MAY
    override fun createElement(node: ASTNode): PsiElement = ElasticsearchKqlParserUtil.createElement(node)
    override fun createParser(project: Project) = ElasticsearchKqlParser()

    override fun createFile(viewProvider: FileViewProvider): ElasticsearchKqlFile = ElasticsearchKqlFile(viewProvider)

    override fun getFileNodeType(): IFileElementType {
        return ILightStubFileElementType<PsiFileStub<ElasticsearchKqlFile>>(
            ElasticsearchKqlLanguage.INSTANCE
        )
    }

    override fun createLexer(p0: Project?): Lexer = ElasticsearchKqlLexerAdapter()
}