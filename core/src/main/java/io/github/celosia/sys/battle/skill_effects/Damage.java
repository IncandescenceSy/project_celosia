package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.SkillType;

public class Damage implements SkillEffect {

    private SkillType type;
    private int pow;
    private Element element;

    public Damage(SkillType type, Element element, int pow) {
        this.type = type;
        this.element = element;
        this.pow = pow;
    }

    @Override
    public void apply(Combatant self, Combatant target) {
        float off = -1f;
        float def = -1f;

        if(type == SkillType.STR) {
            off = self.getStatsCur().getStr();
            def = target.getStatsCur().getAmr();
        } else if(type == SkillType.MAG) {
            off = self.getStatsCur().getStr();
            def = target.getStatsCur().getAmr();
        } else if(type == SkillType.FTH) {
            off = self.getStatsCur().getFth();
            def = off;
        }

        target.getStatsCur().setHp((int) (target.getStatsCur().getHp() - (off / def) * (pow * 10) * 4));
    }
}
