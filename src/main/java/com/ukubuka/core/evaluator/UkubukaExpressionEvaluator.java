package com.ukubuka.core.evaluator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.FileRecord;

/**
 * Ukubuka Expression Evaluator
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component
public class UkubukaExpressionEvaluator {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaExpressionEvaluator.class);

    /********************************* Dependency Injections ********************************/
    @Autowired
    private ExpressionParser expressionParser;

    /**
     * Evaluate
     * 
     * @return Evaluated Expression
     */
    public Object evaluate(final FileContents fileContents,
            final FileRecord fileRecord, final String expression) {
        /* Create Context */
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext(
                fileRecord);
        standardEvaluationContext.setVariable("aggregations",
                fileContents.getAggregations());

        /* Evaluate Expressions */
        LOGGER.info("Evaluating Expression: {}", expression);
        return expressionParser.parseExpression(expression)
                .getValue(standardEvaluationContext);
    }
}
