package io.github.celosia.sys.battle;

// Misc stuff for battles
public class BattleLib {
    public static BuffType getStageBuffType(int stacks) {
        return (stacks >= 0) ? BuffType.BUFF : BuffType.DEBUFF;
    }
}
