package expression;

public class Min extends AbstractOperations {
    public Min(CommonExpression c1, CommonExpression c2) {
        super(c1, c2, "min");
    }

    public int action(int x, int y) {
        if (x >= y) {
            return y;
        } else {
            return x;
        }
    }
}