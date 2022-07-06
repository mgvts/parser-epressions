package expression;

public class Divide extends AbstractOperations {
    public Divide(CommonExpression c1, CommonExpression c2) {
        super(c1, c2, "/");
    }

    public int action(int x, int y) {
        return x / y;
    }
}
