package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Stat;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.battle.Calcs.getDisplayStatWithStage;
import static io.github.celosia.sys.menu.TextLib.c_stat;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.formatNum;
import static io.github.celosia.sys.menu.TextLib.getColor;
import static io.github.celosia.sys.menu.TextLib.getSign;
import static io.github.celosia.sys.menu.TextLib.getStatColor;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeStat implements BuffEffect {
	private final Stat stat;
	private final int change; // Change in tenths of a %; 1000 = +100%

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

		appendToLog(lang.format("log.change_stat", formatName(self.getUnitType().name(), self.getPos()),
				c_stat + stat.getName(),
				getStatColor(statOldDispWithStage, statDefaultDisp) + formatNum(statOldDispWithStage),
				getStatColor(statNewDispWithStage, statDefaultDisp) + formatNum(statNewDispWithStage),
				formatNum(self.getStatsDefault().getDisplayStat(stat)),
				getColor(changeMod) + getSign(changeMod) + formatNum(statNewDispWithStage - statOldDispWithStage)));
	}
}
