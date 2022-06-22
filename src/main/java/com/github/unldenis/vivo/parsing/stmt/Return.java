package com.github.unldenis.vivo.parsing.stmt;

import com.github.unldenis.vivo.parsing.expr.*;
import com.github.unldenis.vivo.scanning.*;

public class Return extends Stmt {

    public final Token keyword;
    public final Expr value;

    public Return(Token keyword, Expr value) {
        this.keyword = keyword;
        this.value = value;
    }

    @Override
    public <R> R accept(StmtVisitor<R> visitor) {
        return visitor.visitReturnStmt(this);
    }
}
