package io.github.celosia.sys.battle;

import io.github.celosia.sys.util.ExtrapolatingArray;

public class AffLib {

    public static final ExtrapolatingArray DMG_DEALT = new ExtrapolatingArray(
            new int[] { 300, 500, 650, 800, 900, 1000, 1100, 1200, 1350, 1500, 1700 }, 5, 200, -200, true);
    public static final ExtrapolatingArray DMG_TAKEN = new ExtrapolatingArray(
            new int[] { 2500, 2000, 1700, 1400, 1200, 1000, 900, 800, 650, 500, 0 }, 5, 0, 500, true);
    public static final ExtrapolatingArray SP_COST = new ExtrapolatingArray(
            new int[] { 1700, 1500, 1300, 1200, 1100, 1000, 950, 900, 850, 800, 750 }, 5, -50, 200, true);
}
