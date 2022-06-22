package com.github.unldenis.vivo.parsing.stmt;

import com.github.unldenis.vivo.parsing.expr.*;

public class While extends Stmt {

    public final Expr condition;
    public final Stmt body;

    public While(Expr condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public <R> R accept(StmtVisitor<R> visitor) {
        return visitor.visitWhileStmt(this);
    }
}
