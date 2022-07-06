package expression.exceptions;

import expression.TripleExpression;


public class CheckedMax extends AbstractCheckedOperations {
    public CheckedMax(TripleExpression c1, TripleExpression c2) {
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