package com.cap.plugins.elasticsearch.language.sql.highlighter

import com.cap.plugins.elasticsearch.language.sql.grammar.psi.ElasticsearchSqlTypes
import com.cap.plugins.elasticsearch.language.sql.lexer.ElasticsearchSqlLexerAdapter
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class ElasticsearchSqlSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return pack(TEXT_ATTRIBUTES_MAP[tokenType.index])
//        val field = tokenType.javaClass.getDeclaredField("myDebugName")
//        field.isAccessible = true
//        val token = field.get(tokenType).toString().toUpperCase()
//        return pack(TEXT_ATTRIBUTES_MAP[token])
    }

    override fun getHighlightingLexer() =
        ElasticsearchSqlLexerAdapter()

    companion object {
        val ELASTICSEARCH_SQL_KEYWORD = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_SQL.KEYWORD", DefaultLanguageHighlighterColors.KEYWORD
        )
        val ELASTICSEARCH_SQL_FUNCTION = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_SQL.FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_CALL
        )
        val ELASTICSEARCH_SQL_STRING = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_SQL.STRING", DefaultLanguageHighlighterColors.STRING
        )
        val ELASTICSEARCH_SQL_NUMBER = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_SQL.NUMBER", DefaultLanguageHighlighterColors.NUMBER
        )
        val ELASTICSEARCH_SQL_OPERATOR = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_SQL.OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN
        )
        val ELASTICSEARCH_SQL_DOT = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_SQL.DOT", DefaultLanguageHighlighterColors.DOT
        )
        val ELASTICSEARCH_SQL_SEMICOLON = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_SQL.SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON
        )
        val ELASTICSEARCH_SQL_COMMA = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_SQL.COMMA", DefaultLanguageHighlighterColors.COMMA
        )
        val ELASTICSEARCH_SQL_LINE_COMMENT = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_SQL.LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT
        )
        val ELASTICSEARCH_SQL_PAREN = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_SQL.PAREN", DefaultLanguageHighlighterColors.PARENTHESES
        )
        val ELASTICSEARCH_SQL_DOC = TextAttributesKey.createTextAttributesKey(
            "ELASTICSEARCH_SQL.DOC", DefaultLanguageHighlighterColors.DOC_COMMENT
        )

        private val TEXT_ATTRIBUTES_MAP = mapOf(
            ElasticsearchSqlTypes.DIGIT.index to ELASTICSEARCH_SQL_NUMBER,

            ElasticsearchSqlTypes.COMMA.index to ELASTICSEARCH_SQL_COMMA,

            ElasticsearchSqlTypes.DOT.index to ELASTICSEARCH_SQL_DOT,

            ElasticsearchSqlTypes.LP.index to ELASTICSEARCH_SQL_PAREN,
            ElasticsearchSqlTypes.RP.index to ELASTICSEARCH_SQL_PAREN,

            ElasticsearchSqlTypes.SEMI.index to ELASTICSEARCH_SQL_SEMICOLON,

            ElasticsearchSqlTypes.EQ.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.PLUS.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.MINUS.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.SHIFT_RIGHT.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.SHIFT_LEFT.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.LT.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.GT.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.LTE.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.GTE.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.EQ2.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.NEQ.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.NEQ2.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.MULTIPLY.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.DIVIDE.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.MOD.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.BITWISE_AND.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.BITWISE_OR.index to ELASTICSEARCH_SQL_OPERATOR,
            ElasticsearchSqlTypes.CONCAT.index to ELASTICSEARCH_SQL_OPERATOR,

            // String
            ElasticsearchSqlTypes.STRING.index to ELASTICSEARCH_SQL_STRING,

            // Comments
            ElasticsearchSqlTypes.COMMENT.index to ELASTICSEARCH_SQL_LINE_COMMENT,
            ElasticsearchSqlTypes.JAVADOC.index to ELASTICSEARCH_SQL_DOC,

            // ElasticsearchSQL
            // Keywords
            ElasticsearchSqlTypes.ALL.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.AND.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ANY.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.AS.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ASC.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.BETWEEN.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.BY.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CAST.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CATALOG.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CONVERT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CURRENT_DATE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CURRENT_TIMESTAMP.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CURRENT_TIME.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DAY.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DAYS.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DESC.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DESCRIBE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DISTINCT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ESCAPE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.EXISTS.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.EXPLAIN.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.EXTRACT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.FALSE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.FIRST.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.FROM.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.FULL.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.GROUP.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.HAVING.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.HOUR.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.HOURS.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.IN.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.INNER.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.INTERVAL.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.IS.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.JOIN.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.LEFT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.LIKE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.LIMIT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.MATCH.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.MINUTE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.MINUTES.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.MONTH.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.NATURAL.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.NOT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.NULL.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.NULLS.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ON.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.OR.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ORDER.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.OUTER.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.RIGHT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.RLIKE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.QUERY.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.SECOND.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.SECONDS.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.SELECT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.SESSION.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.TABLE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.TABLES.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.THEN.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.TO.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.TOP.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.TRUE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.TYPE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.USING.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.WHEN.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.WHERE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.WITH.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.YEAR.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.YEARS.index to ELASTICSEARCH_SQL_KEYWORD,

            ElasticsearchSqlTypes.SHOW.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.COLUMNS.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.FUNCTIONS.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.PIVOT.index to ELASTICSEARCH_SQL_KEYWORD,

            // SQL
            // Keywords
            ElasticsearchSqlTypes.ABORT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ACTION.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ADD.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.AFTER.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ALTER.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ANALYZE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ATTACH.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.AUTOINCREMENT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.BEFORE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.BEGIN.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CASCADE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CASE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CHECK.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.COLLATE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.COLUMN.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.COMMIT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CONFLICT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CONSTRAINT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CREATE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.CROSS.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DATABASE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DEFAULT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DEFERRABLE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DEFERRED.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DELETE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DETACH.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DO.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.DROP.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.E.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.EACH.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ELSE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.END.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.EXCEPT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.EXCLUSIVE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.FAIL.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.FOR.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.FOREIGN.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.GLOB.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.IF.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.IGNORE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.IMMEDIATE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.INDEX.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.INDEXED.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.INITIALLY.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.INSERT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.INSTEAD.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.INTERSECT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.INTO.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ISNULL.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.KEY.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.NO.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.NOTHING.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.NOTNULL.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.OF.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.OFFSET.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.PLAN.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.PRAGMA.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.PRIMARY.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.RAISE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.RECURSIVE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.REFERENCES.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.REGEXP.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.REINDEX.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.RELEASE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.RENAME.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.REPLACE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.RESTRICT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ROLLBACK.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ROW.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.ROWID.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.SAVEPOINT.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.SET.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.TEMP.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.TEMPORARY.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.TRANSACTION.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.TRIGGER.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.UNION.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.UNIQUE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.UPDATE.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.VACUUM.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.VALUES.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.VIEW.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.VIRTUAL.index to ELASTICSEARCH_SQL_KEYWORD,
            ElasticsearchSqlTypes.WITHOUT.index to ELASTICSEARCH_SQL_KEYWORD
        )
    }
}