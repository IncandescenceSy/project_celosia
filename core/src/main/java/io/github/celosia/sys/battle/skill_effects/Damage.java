package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

import static io.github.celosia.sys.battle.AffLib.getAffMultDmgDealt;
import static io.github.celosia.sys.battle.AffLib.getAffMultDmgTaken;

public class Damage implements SkillEffect {

    private final SkillType type;
    private final int pow;
    private final Element element;
    private final boolean pierce;
    private final ResultType minResult;
    private final boolean isInstant;
    private final boolean mainTargetOnly;
    private final boolean isFollowUp;

    public Damage(SkillType type, Element element, int pow, boolean pierce, ResultType minResult, boolean isInstant, boolean mainTargetOnly, boolean isFollowUp) {
        this.type = type;
        this.element = element;
        this.pow = pow;
        this.pierce = pierce;
        this.minResult = minResult;
        this.isInstant = isInstant;
        this.mainTargetOnly = mainTargetOnly;
        this.isFollowUp = isFollowUp;
    }

    public Damage(SkillType type, Element element, int pow, boolean pierce, boolean isInstant, boolean mainTargetOnly) {
        this(type, element, pow, pierce, ResultType.HIT_SHIELD, isInstant, mainTargetOnly, false);
    }

    public Damage(SkillType type, Element element, int pow, boolean pierce) {
        this(type, element, pow, pierce, ResultType.HIT_SHIELD, false, false, false);
    }

    public Damage(SkillType type, Element element, int pow, ResultType minResult) {
        this(type, element, pow, false, minResult, false, false, false);
    }

    public Damage(SkillType type, Element element, int pow) {
        this(type, element, pow, false, ResultType.HIT_SHIELD, false, false, false);
    }

    @Override
    public Result apply(Combatant self, Combatant target, boolean isMainTarget, ResultType resultPrev) {
        // Multi-hit attacks should continue unless they hit an immunity
        if (resultPrev.ordinal() >= minResult.ordinal() && (!mainTargetOnly || isMainTarget)) {
            double atk = -1f;
            double def = -1f;

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

            double dmg = (atk / def) * (pow * 10) * 2 * affMultDmgDealt * affMultDmgTaken * (Math.max(self.getMultDmgDealt(), 10) / 100d) * (Math.max(target.getMultDmgTaken(), 10) / 100d) * (Math.max(multWeakDmgDealt, 10) / 100d) * (Math.max(multWeakDmgTaken, 10) / 100d) *
                (Math.max(self.getMultElementDmgDealt(element), 10) / 100d) * (Math.max(target.getMultElementDmgTaken(element), 10) / 100d);

            if(isFollowUp) dmg = dmg * (Math.max(self.getMultFollowUpDmgDealt(), 10) / 100d) * (Math.max(target.getMultFollowUpDmgTaken(), 10) / 100d);

            // Deal damage
            return target.damage((int) dmg, pierce);
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
