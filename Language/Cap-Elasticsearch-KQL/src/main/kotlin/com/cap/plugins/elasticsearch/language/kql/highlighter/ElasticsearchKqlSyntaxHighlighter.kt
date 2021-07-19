package com.cap.plugins.elasticsearch.language.kql.highlighter

import com.cap.plugins.elasticsearch.language.kql.lexer.ElasticsearchKqlLexerAdapter
import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlTypes
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class ElasticsearchKqlSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return pack(TEXT_ATTRIBUTES_MAP[tokenType.index])
//        val field = tokenType.javaClass.getDeclaredField("myDebugName")
//        field.isAccessible = true
//        val token = field.get(tokenType).toString().toUpperCase()
//        return pack(TEXT_ATTRIBUTES_MAP[token])
    }

    override fun getHighlightingLexer() =
        ElasticsearchKqlLexerAdapter()

    companion object {
        val ELASTICSEARCH_KQL_BRACES = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.BRACES", DefaultLanguageHighlighterColors.BRACES
        )
        val ELASTICSEARCH_KQL_BRACKETS = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.BRACKETS", DefaultLanguageHighlighterColors.BRACKETS
        )
        val ELASTICSEARCH_KQL_COMMA = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.COMMA", DefaultLanguageHighlighterColors.COMMA
        )
        val ELASTICSEARCH_KQL_SEMICOLON = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON
        )
        val ELASTICSEARCH_KQL_LINE_COMMENT = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT
        )
        val ELASTICSEARCH_KQL_KEYWORD = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.KEYWORD", DefaultLanguageHighlighterColors.KEYWORD
        )
        val ELASTICSEARCH_KQL_STRING = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.STRING", DefaultLanguageHighlighterColors.STRING
        )
        val ELASTICSEARCH_KQL_NUMBER = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.NUMBER", DefaultLanguageHighlighterColors.NUMBER
        )
        val ELASTICSEARCH_KQL_URL = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.INLINE_PARAMETER_HINT_HIGHLIGHTED", DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT_HIGHLIGHTED
        )
        val ELASTICSEARCH_KQL_GLOBAL_VARIABLE = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.GLOBAL_VARIABLE", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE
        )
        val ELASTICSEARCH_KQL_MARKUP_ATTRIBUTE = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.MARKUP_ATTRIBUTE", DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE
        )


        val ELASTICSEARCH_KQL_DOT = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.DOT", DefaultLanguageHighlighterColors.DOT
        )
        val ELASTICSEARCH_KQL_PAREN = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.PAREN", DefaultLanguageHighlighterColors.PARENTHESES
        )

        val ELASTICSEARCH_KQL_FUNCTION = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_CALL
        )
        val ELASTICSEARCH_KQL_OPERATOR = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        val ELASTICSEARCH_KQL_DOC = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_KQL.DOC", DefaultLanguageHighlighterColors.DOC_COMMENT
        )

        private val TEXT_ATTRIBUTES_MAP = mapOf(
            ElasticsearchKqlTypes.BRACE1.index to ELASTICSEARCH_KQL_BRACES,
            ElasticsearchKqlTypes.BRACE2.index to ELASTICSEARCH_KQL_BRACES,

            ElasticsearchKqlTypes.BRACK1.index to ELASTICSEARCH_KQL_BRACKETS,
            ElasticsearchKqlTypes.BRACK2.index to ELASTICSEARCH_KQL_BRACKETS,

            ElasticsearchKqlTypes.COMMA.index to ELASTICSEARCH_KQL_COMMA,

            ElasticsearchKqlTypes.COLON.index to ELASTICSEARCH_KQL_SEMICOLON,

            ElasticsearchKqlTypes.COMMENT.index to ELASTICSEARCH_KQL_LINE_COMMENT,

            ElasticsearchKqlTypes.REQUEST_URL.index to ELASTICSEARCH_KQL_URL,
            ElasticsearchKqlTypes.URL_PREFIX.index to ELASTICSEARCH_KQL_URL,
            ElasticsearchKqlTypes.URL_ROOT.index to ELASTICSEARCH_KQL_URL,
            ElasticsearchKqlTypes.URL_PATH.index to ELASTICSEARCH_KQL_URL,
            ElasticsearchKqlTypes.URL_OPTION.index to ELASTICSEARCH_KQL_URL,
            ElasticsearchKqlTypes.URL_PARAM1.index to ELASTICSEARCH_KQL_URL,
            ElasticsearchKqlTypes.URL_PARAM2.index to ELASTICSEARCH_KQL_URL,
            ElasticsearchKqlTypes.URL_PARAM_VALUE.index to ELASTICSEARCH_KQL_URL,

            ElasticsearchKqlTypes.DELETE.index to ELASTICSEARCH_KQL_KEYWORD,
            ElasticsearchKqlTypes.GET.index to ELASTICSEARCH_KQL_KEYWORD,
            ElasticsearchKqlTypes.HEAD.index to ELASTICSEARCH_KQL_KEYWORD,
            ElasticsearchKqlTypes.POST.index to ELASTICSEARCH_KQL_KEYWORD,
            ElasticsearchKqlTypes.PUT.index to ELASTICSEARCH_KQL_KEYWORD,

            ElasticsearchKqlTypes.NUMBER.index to ELASTICSEARCH_KQL_NUMBER,

            ElasticsearchKqlTypes.OBJ_NAME.index to ELASTICSEARCH_KQL_KEYWORD,

            ElasticsearchKqlTypes.NULL.index to ELASTICSEARCH_KQL_KEYWORD,
            ElasticsearchKqlTypes.TRUE.index to ELASTICSEARCH_KQL_KEYWORD,
            ElasticsearchKqlTypes.FALSE.index to ELASTICSEARCH_KQL_KEYWORD,

            ElasticsearchKqlTypes.STRING.index to ELASTICSEARCH_KQL_STRING,
        )
    }
}