package com.ukubuka.core.evaluator;

import java.util.List;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

/**
 * Ukubuka Expression Evaluator
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component
public class UkubukaExpressionEvaluator {

    public static void main(String[] args) {
        new UkubukaExpressionEvaluator().evaluate(null, null);
    }

    /**
     * Evaluate
     * 
     * @return Evaluated Expression
     */
    public String evaluate(final List<String> fileContents,
            final String expression) {
        ExpressionParser parser = new SpelExpressionParser();
        Expression ex = parser
                .parseExpression("T(java.lang.Math).log(100) + 100");
        Object o = ex.getValue();
        System.out.println(o);

        return null;
    }
}
