package io.github.celosia.sys.battle;

// Misc stuff for battles
public class BattleLib {
    public static final int STAT_FACTOR = 10000;

    public static BuffType getStageBuffType(int stacks) {
        return (stacks >= 0) ? BuffType.BUFF : BuffType.DEBUFF;
    }

    // Returns the index a skill should start at based off of its role
    // todo more complex logic
    public static int getStartingIndex(Skill skill) {
        return (skill.shouldTargetOpponent()) ? 4 : 0;
    }
}
