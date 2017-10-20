package com.ukubuka.core.evaluator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.ukubuka.core.model.FileRecord;

/**
 * Ukubuka Expression Evaluator Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaExpressionEvaluatorTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private ExpressionParser expressionParser;

    @Mock
    private Expression expression;

    @InjectMocks
    private UkubukaExpressionEvaluator ukubukaExpressionEvaluator;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_evaluate_success() {
        Mockito.when(expressionParser.parseExpression(Mockito.anyString()))
                .thenReturn(expression);
        Mockito.when(
                expression.getValue(Mockito
                        .any(StandardEvaluationContext.class))).thenReturn(
                new Object());

        ukubukaExpressionEvaluator.evaluate(new FileRecord(), "fooBar");

        Mockito.verify(expressionParser, Mockito.times(1)).parseExpression(
                Mockito.anyString());
        Mockito.verify(expression, Mockito.times(1)).getValue(
                Mockito.any(StandardEvaluationContext.class));
    }

    @Test(expected = ParseException.class)
    public void test_evaluate_parse_exception() {
        Mockito.when(expressionParser.parseExpression(Mockito.anyString()))
                .thenThrow(new ParseException(0, "foo"));

        ukubukaExpressionEvaluator.evaluate(new FileRecord(), "fooBar");
    }

    @Test(expected = EvaluationException.class)
    public void test_evaluate_evaluation_exception() {
        Mockito.when(expressionParser.parseExpression(Mockito.anyString()))
                .thenReturn(expression);
        Mockito.when(
                expression.getValue(Mockito
                        .any(StandardEvaluationContext.class))).thenThrow(
                new EvaluationException("bar"));

        ukubukaExpressionEvaluator.evaluate(new FileRecord(), "fooBar");
    }
}
