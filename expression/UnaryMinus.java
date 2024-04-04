package expression;

public class UnaryMinus implements MyExpression {
    private final MyExpression expression;

    public UnaryMinus(MyExpression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
//        System.out.println("Unar");
//        System.out.println(expression);
        return !expression.toString().equals("-2147483648") ?  "-" + expression.toString() : expression.toString() ;
    }

    public static MyExpression getUnaryMinus(MyExpression expression) {
        if (expression instanceof Const) {
            if (expression.toString() == "-2147483648") return new Const(expression.evaluate(0));
            return new Const(-(expression).evaluate(0));
        }
        return new UnaryMinus(expression);
    }

    @Override
    public int evaluate(int x) {
        return (-expression.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return (-expression.evaluate(x, y, z));
    }
}
