package com.cap.plugins.elasticsearch.language.kql.structure

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlLanguage
import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlTypes
import com.intellij.codeInsight.highlighting.PairedBraceMatcherAdapter
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

@Keep
class ElasticsearchKqlPairedBraceMatcherAdapter :
    PairedBraceMatcherAdapter(MyPairedBraceMatcher(), ElasticsearchKqlLanguage.INSTANCE) {
    companion object {
        val PAIRS: Array<BracePair> = arrayOf(
            BracePair(ElasticsearchKqlTypes.BRACE1, ElasticsearchKqlTypes.BRACE2, true),
            BracePair(ElasticsearchKqlTypes.BRACK1, ElasticsearchKqlTypes.BRACK2, true)
        )
    }

    class MyPairedBraceMatcher : PairedBraceMatcher {
        override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int {
            return openingBraceOffset
        }

        override fun getPairs(): Array<BracePair> {
            return PAIRS
        }

        override fun isPairedBracesAllowedBeforeType(p0: IElementType, p1: IElementType?): Boolean {
            return true
        }
    }
}