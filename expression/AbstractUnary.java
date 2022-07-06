package expression;

import java.util.Objects;

public abstract class AbstractUnary implements CommonExpression {
    private String expression;
    protected CommonExpression c1;

    abstract int action(int x);


    public AbstractUnary(CommonExpression c1, String symbol) {
        this.c1 = c1;
        expression = symbol + "(" + c1.toString() + ")";
    }


    public int evaluate(int x, int y, int z) {
        return action(c1.evaluate(x, y, z));
    }


    public int evaluate(int x) {
        return action(c1.evaluate(x));
    }


    @Override
    public String toString() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractUnary)) return false;
        AbstractUnary that = (AbstractUnary) o;
        return expression.equals(that.expression) && c1.equals(that.c1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, c1);
    }
}
