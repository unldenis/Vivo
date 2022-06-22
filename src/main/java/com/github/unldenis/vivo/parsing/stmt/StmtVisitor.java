package com.github.unldenis.vivo.parsing.stmt;



public interface StmtVisitor<R> {

    R visitExpressionStmt(Expression stmt);
    R visitPrintStmt(Print stmt);
    R visitVarStmt(Var stmt);
    R visitBlockStmt(Block stmt);
    R visitIfStmt(If stmt);
    R visitWhileStmt(While stmt);
    R visitLoopStmt(Loop stmt);
    R visitFunctionStmt(Function stmt);
    R visitReturnStmt(Return stmt);
}
