package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Stat;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.battle.Calcs.getDisplayStatWithStage;
import static io.github.celosia.sys.menu.TextLib.*;

public class ChangeStat implements BuffEffect {

    private final Stat stat;
    private final int change; // Change in hundredths of a %; 10,000 = +100%

    public ChangeStat(Stat stat, int change) {
        this.stat = stat;
        this.change = change;
    }

    @Override
    public void onGive(Unit self, int stacks) {
        int statDefault = self.getStatsDefault().getStat(stat);
        int statOld = self.getStatsCur().getStat(stat);
        int statNew = (int) (statOld + (statDefault * ((long) change * stacks) / 10000));
        self.getStatsCur().setStat(stat, statNew);

        int statDefaultDisp = self.getStatsDefault().getDisplayStat(stat);
        int statOldDispWithStage = getDisplayStatWithStage(statOld, statDefault, self.getStage(stat.getMatchingStageType()));
        int statNewDispWithStage = getDisplayStatWithStage(statNew, statDefault, self.getStage(stat.getMatchingStageType()));
        appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + stat.getName() + " " + getStatColor(statOldDispWithStage, statDefaultDisp) + String.format("%,d", statOldDispWithStage) + "[WHITE] → " + getStatColor(statNewDispWithStage, statDefaultDisp) + String.format("%,d", statNewDispWithStage) + "[WHITE]/" + c_num + String.format("%,d", self.getStatsDefault().getDisplayStat(stat)) + "[WHITE] (" + getColor(change) + ((change >= 0f) ? "+" : "") + String.format("%,d", statNewDispWithStage - statOldDispWithStage) + "[WHITE])");
    }

    @Override
    public void onRemove(Unit self, int stacks) {
        int statDefault = self.getStatsDefault().getStat(stat);
        int statOld = self.getStatsCur().getStat(stat);
        int statNew = (int) (statOld - (statDefault * ((long) change * stacks) / 10000));
        self.getStatsCur().setStat(stat, statNew);

        int statDefaultDisp = self.getStatsDefault().getDisplayStat(stat);
        int statOldDispWithStage = getDisplayStatWithStage(statOld, statDefault, self.getStage(stat.getMatchingStageType()));
        int statNewDispWithStage = getDisplayStatWithStage(statNew, statDefault, self.getStage(stat.getMatchingStageType()));
        appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + stat.getName() + " " + getStatColor(statOldDispWithStage, statDefaultDisp) + String.format("%,d", statOldDispWithStage) + "[WHITE] → " + getStatColor(statNewDispWithStage, statDefaultDisp) + String.format("%,d", statNewDispWithStage) + "[WHITE]/" + c_num + String.format("%,d", self.getStatsDefault().getDisplayStat(stat)) + "[WHITE] (" + getColor(change * -1) + ((change <= 0f) ? "+" : "") + String.format("%,d", statNewDispWithStage - statOldDispWithStage) + "[WHITE])");
    }
}
