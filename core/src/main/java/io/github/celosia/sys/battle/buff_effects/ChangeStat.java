package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Stat;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.battle.Calcs.getDisplayStatWithStage;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_STAT;
import static io.github.celosia.sys.util.TextLib.formatName;
import static io.github.celosia.sys.util.TextLib.formatNum;
import static io.github.celosia.sys.util.TextLib.getColor;
import static io.github.celosia.sys.util.TextLib.getSign;
import static io.github.celosia.sys.util.TextLib.getStatColor;

public class ChangeStat implements BuffEffect {

    private final Stat stat;
    // Change in tenths of a %; 1000 = +100%
    private final int change;

    public ChangeStat(Stat stat, int change) {
        this.stat = stat;
        this.change = change;
    }

    @Override
    public void onGive(Unit self, int stacks) {
        calc(self, change * stacks);
    }

    @Override
    public void onRemove(Unit self, int stacks) {
        calc(self, (change * stacks) * -1);
    }

    public void calc(Unit self, int changeMod) {
        long statDefaultDisp = self.getStatsDefault().getDisplayStat(stat);
        long statOldDispWithStage = getDisplayStatWithStage(self.getStat(stat), self.getStatsDefault().getStat(stat),
                self.getStage(stat.getMatchingStageType()));

        self.getStatsMult().addToStat(stat, changeMod);

        long statNewDispWithStage = getDisplayStatWithStage(self.getStat(stat), self.getStatsDefault().getStat(stat),
                self.getStage(stat.getMatchingStageType()));

        appendToLog(lang.format("log.change_stat", formatName(self.getUnitType().getName(), self.getPos()),
                C_STAT + stat.getName(),
                getStatColor(statOldDispWithStage, statDefaultDisp) + formatNum(statOldDispWithStage),
                getStatColor(statNewDispWithStage, statDefaultDisp) + formatNum(statNewDispWithStage),
                formatNum(self.getStatsDefault().getDisplayStat(stat)),
                getColor(changeMod) + getSign(changeMod) + formatNum(statNewDispWithStage - statOldDispWithStage)));
    }
}
