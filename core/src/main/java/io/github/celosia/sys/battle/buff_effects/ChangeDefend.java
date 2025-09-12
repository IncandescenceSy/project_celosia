package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.menu.TextLib.c_buff;
import static io.github.celosia.sys.menu.TextLib.c_neg;
import static io.github.celosia.sys.menu.TextLib.c_pos;
import static io.github.celosia.sys.menu.TextLib.c_shield;
import static io.github.celosia.sys.menu.TextLib.c_stat;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeDefend implements BuffEffect {

	private final int change; // Defend to add in tenths of a % of max HP (1000 = +100%)

	public ChangeDefend(int change) {
		this.change = change;
	}

	@Override
	public void onGive(Unit self, int stacks) {
		int hpMax = self.getStatsDefault().getHp();
		// Add defend (shield + defend cannot exceed max HP)
		int defendOld = self.getDefend();
		int defendNew = (int) ((self.getShield() + defendOld + (((change / 1000d) * hpMax) * stacks) > hpMax)
				? hpMax - self.getShield()
				: ((change / 1000d) * hpMax) * stacks);

		int defendOldDisp = self.getDisplayDefend();
		self.setDefend(defendNew);
		int defendNewDisp = self.getDisplayDefend();

		int shieldDisp = self.getDisplayShield();

		appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_buff + lang.get("shield") + " "
				+ c_shield + String.format("%,d", (shieldDisp + defendOldDisp)) + "[WHITE]" + " → " + c_shield
				+ String.format("%,d", (shieldDisp + defendNewDisp)) + "[WHITE]/" + c_shield
				+ String.format("%,d", self.getStatsDefault().getDisplayHp()) + "[WHITE] (" + c_pos + "+"
				+ String.format("%,d", ((shieldDisp + defendNewDisp) - (shieldDisp + defendOldDisp))) + "[WHITE])");
		if (self.isEffectBlock() && self.getShield() == 0 && defendOld == 0)
			appendToLog(formatName(self.getUnitType().getName(), self.getPos(), false) + " " + lang.get("log.is_now")
					+ " " + c_stat + lang.get("log.effect_block"));
	}

	@Override
	public void onRemove(Unit self, int stacks) {
		int defendOldDisp = self.getDisplayDefend();
		self.setDefend(0);
		int shieldDisp = self.getDisplayShield();

		if (self.getShield() > 0)
			appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_buff + lang.get("shield")
					+ " " + c_shield + String.format("%,d", (shieldDisp + defendOldDisp)) + "[WHITE]" + " → " + c_shield
					+ String.format("%,d", shieldDisp) + "[WHITE]/" + c_shield
					+ String.format("%,d", self.getStatsDefault().getDisplayHp()) + "[WHITE] (" + c_neg
					+ String.format("%,d", (defendOldDisp * -1)) + "[WHITE])");
		else
			appendToLog(formatName(self.getUnitType().getName(), self.getPos(), false) + " " + lang.get("log.loses")
					+ " " + c_shield + String.format("%,d", defendOldDisp) + "[WHITE] " + c_buff + lang.get("shield"));
		if (self.isEffectBlock() && self.getShield() == 0)
			appendToLog(formatName(self.getUnitType().getName(), self.getPos(), false) + " "
					+ lang.get("log.is_no_longer") + " " + c_stat + lang.get("log.effect_block"));
	}
}
