/*
 * https://github.com/antlr/grammars-v4
 * http://bkiers.blogspot.com/2011/03/5-building-ast.html
 * https://wincent.com/wiki/ANTLR_lexers_in_depth
 * https://theantlrguy.atlassian.net/wiki/display/ANTLR4/Wildcard+Operator+and+Nongreedy+Subrules
 * https://theantlrguy.atlassian.net/wiki/display/ANTLR4/Lexer+Rules
 * http://www.gregbugaj.com/?p=251
 */
grammar BraceExpansion;

root : ot ( brace ot )* ;
ot: ( ESC | CM | T )* ;

brace : LB sub ( CM sub )* RB ;

sub : str ( brace str )* ;

str: ( ESC | T )* ;

ESC: '\\\\' | '\\' . ;

T : ~[{},\\] ;
LB : '{' ;
RB : '}' ;
CM : ',' ;
