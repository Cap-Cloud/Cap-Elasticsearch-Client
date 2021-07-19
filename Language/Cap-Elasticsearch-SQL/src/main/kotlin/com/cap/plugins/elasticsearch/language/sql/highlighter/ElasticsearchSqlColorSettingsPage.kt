package com.cap.plugins.elasticsearch.language.sql.highlighter

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.language.sql.ElasticsearchSqlIcons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

@Keep
class ElasticsearchSqlColorSettingsPage : ColorSettingsPage {
    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey> {
        return mutableMapOf()
    }

    override fun getIcon(): Icon? {
        return ElasticsearchSqlIcons.ELASTICSEARCH_ICON
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return arrayOf(
            AttributesDescriptor("Comma",
                ElasticsearchSqlSyntaxHighlighter.ELASTICSEARCH_SQL_COMMA
            ),
            AttributesDescriptor("Doc",
                ElasticsearchSqlSyntaxHighlighter.ELASTICSEARCH_SQL_DOC
            ),
            AttributesDescriptor("Dot",
                ElasticsearchSqlSyntaxHighlighter.ELASTICSEARCH_SQL_DOT
            ),
            AttributesDescriptor("Function",
                ElasticsearchSqlSyntaxHighlighter.ELASTICSEARCH_SQL_FUNCTION
            ),
            AttributesDescriptor("Keyword",
                ElasticsearchSqlSyntaxHighlighter.ELASTICSEARCH_SQL_KEYWORD
            ),
            AttributesDescriptor("Line Comment",
                ElasticsearchSqlSyntaxHighlighter.ELASTICSEARCH_SQL_LINE_COMMENT
            ),
            AttributesDescriptor("Number",
                ElasticsearchSqlSyntaxHighlighter.ELASTICSEARCH_SQL_NUMBER
            ),
            AttributesDescriptor("Operator",
                ElasticsearchSqlSyntaxHighlighter.ELASTICSEARCH_SQL_OPERATOR
            ),
            AttributesDescriptor("Paren",
                ElasticsearchSqlSyntaxHighlighter.ELASTICSEARCH_SQL_PAREN
            ),
            AttributesDescriptor("Semicolon",
                ElasticsearchSqlSyntaxHighlighter.ELASTICSEARCH_SQL_SEMICOLON
            ),
            AttributesDescriptor("String",
                ElasticsearchSqlSyntaxHighlighter.ELASTICSEARCH_SQL_STRING
            )
        )
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "Cap Elasticsearch SQL"
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return ElasticsearchSqlSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return """
            --This is a demo.
            select author_id, author_name
              from twitter
             where author_id > 55
                or author_name in ('Andy Chesney', 'Cap Elasticsearch SQL');
        """.trimIndent()
    }
}