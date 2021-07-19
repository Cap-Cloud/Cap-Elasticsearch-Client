package com.cap.plugins.elasticsearch.language.kql.formatter

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.TokenType
import java.util.*

abstract class ElasticsearchKqlBaseBlock(var myNode: ASTNode) : ASTBlock {
    private val NONE = ChildAttributes(Indent.getAbsoluteNoneIndent(), null)
    private val myIndent: Indent  = Indent.getAbsoluteNoneIndent()
    override fun getAlignment(): Alignment? = null

    override fun isIncomplete(): Boolean = false

    override fun isLeaf(): Boolean = true

    override fun getNode(): ASTNode? = myNode

    override fun getTextRange(): TextRange = myNode.textRange

    override fun getSubBlocks(): MutableList<Block> {
        val nodes = node!!.getChildren(null)
        val blocks = ArrayList<Block>(nodes.size)
        for (node in nodes) {
            if (node.elementType != TokenType.WHITE_SPACE) {
                blocks.add(createBlock(node))
            }
        }
        return if (blocks.isEmpty()) {
            mutableListOf()
        } else {
            blocks
        }
    }

    open fun createBlock(node: ASTNode): Block {
        return ElasticsearchKqlLeafBlock(node)
    }
    override fun getChildAttributes(newChildIndex: Int): ChildAttributes = NONE

    override fun getIndent(): Indent? = myIndent
}