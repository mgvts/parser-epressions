package expression;


public class Add extends AbstractOperations {
    public Add(CommonExpression c1, CommonExpression c2) {
        super(c1, c2, "+");
    }


    public int action(int x, int y) {
        return x + y;
    }
}
