package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.menu.TextLib.c_sp;
import static io.github.celosia.sys.menu.TextLib.c_stat;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeInfiniteSp implements BuffEffect {

	private final int change; // Amount to add

	public ChangeInfiniteSp(int change) {
		this.change = change;
	}

	@Override
	public void onGive(Unit self, int stacks) {
		int infiniteSpOld = self.getInfiniteSp();
		int infiniteSpNew = infiniteSpOld + (change * stacks);
		self.setInfiniteSp(infiniteSpNew);
		if (infiniteSpNew > 0 && self.getShield() == 0 && self.getDefend() == 0)
			appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + lang.get("sp") + " "
					+ c_sp + String.format("%,d", self.getSp()) + "[WHITE] → " + c_sp + "∞");
	}

	@Override
	public void onRemove(Unit self, int stacks) {
		int infiniteSpOld = self.getInfiniteSp();
		int infiniteSpNew = infiniteSpOld - (change * stacks);
		self.setInfiniteSp(infiniteSpNew);
		if (infiniteSpNew <= 0 && self.getShield() == 0 && self.getDefend() == 0)
			appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + lang.get("sp") + c_sp
					+ " ∞" + "[WHITE] → " + c_sp + String.format("%,d", self.getSp()));
	}
}
