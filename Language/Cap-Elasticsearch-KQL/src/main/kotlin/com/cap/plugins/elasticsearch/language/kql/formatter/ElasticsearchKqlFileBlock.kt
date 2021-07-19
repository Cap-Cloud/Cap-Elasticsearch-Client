package com.cap.plugins.elasticsearch.language.kql.formatter

import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlTypes
import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.SettingsAwareBlock
import com.intellij.psi.tree.IElementType

class ElasticsearchKqlFileBlock(myNode: ASTNode, var mySettings: CodeStyleSettings) : ElasticsearchKqlBaseBlock(myNode),
    SettingsAwareBlock {

    override fun createBlock(node: ASTNode): Block {
        val type: IElementType = node.elementType
        if (type === ElasticsearchKqlTypes.REQUEST_GROUP) {
            return ElasticsearchKqlRequestGroupBlock(
                node,
                settings
            )
        }
        return super.createBlock(node)
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        if (child1 is ElasticsearchKqlRequestGroupBlock &&
            (child2 is ElasticsearchKqlRequestGroupBlock || isRequestSeparatorBlock(child2))
        ) {
            return Spacing.createSpacing(0, 0, 2, true, 100);
        }
        return Spacing.getReadOnlySpacing();
    }


    private fun isRequestSeparatorBlock(node: Block): Boolean {
        return node is ElasticsearchKqlBaseBlock
    }

    override fun getWrap(): Wrap? = null

    override fun isLeaf(): Boolean = false

    override fun getSettings(): CodeStyleSettings = mySettings
}