package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Stat;

public class ChangeStat implements BuffEffect {

    private final Stat stat;
    private final double change; // Change in percentage. 1 = 100%

    public ChangeStat(Stat stat, double change) {
        this.stat = stat;
        this.change = change;
    }

    @Override
    public String[] onGive(Combatant self) {
        int statOld = self.getStatsCur().getStat(stat);
        int statNew = (int) Math.floor(statOld + (change * self.getStatsDefault().getStat(stat)));
        self.getStatsCur().setStat(stat, statNew);
        // todo condense lines when giving/removing multiple stacks at once
        return new String[]{self.getCmbType().getName() + "'s " + stat.getName() + " " + String.format("%,d", statOld) + " -> " + String.format("%,d", statNew) + "/" +
            String.format("%,d", self.getStatsDefault().getStat(stat)) + ((change >= 0f) ? " (+" : " (") + String.format("%,d", (statNew - statOld)) + ")"};
    }

    @Override
    public String[] onRemove(Combatant self) {
        int statOld = self.getStatsCur().getStat(stat);
        int statNew = (int) Math.ceil(statOld - (change * self.getStatsDefault().getStat(stat)));
        self.getStatsCur().setStat(stat, statNew);
        return new String[]{self.getCmbType().getName() + "'s " + stat.getName() + " " + String.format("%,d", statOld) + " -> " + String.format("%,d", statNew) + "/" +
            String.format("%,d", self.getStatsDefault().getStat(stat)) + ((change <= 0f) ? " (+" : " (") + String.format("%,d", (statNew - statOld)) + ")"};
    }
}
