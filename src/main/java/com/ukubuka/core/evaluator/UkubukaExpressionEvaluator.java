package com.ukubuka.core.evaluator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.ukubuka.core.model.FileRecord;

/**
 * Ukubuka Expression Evaluator
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component
public class UkubukaExpressionEvaluator {

    /************************* Dependency Injections *************************/
    @Autowired
    private ExpressionParser expressionParser;

    /**
     * Evaluate
     * 
     * @return Evaluated Expression
     */
    public Object evaluate(final FileRecord fileRecord, final String expression) {
        return expressionParser.parseExpression(
                expression.substring(1, expression.length() - 1)).getValue(
                new StandardEvaluationContext(fileRecord));
    }
}
