package expression;

import java.util.Objects;

public class Const implements CommonExpression {
    private int value;

//  public Const(Number c){this.value = c}
    public Const(int c) {
        this.value = c;
    }

    public Integer getValue() {
        return value;
    }


    public int evaluate(int x) {
        return value;
    }


    public int evaluate(int x, int y, int z) {
        return value;
    }


    @Override
    public String toString() {
        return Integer.toString(value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Const)) return false;
        Const aConst = (Const) o;
        return value == aConst.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
