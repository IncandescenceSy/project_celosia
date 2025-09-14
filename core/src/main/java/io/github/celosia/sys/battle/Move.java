package io.github.celosia.sys.battle;

import static io.github.celosia.sys.battle.PosLib.getHeight;
import static io.github.celosia.sys.battle.PosLib.getRelativeSide;

// A skill with self and target attached
public record Move(Skill skill, Unit self, int targetPos) {

	public boolean isValid() {
		// Check for disallowed self-targeting
		if ((!skill.getRange().isCanTargetSelf()) && (targetPos == self.getPos()))
			return false;

		// Check if target is within vertical range
		if (Math.abs(getHeight(self.getPos()) - getHeight(targetPos)) > skill.getRange().getRangeVertical()
				+ self.getModRange())
			return false;

		// Check if the targeted side is allowed
		return skill.getRange().getSide() == Side.BOTH
				|| skill.getRange().getSide() == getRelativeSide(self.getPos(), targetPos);
	}

}
