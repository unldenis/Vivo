package com.github.unldenis.vivo.parsing.stmt;

import java.util.*;

public class Block extends Stmt {

    public final List<Stmt> statements;

    public Block(List<Stmt> statements) {
        this.statements = statements;
    }

    @Override
    public <R> R accept(StmtVisitor<R> visitor) {
        return visitor.visitBlockStmt(this);
    }
}
