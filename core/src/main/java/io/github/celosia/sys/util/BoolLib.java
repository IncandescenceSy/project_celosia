package io.github.celosia.sys.util;

// Treats true == >0; should technically be true == !0, but this is sufficient for my purposes
public class BoolLib {

    public static boolean isBothTruthy(int a, int b) {
        return a >= 1 && b >= 1;
    }

    public static int bool2Int(boolean a) {
        return a ? 1 : 0;
    }

    public static int bool2Sign(boolean a) {
        return a ? 1 : -1;
    }
}
