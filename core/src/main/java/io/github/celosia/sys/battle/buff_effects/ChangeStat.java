package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Stat;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.battle.Calcs.getDisplayStatWithStage;
import static io.github.celosia.sys.menu.TextLib.c_num;
import static io.github.celosia.sys.menu.TextLib.c_stat;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.formatNum;
import static io.github.celosia.sys.menu.TextLib.getColor;
import static io.github.celosia.sys.menu.TextLib.getStatColor;

public class ChangeStat implements BuffEffect {
	private final Stat stat;
	private final int change; // Change in tenths of a %; 1000 = +100%

	public ChangeStat(Stat stat, int change) {
		this.stat = stat;
		this.change = change;
	}

	@Override
	public void onGive(Unit self, int stacks) {
		long statDefault = self.getStatsDefault().getStat(stat);
		long statOld = self.getStatsCur().getStat(stat);
		long statNew = (long) (statOld + (statDefault * (((long) change * stacks) / 1000d)));
		self.getStatsCur().setStat(stat, statNew);

		long statDefaultDisp = self.getStatsDefault().getDisplayStat(stat);
		long statOldDispWithStage = getDisplayStatWithStage(statOld, statDefault,
				self.getStage(stat.getMatchingStageType()));
		long statNewDispWithStage = getDisplayStatWithStage(statNew, statDefault,
				self.getStage(stat.getMatchingStageType()));

		appendToLog(formatName(self.getUnitType().name(), self.getPos()) + " " + c_stat + stat.getName() + " "
				+ getStatColor(statOldDispWithStage, statDefaultDisp) + formatNum(statOldDispWithStage) + "[WHITE] → "
				+ getStatColor(statNewDispWithStage, statDefaultDisp) + formatNum(statNewDispWithStage) + "[WHITE]/"
				+ c_num + formatNum(self.getStatsDefault().getDisplayStat(stat)) + "[WHITE] (" + getColor(change)
				+ ((change >= 0f) ? "+" : "") + formatNum(statNewDispWithStage - statOldDispWithStage) + "[WHITE])");
	}

	@Override
	public void onRemove(Unit self, int stacks) {
		long statDefault = self.getStatsDefault().getStat(stat);
		long statOld = self.getStatsCur().getStat(stat);
		long statNew = (long) (statOld - (statDefault * (((long) change * stacks) / 1000d)));
		self.getStatsCur().setStat(stat, statNew);

		long statDefaultDisp = self.getStatsDefault().getDisplayStat(stat);
		long statOldDispWithStage = getDisplayStatWithStage(statOld, statDefault,
				self.getStage(stat.getMatchingStageType()));
		long statNewDispWithStage = getDisplayStatWithStage(statNew, statDefault,
				self.getStage(stat.getMatchingStageType()));
		appendToLog(formatName(self.getUnitType().name(), self.getPos()) + " " + c_stat + stat.getName() + " "
				+ getStatColor(statOldDispWithStage, statDefaultDisp) + formatNum(statOldDispWithStage) + "[WHITE] → "
				+ getStatColor(statNewDispWithStage, statDefaultDisp) + formatNum(statNewDispWithStage) + "[WHITE]/"
				+ c_num + formatNum(self.getStatsDefault().getDisplayStat(stat)) + "[WHITE] (" + getColor(change * -1)
				+ ((change <= 0f) ? "+" : "") + formatNum(statNewDispWithStage - statOldDispWithStage) + "[WHITE])");
	}
}
