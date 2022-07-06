package expression;

public class Multiply extends AbstractOperations {
    public Multiply(CommonExpression op1, CommonExpression op2) {
        super(op1, op2, "*");
    }


    public int action(int x, int y) {
        return x * y;
    }

}
