package com.github.unldenis.vivo.parsing.expr;

import com.github.unldenis.vivo.scanning.*;

public class Assign extends Expr {

    public final Token name;
    public final Expr value;

    public Assign(Token name, Expr value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitAssignExpr(this);
    }
}
