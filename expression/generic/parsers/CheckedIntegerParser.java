package expression.generic.parsers;

public class CheckedIntegerParser implements Parser<Integer> {
    @Override
    public Integer add(Integer x, Integer y) {
        if (x > 0 && y > 0 && x + y < 0) {
            throw new ArithmeticException("overflow");
        } else if (x < 0 && y < 0 && x + y >= 0) {
            throw new ArithmeticException("overflow");
        }
        return x + y;
    }

    @Override
    public Integer subtract(Integer x, Integer y) {
        if (x <= 0 && y > 0 && x - y > 0) {
            throw new ArithmeticException("overflow");
        } else if(x >= 0 && y <= 0 && x - y < 0){
            throw new ArithmeticException("overflow");
        }
        return x - y;
    }

    @Override
    public Integer multiply(Integer x, Integer y) {
        int res = x * y;
        if (x > 0 && y > 0 && res <= 0) {
            throw new ArithmeticException("overflow");
        } else if (x < 0 && y < 0 && res <= 0) {
            throw new ArithmeticException("overflow");
        } else if (x != 0 && y != 0 && !(res / x == y || res / y == x)) {
            throw new ArithmeticException("overflow");
        }

        return x * y;
    }

    @Override
    public Integer divide(Integer x, Integer y) {
        if (y == 0) {
            throw new ArithmeticException("Zero Division error");
        } else if (x < 0 && y < 0 && x / y < 0) {
            throw new ArithmeticException("overflow");
        }
        return x / y;
    }

    @Override
    public Integer min(Integer a, Integer b) {
        return Integer.min(a, b);
    }

    @Override
    public Integer max(Integer a, Integer b) {
        return Integer.max(a, b);
    }

    @Override
    public Integer negate(Integer x) {
        if (x == Integer.MIN_VALUE){
            throw new ArithmeticException("overflow");
        }
        return -x;
    }

    @Override
    public Integer count(Integer x) {
        return Long.bitCount(Double.doubleToLongBits(x));
    }

    @Override
    public Integer parse(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public Integer parse(int value) {
        return value;
    }
}