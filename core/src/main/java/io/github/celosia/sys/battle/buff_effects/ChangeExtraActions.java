package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.getColor;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeExtraActions implements BuffEffect {
	private final int change; // Amount to add

	public ChangeExtraActions(int change) {
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
		int exAOld = self.getExtraActions();
		int exANew = exAOld + changeFull;
		self.setExtraActions(exANew);
		appendToLog(lang.format("log.change_extra_actions", formatName(self.getUnitType().name(), self.getPos()),
				getColor(exAOld) + Math.max(exAOld, 0), getColor(exANew) + Math.max(exANew, 0)));
	}
}
