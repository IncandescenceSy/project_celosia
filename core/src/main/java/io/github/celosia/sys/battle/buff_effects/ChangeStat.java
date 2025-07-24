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

    // todo fix bad rounding
    @Override
    public String onGive(Combatant self) {
        int statOld = self.getStatsCur().getStat(stat);
        int statNew = (int) (statOld + (change * self.getStatsDefault().getStat(stat)));
        self.getStatsCur().setStat(stat, statNew);
        // todo condense lines when giving/removing multiple stacks at once and fix no + appearing when increasing stat
        return self.getCmbType().getName() + "'s " + stat.getName() + " " + statOld + " -> " + statNew + "/" + self.getStatsDefault().getStat(stat) + ((change > 0f) ? " (+" : " (") + (statNew - statOld) + ")";
    }

    @Override
    public String onRemove(Combatant self) {
        int statOld = self.getStatsCur().getStat(stat);
        int statNew = (int) (statOld - (change * self.getStatsDefault().getStat(stat)));
        self.getStatsCur().setStat(stat, statNew);
        return self.getCmbType().getName() + "'s " + stat.getName() + " " + statOld + " -> " + statNew + "/" + self.getStatsDefault().getStat(stat) + ((change < 0f) ? " (+" : " (") + (statNew - statOld) + ")";
    }

    @Override
    public String onUseSkill(Combatant self, Combatant target) {
        return "";
    }

    @Override
    public String onTakeDamage(Combatant self) {
        return "";
    }

    @Override
    public String onTurnEnd(Combatant self) {
        return "";
    }
}
