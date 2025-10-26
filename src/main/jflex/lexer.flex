/* JFlex specification for TestLang++ */

package com.testlang.parser;

import java_cup.runtime.*;

%%

%public
%class Lexer
%unicode
%cup
%line
%column

%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline + 1, yycolumn + 1);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }

    public int getLine() {
        return yyline + 1;
    }

    public int getColumn() {
        return yycolumn + 1;
    }
%}

/* Regular expression definitions */
LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]
Comment = "//" [^\r\n]*

Letter = [A-Za-z_]
Digit = [0-9]
Identifier = {Letter}({Letter}|{Digit})*
Number = {Digit}+

/* String literals with escape sequences */
StringCharacter = [^\r\n\"\\]
EscapeSequence = \\[\"\\]
StringContent = ({StringCharacter}|{EscapeSequence})*

%%

/* Keywords */
"config"        { return symbol(sym.CONFIG); }
"base_url"      { return symbol(sym.BASE_URL); }
"header"        { return symbol(sym.HEADER); }
"let"           { return symbol(sym.LET); }
"test"          { return symbol(sym.TEST); }
"GET"           { return symbol(sym.GET); }
"POST"          { return symbol(sym.POST); }
"PUT"           { return symbol(sym.PUT); }
"DELETE"        { return symbol(sym.DELETE); }
"expect"        { return symbol(sym.EXPECT); }
"status"        { return symbol(sym.STATUS); }
"body"          { return symbol(sym.BODY); }
"contains"      { return symbol(sym.CONTAINS); }
"in"            { return symbol(sym.IN); }

/* Operators and delimiters */
"{"             { return symbol(sym.LBRACE); }
"}"             { return symbol(sym.RBRACE); }
";"             { return symbol(sym.SEMICOLON); }
"="             { return symbol(sym.EQUALS); }
".."            { return symbol(sym.DOTDOT); }

/* String literal */
\"{StringContent}\" {
    String str = yytext();
    // Remove surrounding quotes
    str = str.substring(1, str.length() - 1);
    // Process escape sequences
    str = str.replace("\\\"", "\"");
    str = str.replace("\\\\", "\\");
    return symbol(sym.STRING, str);
}

/* Triple-quoted string (optional extra) */
\"\"\"{StringContent}\"\"\" {
    String str = yytext();
    // Remove surrounding triple quotes
    str = str.substring(3, str.length() - 3);
    // Process escape sequences
    str = str.replace("\\\"", "\"");
    str = str.replace("\\\\", "\\");
    return symbol(sym.STRING, str);
}

/* Identifiers */
{Identifier}    { return symbol(sym.IDENTIFIER, yytext()); }

/* Numbers */
{Number}        { return symbol(sym.NUMBER, Integer.parseInt(yytext())); }

/* Whitespace */
{WhiteSpace}    { /* ignore */ }

/* Comments */
{Comment}       { /* ignore */ }

/* Error handling */
.               { 
    throw new Error("Line " + (yyline + 1) + ", Column " + (yycolumn + 1) + 
                    ": Illegal character <" + yytext() + ">"); 
}
