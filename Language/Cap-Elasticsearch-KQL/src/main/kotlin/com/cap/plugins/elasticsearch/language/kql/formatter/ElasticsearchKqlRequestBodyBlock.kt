package com.cap.plugins.elasticsearch.language.kql.formatter

import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlTypes
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.DefaultInjectedLanguageBlockBuilder
import com.intellij.psi.formatter.common.SettingsAwareBlock
import com.intellij.psi.tree.IElementType
import java.util.*


class ElasticsearchKqlRequestBodyBlock(myNode: ASTNode, var mySettings: CodeStyleSettings) :
    ElasticsearchKqlBaseBlock(myNode), SettingsAwareBlock {
//    private val myInjectedBlockBuilder: DefaultInjectedLanguageBlockBuilder =
//        DefaultInjectedLanguageBlockBuilder(mySettings)
//
//    override fun getSubBlocks(): MutableList<Block> {
//        val result: MutableList<Block> = ArrayList()
//        myInjectedBlockBuilder.addInjectedBlocks(
//            result,
//            node,
//            wrap,
//            alignment,
//            Indent.getAbsoluteNoneIndent()
//        )
//        return result
//    }

    override fun createBlock(node: ASTNode): Block {
        val type: IElementType = node.elementType
        if (type === ElasticsearchKqlTypes.JSON_OBJECT) {
            return ElasticsearchKqlRequestJsonObjectBlock(
                node,
                settings
            )
        }
        return super.createBlock(node)
    }

    override fun getSpacing(p0: Block?, p1: Block): Spacing? = null

    override fun getWrap(): Wrap? = null

    override fun isLeaf(): Boolean = false

    override fun getSettings(): CodeStyleSettings = mySettings
}