package io.github.celosia.sys.battle.skill_effects;

import com.badlogic.gdx.math.MathUtils;
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
    private final boolean isBarrier;

    // Affinity multipliers
    private final float[] affAtk = {0.3f, 0.5f, 0.65f, 0.8f, 0.9f, 1f, 1.1f, 1.2f, 1.35f, 1.5f, 1.7f}; // Damage dealt
    private final float[] affDef = {3f, 2f, 1.7f, 1.4f, 1.2f, 1f, 0.9f, 0.8f, 0.65f, 0.5f, 0f}; // Damage taken

    public Damage(SkillType type, Element element, int pow, boolean isBarrier) {
        this.type = type;
        this.element = element;
        this.pow = pow;
        this.isBarrier = isBarrier;
    }

    public Damage(SkillType type, Element element, int pow) {
        this(type, element, pow, false);
    }

    @Override
    public boolean apply(Combatant self, Combatant target) {
        //Logger l = Logger.getLogger("");

        float atk = -1f;
        float def = -1f;

        float multAtk = self.getMultAtk();
        float multDef = target.getMultDef();

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
            affMultDef = affDef[target.getCmbType().getAffs()[element.ordinal() - 1] + 5];
        }

        float ratio = (atk * multAtk) / (def * multDef);

        int dmg = (int) ((type == SkillType.FTH) ? ((float) self.getFthWithStage() / 50) * pow : (ratio * (pow * 30) * 6 * affMultAtk * affMultDef));

        //l.log(Level.INFO, self.getCmbType().getName() + " uses a skill on" + target.getCmbType().getName() + "; atk: " + atk + ", def: " + def + ", ratio: " + ratio + ", affMultAtk: " + affMultAtk + ", affMultDef: " + affMultDef + ", dmg: " + dmg + ", test: " + test + ", test2: " + test2);

        if(isBarrier) { // Add barrier (cannot exceed max HP)
            target.setBarrier(Math.min(target.getBarrier() + dmg, target.getStatsDefault().getHp()));
        } else { // Deal damage
            int barrier = target.getBarrier();
            if(barrier > 0 && dmg > 0) {
                if(barrier - dmg > 0) {
                    target.setBarrier(barrier - dmg);
                    dmg -= barrier;
                } else {
                    target.setBarrier(0);
                    dmg -= barrier;
                    target.getStatsCur().setHp(target.getStatsCur().getHp() - dmg);
                }
            } else target.getStatsCur().setHp(MathUtils.clamp(target.getStatsCur().getHp() - dmg, 0, target.getStatsDefault().getHp()));
        }

        return dmg > 0; // false if didn't do any damage or only hit barrier
    }
}
