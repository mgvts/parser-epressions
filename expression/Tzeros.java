package expression;

public class Tzeros extends AbstractUnary{

    public Tzeros(CommonExpression c1) {
        super(c1, "t0");
    }

    int action(int x) {
        return Integer.numberOfTrailingZeros(x);
    }
}