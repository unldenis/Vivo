package com.github.unldenis.vivo.scanning;

public class Token {
    /*
        Type
     */
    public final TokenType type;
    /*
        lexeme from scanning
     */
    public final String lexeme;
    /*
        literal is token parsed
     */
    public final Object literal;

    /*
        Error handling
     */
    public final int line;
    public final int column;

    public Token(TokenType type, String lexeme, Object literal, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", lexeme='" + lexeme + '\'' +
                ", literal=" + literal +
                ", line=" + line +
                ", column=" + column +
                '}';
    }
}
