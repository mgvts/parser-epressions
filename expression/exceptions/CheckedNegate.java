package expression.exceptions;

import expression.TripleExpression;


public class CheckedNegate extends AbstractCheckedUnary {
    public CheckedNegate(TripleExpression c1) {
        super(c1, "-");
    }

    @Override
    public int action(int x) {
        if (x == Integer.MIN_VALUE){
            throw new ArithmeticException("overflow");
        }
        return -x;
    }
}
