/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smb.erp.util;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 *
 * @author FatemaLaptop
 */
public class EvaluateExpression {

    public static double evaluateExpression(Object bean, String expression) {
        ExpressionParser parser = new SpelExpressionParser();

        StandardEvaluationContext itemContext = new StandardEvaluationContext(bean);
        Expression exp = parser.parseExpression(expression);

        return exp.getValue(itemContext, Double.class);
    }
}
