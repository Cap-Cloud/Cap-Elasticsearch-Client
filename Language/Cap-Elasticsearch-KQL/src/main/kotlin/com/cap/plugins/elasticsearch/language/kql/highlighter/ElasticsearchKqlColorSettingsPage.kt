package com.cap.plugins.elasticsearch.language.kql.highlighter

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlIcons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

@Keep
class ElasticsearchKqlColorSettingsPage : ColorSettingsPage {
    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey> {
        return mutableMapOf()
    }

    override fun getIcon(): Icon? {
        return ElasticsearchKqlIcons.ELASTICSEARCH_ICON
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return arrayOf(
            AttributesDescriptor("Braces",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_BRACES
            ),
            AttributesDescriptor("Brackets",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_BRACKETS
            ),
            AttributesDescriptor("Comma",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_COMMA
            ),
            AttributesDescriptor("Doc",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_DOC
            ),
            AttributesDescriptor("Dot",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_DOT
            ),
            AttributesDescriptor("Function",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_FUNCTION
            ),
            AttributesDescriptor("Keyword",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_KEYWORD
            ),
            AttributesDescriptor("Line Comment",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_LINE_COMMENT
            ),
            AttributesDescriptor("Number",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_NUMBER
            ),
            AttributesDescriptor("Operator",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_OPERATOR
            ),
            AttributesDescriptor("Paren",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_PAREN
            ),
            AttributesDescriptor("Semicolon",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_SEMICOLON
            ),
            AttributesDescriptor("String",
                ElasticsearchKqlSyntaxHighlighter.ELASTICSEARCH_KQL_STRING
            )
        )
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "Cap Elasticsearch KQL"
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return ElasticsearchKqlSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return """
            # This is a demo.
            GET /twitter/_search
            {
                "from": 0,
                "size": 10
            }
        """.trimIndent()
    }
}