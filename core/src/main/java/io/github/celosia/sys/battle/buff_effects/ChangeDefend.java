package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.menu.TextLib.c_hp;
import static io.github.celosia.sys.menu.TextLib.c_shield;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.formatNum;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeDefend implements BuffEffect {
	private final int change; // Defend to add in tenths of a % of max HP (1000 = +100%)

	public ChangeDefend(int change) {
		this.change = change;
	}

	@Override
	public void onGive(Unit self, int stacks) {
		long hpMax = self.getMaxHp();
		// Add defend (shield + defend cannot exceed max HP)
		long defendOld = self.getDefend();
		long defendNew = (long) ((self.getShield() + defendOld + (((change / 1000d) * hpMax) * stacks) > hpMax)
				? hpMax - self.getShield()
				: ((change / 1000d) * hpMax) * stacks);

		long defendOldDisp = self.getDisplayDefend();
		self.setDefend(defendNew);
		long defendNewDisp = self.getDisplayDefend();

		long shieldDisp = self.getDisplayShield();

		appendToLog(lang.format("log.change_shield", formatName(self.getUnitType().name(), self.getPos()),
				c_shield + formatNum((shieldDisp + defendOldDisp)), c_shield + formatNum((shieldDisp + defendNewDisp)),
				c_hp + formatNum(self.getDisplayMaxHp()),
				c_shield + "+" + formatNum(((shieldDisp + defendNewDisp) - (shieldDisp + defendOldDisp)))));

		if (!self.isEffectBlock() && self.getShield() == 0 && defendOld == 0) {
			appendToLog(lang.format("log.change_effect_block",
					formatName(self.getUnitType().name(), self.getPos(), false), 1));
		}
	}

	@Override
	public void onRemove(Unit self, int stacks) {
		long defendOldDisp = self.getDisplayDefend();
		self.setDefend(0);
		long shieldDisp = self.getDisplayShield();

		if (self.getShield() > 0) {
			appendToLog(lang.format("log.change_shield", formatName(self.getUnitType().name(), self.getPos()),
					c_shield + formatNum((shieldDisp + defendOldDisp)), c_shield + formatNum((shieldDisp)),
					c_hp + formatNum(self.getDisplayMaxHp()), c_shield + formatNum(defendOldDisp * -1)));
		} else {
			appendToLog(lang.format("log.lose_shield", formatName(self.getUnitType().name(), self.getPos(), false),
					c_shield + formatNum(defendOldDisp)));
		}
		if (!self.isEffectBlock() && self.getShield() == 0) {
			appendToLog(lang.format("log.change_effect_block",
					formatName(self.getUnitType().name(), self.getPos(), false), 0));
		}
	}
}
