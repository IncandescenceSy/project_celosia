package io.github.celosia.sys.battle;

// Misc stuff for battles
public class BattleLib {
	// Basic stats are all multiplied by these numbers
	// Hidden mult multiplies them in secret while displaying unmultiplied to the
	// player
	public static final int STAT_MULT_HIDDEN = 1;
	public static final int STAT_MULT_VISIBLE = 100;

	public static BuffType getStageBuffType(int stacks) {
		return (stacks >= 0) ? BuffType.BUFF : BuffType.DEBUFF;
	}

	// Returns the index a skill should start at based off of its role
	// todo more complex logic
	public static int getStartingIndex(Skill skill) {
		return (skill.shouldTargetOpponent()) ? 4 : 0;
	}
}
