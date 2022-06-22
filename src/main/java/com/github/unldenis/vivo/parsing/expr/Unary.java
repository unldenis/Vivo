package com.github.unldenis.vivo.parsing.expr;

import com.github.unldenis.vivo.scanning.*;

public class Unary extends Expr {

    public final Token operator;
    public final Expr right;

    public Unary(Token operator, Expr right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitUnaryExpr(this);
    }
}
