// 用户代码段，代码直接复制到生成的类中
package com.cap.plugins.elasticsearch.language.kql.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlTypes.*;

%%

// 参数设置和声明段

// 参数设置

%{
  public ElasticsearchKqlFlexLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class ElasticsearchKqlFlexLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

// 宏声明

EOL=\R
WHITE_SPACE=\s+

SPACE=[ \t\n\x0B\f\r]+
COMMENT=(#.*)|(\/\/.*)
NUMBER=[0-9]+(\.[0-9]*)?
ID=([a-zA-Z_][a-zA-Z_0-9]*)|(`[a-zA-Z_0-9 ]+`)|(\[[a-zA-Z_0-9 ]+\])
STRING=('([^'])*'|\"([^\"])*\")

GET="GET"
HEAD="HEAD"
POST="POST"
PUT="PUT"
DELETE="DELETE"

// URL=(((ht|f)tps?)\:\/\/|\/)[^\s\r\n]*

URL_PREFIX=(((ht|f)tps?)\:\/\/)[^\s\r\n\\\/\?\&]+
URL_ROOT=\/
URL_PATH=\/[^\s\,\?\&\=\r\\\/\n]+
URL_OPTION=\,[^\s\,\?\&\=\r\\\/\n]+
URL_PARAM1=\?[^\s\,\?\&\=\r\\\/\n]+
URL_PARAM2=\&[^\s\,\?\&\=\r\\\/\n]+
URL_PARAM_VALUE=\=[^\s\,\?\&\=\r\\\/\n]+

NULL="null"
FALSE="false"
TRUE="true"

END=\n+(-){5}.*

%%

// 词法规则段

// YYINITIAL 是一个预定义的词法状态，是词法分析器初始扫描输入的状态。
<YYINITIAL> {
  {WHITE_SPACE}          { return WHITE_SPACE; }

  ","                    { return COMMA; }
  ":"                    { return COLON; }
  "{"                    { return BRACE1; }
  "}"                    { return BRACE2; }
  "["                    { return BRACK1; }
  "]"                    { return BRACK2; }

  {END}                  { return SEPARATOR; }

  {NULL}                 { return NULL; }
  {FALSE}                { return FALSE; }
  {TRUE}                 { return TRUE; }

  {GET}                  { return GET; }
  {HEAD}                 { return HEAD; }
  {POST}                 { return POST; }
  {PUT}                  { return PUT; }
  {DELETE}               { return DELETE; }

  {URL_PREFIX}           { return URL_PREFIX; }
  {URL_ROOT}             { return URL_ROOT; }
  {URL_PATH}             { return URL_PATH; }
  {URL_OPTION}           { return URL_OPTION; }
  {URL_PARAM1}           { return URL_PARAM1; }
  {URL_PARAM2}           { return URL_PARAM2; }
  {URL_PARAM_VALUE}      { return URL_PARAM_VALUE; }

  {SPACE}                { return SPACE; }
  {COMMENT}              { return COMMENT; }
  {NUMBER}               { return NUMBER; }
  {ID}                   { return ID; }
  {STRING}               { return STRING; }
}

[^] { return BAD_CHARACTER; }
