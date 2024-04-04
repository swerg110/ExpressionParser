package expression.parser;

import expression.*;
import expression.TripleExpression;


public class ExpressionParser extends BaseParser implements TripleParser{
    int count = 0;


    @Override
    public TripleExpression parse(String expression) {

        System.err.println(expression);
        System.err.println(count++);

        setSource(new StringSource(expression));
        setOperationPriority();

        return parseExpression();
    }

    private MyExpression parseExpression(){

        PostStringSource postfixStringSource = ToPostfixStringSource();
        PostfixExpressionParser parserResult = new PostfixExpressionParser();

        return parserResult.parse(postfixStringSource);
    }


}




