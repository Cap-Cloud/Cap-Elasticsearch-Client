package com.cap.plugins.elasticsearch.language.kql.grammar

import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlTypes
import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase

abstract class ElasticsearchKqlModuleParserUtil : GeneratedParserUtilBase() {
    companion object {
        @JvmStatic
        fun custom_module_argument(builder: PsiBuilder, level: Int, columnName: Parser): Boolean {
            if (!recursion_guard_(builder, level, "module_argument_real")) return false
            var result: Boolean
            val marker = enter_section_(
                builder, level, _COLLAPSE_, "<module argument real>"
            )
            columnName.parse(builder, level + 1)
            var parens = 0
            while (builder.tokenType != ElasticsearchKqlTypes.COMMA) {
                builder.advanceLexer()
            }
            result = (parens <= 0)
            exit_section_(builder, level, marker, result, false, null)
            return result
        }
    }
}
