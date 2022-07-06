package expression.exceptions;

import expression.Const;
import expression.TripleExpression;
import expression.Variable;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;

public class ExpressionParser implements TripleParser {
    private String exp;
    private final String operators = "+-*/m";
    private final String unar = "-tl";
    private final String variable = "xyz";
    private final String allPosibleChars = "()1234567890+-*/mtlxyz";
    private int pos;
    int howopnen;


    public TripleExpression parse(String expression) {
//        System.err.println(expression);
        this.pos = 0;
        this.exp = expression;
        howopnen = 0;
        List<TripleExpression> value = new ArrayList<>();
        List<String> operations = new ArrayList<>();
        while (pos < exp.length()) {
            if (value.size() - operations.size() == 2 || value.size() - operations.size() == -1) {
                throw new IllegalArgumentException("bad with kolvo operations and value");
            }
            if (isDigit(exp.charAt(pos))) {
                value.add(parseConst(false));
                continue;
            }

            if (variable.contains(exp.charAt(pos) + "")) {
                value.add(parseVariable());
                continue;
            }

            if (exp.charAt(pos) == '-') {
                if (value.size() - 1 == operations.size()) {
                    operations.add(exp.charAt(pos) + "");
                    pos++;
                    continue;
                } else {
                    value.add(parseUnar());
                    continue;
                }
            }

            if (unar.contains(exp.charAt(pos) + "")) {
                value.add(parseUnar());
                continue;
            }

            if (operators.contains(exp.charAt(pos) + "")) {
                if (exp.charAt(pos) == 'm') {
                    switch (exp.substring(pos, pos + 3)) {
                        case "min" -> operations.add(exp.substring(pos, pos + 3));
                        case "max" -> operations.add(exp.substring(pos, pos + 3));
                        default -> throw new IllegalArgumentException("bad operation minMax");
                    }
                    if ((isWhitespace(exp.charAt(pos - 1)) || exp.charAt(pos - 1) == ')') &&
                            (isWhitespace(exp.charAt(pos + 3)) || exp.charAt(pos + 3) == '-' || exp.charAt(pos + 3) == '(')) {
                        pos += 3;
                        continue;
                    } else {
                        throw new IllegalArgumentException("bad _min_");
                    }
                } else {
                    operations.add(exp.charAt(pos++) + "");
                    continue;
                }
            }

            if (exp.charAt(pos) == '(') {
                pos++;
                value.add(parseBracket());
                continue;
            }

            if (!(allPosibleChars.contains(exp.charAt(pos) + "") || isWhitespace(exp.charAt(pos)))) {
                throw new IllegalArgumentException("unknown symbol");
            }

            if (exp.charAt(pos) == ')') {

                throw new IllegalArgumentException("bad )");
            }
            pos++;
            skipWhiteChars();
        }
        if (value.size() - operations.size() != 1) {
            throw new IllegalArgumentException("bad with kolvo operations and value");
        }
        // "*" "/"
        for (int i = 0; i < operations.size(); i++) {
            switch (operations.get(i)) {
                case "*" -> {
                    value.set(i, new CheckedMultiply(value.get(i), value.remove(i + 1)));
                    operations.remove(i);
                    i--;
                }
                case "/" -> {
                    value.set(i, new CheckedDivide(value.get(i), value.remove(i + 1)));
                    operations.remove(i);
                    i--;
                }
            }
        }
        // "+" "-"
        for (int i = 0; i < operations.size(); i++) {
            switch (operations.get(i)) {
                case "+" -> {
                    value.set(i, new CheckedAdd(value.get(i), value.remove(i + 1)));
                    operations.remove(i);
                    i--;
                }
                case "-" -> {
                    value.set(i, new CheckedSubtract(value.get(i), value.remove(i + 1)));
                    operations.remove(i);
                    i--;
                }
            }
        }
        // "min" "max"
        for (int i = 0; i < operations.size(); i++) {
            switch (operations.get(i)) {
                case "min" -> {
                    value.set(i, new CheckedMin(value.get(i), value.remove(i + 1)));
                    operations.remove(i);
                    i--;
                }
                case "max" -> {
                    value.set(i, new CheckedMax(value.get(i), value.remove(i + 1)));
                    operations.remove(i);
                    i--;
                }
            }
        }
//        System.err.println("pass");
        return value.get(0);
    }

    private TripleExpression parseUnar() {
        switch (exp.charAt(pos)) {
            case 'l': {
                if (exp.startsWith("l0", pos)) {
                    if (isWhitespace(exp.charAt(pos + 2)) ||
                            exp.charAt(pos + 2) == '-' ||
                            exp.charAt(pos + 2) == '(') {
                        pos += 2;
                        return new CheckedLzeros(parseElement());
                    } else {
                        throw new IllegalArgumentException("bad with \" \" in l0");
                    }
                } else {
                    throw new IllegalArgumentException("bad l0");
                }
            }
            case 't': {
                if (exp.startsWith("t0", pos)) {
                    if (isWhitespace(exp.charAt(pos + 2)) ||
                            exp.charAt(pos + 2) == '-' ||
                            exp.charAt(pos + 2) == '(') {
                        pos += 2;
                        return new CheckedTzeros(parseElement());
                    } else {
                        throw new IllegalArgumentException("bad with \" \" in t0");
                    }
                } else {
                    throw new IllegalArgumentException("bad l0");
                }
            }
            case '-': {
                int howManyNegative = 0;
                TripleExpression res;
                while (pos < exp.length() && exp.charAt(pos) == '-') {
                    howManyNegative++;
                    pos++;
                    skipWhiteChars();
                }
                skipWhiteChars();
                if (isDigit(exp.charAt(pos))) {
                    howManyNegative--;
                    res = parseConst(true);
                } else {
                    res = parseElement();
                }

                for (int i = 0; i < howManyNegative; i++) {
                    res = new CheckedNegate(res);
                }
                return res;
            }
            default:
                throw new IllegalArgumentException("bad Unar");
        }
    }

    private TripleExpression parseElement() {
        skipWhiteChars();
        if (isDigit(exp.charAt(pos))) {
            return parseConst(false);
        }

        if (variable.contains(exp.charAt(pos) + "")) {
            return parseVariable();
        }

        if (unar.contains(exp.charAt(pos) + "")) {
            return parseUnar();
        }

        if (exp.charAt(pos) == '(') {
            pos++;
            return parseBracket();
        }
        throw new IllegalArgumentException("bad element");
    }

    private TripleExpression parseConst(Boolean isNegative) {
        StringBuilder num = new StringBuilder();
        while (pos < exp.length() && isDigit(exp.charAt(pos))) {
            num.append(exp.charAt(pos));
            pos++;
        }
        if (isNegative) {
            return new Const(Integer.parseInt("-" + num));
        } else {
            return new Const(Integer.parseInt("" + num));
        }
    }


    private TripleExpression parseVariable() {
        return new Variable(Character.toString(exp.charAt(pos++)));
    }

    private void skipWhiteChars() {
        while (pos < exp.length() && isWhitespace(exp.charAt(pos))) {
            pos++;
        }
    }

    private TripleExpression parseBracket() {
        List<TripleExpression> value1 = new ArrayList<>();
        List<String> operations1 = new ArrayList<>();
        while (true) {
            if (value1.size() - operations1.size() == 2 || value1.size() - operations1.size() == -1) {
                throw new IllegalArgumentException("bad with kolvo operations and value");
            }
            if (isDigit(exp.charAt(pos))) {
                value1.add(parseConst(false));
                continue;
            }

            if (variable.contains(exp.charAt(pos) + "")) {
                value1.add(parseVariable());
                continue;
            }

            if (exp.charAt(pos) == '-') {
                if (value1.size() - 1 == operations1.size()) {
                    operations1.add(exp.charAt(pos) + "");
                    pos++;
                    continue;
                } else {
                    value1.add(parseUnar());
                    continue;
                }
            }

            if (unar.contains(exp.charAt(pos) + "")) {
                value1.add(parseUnar());
                continue;
            }

            if (operators.contains(exp.charAt(pos) + "")) {
                if (exp.charAt(pos) == 'm') {
                    switch (exp.substring(pos, pos + 3)) {
                        case "min" -> operations1.add(exp.substring(pos, pos + 3));
                        case "max" -> operations1.add(exp.substring(pos, pos + 3));
                        default -> throw new IllegalArgumentException("bad operation minMax");
                    }

                    if ((isWhitespace(exp.charAt(pos - 1)) || exp.charAt(pos - 1) == ')') &&
                            (isWhitespace(exp.charAt(pos + 3)) || exp.charAt(pos + 3) == '-' || exp.charAt(pos + 3) == '(')) {
                        pos += 3;
                        continue;
                    } else {
                        throw new IllegalArgumentException("bad ?min?");
                    }
                } else {
                    operations1.add(exp.charAt(pos++) + "");
                    continue;
                }
            }

            if (exp.charAt(pos) == '(') {
                pos++;
                value1.add(parseBracket());
                continue;
            }

            if (exp.charAt(pos) == ')') {
                pos++;
                break;
            }
            if (!(allPosibleChars.contains(exp.charAt(pos) + "") || isWhitespace(exp.charAt(pos)))) {
                throw new IllegalArgumentException("unknown symbol");
            }
            pos++;
            skipWhiteChars();
        }
        if (value1.size() - operations1.size() != 1) {
            throw new IllegalArgumentException("bad with kolvo operations and value");
        }
        // "*" "/"
        for (int i = 0; i < operations1.size(); i++) {
            switch (operations1.get(i)) {
                case "*" -> {
                    value1.set(i, new CheckedMultiply(value1.get(i), value1.remove(i + 1)));
                    operations1.remove(i);
                    i--;
                }
                case "/" -> {
                    value1.set(i, new CheckedDivide(value1.get(i), value1.remove(i + 1)));
                    operations1.remove(i);
                    i--;
                }
            }
        }
        // "+" "-"
        for (int i = 0; i < operations1.size(); i++) {
            switch (operations1.get(i)) {
                case "+" -> {
                    value1.set(i, new CheckedAdd(value1.get(i), value1.remove(i + 1)));
                    operations1.remove(i);
                    i--;
                }
                case "-" -> {
                    value1.set(i, new CheckedSubtract(value1.get(i), value1.remove(i + 1)));
                    operations1.remove(i);
                    i--;
                }
            }
        }
        // "min" "max"
        for (int i = 0; i < operations1.size(); i++) {
            switch (operations1.get(i)) {
                case "min" -> {
                    value1.set(i, new CheckedMin(value1.get(i), value1.remove(i + 1)));
                    operations1.remove(i);
                    i--;
                }
                case "max" -> {
                    value1.set(i, new CheckedMax(value1.get(i), value1.remove(i + 1)));
                    operations1.remove(i);
                    i--;
                }
            }
        }
        return value1.get(0);
    }
}
