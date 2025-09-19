package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.formatNum;
import static io.github.celosia.sys.settings.Lang.lang;
import static io.github.celosia.sys.util.MiscLib.isMatchingTruthiness;

public class ChangeInfiniteSp implements BuffEffect {
	private final int change; // Amount to add

	public ChangeInfiniteSp(int change) {
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

	public void calc(Unit self, int changeFull) {
		int infiniteSpOld = self.getInfiniteSp();
		int infiniteSpNew = infiniteSpOld + changeFull;
		self.setInfiniteSp(infiniteSpNew);
		if (!isMatchingTruthiness(infiniteSpOld, infiniteSpNew)) {
			appendToLog(lang.format("log.change_sp.infinite", formatName(self.getUnitType().name(), self.getPos()),
					formatNum(self.getSp()), infiniteSpNew));
		}
	}
}
