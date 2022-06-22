package com.github.unldenis.vivo;

import com.github.unldenis.vivo.parsing.*;
import com.github.unldenis.vivo.parsing.Compiler;
import com.github.unldenis.vivo.parsing.stmt.*;
import com.github.unldenis.vivo.scanning.*;
import com.github.unldenis.vivo.scanning.Scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Vivo {

    public static void main(String[] args) throws IOException {

        if (args.length > 1) {
            System.out.println("Usage: vivo [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }

    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (hadError) System.exit(65);
        //if (hadRuntimeError) System.exit(70);
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);

            hadError = false;
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        // Stop if there was a syntax error.
        if (hadError) return;

        Compiler compiler = new Compiler();
        final List<String> compiledSource = new ArrayList<>();
        for(Stmt stmt: statements) {
            compiledSource.add(compiler.print(stmt));
        }
        if(compiler.areFunctionsDefined()) {
            compiledSource.forEach(System.out::println);
        } else {
            compiler.printUndefinedFunctions();
        }

    }

    /*
        Error handling - Scanning
     */

    private static boolean hadError = false;

    public static void error(int line, int column, String message) {
        report(line, column, "", message);
    }

    private static void report(int line, int column, String where,
                               String message) {
        System.err.println(
                "[line " + line + ", column " + column+ "] Error" + where + ": " + message);
        hadError = true;
    }

    /*
        Error handling - Parsing
     */

    public static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, token.column, " at end", message);
        } else {
            report(token.line, token.column, " at '" + token.lexeme + "'", message);
        }
    }


    /*

        Error handling - Evaluating


    private static final Interpreter interpreter = new Interpreter();
    private static boolean hadRuntimeError = false;

    public static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
                "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }

     */
}