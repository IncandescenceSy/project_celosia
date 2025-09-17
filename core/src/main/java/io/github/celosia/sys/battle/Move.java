package io.github.celosia.sys.battle;

import static io.github.celosia.sys.battle.PosLib.getHeight;
import static io.github.celosia.sys.battle.PosLib.getRelativeSide;

// A skill with self and target attached
public record Move(SkillInstance skillInstance, Unit self, int targetPos) {
	public boolean isInRange() {
		// Range checks
		Range range = skillInstance.getSkill().getRange();

		// Check for disallowed self-targeting
		if ((!range.canTargetSelf()) && (targetPos == self.getPos())) {
			return false;
		}

		// Check if target is within vertical range
		if (Math.abs(getHeight(self.getPos()) - getHeight(targetPos)) > range.rangeVertical() + self.getModRange()) {
			return false;
		}

		// Check if the targeted side is allowed
		return range.side() == Side.BOTH || range.side() == getRelativeSide(self.getPos(), targetPos);
	}
}
