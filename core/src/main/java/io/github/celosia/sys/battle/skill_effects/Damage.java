package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.SkillEffect;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Damage implements SkillEffect {

    private int pow;
    private Element element;

    // todo damage types

    public Damage(int pow, Element element) {
        this.pow = pow;
        this.element = element;
    }

    // todo use str/mag/fth vs amr/res/na
    @Override
    public void apply(Combatant self, Combatant target) {
        target.getStatsCur().setHp((int) (target.getStatsCur().getHp() - ((float) self.getStatsCur().getStr() / (float) target.getStatsCur().getAmr()) * (pow * 10) * 4));
    }
}
