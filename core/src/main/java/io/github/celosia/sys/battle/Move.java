package io.github.celosia.sys.battle;

import static io.github.celosia.sys.battle.PosLib.getHeight;
import static io.github.celosia.sys.battle.PosLib.getRelativeSide;

public record Move(SkillInstance skillInstance, Unit self, int targetPos) {

    public boolean isInRange() {
        Range range = skillInstance.getSkill().getRange();

        // Check for disallowed self-targeting
        if ((!range.canTargetSelf()) && (targetPos == self.getPos())) {
            return false;
        }

        // Check if target is within vertical range
        if (Math.abs(getHeight(self.getPos()) - getHeight(targetPos)) >
                range.getRangeVertical() + self.getMod(Mod.RANGE)) {
            return false;
        }

        // Check if the targeted side is allowed
        return range.getSide() == Side.BOTH || range.getSide() == getRelativeSide(self.getPos(), targetPos);
    }
}
