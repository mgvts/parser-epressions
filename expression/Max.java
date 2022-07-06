package expression;

public class Max extends AbstractOperations {
    public Max(CommonExpression c1, CommonExpression c2) {
        super(c1, c2, "max");
    }

    public int action(int x, int y) {
        if (x >= y) {
            return x;
        } else {
            return y;
        }
    }
}


