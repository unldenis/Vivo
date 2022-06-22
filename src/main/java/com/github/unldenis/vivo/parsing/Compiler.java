package com.github.unldenis.vivo.parsing;

import com.github.unldenis.vivo.module.*;
import com.github.unldenis.vivo.parsing.expr.*;
import com.github.unldenis.vivo.parsing.stmt.*;
import com.github.unldenis.vivo.scanning.*;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;


public class Compiler implements ExprVisitor<String>, StmtVisitor<String> {

    private static final Map<String, VivoFunction> nativeFunctionMap = new HashMap<>();


    static {
        nativeFunctionMap.put("abs", arguments -> "Math.abs((double)" + arguments.get(0) + ")");
        nativeFunctionMap.put("sqrt", arguments -> "Math.sqrt((double)" + arguments.get(0) + ")");
    }



    private Environment environment = new Environment();
    private final AtomicInteger iteratorSuffix = new AtomicInteger(100);
    private final Set<String> functionsDefined = new HashSet<>();
    private final Set<String> functionsCalled = new HashSet<>();

    public String print(Stmt stmt) {
        return stmt.accept(this);
    }

    public boolean areFunctionsDefined() {
        functionsCalled.removeAll(functionsDefined);
        return functionsCalled.isEmpty();
    }

    public void printUndefinedFunctions() {
        for (String s : functionsCalled) {
            System.out.println("Function " + s + " not defined!");
        }
    }

    @Override
    public String visitExpressionStmt(Expression stmt) {
        return evaluate(stmt.expression) + ";";
    }

    @Override
    public String visitPrintStmt(Print stmt) {
        return "System.out.println(" + evaluate(stmt.expression) + ");";
    }

    @Override
    public String visitVarStmt(Var stmt) {
        String o = "Object " + stmt.name.lexeme + " = ";
        if (stmt.initializer != null) {
            o += evaluate(stmt.initializer);
        } else {
            o += "null";
        }
        o += ";";


        // define
        if(environment.hasToken(stmt.name)) {
            throw new RuntimeException("Variable '" + stmt.name.lexeme + "' already defined.");
        }
        environment.define(stmt.name.lexeme);
        return o;
    }

    @Override
    public String visitBlockStmt(Block stmt) {
        StringBuilder result = new StringBuilder("{");
        for (Stmt statement : stmt.statements) {
            result.append("\n")
                  .append(statement.accept(this));
        }
        result.append("\n}");
        return result.toString();
    }

    @Override
    public String visitIfStmt(If stmt) {
        StringBuilder result = new StringBuilder("if(");
        result.append(evaluate(stmt.condition));
        result.append(")\n");
        result.append(stmt.thenBranch.accept(this));
        result.append("\n");
        if(stmt.elseBranch != null) {
            result.append(" else\n");
            result.append(stmt.elseBranch.accept(this));
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public String visitWhileStmt(While stmt) {
        StringBuilder result = new StringBuilder("while(");
        result.append(evaluate(stmt.condition));
        result.append(")\n");
        result.append(stmt.body.accept(this));
        result.append("\n");
        return result.toString();
    }

    @Override
    public String visitLoopStmt(Loop stmt) {
        StringBuilder result = new StringBuilder();
        String variable = "i" + iteratorSuffix.getAndIncrement();
        result.append("int " + variable + " = 0;");
        result.append("\n");
        result.append("while(" + variable + " < (int)");
        result.append(evaluate(stmt.incrementCondition));
        result.append(")\n");
        result.append("{\n");
        result.append(stmt.body.accept(this));
        result.append("\n");
        result.append(variable + "++;");
        result.append("\n");
        result.append("}");

        return result.toString();
    }

    @Override
    public String visitFunctionStmt(Function stmt) {
        // custom function
        String functionName = stmt.name.lexeme;
        functionsDefined.add(functionName);

        if(stmt.name.lexeme.equals("onEnable") && stmt.params.isEmpty()) {
            return buildFunction(true, "onEnable", stmt);
        } else {
            return buildFunction(false, functionName, stmt);
        }
    }

    @Override
    public String visitReturnStmt(Return stmt) {
        String value = "";
        if(stmt.value != null) {
            value = evaluate(stmt.value);
        }
        return "return " + value + ";";
    }

    private String buildFunction(boolean isVoid, String name, Function stmt) {
        StringBuilder result = new StringBuilder();
        result.append("public "+ (isVoid ? "void" : "Object") + " " + name + "(");
        StringJoiner arguments = new StringJoiner(",");
        stmt.params.forEach(token -> arguments.add("Object " + token.lexeme));
        result.append(arguments);
        result.append(") {");

        if(!isVoid) {
            result.append("\nif(true) {");
            result.append("\n");
        }

        for(Stmt s: stmt.body) {
            result.append("\n");
            result.append(s.accept(this));
        }

        if(!isVoid) {
            result.append("\n");
            result.append("}");
            result.append("\nreturn null;");
        }

        result.append("\n");
        result.append("}");

        return result.toString();
    }

    /*
        Expression
     */

    @Override
    public String visitBinaryExpr(Binary expr) {
        String left = evaluate(expr.left);
        String right = evaluate(expr.right);
        switch (expr.operator.type) {
            case BANG_EQUAL:
                return "!Objects.equals(" + left + ", " + right + ")";
            case EQUAL_EQUAL:
                return "Objects.equals(" + left + ", " + right + ")";
            case PLUS:
                return "VivoUtils.sum(" + left +  ", " + right + " )";
            default:
                return "(double)" + evaluate(expr.left) + " " + expr.operator.lexeme + " (double)" + evaluate(expr.right);

        }
        // Unreachable.
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return "( " + evaluate(expr.expression) + " )";
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        if(expr.value instanceof String) {
            return "\"" + expr.value + "\"";
        }
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return expr.operator.lexeme + evaluate(expr.right);
    }

    @Override
    public String visitVariableExpr(Variable expr) {
        if(!environment.hasToken(expr.name)) {
            //System.err.println("Variable " + expr.name.lexeme + " doesn't exist");
        }
        return expr.name.lexeme;
    }

    @Override
    public String visitAssignExpr(Assign expr) {
        if(!environment.hasToken(expr.name)) {
            throw new RuntimeException("Undefined variable '" + expr.name.lexeme + "'.");
        }
        String value = evaluate(expr.value);
        return expr.name.lexeme + " = " + value;
    }

    @Override
    public String visitLogicalExpr(Logical expr) {
        String operator = expr.operator.type == TokenType.OR ? " || " : " && ";
        return evaluate(expr.left) + operator + evaluate(expr.right);
    }

    @Override
    public String visitCallExpr(Call expr) {
        StringBuilder result = new StringBuilder();

        List<String> params = expr
                .arguments
                .stream()
                .map(this::evaluate)
                .collect(Collectors.toList());
        String callee = evaluate(expr.callee);

        VivoFunction vivoFunction = nativeFunctionMap.get(callee);
        if(vivoFunction == null) {
            // custom function
            functionsCalled.add(callee);


            result.append(evaluate(expr.callee) + "(");
            StringJoiner arguments = new StringJoiner(",");
            params.forEach(arguments::add);
            result.append(arguments);
            result.append(")");
        } else {
            result.append(vivoFunction.execute(params));
        }

        return result.toString();
    }

    private String evaluate(Expr expr) {
        return expr.accept(this);
    }
}
