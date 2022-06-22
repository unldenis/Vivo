package com.github.unldenis.vivo.parsing.expr;

import com.github.unldenis.vivo.scanning.*;

import java.util.*;

public class Call extends Expr {

    public final Expr callee;
    public final Token paren;
    public final List<Expr> arguments;

    public Call(Expr callee, Token paren, List<Expr> arguments) {
        this.callee = callee;
        this.paren = paren;
        this.arguments = arguments;
    }

    @Override
    public <R> R accept(ExprVisitor<R> visitor) {
        return visitor.visitCallExpr(this);
    }
}
