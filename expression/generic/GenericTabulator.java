//package expression.generic;
//
//import expression.generic.parsers.*;
//
//
//import java.util.Map;
//
//public class GenericTabulator implements Tabulator {
//    private final Map<String, Parser<?>> MODES = Map.of(
//            "i", new UncheckedIntegerParser(),
//            "u", new CheckedIntegerParser(),
//            "d", new DoubleParser(),
//            "bi", new BigIntegerParser()
////            "l", new LongParser(),
////            "s", new ShortParser()
//    );
//
//
//    @Override
//    public Object[][][] tabulate(String mode, String expression,
//                                 int x1, int x2,
//                                 int y1, int y2,
//                                 int z1, int z2) throws Exception {
//        if (MODES.containsKey(mode)) {
//            return makeTable(MODES.get(mode), expression, x1, x2, y1, y2, z1, z2);
//        } else {
//            throw new IllegalArgumentException("Unsupported operation mode");
//        }
//    }
//
//
//    private <T> Object[][][] makeTable(Parser<T> parser, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
//        ExpressionParser<T> parser = new ExpressionParser<>(parser);
//        GenericExpression<T> genericExpression = parser.parse(expression);
//        Object[][][] ans = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
//        for (int x = 0; x <= x2 - x1; x++) {
//            for (int y = 0; y <= y2 - y1; y++) {
//                for (int z = 0; z <= z2 - z1; z++) {
//                    try {
//                        ans[x][y][z] = genericExpression.evaluate(parser.parse(x + x1), parser.parse(y + y1), parser.parse(z + z1));
//                    } catch (ExpressionException ignored) {
//                    }
//                }
//            }
//        }
//        return ans;
//    }
//}
