package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Stats;

// Todo heavily limit %-based damage on bosses
public class Damage implements BuffEffect {

    private float dmg; // Damage to do each turn in % of max HP

    public Damage(float dmg) {
        this.dmg = dmg;
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
        Stats stats = self.getStatsCur();
        stats.setHp((int) (stats.getHp() - (self.getStatsDefault().getHp() * dmg)));
    }
}
