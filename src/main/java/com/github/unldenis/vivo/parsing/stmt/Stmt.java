package com.github.unldenis.vivo.parsing.stmt;


public abstract class Stmt {

    public abstract <R> R accept(StmtVisitor<R> visitor);

}
