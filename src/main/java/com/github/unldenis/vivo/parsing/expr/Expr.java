package com.github.unldenis.vivo.parsing.expr;

public abstract class Expr {

    public abstract <R> R accept(ExprVisitor<R> visitor);

}
