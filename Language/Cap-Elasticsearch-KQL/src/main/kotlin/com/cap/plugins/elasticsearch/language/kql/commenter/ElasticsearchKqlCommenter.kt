package com.cap.plugins.elasticsearch.language.kql.commenter

import com.cap.plugins.common.annotation.Keep
import com.intellij.codeInsight.generation.CommenterDataHolder
import com.intellij.codeInsight.generation.SelfManagingCommenter
import com.intellij.lang.Commenter
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import com.intellij.util.text.CharArrayUtil

@Keep
class ElasticsearchKqlCommenter : Commenter, SelfManagingCommenter<CommenterDataHolder> {
    private val HASH_COMMENT_PREFIX = "#"

    private val SLASH_COMMENT_PREFIX = "//"

    // Commenter
    override fun getCommentedBlockCommentPrefix(): String? = null

    override fun getCommentedBlockCommentSuffix(): String? = null

    override fun getBlockCommentPrefix(): String? = null

    override fun getBlockCommentSuffix(): String? = null

    override fun getLineCommentPrefix(): String? = HASH_COMMENT_PREFIX

    // SelfManagingCommenter
    override fun getBlockCommentPrefix(p0: Int, p1: Document, p2: CommenterDataHolder): String? = blockCommentPrefix

    override fun getBlockCommentSuffix(p0: Int, p1: Document, p2: CommenterDataHolder): String? = blockCommentSuffix

    override fun uncommentBlockComment(p0: Int, p1: Int, p2: Document?, p3: CommenterDataHolder?) {
    }

    override fun insertBlockComment(p0: Int, p1: Int, p2: Document?, p3: CommenterDataHolder?): TextRange {
        return TextRange(0, 0)
    }

    override fun uncommentLine(line: Int, offset: Int, document: Document, data: CommenterDataHolder) {
        if (CharArrayUtil.regionMatches(document.charsSequence, offset, HASH_COMMENT_PREFIX)) {
            document.deleteString(offset, offset + HASH_COMMENT_PREFIX.length)
        } else {
            document.deleteString(offset, offset + SLASH_COMMENT_PREFIX.length)
        }
    }

    override fun getBlockCommentRange(p0: Int, p1: Int, p2: Document, p3: CommenterDataHolder): TextRange? = null

    override fun isLineCommented(line: Int, offset: Int, document: Document, data: CommenterDataHolder): Boolean {
        return (CharArrayUtil.regionMatches(document.charsSequence, offset, HASH_COMMENT_PREFIX) ||
                CharArrayUtil.regionMatches(document.charsSequence, offset, SLASH_COMMENT_PREFIX))
    }

    override fun commentLine(line: Int, offset: Int, document: Document, data: CommenterDataHolder) {
        document.insertString(offset, HASH_COMMENT_PREFIX)
    }

    override fun getCommentPrefix(p0: Int, p1: Document, p2: CommenterDataHolder): String? = HASH_COMMENT_PREFIX

    override fun createBlockCommentingState(p0: Int, p1: Int, p2: Document, p3: PsiFile): CommenterDataHolder? = null

    override fun createLineCommentingState(p0: Int, p1: Int, p2: Document, p3: PsiFile): CommenterDataHolder? = null
}
