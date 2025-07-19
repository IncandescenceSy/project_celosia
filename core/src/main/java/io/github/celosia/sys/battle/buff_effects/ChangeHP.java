package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;

// Todo heavily limit %-based damage on bosses
public class ChangeHP implements BuffEffect {

    private final float change; // Amount to change HP by. 1f = 100%

    public ChangeHP(float change) {
        this.change = change;
    }

    @Override
    public void onGive(Combatant self) {
    }

    @Override
    public void onRemove(Combatant self) {
    }

    @Override
    public void onUseSkill(Combatant self, Combatant target) {
    }

    @Override
    public void onTakeDamage(Combatant self) {
    }

    @Override
    public void onTurnEnd(Combatant self) {
        self.damage((int) (self.getStatsDefault().getHp() * change));
    }
}
