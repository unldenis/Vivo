package com.github.unldenis.vivo.parsing.expr;

import com.github.unldenis.vivo.scanning.*;

public class Variable extends Expr {

    public final Token name;

    public Variable(Token name) {
        this.name = name;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitVariableExpr(this);
    }
}
