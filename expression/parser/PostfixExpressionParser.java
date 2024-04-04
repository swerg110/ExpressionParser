package expression.parser;

import expression.*;

import java.util.Stack;

public class PostfixExpressionParser extends BaseParser {
    private CharSource source;
    private static final char END = '\0';
    private final MyExpression VAR = new Variable("#");
    private char ch = 0xffff;

    private boolean hasMinValue = false;
    private int countUnarMinus = 0;

    public MyExpression parse(PostStringSource postStringSource) {

        setSource(postStringSource);
        setOperationPriority();
//        System.out.println(source.getData());
        return parsePostfixExpression();
    }

    private MyExpression parsePostfixExpression() {
        Stack<MyExpression> stack = new Stack<>();

//        System.out.println("Мы в методе parsePostfixExpression()!");
//        System.out.println(source.getData());

        while (source.hasNext()) {
//            System.out.println(stack);
//            source.skipWhitespace();

            if (between('0', '9')) {
                stack.push(new Const(getNum()));

            } else if (operationPriority.containsKey(ch)) {

                if (ch == '~') {

                    MyExpression prevSymb = !stack.isEmpty() ? stack.pop() : VAR;
                    stack.push(executeOperation('~', VAR, prevSymb));

                    take();
                    continue;
                }

                MyExpression secondSymb = !stack.isEmpty() ? stack.pop() : VAR;
                MyExpression firstSymb = !stack.isEmpty() ? stack.pop() : VAR;

                if (firstSymb.equals(VAR)) {
                    stack.push(executeOperation('~', VAR, secondSymb));
                } else {
                    stack.push(executeOperation(ch, firstSymb, secondSymb));
                }


            } else if (Character.isLetter(ch)) {
                stack.push(new Variable(String.valueOf(ch)));
            }

            take();
//            System.out.println(stack);
        }

        return stack.pop();
    }


    protected int getNum() {
        StringBuilder result = new StringBuilder();

        while (between('0', '9')) {
            result.append(take());
        }

        if (result.toString().equals("2147483648")) {
            hasMinValue = true;
            return Integer.MIN_VALUE;
        }
        return Integer.parseInt(result.toString());

    }


    @Override
    protected void setSource(CharSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }


    private MyExpression executeOperation(char operation, MyExpression first, MyExpression second) {
        return switch (operation) {
            case '+' -> new Add(first, second);
            case '-' -> new Subtract(first, second);
            case '*' -> new Multiply(first, second);
            case '/' -> new Divide(first, second);
            case '~' -> new UnaryMinus(first).getUnaryMinus(second);

            default -> throw error("Impossible");
        };
    }
}
