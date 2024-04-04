package expression.parser;

import expression.TripleExpression;
import java.util.Scanner;
//  (-2147483648 / 4)


public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        ExpressionParser parser = new ExpressionParser();

        String st = in.nextLine();
        TripleExpression expression = parser.parse(st);

//        System.err.println(st);
        System.out.println("PARSE RESULT:");
        System.out.println(expression.evaluate(1,1,1));
        System.out.println(expression.toString());

    }
}
