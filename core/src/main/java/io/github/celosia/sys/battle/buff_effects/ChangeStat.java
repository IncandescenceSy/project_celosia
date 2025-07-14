package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Stat;

public class ChangeStat implements BuffEffect {

    private Stat stat;
    private float change; // Change in percentage. 1 = 100%

    public ChangeStat(Stat stat, float change) {
        this.stat = stat;
        this.change = change;
    }

    @Override
    public void onGive(Combatant self) {
        self.getStatsCur().setStat(stat, (int) (self.getStatsCur().getStat(stat) + (change * self.getStatsDefault().getStat(stat))));
    }

    @Override
    public void onRemove(Combatant self) {
        self.getStatsCur().setStat(stat, (int) (self.getStatsCur().getStat(stat) - (change * self.getStatsDefault().getStat(stat))));
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
