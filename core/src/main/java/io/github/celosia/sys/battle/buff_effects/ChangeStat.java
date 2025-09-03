package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Stat;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.menu.TextLib.*;

public class ChangeStat implements BuffEffect {

    private final Stat stat;
    private final double change; // Change in percentage. 1 = 100%

    public ChangeStat(Stat stat, double change) {
        this.stat = stat;
        this.change = change;
    }

    @Override
    public void onGive(Unit self, int stacks) {
        int statOld = self.getStatWithStage(stat);
        int statNew = (int) Math.floor(statOld + ((change * self.getStatsDefault().getStat(stat)) * stacks));
        self.getStatsCur().setStat(stat, statNew);
        // todo condense lines when giving/removing multiple stacks at once
        appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + stat.getName() + " " + getStatColor(statOld, self.getStatsDefault().getStat(stat))
            + String.format("%,d", statOld) + "[WHITE] → " + getStatColor(statNew, self.getStatsDefault().getStat(stat)) + String.format("%,d", statNew) + "[WHITE]/" +
            c_num + String.format("%,d", self.getStatsDefault().getStat(stat)) + "[WHITE] (" + getColor(change) + ((change >= 0f) ? "+" : "") + String.format("%,d", (statNew - statOld)) + "[WHITE])");
    }

    @Override
    public void onRemove(Unit self, int stacks) {
        int statOld = self.getStatWithStage(stat);
        int statNew = (int) Math.ceil(statOld - ((change * self.getStatsDefault().getStat(stat)) * stacks));
        self.getStatsCur().setStat(stat, statNew);
        appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + stat.getName() + " " + getStatColor(statOld, self.getStatsDefault().getStat(stat))
            + String.format("%,d", statOld) + "[WHITE] → " + getStatColor(statNew, self.getStatsDefault().getStat(stat)) + String.format("%,d", statNew) + "[WHITE]/" +
            c_num + String.format("%,d", self.getStatsDefault().getStat(stat)) + "[WHITE] (" + getColor(change * -1) + ((change <= 0f) ? "+" : "") + String.format("%,d", (statNew - statOld)) + "[WHITE])");
    }
}
