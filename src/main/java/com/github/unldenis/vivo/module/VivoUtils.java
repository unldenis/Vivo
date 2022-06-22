package com.github.unldenis.vivo.module;

import java.math.*;

public class VivoUtils {

    public static Object sum(Object f, Object s) {
        if(f instanceof String || s instanceof String) {
            return f + "" + s;
        }
        if(f instanceof Number && s instanceof Number) {
            return new BigDecimal(f.toString()).add(new BigDecimal(s.toString())).doubleValue();
        }
        throw new RuntimeException("Vivo Error | Can't sum " + f + " with " + s);
    }


}
