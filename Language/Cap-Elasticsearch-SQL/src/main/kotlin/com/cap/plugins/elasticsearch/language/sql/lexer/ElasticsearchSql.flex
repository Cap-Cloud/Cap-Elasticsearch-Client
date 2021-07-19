package com.cap.plugins.elasticsearch.language.sql.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.cap.plugins.elasticsearch.language.sql.grammar.psi.ElasticsearchSqlTypes.*;

%%

%{
  public ElasticsearchSqlFlexLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class ElasticsearchSqlFlexLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

SPACE=[ \t\n\x0B\f\r]+
COMMENT=--.*
JAVADOC="/"\*\*([^*]|\*+[^/*])*\*"/"
DIGIT=[0-9]+(\.[0-9]*)?
ID=([a-zA-Z_][a-zA-Z_0-9]*)|(`[a-zA-Z_0-9 ]+`)|(\[[a-zA-Z_0-9 ]+\])
STRING=('([^'])*'|\"([^\"])*\")

ALL=(A|a)(L|l)(L|l)
AND=(A|a)(N|n)(D|d)
ANY=(A|a)(N|n)(Y|y)
AS=(A|a)(S|s)
ASC=(A|a)(S|s)(C|c)
BETWEEN=(B|b)(E|e)(T|t)(W|w)(E|e)(E|e)(N|n)
BY=(B|b)(Y|y)
CAST=(C|c)(A|a)(S|s)(T|t)
CATALOG=(C|c)(A|a)(T|t)(A|a)(L|l)(O|o)(G|g)
CONVERT=(C|c)(O|o)(N|n)(V|v)(E|e)(R|r)(T|t)
CURRENT_DATE=(C|c)(U|u)(R|r)(R|r)(E|e)(N|n)(T|t)(_|_)(D|d)(A|a)(T|t)(E|e)
CURRENT_TIMESTAMP=(C|c)(U|u)(R|r)(R|r)(E|e)(N|n)(T|t)(_|_)(T|t)(I|i)(M|m)(E|e)(S|s)(T|t)(A|a)(M|m)(P|p)
CURRENT_TIME=(C|c)(U|u)(R|r)(R|r)(E|e)(N|n)(T|t)(_|_)(T|t)(I|i)(M|m)(E|e)
DAY=(D|d)(A|a)(Y|y)
DAYS=(D|d)(A|a)(Y|y)(S|s)
DESC=(D|d)(E|e)(S|s)(C|c)
DESCRIBE=(D|d)(E|e)(S|s)(C|c)(R|r)(I|i)(B|b)(E|e)
DISTINCT=(D|d)(I|i)(S|s)(T|t)(I|i)(N|n)(C|c)(T|t)
ESCAPE=(E|e)(S|s)(C|c)(A|a)(P|p)(E|e)
EXISTS=(E|e)(X|x)(I|i)(S|s)(T|t)(S|s)
EXPLAIN=(E|e)(X|x)(P|p)(L|l)(A|a)(I|i)(N|n)
EXTRACT=(E|e)(X|x)(T|t)(R|r)(A|a)(C|c)(T|t)
FALSE=(F|f)(A|a)(L|l)(S|s)(E|e)
FIRST=(F|f)(I|i)(R|r)(S|s)(T|t)
FROM=(F|f)(R|r)(O|o)(M|m)
FULL=(F|f)(U|u)(L|l)(L|l)
GROUP=(G|g)(R|r)(O|o)(U|u)(P|p)
HAVING=(H|h)(A|a)(V|v)(I|i)(N|n)(G|g)
HOUR=(H|h)(O|o)(U|u)(R|r)
HOURS=(H|h)(O|o)(U|u)(R|r)(S|s)
IN=(I|i)(N|n)
INNER=(I|i)(N|n)(N|n)(E|e)(R|r)
INTERVAL=(I|i)(N|n)(T|t)(E|e)(R|r)(V|v)(A|a)(L|l)
IS=(I|i)(S|s)
JOIN=(J|j)(O|o)(I|i)(N|n)
LEFT=(L|l)(E|e)(F|f)(T|t)
LIKE=(L|l)(I|i)(K|k)(E|e)
LIMIT=(L|l)(I|i)(M|m)(I|i)(T|t)
MATCH=(M|m)(A|a)(T|t)(C|c)(H|h)
MINUTE=(M|m)(I|i)(N|n)(U|u)(T|t)(E|e)
MINUTES=(M|m)(I|i)(N|n)(U|u)(T|t)(E|e)(S|s)
MONTH=(M|m)(O|o)(N|n)(T|t)(H|h)
NATURAL=(N|n)(A|a)(T|t)(U|u)(R|r)(A|a)(L|l)
NOT=(N|n)(O|o)(T|t)
NULL=(N|n)(U|u)(L|l)(L|l)
NULLS=(N|n)(U|u)(L|l)(L|l)(S|s)
ON=(O|o)(N|n)
OR=(O|o)(R|r)
ORDER=(O|o)(R|r)(D|d)(E|e)(R|r)
OUTER=(O|o)(U|u)(T|t)(E|e)(R|r)
RIGHT=(R|r)(I|i)(G|g)(H|h)(T|t)
RLIKE=(R|r)(L|l)(I|i)(K|k)(E|e)
QUERY=(Q|q)(U|u)(E|e)(R|r)(Y|y)
SECOND=(S|s)(E|e)(C|c)(O|o)(N|n)(D|d)
SECONDS=(S|s)(E|e)(C|c)(O|o)(N|n)(D|d)(S|s)
SELECT=(S|s)(E|e)(L|l)(E|e)(C|c)(T|t)
SESSION=(S|s)(E|e)(S|s)(S|s)(I|i)(O|o)(N|n)
TABLE=(T|t)(A|a)(B|b)(L|l)(E|e)
TABLES=(T|t)(A|a)(B|b)(L|l)(E|e)(S|s)
THEN=(T|t)(H|h)(E|e)(N|n)
TO=(T|t)(O|o)
TOP=(T|t)(O|o)(P|p)
TRUE=(T|t)(R|r)(U|u)(E|e)
TYPE=(T|t)(Y|y)(P|p)(E|e)
USING=(U|u)(S|s)(I|i)(N|n)(G|g)
WHEN=(W|w)(H|h)(E|e)(N|n)
WHERE=(W|w)(H|h)(E|e)(R|r)(E|e)
WITH=(W|w)(I|i)(T|t)(H|h)
YEAR=(Y|y)(E|e)(A|a)(R|r)
YEARS=(Y|y)(E|e)(A|a)(R|r)(S|s)

SHOW=(S|s)(H|h)(O|o)(W|w)
COLUMNS=(C|c)(O|o)(L|l)(U|u)(M|m)(N|n)(S|s)
FUNCTIONS=(F|f)(U|u)(N|n)(C|c)(T|t)(I|i)(O|o)(N|n)(S|s)
PIVOT=(P|p)(I|i)(V|v)(O|o)(T|t)

ABORT=(A|a)(B|b)(O|o)(R|r)(T|t)
ACTION=(A|a)(C|c)(T|t)(I|i)(O|o)(N|n)
ADD=(A|a)(D|d)(D|d)
AFTER=(A|a)(F|f)(T|t)(E|e)(R|r)
ALTER=(A|a)(L|l)(T|t)(E|e)(R|r)
ANALYZE=(A|a)(N|n)(A|a)(L|l)(Y|y)(Z|z)(E|e)
ATTACH=(A|a)(T|t)(T|t)(A|a)(C|c)(H|h)
AUTOINCREMENT=(A|a)(U|u)(T|t)(O|o)(I|i)(N|n)(C|c)(R|r)(E|e)(M|m)(E|e)(N|n)(T|t)
BEFORE=(B|b)(E|e)(F|f)(O|o)(R|r)(E|e)
BEGIN=(B|b)(E|e)(G|g)(I|i)(N|n)
CASCADE=(C|c)(A|a)(S|s)(C|c)(A|a)(D|d)(E|e)
CASE=(C|c)(A|a)(S|s)(E|e)
CHECK=(C|c)(H|h)(E|e)(C|c)(K|k)
COLLATE=(C|c)(O|o)(L|l)(L|l)(A|a)(T|t)(E|e)
COLUMN=(C|c)(O|o)(L|l)(U|u)(M|m)(N|n)
COMMIT=(C|c)(O|o)(M|m)(M|m)(I|i)(T|t)
CONFLICT=(C|c)(O|o)(N|n)(F|f)(L|l)(I|i)(C|c)(T|t)
CONSTRAINT=(C|c)(O|o)(N|n)(S|s)(T|t)(R|r)(A|a)(I|i)(N|n)(T|t)
CREATE=(C|c)(R|r)(E|e)(A|a)(T|t)(E|e)
CROSS=(C|c)(R|r)(O|o)(S|s)(S|s)
DATABASE=(D|d)(A|a)(T|t)(A|a)(B|b)(A|a)(S|s)(E|e)
DEFAULT=(D|d)(E|e)(F|f)(A|a)(U|u)(L|l)(T|t)
DEFERRABLE=(D|d)(E|e)(F|f)(E|e)(R|r)(R|r)(A|a)(B|b)(L|l)(E|e)
DEFERRED=(D|d)(E|e)(F|f)(E|e)(R|r)(R|r)(E|e)(D|d)
DELETE=(D|d)(E|e)(L|l)(E|e)(T|t)(E|e)
DETACH=(D|d)(E|e)(T|t)(A|a)(C|c)(H|h)
DO=(D|d)(O|o)
DROP=(D|d)(R|r)(O|o)(P|p)
E=(E|e)
EACH=(E|e)(A|a)(C|c)(H|h)
ELSE=(E|e)(L|l)(S|s)(E|e)
END=(E|e)(N|n)(D|d)
EXCEPT=(E|e)(X|x)(C|c)(E|e)(P|p)(T|t)
EXCLUSIVE=(E|e)(X|x)(C|c)(L|l)(U|u)(S|s)(I|i)(V|v)(E|e)
FAIL=(F|f)(A|a)(I|i)(L|l)
FOR=(F|f)(O|o)(R|r)
FOREIGN=(F|f)(O|o)(R|r)(E|e)(I|i)(G|g)(N|n)
GLOB=(G|g)(L|l)(O|o)(B|b)
IF=(I|i)(F|f)
IGNORE=(I|i)(G|g)(N|n)(O|o)(R|r)(E|e)
IMMEDIATE=(I|i)(M|m)(M|m)(E|e)(D|d)(I|i)(A|a)(T|t)(E|e)
INDEX=(I|i)(N|n)(D|d)(E|e)(X|x)
INDEXED=(I|i)(N|n)(D|d)(E|e)(X|x)(E|e)(D|d)
INITIALLY=(I|i)(N|n)(I|i)(T|t)(I|i)(A|a)(L|l)(L|l)(Y|y)
INSERT=(I|i)(N|n)(S|s)(E|e)(R|r)(T|t)
INSTEAD=(I|i)(N|n)(S|s)(T|t)(E|e)(A|a)(D|d)
INTERSECT=(I|i)(N|n)(T|t)(E|e)(R|r)(S|s)(E|e)(C|c)(T|t)
INTO=(I|i)(N|n)(T|t)(O|o)
ISNULL=(I|i)(S|s)(N|n)(U|u)(L|l)(L|l)
KEY=(K|k)(E|e)(Y|y)
NO=(N|n)(O|o)
NOTHING=(N|n)(O|o)(T|t)(H|h)(I|i)(N|n)(G|g)
NOTNULL=(N|n)(O|o)(T|t)(N|n)(U|u)(L|l)(L|l)
OF=(O|o)(F|f)
OFFSET=(O|o)(F|f)(F|f)(S|s)(E|e)(T|t)
PLAN=(P|p)(L|l)(A|a)(N|n)
PRAGMA=(P|p)(R|r)(A|a)(G|g)(M|m)(A|a)
PRIMARY=(P|p)(R|r)(I|i)(M|m)(A|a)(R|r)(Y|y)
RAISE=(R|r)(A|a)(I|i)(S|s)(E|e)
RECURSIVE=(R|r)(E|e)(C|c)(U|u)(R|r)(S|s)(I|i)(V|v)(E|e)
REFERENCES=(R|r)(E|e)(F|f)(E|e)(R|r)(E|e)(N|n)(C|c)(E|e)(S|s)
REGEXP=(R|r)(E|e)(G|g)(E|e)(X|x)(P|p)
REINDEX=(R|r)(E|e)(I|i)(N|n)(D|d)(E|e)(X|x)
RELEASE=(R|r)(E|e)(L|l)(E|e)(A|a)(S|s)(E|e)
RENAME=(R|r)(E|e)(N|n)(A|a)(M|m)(E|e)
REPLACE=(R|r)(E|e)(P|p)(L|l)(A|a)(C|c)(E|e)
RESTRICT=(R|r)(E|e)(S|s)(T|t)(R|r)(I|i)(C|c)(T|t)
ROLLBACK=(R|r)(O|o)(L|l)(L|l)(B|b)(A|a)(C|c)(K|k)
ROW=(R|r)(O|o)(W|w)
ROWID=(R|r)(O|o)(W|w)(I|i)(D|d)
SAVEPOINT=(S|s)(A|a)(V|v)(E|e)(P|p)(O|o)(I|i)(N|n)(T|t)
SET=(S|s)(E|e)(T|t)
TEMP=(T|t)(E|e)(M|m)(P|p)
TEMPORARY=(T|t)(E|e)(M|m)(P|p)(O|o)(R|r)(A|a)(R|r)(Y|y)
TRANSACTION=(T|t)(R|r)(A|a)(N|n)(S|s)(A|a)(C|c)(T|t)(I|i)(O|o)(N|n)
TRIGGER=(T|t)(R|r)(I|i)(G|g)(G|g)(E|e)(R|r)
UNION=(U|u)(N|n)(I|i)(O|o)(N|n)
UNIQUE=(U|u)(N|n)(I|i)(Q|q)(U|u)(E|e)
UPDATE=(U|u)(P|p)(D|d)(A|a)(T|t)(E|e)
VACUUM=(V|v)(A|a)(C|c)(U|u)(U|u)(M|m)
VALUES=(V|v)(A|a)(L|l)(U|u)(E|e)(S|s)
VIEW=(V|v)(I|i)(E|e)(W|w)
VIRTUAL=(V|v)(I|i)(R|r)(T|t)(U|u)(A|a)(L|l)
WITHOUT=(W|w)(I|i)(T|t)(H|h)(O|o)(U|u)(T|t)

%%
<YYINITIAL> {
  {WHITE_SPACE}          { return WHITE_SPACE; }

  ";"                    { return SEMI; }
  "="                    { return EQ; }
  "("                    { return LP; }
  ")"                    { return RP; }
  "."                    { return DOT; }
  ","                    { return COMMA; }
  "+"                    { return PLUS; }
  "-"                    { return MINUS; }
  "~"                    { return BITWISE_NOT; }
  ">>"                   { return SHIFT_RIGHT; }
  "<<"                   { return SHIFT_LEFT; }
  "<"                    { return LT; }
  ">"                    { return GT; }
  "<="                   { return LTE; }
  ">="                   { return GTE; }
  "=="                   { return EQ2; }
  "!="                   { return NEQ; }
  "<>"                   { return NEQ2; }
  "*"                    { return MULTIPLY; }
  "/"                    { return DIVIDE; }
  "%"                    { return MOD; }
  "&"                    { return BITWISE_AND; }
  "|"                    { return BITWISE_OR; }
  "||"                   { return CONCAT; }

  {ALL}                  { return ALL; }
  {AND}                  { return AND; }
  {ANY}                  { return ANY; }
  {AS}                   { return AS; }
  {ASC}                  { return ASC; }
  {BETWEEN}              { return BETWEEN; }
  {BY}                   { return BY; }
  {CAST}                 { return CAST; }
  {CATALOG}              { return CATALOG; }
  {CONVERT}              { return CONVERT; }
  {CURRENT_DATE}         { return CURRENT_DATE; }
  {CURRENT_TIMESTAMP}    { return CURRENT_TIMESTAMP; }
  {CURRENT_TIME}         { return CURRENT_TIME; }
  {DAY}                  { return DAY; }
  {DAYS}                 { return DAYS; }
  {DESC}                 { return DESC; }
  {DESCRIBE}             { return DESCRIBE; }
  {DISTINCT}             { return DISTINCT; }
  {ESCAPE}               { return ESCAPE; }
  {EXISTS}               { return EXISTS; }
  {EXPLAIN}              { return EXPLAIN; }
  {EXTRACT}              { return EXTRACT; }
  {FALSE}                { return FALSE; }
  {FIRST}                { return FIRST; }
  {FROM}                 { return FROM; }
  {FULL}                 { return FULL; }
  {GROUP}                { return GROUP; }
  {HAVING}               { return HAVING; }
  {HOUR}                 { return HOUR; }
  {HOURS}                { return HOURS; }
  {IN}                   { return IN; }
  {INNER}                { return INNER; }
  {INTERVAL}             { return INTERVAL; }
  {IS}                   { return IS; }
  {JOIN}                 { return JOIN; }
  {LEFT}                 { return LEFT; }
  {LIKE}                 { return LIKE; }
  {LIMIT}                { return LIMIT; }
  {MATCH}                { return MATCH; }
  {MINUTE}               { return MINUTE; }
  {MINUTES}              { return MINUTES; }
  {MONTH}                { return MONTH; }
  {NATURAL}              { return NATURAL; }
  {NOT}                  { return NOT; }
  {NULL}                 { return NULL; }
  {NULLS}                { return NULLS; }
  {ON}                   { return ON; }
  {OR}                   { return OR; }
  {ORDER}                { return ORDER; }
  {OUTER}                { return OUTER; }
  {RIGHT}                { return RIGHT; }
  {RLIKE}                { return RLIKE; }
  {QUERY}                { return QUERY; }
  {SECOND}               { return SECOND; }
  {SECONDS}              { return SECONDS; }
  {SELECT}               { return SELECT; }
  {SESSION}              { return SESSION; }
  {TABLE}                { return TABLE; }
  {TABLES}               { return TABLES; }
  {THEN}                 { return THEN; }
  {TO}                   { return TO; }
  {TOP}                  { return TOP; }
  {TRUE}                 { return TRUE; }
  {TYPE}                 { return TYPE; }
  {USING}                { return USING; }
  {WHEN}                 { return WHEN; }
  {WHERE}                { return WHERE; }
  {WITH}                 { return WITH; }
  {YEAR}                 { return YEAR; }
  {YEARS}                { return YEARS; }

  {SHOW}                 { return SHOW; }
  {COLUMNS}              { return COLUMNS; }
  {FUNCTIONS}            { return FUNCTIONS; }
  {PIVOT}                { return PIVOT;}

  {ABORT}                { return ABORT; }
  {ACTION}               { return ACTION; }
  {ADD}                  { return ADD; }
  {AFTER}                { return AFTER; }
  {ALTER}                { return ALTER; }
  {ANALYZE}              { return ANALYZE; }
  {ATTACH}               { return ATTACH; }
  {AUTOINCREMENT}        { return AUTOINCREMENT; }
  {BEFORE}               { return BEFORE; }
  {BEGIN}                { return BEGIN; }
  {CASCADE}              { return CASCADE; }
  {CASE}                 { return CASE; }
  {CHECK}                { return CHECK; }
  {COLLATE}              { return COLLATE; }
  {COLUMN}               { return COLUMN; }
  {COMMIT}               { return COMMIT; }
  {CONFLICT}             { return CONFLICT; }
  {CONSTRAINT}           { return CONSTRAINT; }
  {CREATE}               { return CREATE; }
  {CROSS}                { return CROSS; }
  {DATABASE}             { return DATABASE; }
  {DEFAULT}              { return DEFAULT; }
  {DEFERRABLE}           { return DEFERRABLE; }
  {DEFERRED}             { return DEFERRED; }
  {DELETE}               { return DELETE; }
  {DETACH}               { return DETACH; }
  {DO}                   { return DO; }
  {DROP}                 { return DROP; }
  {E}                    { return E; }
  {EACH}                 { return EACH; }
  {ELSE}                 { return ELSE; }
  {END}                  { return END; }
  {EXCEPT}               { return EXCEPT; }
  {EXCLUSIVE}            { return EXCLUSIVE; }
  {FAIL}                 { return FAIL; }
  {FOR}                  { return FOR; }
  {FOREIGN}              { return FOREIGN; }
  {GLOB}                 { return GLOB; }
  {IF}                   { return IF; }
  {IGNORE}               { return IGNORE; }
  {IMMEDIATE}            { return IMMEDIATE; }
  {INDEX}                { return INDEX; }
  {INDEXED}              { return INDEXED; }
  {INITIALLY}            { return INITIALLY; }
  {INSERT}               { return INSERT; }
  {INSTEAD}              { return INSTEAD; }
  {INTERSECT}            { return INTERSECT; }
  {INTO}                 { return INTO; }
  {ISNULL}               { return ISNULL; }
  {KEY}                  { return KEY; }
  {NO}                   { return NO; }
  {NOTHING}              { return NOTHING; }
  {NOTNULL}              { return NOTNULL; }
  {OF}                   { return OF; }
  {OFFSET}               { return OFFSET; }
  {PLAN}                 { return PLAN; }
  {PRAGMA}               { return PRAGMA; }
  {PRIMARY}              { return PRIMARY; }
  {RAISE}                { return RAISE; }
  {RECURSIVE}            { return RECURSIVE; }
  {REFERENCES}           { return REFERENCES; }
  {REGEXP}               { return REGEXP; }
  {REINDEX}              { return REINDEX; }
  {RELEASE}              { return RELEASE; }
  {RENAME}               { return RENAME; }
  {REPLACE}              { return REPLACE; }
  {RESTRICT}             { return RESTRICT; }
  {ROLLBACK}             { return ROLLBACK; }
  {ROW}                  { return ROW; }
  {ROWID}                { return ROWID; }
  {SAVEPOINT}            { return SAVEPOINT; }
  {SET}                  { return SET; }
  {TEMP}                 { return TEMP; }
  {TEMPORARY}            { return TEMPORARY; }
  {TRANSACTION}          { return TRANSACTION; }
  {TRIGGER}              { return TRIGGER; }
  {UNION}                { return UNION; }
  {UNIQUE}               { return UNIQUE; }
  {UPDATE}               { return UPDATE; }
  {VACUUM}               { return VACUUM; }
  {VALUES}               { return VALUES; }
  {VIEW}                 { return VIEW; }
  {VIRTUAL}              { return VIRTUAL; }
  {WITHOUT}              { return WITHOUT; }

  {SPACE}                { return SPACE; }
  {COMMENT}              { return COMMENT; }
  {JAVADOC}              { return JAVADOC; }
  {DIGIT}                { return DIGIT; }
  {ID}                   { return ID; }
  {STRING}               { return STRING; }
}

[^] { return BAD_CHARACTER; }
