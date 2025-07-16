package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.SkillType;

public class ChangeMP implements SkillEffect {

    private final int change;

    public ChangeMP(int change) {
        this.change = change;
    }

    @Override
    public boolean apply(Combatant self, Combatant target) {
        target.setMp(target.getMp() + change);
        return true;
    }
}
