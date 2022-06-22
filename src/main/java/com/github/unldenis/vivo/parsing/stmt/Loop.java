package com.github.unldenis.vivo.parsing.stmt;

import com.github.unldenis.vivo.parsing.expr.*;

public class Loop extends Stmt {

    public final Expr incrementCondition;
    public final Stmt body;

    public Loop(Expr incrementCondition, Stmt body) {
        this.incrementCondition = incrementCondition;
        this.body = body;
    }


    @Override
    public <R> R accept(StmtVisitor<R> visitor) {
        return visitor.visitLoopStmt(this);
    }
}
