package com.cap.plugins.elasticsearch.language.kql.formatter

import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.formatting.Wrap
import com.intellij.lang.ASTNode
import java.util.*

class ElasticsearchKqlLeafBlock(myNode: ASTNode) : ElasticsearchKqlBaseBlock(myNode) {

    override fun getSubBlocks(): MutableList<Block> = Collections.emptyList()

    override fun getSpacing(p0: Block?, p1: Block): Spacing? = null

    override fun getWrap(): Wrap? = null

    override fun isLeaf(): Boolean = true
}