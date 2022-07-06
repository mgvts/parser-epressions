package expression;

public class Lzeros extends AbstractUnary{

    public Lzeros(CommonExpression c1) {
        super(c1, "l0");
    }

    int action(int x) {
        return Integer.numberOfLeadingZeros(x);
    }
}
