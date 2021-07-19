package com.cap.plugins.elasticsearch.language.sql.commenter

import com.cap.plugins.elasticsearch.language.sql.grammar.psi.ElasticsearchSqlTypes
import com.cap.plugins.common.annotation.Keep
import com.intellij.lang.CodeDocumentationAwareCommenter
import com.intellij.psi.PsiComment
import com.intellij.psi.tree.IElementType

@Keep
class ElasticsearchSqlCommenter : CodeDocumentationAwareCommenter {

    override fun getLineCommentTokenType(): IElementType = ElasticsearchSqlTypes.COMMENT
    override fun getLineCommentPrefix() = "-- "

    override fun getBlockCommentTokenType(): IElementType? = null
    override fun getBlockCommentPrefix(): String? = null
    override fun getBlockCommentSuffix(): String? = null

    override fun getDocumentationCommentTokenType(): IElementType = ElasticsearchSqlTypes.JAVADOC
    override fun isDocumentationComment(psiComment: PsiComment?) =
        psiComment?.tokenType == documentationCommentTokenType

    override fun getDocumentationCommentPrefix() = "/**"
    override fun getDocumentationCommentLinePrefix() = "*"
    override fun getDocumentationCommentSuffix() = "*/"

    override fun getCommentedBlockCommentPrefix(): String? = null
    override fun getCommentedBlockCommentSuffix(): String? = null
}
