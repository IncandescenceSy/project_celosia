package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

public class Damage implements SkillEffect {

    private final SkillType type;
    private final int pow;
    private final Element element;
    private final boolean pierce;
    private final Result minResult;

    // Affinity multipliers
    private final float[] affAtk = {0.3f, 0.5f, 0.65f, 0.8f, 0.9f, 1f, 1.1f, 1.2f, 1.35f, 1.5f, 1.7f}; // Damage dealt
    private final float[] affDef = {3f, 2f, 1.7f, 1.4f, 1.2f, 1f, 0.9f, 0.8f, 0.65f, 0.5f, 0f}; // Damage taken

    public Damage(SkillType type, Element element, int pow, boolean pierce, Result minResult) {
        this.type = type;
        this.element = element;
        this.pow = pow;
        this.pierce = pierce;
        this.minResult = minResult;
    }

    public Damage(SkillType type, Element element, int pow, boolean pierce) {
        this(type, element, pow, pierce, Result.HIT_BARRIER);
    }

    public Damage(SkillType type, Element element, int pow, Result minResult) {
        this(type, element, pow, false, minResult);
    }

    public Damage(SkillType type, Element element, int pow) {
        this(type, element, pow, false, Result.HIT_BARRIER);
    }

    @Override
    public Result apply(Combatant self, Combatant target, Result resultPrev) {
        // Multi-hit attacks should continue unless they hit an immunity
        if (resultPrev.ordinal() >= minResult.ordinal()) {
            float atk = -1f;
            float def = -1f;

            if (type == SkillType.STR) {
                atk = Math.max(1, self.getStrWithStage());
                def = Math.max(1, target.getAmrWithStage());
            } else if (type == SkillType.MAG) {
                atk = Math.max(1, self.getMagWithStage());
                def = Math.max(1, target.getResWithStage());
            }

            float affMultAtk;
            float affMultDef;

            if (element == Element.VIS) {
                affMultAtk = 1f;
                affMultDef = 1f;
            } else {
                affMultAtk = affAtk[self.getCmbType().getAffs()[element.ordinal() - 1] + 5];
                affMultDef = Math.max(affDef[target.getCmbType().getAffs()[element.ordinal() - 1] + 5], (pierce) ? 1f : 0f);
            }

            // todo does all the multiplication and shit apply as expected?
            int dmg = (int) (((atk / def) * (pow * 30) * 6 * affMultAtk) * affMultDef * Math.max(self.getMultAtk(), 0.1f));

            //l.log(Level.INFO, self.getCmbType().getName() + " uses a skill on" + target.getCmbType().getName() + "; atk: " + atk + ", def: " + def + ", ratio: " + ratio + ", affMultAtk: " + affMultAtk + ", affMultDef: " + affMultDef + ", dmg: " + dmg + ", test: " + test + ", test2: " + test2);

            // Deal damage
            return target.damage(dmg, pierce);
        } else {
            // If the previous hit failed entirely, this one wouldn't have been reached. If this return statement is ever reached, it's under special circumstances, so let the attack continue just to be safe
            return Result.SUCCESS;
        }
    }
}
