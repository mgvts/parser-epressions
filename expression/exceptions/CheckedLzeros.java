package expression.exceptions;

import expression.TripleExpression;


public class CheckedLzeros extends AbstractCheckedUnary {

    public CheckedLzeros(TripleExpression c1) {
        super(c1, "l0");
    }

    int action(int x) {
        return Integer.numberOfLeadingZeros(x);
    }
}