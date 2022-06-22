package com.github.unldenis.vivo.parsing.expr;

import com.github.unldenis.vivo.scanning.*;

public class Logical extends Expr {

    public final Expr left;
    public final Token operator;
    public final Expr right;

    public Logical(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitLogicalExpr(this);
    }
}
