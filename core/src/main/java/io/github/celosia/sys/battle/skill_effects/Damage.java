package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

import static io.github.celosia.sys.battle.AffLib.getAffMultDmgDealt;
import static io.github.celosia.sys.battle.AffLib.getAffMultDmgTaken;
import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.battle.BattleLib.STAT_FACTOR;

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
    public ResultType apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
        // Multi-hit attacks should continue unless they hit an immunity
        if (resultPrev.ordinal() >= minResult.ordinal() && (!mainTargetOnly || isMainTarget)) {
            int atk = -1;
            int def = -1;

            if (type == SkillType.STR) {
                atk = Math.max(1, self.getStrWithStage());
                def = Math.max(1, target.getAmrWithStage());
            } else if (type == SkillType.MAG) {
                atk = Math.max(1, self.getMagWithStage());
                def = Math.max(1, target.getResWithStage());
            }

            int affMultDmgDealt;
            int affMultDmgTaken;

            int multWeakDmgDealt = 100;
            int multWeakDmgTaken = 100;

            if (element == Element.VIS) {
                affMultDmgDealt = 10000;
                affMultDmgTaken = 10000;
            } else {
                affMultDmgDealt = getAffMultDmgDealt(self.getAff(element));
                affMultDmgTaken = getAffMultDmgTaken(target.getAff(element));

                if(target.getAff(element) < 0) {
                    multWeakDmgDealt = self.getMultWeakDmgDealt();
                    multWeakDmgTaken = target.getMultWeakDmgTaken();
                }
            }

            int dmg = (int) (STAT_FACTOR * ((double) atk / def) * (pow * 10) * (affMultDmgDealt / 10000d) * (affMultDmgTaken / 10000d) * (Math.max(self.getMultDmgDealt(), 1000) / 10000d) * (Math.max(target.getMultDmgTaken(), 1000) / 10000d) * (Math.max(self.getMultElementDmgDealt(element), 1000) / 10000d) * (Math.max(target.getMultElementDmgTaken(element), 1000) / 10000d) * (Math.max(multWeakDmgDealt, 1000) / 10000d) * (Math.max(multWeakDmgTaken, 1000) / 10000d));

            if(isFollowUp) dmg = (int) (dmg * (Math.max(self.getMultFollowUpDmgDealt(), 1000) / 10000d) * (Math.max(target.getMultFollowUpDmgTaken(), 1000) / 10000d));

            // Deal damage
            Result result = target.damage(dmg, isPierce);
            appendToLog(result.getMessages());
            return result.getResultType();
        } else {
            // If the previous hit failed entirely, this one wouldn't have been reached. If this return statement is ever reached, it's under special circumstances, so let the attack continue just to be safe
            return ResultType.SUCCESS;
        }
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }
}
