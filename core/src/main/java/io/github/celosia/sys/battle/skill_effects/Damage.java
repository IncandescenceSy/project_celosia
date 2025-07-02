package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.SkillEffect;

public class Damage implements SkillEffect {

    private int pow;
    private Element element;

    // todo damage types

    public Damage(int pow, Element element) {
        this.pow = pow;
        this.element = element;
    }

    @Override
    public void apply(Combatant self, Combatant target) {
        target.getStatsCur().setHp(target.getStatsCur().getHp() - ((pow * self.getStatsCur().getStr()) - (target.getStatsCur().getAmr())));
    }
}
