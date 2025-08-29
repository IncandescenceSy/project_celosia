package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

import static io.github.celosia.sys.battle.AffLib.getAffMultDmgDealt;
import static io.github.celosia.sys.battle.AffLib.getAffMultDmgTaken;

public class Damage implements SkillEffect {

    private final SkillType type;
    private final Element element;
    private final int pow;
    private final ResultType minResult;
    private final boolean isPierce;
    private final boolean isInstant;
    private final boolean mainTargetOnly;
    private final boolean isFollowUp;

    public Damage(Builder builder) {
        type = builder.type;
        element = builder.element;
        pow = builder.pow;
        minResult = builder.minResult;
        isPierce = builder.isPierce;
        isInstant = builder.isInstant;
        mainTargetOnly = builder.mainTargetOnly;
        isFollowUp = builder.isFollowUp;
    }

    public static class Builder {
        private final SkillType type;
        private final Element element;
        private final int pow;
        private ResultType minResult = ResultType.HIT_SHIELD;
        private boolean isPierce = false;
        private boolean isInstant = false;
        private boolean mainTargetOnly = false;
        private boolean isFollowUp = false;

        public Builder(SkillType type, Element element, int pow) {
            this.type = type;
            this.element = element;
            this.pow = pow;
        }

        public Builder minResult(ResultType minResult) {
            this.minResult = minResult;
            return this;
        }

        public Builder pierce() {
            this.isPierce = true;
            return this;
        }

        public Builder instant() {
            this.isInstant = true;
            return this;
        }

        public Builder mainTargetOnly() {
            this.mainTargetOnly = true;
            return this;
        }

        public Builder followUp() {
            this.isFollowUp = true;
            return this;
        }

        public Damage build() {
            return new Damage(this);
        }
    }

    @Override
    public Result apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
        // Multi-hit attacks should continue unless they hit an immunity
        if (resultPrev.ordinal() >= minResult.ordinal() && (!mainTargetOnly || isMainTarget)) {
            double atk = -1;
            double def = -1;

            if (type == SkillType.STR) {
                atk = Math.max(1, self.getStrWithStage());
                def = Math.max(1, target.getAmrWithStage());
            } else if (type == SkillType.MAG) {
                atk = Math.max(1, self.getMagWithStage());
                def = Math.max(1, target.getResWithStage());
            }

            double affMultDmgDealt;
            double affMultDmgTaken;

            int multWeakDmgDealt = 100;
            int multWeakDmgTaken = 100;

            if (element == Element.VIS) {
                affMultDmgDealt = 1f;
                affMultDmgTaken = 1f;
            } else {
                affMultDmgDealt = getAffMultDmgDealt(self.getAff(element));
                affMultDmgTaken = getAffMultDmgTaken(target.getAff(element));

                if(target.getAff(element) < 0) {
                    multWeakDmgDealt = self.getMultWeakDmgDealt();
                    multWeakDmgTaken = target.getMultWeakDmgTaken();
                }
            }

            double dmg = (atk / def) * (pow * 10) * affMultDmgDealt * affMultDmgTaken * (Math.max(self.getMultDmgDealt(), 10) / 100d) *
                (Math.max(target.getMultDmgTaken(), 10) / 100d) * (Math.max(self.getMultElementDmgDealt(element), 10) / 100d) *
                (Math.max(target.getMultElementDmgTaken(element), 10) / 100d) * (Math.max(multWeakDmgDealt, 10) / 100d) * (Math.max(multWeakDmgTaken, 10) / 100d);

            if(isFollowUp) dmg = dmg * (Math.max(self.getMultFollowUpDmgDealt(), 10) / 100d) * (Math.max(target.getMultFollowUpDmgTaken(), 10) / 100d);

            // Deal damage
            return target.damage((int) dmg, isPierce);
        } else {
            // If the previous hit failed entirely, this one wouldn't have been reached. If this return statement is ever reached, it's under special circumstances, so let the attack continue just to be safe
            return new Result(ResultType.SUCCESS, "");
        }
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }
}
