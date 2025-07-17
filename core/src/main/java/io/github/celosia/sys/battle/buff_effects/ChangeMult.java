package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Mult;

public class ChangeMult implements BuffEffect {

    private Mult mult;
    private float change; // Amount to add

    public ChangeMult(Mult mult, float change) {
        this.mult = mult;
        this.change = change;
    }

    @Override
    public void onGive(Combatant self) {
        self.setMult(mult, self.getMult(mult) + change);
    }

    @Override
    public void onRemove(Combatant self) {
        self.setMult(mult, self.getMult(mult) - change);
    }

    @Override
    public void onUseSkill(Combatant self, Combatant target) {
    }

    @Override
    public void onTakeDamage(Combatant self) {
    }

    @Override
    public void onTurnEnd(Combatant self) {
    }
}
