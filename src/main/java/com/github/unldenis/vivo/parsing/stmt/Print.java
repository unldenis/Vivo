package com.github.unldenis.vivo.parsing.stmt;

import com.github.unldenis.vivo.parsing.expr.*;

public class Print extends Stmt {

    public final Expr expression;

    public Print(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <R> R accept(StmtVisitor<R> visitor) {
        return visitor.visitPrintStmt(this);
    }
}
