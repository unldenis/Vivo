package com.github.unldenis.vivo.parsing.stmt;

import com.github.unldenis.vivo.parsing.expr.*;
import com.github.unldenis.vivo.scanning.*;

public class Var extends Stmt {

    public final Token name;
    public final Expr initializer;

    public Var(Token name, Expr initializer) {
        this.name = name;
        this.initializer = initializer;
    }


    @Override
    public <R> R accept(StmtVisitor<R> visitor) {
        return visitor.visitVarStmt(this);
    }
}
