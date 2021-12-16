package com.holelin.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Compile the script into a Expression instance.
        Expression exp = AviatorEvaluator.getInstance().compileScript("examples/hello.av");
        // Run the exprssion.
        exp.execute();
    }
}
