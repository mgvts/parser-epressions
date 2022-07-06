package expression;

public class UnaryMinus extends AbstractUnary {
    public UnaryMinus(CommonExpression c1) {
        super(c1, "-");
    }

    public int action(int x) {
        return -x;
    }
}
