package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.SkillType;

//import java.util.logging.Level;
//import java.util.logging.Logger;

public class Damage implements SkillEffect {

    private final SkillType type;
    private final int pow;
    private final Element element;
    private final boolean pierce;
    private final int barrierTurns;

    // Affinity multipliers
    private final float[] affAtk = {0.3f, 0.5f, 0.65f, 0.8f, 0.9f, 1f, 1.1f, 1.2f, 1.35f, 1.5f, 1.7f}; // Damage dealt
    private final float[] affDef = {3f, 2f, 1.7f, 1.4f, 1.2f, 1f, 0.9f, 0.8f, 0.65f, 0.5f, 0f}; // Damage taken

    public Damage(SkillType type, Element element, int pow, boolean pierce, int barrierTurns) {
        this.type = type;
        this.element = element;
        this.pow = pow;
        this.pierce = pierce;
        this.barrierTurns = barrierTurns;
    }

    public Damage(SkillType type, Element element, int pow, boolean pierce) {
        this(type, element, pow, pierce, 0);
    }

    public Damage(SkillType type, Element element, int pow, int barrierTurns) {
        this(type, element, pow, false, barrierTurns);
    }

    public Damage(SkillType type, Element element, int pow) {
        this(type, element, pow, false, 0);
    }

    @Override
    public boolean apply(Combatant self, Combatant target) {
        //Logger l = Logger.getLogger("");

        float atk = -1f;
        float def = -1f;

        if(type == SkillType.STR) {
            atk = Math.max(1, self.getStrWithStage());
            def = Math.max(1, target.getAmrWithStage());
        } else if(type == SkillType.MAG) {
            atk = Math.max(1, self.getMagWithStage());
            def = Math.max(1, target.getResWithStage());
        }

        float affMultAtk;
        float affMultDef;

        if(element == Element.VIS || type == SkillType.FTH) {
            affMultAtk = 1f;
            affMultDef = 1f;
        } else {
            affMultAtk = affAtk[self.getCmbType().getAffs()[element.ordinal() - 1] + 5];
            affMultDef = Math.max(affDef[target.getCmbType().getAffs()[element.ordinal() - 1] + 5], (pierce) ? 1f : 0f);
        }

        float ratio = atk / def;

        // todo does all the multiplication and shit apply as expected?
        int dmg = (int) ((type == SkillType.FTH) ? ((float) self.getFthWithStage() / 50) * pow : (ratio * (pow * 30) * 6 * affMultAtk) * affMultDef * Math.max(self.getMultAtk(), 0.1f));

        //l.log(Level.INFO, self.getCmbType().getName() + " uses a skill on" + target.getCmbType().getName() + "; atk: " + atk + ", def: " + def + ", ratio: " + ratio + ", affMultAtk: " + affMultAtk + ", affMultDef: " + affMultDef + ", dmg: " + dmg + ", test: " + test + ", test2: " + test2);

        boolean success;

        if(barrierTurns > 0) { // Add barrier (barrier + defend cannot exceed max HP)
            int maxHp = target.getStatsDefault().getHp();
            target.setBarrier((target.getBarrier() + target.getDefend() + dmg > maxHp) ? maxHp : target.getBarrier() + dmg);
            target.setBarrierTurns(Math.max(target.getBarrierTurns(), barrierTurns));
            success = true;
        } else { // Deal damage
            success = target.damage(dmg, pierce);
        }

        // todo: multihits shouldnt immediately end if they don't lower HP on the first hit
        // idea: adding an argument to Damage to make it always return true, and using that on all hits of multihits except the last (but it should really only return true on barrier, not immunity)
        // or set if SkillEffects care about the result of the last hit (same problem) (make cmb.damage() return either damagedHp, damagedBarrier, didntDamage
        return success; // false if didn't do any damage or only hit barrier
    }
}
