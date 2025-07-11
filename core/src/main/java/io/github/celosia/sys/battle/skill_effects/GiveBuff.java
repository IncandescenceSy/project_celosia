package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Buff;
import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.SkillEffect;

public class GiveBuff implements SkillEffect {

    private Buff buff;
    private int turns;

    public GiveBuff(Buff buff, int turns) {
        this.buff = buff;
        this.turns = turns;
    }

    @Override
    public void apply(Combatant self, Combatant target) {
        // todo
    }
}
