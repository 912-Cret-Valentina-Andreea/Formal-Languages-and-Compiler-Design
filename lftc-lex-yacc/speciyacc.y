%{
#include <stdio.h>
#include <stdlib.h>
%}

%token START
%token DONE
%token CHAR
%token NUMBER
%token IF
%token THEN
%token ELSE
%token WHILE
%token FOR
%token READ
%token WRITE

%token CONST
%token ID

%token COLON
%token SEMI_COLON
%token DOT
%token COMMA
%token CURLY_OPEN_BRACKET
%token CURLY_CLOSE_BRACKET
%token ROUND_OPEN_BRACKET
%token ROUND_CLOSE_BRACKET
%token RIGHT_OPEN_BRACKET
%token RIGHT_CLOSE_BRACKET

%token AND
%token OR
%token PLUS
%token MINUS
%token MUL
%token DIV
%token LT
%token LE
%token GT
%token GE
%token NE
%token EQ
%token ATTRIB
%token NOT

%%

program: START cmpdstmt DONE
       ;
cmpdstmt: CURLY_OPEN_BRACKET stmtlist CURLY_CLOSE_BRACKET
	;
stmtlist: stmt | stmt SEMI_COLON stmtlist
	;
stmt: simplestmt | structstmt | iostmt
    ;
simplestmt: ROUND_OPEN_BRACKET assignstmt ROUND_CLOSE_BRACKET SEMI_COLON | ROUND_OPEN_BRACKET declaration ROUND_CLOSE_BRACKET SEMI_COLON
	  ;
assignstmt: ID ATTRIB expression
	  ;
structstmt: cmpdstmt | ifstmt | whilestmt | forstmt
	  ;
declaration: type ID SEMI_COLON
	   ;
expression: expression PLUS term | term
	  ;
ifstmt: IF condition THEN CURLY_OPEN_BRACKET stmtlist CURLY_CLOSE_BRACKET ELSE CURLY_OPEN_BRACKET stmtlist CURLY_CLOSE_BRACKET
      ;
whilestmt: WHILE condition CURLY_OPEN_BRACKET stmtlist CURLY_CLOSE_BRACKET
	 ;
forstmt: FOR forheader CURLY_OPEN_BRACKET stmtlist CURLY_CLOSE_BRACKET
       ;
type: simpletype | arraydecl
    ;
term: term MUL factor | factor
    ;
condition: ROUND_OPEN_BRACKET expression relation expression ROUND_CLOSE_BRACKET 
	 ;
forheader: ROUND_OPEN_BRACKET NUMBER assignstmt SEMI_COLON condition SEMI_COLON assignstmt ROUND_CLOSE_BRACKET
	 ;
simpletype: CHAR | NUMBER
	  ;
arraydecl: simpletype RIGHT_OPEN_BRACKET CONST RIGHT_CLOSE_BRACKET SEMI_COLON
	 ;
factor: ROUND_OPEN_BRACKET expression ROUND_CLOSE_BRACKET | ID | CONST
      ;
relation: LT | LE | EQ | NE | GT | GE
	;
whilestmt: WHILE ROUND_OPEN_BRACKET condition ROUND_CLOSE_BRACKET CURLY_OPEN_BRACKET stmtlist CURLY_CLOSE_BRACKET
	 ;
iostmt: READ ROUND_OPEN_BRACKET ID ROUND_CLOSE_BRACKET SEMI_COLON | WRITE ROUND_OPEN_BRACKET ID ROUND_CLOSE_BRACKET SEMI_COLON
      ;

%%

int main(int argc, char **argv)
{
	if(argc>1) 
		yyin =  fopen(argv[1],"r");
	yyparse();
}



