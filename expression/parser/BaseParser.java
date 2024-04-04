package expression.parser;

import java.util.HashMap;
import java.util.Stack;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseParser {
    private static final char END = '\0';

    private boolean flag2Unary = false;
    private CharSource source;
    //Последний считанный значимый символ в строке.
    private char lastSymb = '#';
    HashMap<Character, Integer> operationPriority = new HashMap<>();
    private char ch = 0xffff;

    protected void setSource(CharSource source) {
        this.source = source;
//        System.out.println(source.getData());
//        take();
    }

    protected void setOperationPriority() {
        operationPriority.put('(', 0);
        operationPriority.put('+', 1);
        operationPriority.put('-', 1);
        operationPriority.put('*', 2);
        operationPriority.put('/', 2);
        operationPriority.put('^', 3);
        operationPriority.put('~', 4); // для переобозначения унарного минуса
    }

    protected PostStringSource ToPostfixStringSource() {
        StringBuilder postString = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        //Есть костыль

        while (source.hasNext()) {
//            System.out.println("====");
            skipWhitespace();
//            System.out.println(ch);
//            System.out.println(lastSymb);
//            System.out.println(postString);
//            System.out.println(stack);
//            System.out.println("------------------");
//            System.out.println(source.getPosIndex());-1
            if (between('0', '9')) {
                postString.append(getNumber()).append(" ");
                //костыль
                // '0' - тк нам важно смотреть на то, ЦИФРА ли последний символ
                lastSymb = '0';
                continue;

            } else if (ch == '(') {
                stack.push(ch);

            } else if (ch == ')') {

                while (!stack.empty() && stack.peek() != '(') {
                    postString.append(stack.pop());

                }
                stack.pop();

            }
            else if(operationPriority.containsKey(ch)){

                if(ch == '-' && (lastSymb == '(' || operationPriority.containsKey(lastSymb) || lastSymb == '\uFFFF') ){
                    ch = '~';
                }

                if (lastSymb == '~' && ch == '~') flag2Unary = true;
                else flag2Unary = false;

                while (!stack.empty() && operationPriority.get(stack.peek()) >= operationPriority.get(ch) && !flag2Unary ){
                    postString.append(stack.pop());
                }

                stack.push(ch);
            }
            else if(Character.isLetter(ch)){
                postString.append(ch);
            }

            lastSymb = take();
        }

        while (!stack.isEmpty()){
            postString.append(stack.pop());
        }

//        System.out.println(postString);
        return new PostStringSource(postString.toString());
    }

    protected StringBuilder getNumber() {
        StringBuilder result = new StringBuilder();
        while (between('0', '9')) {
            result.append(take());
        }
        return result;
    }

    public void skipWhitespace() {
        source.skipWhitespace();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected void expect(final String value) {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean eof() {
        return take(END);
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }



}
