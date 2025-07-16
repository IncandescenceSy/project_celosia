package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.SkillType;

public class Damage implements SkillEffect {

    private final SkillType type;
    private final int pow;
    private final Element element;

    // Affinity multipliers
    private final float[] affAtk = {0.3f, 0.5f, 0.65f, 0.8f, 0.9f, 1f, 1.1f, 1.2f, 1.35f, 1.5f, 1.7f}; // Damage dealt
    private final float[] affDef = {3f, 2f, 1.7f, 1.4f, 1.2f, 1f, 0.9f, 0.8f, 0.65f, 0.5f, 0f}; // Damage taken

    public Damage(SkillType type, Element element, int pow) {
        this.type = type;
        this.element = element;
        this.pow = pow;
    }

    @Override
    public boolean apply(Combatant self, Combatant target) {
        float atk = -1f;
        float def = -1f;

        if(type == SkillType.STR) {
            atk = Math.max(1, self.getStrWithStage());
            def = Math.max(1, target.getAmrWithStage());
        } else if(type == SkillType.MAG) {
            atk = Math.max(1, self.getMagWithStage());
            def = Math.max(1, target.getResWithStage());
        } else if(type == SkillType.FTH) {
            atk = Math.max(1, self.getFthWithStage());
            def = Math.max(1, atk);
        }

        int dmg = (int) ((atk / def) * (pow * 10) * ((element == Element.VIS) ? 1 : affAtk[self.getCmbType().getAffs()[element.ordinal() - 1] + 5]) * ((element == Element.VIS) ? 1 : affDef[target.getCmbType().getAffs()[element.ordinal() - 1] + 5]));

        target.getStatsCur().setHp(target.getStatsCur().getHp() - dmg);

        return dmg > 0; // false if didn't do any dmg
    }
}
