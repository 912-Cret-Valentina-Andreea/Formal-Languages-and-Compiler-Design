/* A Bison parser, made by GNU Bison 3.5.1.  */

/* Bison interface for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015, 2018-2020 Free Software Foundation,
   Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* Undocumented macros, especially those whose name start with YY_,
   are private implementation details.  Do not rely on them.  */

#ifndef YY_YY_Y_TAB_H_INCLUDED
# define YY_YY_Y_TAB_H_INCLUDED
/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token type.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
    START = 258,
    DONE = 259,
    CHAR = 260,
    NUMBER = 261,
    IF = 262,
    THEN = 263,
    ELSE = 264,
    WHILE = 265,
    FOR = 266,
    READ = 267,
    WRITE = 268,
    CONST = 269,
    ID = 270,
    COLON = 271,
    SEMI_COLON = 272,
    DOT = 273,
    COMMA = 274,
    CURLY_OPEN_BRACKET = 275,
    CURLY_CLOSE_BRACKET = 276,
    ROUND_OPEN_BRACKET = 277,
    ROUND_CLOSE_BRACKET = 278,
    RIGHT_OPEN_BRACKET = 279,
    RIGHT_CLOSE_BRACKET = 280,
    AND = 281,
    OR = 282,
    PLUS = 283,
    MINUS = 284,
    MUL = 285,
    DIV = 286,
    LT = 287,
    LE = 288,
    GT = 289,
    GE = 290,
    NE = 291,
    EQ = 292,
    ATTRIB = 293,
    NOT = 294
  };
#endif
/* Tokens.  */
#define START 258
#define DONE 259
#define CHAR 260
#define NUMBER 261
#define IF 262
#define THEN 263
#define ELSE 264
#define WHILE 265
#define FOR 266
#define READ 267
#define WRITE 268
#define CONST 269
#define ID 270
#define COLON 271
#define SEMI_COLON 272
#define DOT 273
#define COMMA 274
#define CURLY_OPEN_BRACKET 275
#define CURLY_CLOSE_BRACKET 276
#define ROUND_OPEN_BRACKET 277
#define ROUND_CLOSE_BRACKET 278
#define RIGHT_OPEN_BRACKET 279
#define RIGHT_CLOSE_BRACKET 280
#define AND 281
#define OR 282
#define PLUS 283
#define MINUS 284
#define MUL 285
#define DIV 286
#define LT 287
#define LE 288
#define GT 289
#define GE 290
#define NE 291
#define EQ 292
#define ATTRIB 293
#define NOT 294

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;

int yyparse (void);

#endif /* !YY_YY_Y_TAB_H_INCLUDED  */
