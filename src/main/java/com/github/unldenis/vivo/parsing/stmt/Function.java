package com.github.unldenis.vivo.parsing.stmt;

import com.github.unldenis.vivo.scanning.*;

import java.util.*;

public class Function extends Stmt {

    public final Token name;
    public final List<Token> params;
    public final List<Stmt> body;

    public Function(Token name, List<Token> params, List<Stmt> body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    public <R> R accept(StmtVisitor<R> visitor) {
        return visitor.visitFunctionStmt(this);
    }
}
