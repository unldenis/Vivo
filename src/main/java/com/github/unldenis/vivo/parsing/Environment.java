package com.github.unldenis.vivo.parsing;

import com.github.unldenis.vivo.scanning.*;

import java.util.*;

public class Environment {

    private final List<String> values = new ArrayList<>();


    public void define(String name) {
        values.add(name);
    }

    public boolean hasToken(Token name) {
        return values.contains(name.lexeme);
    }

}

