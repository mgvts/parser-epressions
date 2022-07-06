package expression.exceptions;

import expression.TripleExpression;

public class CheckedDivide extends AbstractCheckedOperations {
    public CheckedDivide(TripleExpression c1, TripleExpression c2) {
        super(c1, c2, "/");
    }

    @Override
    public int action(int x, int y) {
        if (y == 0) {
            throw new ArithmeticException("Zero Division error");
        } else if (x < 0 && y < 0 && x / y < 0) {
            throw new ArithmeticException("overflow");
        }
        return x / y;
    }
}
