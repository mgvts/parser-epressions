package expression;


import java.util.Objects;

public abstract class AbstractOperations implements CommonExpression {
    private String expression;
    protected CommonExpression c1, c2;

    abstract int action(int x, int y);


    public AbstractOperations(CommonExpression c1, CommonExpression c2, String symbol) {
        this.c1 = c1;
        this.c2 = c2;
        expression = "(" + c1.toString() + " " + symbol + " " + c2.toString() + ")";
    }


    public int evaluate(int x, int y, int z) {
        return action(c1.evaluate(x, y, z), c2.evaluate(x, y, z));
    }


    public int evaluate(int x) {
        return action(c1.evaluate(x), c2.evaluate(x));
    }


    @Override
    public String toString() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractOperations)) return false;
        AbstractOperations that = (AbstractOperations) o;
        return expression.equals(that.expression) && c1.equals(that.c1) && c2.equals(that.c2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expression, c1, c2);
    }
}

