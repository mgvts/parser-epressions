package expression;

import java.util.Objects;

public class Variable implements CommonExpression {
    private final String value;

    public Variable(String str) {
        this.value = str;
    }


    public int evaluate(int x) {
        return x;
    }


    public int evaluate(int x, int y, int z) {
        return switch (value) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> -1;
        };
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variable)) return false;
        Variable variable = (Variable) o;
        return value.equals(variable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

