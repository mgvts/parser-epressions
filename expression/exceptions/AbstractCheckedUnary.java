package expression.exceptions;



import expression.TripleExpression;

import java.util.Objects;

public abstract class AbstractCheckedUnary implements TripleExpression {
    private String expression;
    protected TripleExpression c1;

    abstract int action(int x);


    public AbstractCheckedUnary (TripleExpression c1, String symbol) {
        this.c1 = c1;
        expression = symbol + "(" + c1.toString() + ")";
    }


    public int evaluate(int x, int y, int z) {
        return action(c1.evaluate(x, y, z));
    }



    @Override
    public String toString() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractCheckedUnary)) return false;
        AbstractCheckedUnary that = (AbstractCheckedUnary) o;
        return c1.equals(that.c1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c1);
    }
}
