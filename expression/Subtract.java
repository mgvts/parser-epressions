package expression;

public class Subtract extends AbstractOperations {
    public Subtract(CommonExpression op1, CommonExpression op2) {
        super(op1, op2, "-");
    }


    public int action(int x, int y) {
        return x - y;
    }
}
