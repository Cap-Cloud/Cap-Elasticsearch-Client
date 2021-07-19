package com.cap.plugins.elasticsearch.language.sql.grammar

import com.cap.plugins.elasticsearch.language.sql.grammar.psi.ElasticsearchSqlTypes
import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase

abstract class ElasticsearchSqlModuleParserUtil : GeneratedParserUtilBase() {
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
            while (builder.tokenType != ElasticsearchSqlTypes.COMMA) {
                if (builder.tokenType == ElasticsearchSqlTypes.LP) parens++
                if (builder.tokenType == ElasticsearchSqlTypes.RP && (--parens == -1)) break
                builder.advanceLexer()
            }
            result = (parens <= 0)
            exit_section_(builder, level, marker, result, false, null)
            return result
        }
    }
}
