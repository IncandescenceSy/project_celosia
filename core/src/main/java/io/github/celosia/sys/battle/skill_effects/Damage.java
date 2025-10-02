package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.AffLib;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.Elements;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.SkillType;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.battle.BattleLib.STAT_MULT_HIDDEN;
import static io.github.celosia.sys.battle.BattleLib.STAT_MULT_VISIBLE;

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
            isPierce = true;
            return this;
        }

        public Builder instant() {
            isInstant = true;
            return this;
        }

        public Builder mainTargetOnly() {
            mainTargetOnly = true;
            return this;
        }

        public Builder followUp() {
            isFollowUp = true;
            return this;
        }

        public Damage build() {
            return new Damage(this);
        }
    }

    @Override
    public ResultType apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
        // If the previous hit failed entirely, this one wouldn't have been reached. If
        // this return statement is ever reached, it's under special circumstances (such
        // as a main target only effect), so let the attack continue just to be safe
        if (resultPrev.ordinal() < minResult.ordinal() || (mainTargetOnly && !isMainTarget)) {
            return ResultType.SUCCESS;
        }

        long atk = 1;
        long def = 1;

        if (type == SkillType.STR) {
            atk = self.getStrWithStage();
            def = target.getAmrWithStage();
        } else if (type == SkillType.MAG) {
            atk = self.getMagWithStage();
            def = target.getResWithStage();
        }

        int affMultDmgDealt;
        int affMultDmgTaken;

        double multWeakDmgDealt = 1;
        double multWeakDmgTaken = 1;

        if (element == Elements.VIS) {
            affMultDmgDealt = 1000;
            affMultDmgTaken = 1000;
        } else {
            affMultDmgDealt = AffLib.DMG_DEALT.get(self.getAffinity(element));
            affMultDmgTaken = AffLib.DMG_TAKEN.get(target.getAffinity(element));

            if (target.isWeakTo(element)) {
                multWeakDmgDealt = self.getMultWithExpWeakDmgDealt();
                multWeakDmgTaken = target.getMultWithExpWeakDmgTaken();
            }
        }

        double multFollowUpDmgDealt = 1;
        double multFollowUpDmgTaken = 1;

        if (isFollowUp) {
            multFollowUpDmgDealt = self.getMultWithExpFollowUpDmgDealt();
            multFollowUpDmgTaken = target.getMultWithExpFollowUpDmgTaken();
        }

        long dmg;

        // No damage on affinity immunity
        if (affMultDmgTaken == 0) {
            dmg = 0;
        } else {
            dmg = STAT_MULT_HIDDEN * STAT_MULT_VISIBLE *
                    (long) (((double) atk / def) * pow * (affMultDmgDealt / 1000d) * (affMultDmgTaken / 1000d) *
                            self.getMultWithExpDmgDealt() * target.getMultWithExpDmgTaken() *
                            self.getMultWithExpElementDmgDealt(element) *
                            target.getMultWithExpElementDmgTaken(element) * multWeakDmgDealt * multWeakDmgTaken *
                            multFollowUpDmgDealt * multFollowUpDmgTaken);

            // Only way for dmg to be negative is if an overflow happened, so set it to max
            // long just to be safe
            if (dmg < 0) {
                dmg = Long.MAX_VALUE;
            } else if (dmg == 0) {
                dmg = 1;
            }

            self.onDealDamage(target, dmg, element);
            target.onTakeDamage(self, dmg, element);
        }

        // Deal damage
        Result result = target.damage(dmg, isPierce);
        appendToLog(result.messages());
        return result.resultType();
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }
}
