package expression.exceptions;


import expression.TripleExpression;

public class CheckedMin extends AbstractCheckedOperations {
    public CheckedMin(TripleExpression c1, TripleExpression c2) {
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