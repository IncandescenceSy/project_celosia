package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;

public class ChangeDefend implements BuffEffect {

    private float change; // Defend to add in % of max HP

    public ChangeDefend(float change) {
        this.change = change;
    }

    @Override
    public void onGive(Combatant self) {
        int maxHp = self.getStatsDefault().getHp();
        // Add defend (barrier + defend cannot exceed max HP)
        self.setDefend((self.getBarrier() + self.getDefend() + (change * maxHp) > maxHp) ? maxHp - self.getBarrier() : (int) (change * maxHp));
    }

    @Override
    public void onRemove(Combatant self) {
        self.setDefend(0);
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
