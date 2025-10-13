package io.github.celosia.sys.battle;

public class BattleLib {

    // Basic stats are all multiplied by these numbers
    // Hidden mult multiplies them in secret while displaying unmultiplied to the player
    public static final int STAT_MULT_HIDDEN = 1;
    public static final int STAT_MULT_VISIBLE = 10;

    // Unique error key in case someone mods in a new StageType
    public static final int STAGE_TYPE_NOT_FOUND = Integer.MIN_VALUE;

    public static BuffType getStageBuffType(int stacks) {
        return (stacks >= 0) ? BuffType.BUFF : BuffType.DEBUFF;
    }
}
