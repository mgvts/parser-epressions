package expression.exceptions;

import expression.TripleExpression;


public class CheckedTzeros extends AbstractCheckedUnary {

    public CheckedTzeros(TripleExpression c1) {
        super(c1, "t0");
    }

    int action(int x) {
        return Integer.numberOfTrailingZeros(x);
    }
}