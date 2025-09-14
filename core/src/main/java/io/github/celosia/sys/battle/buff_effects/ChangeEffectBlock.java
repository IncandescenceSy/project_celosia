package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.menu.TextLib.c_stat;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeEffectBlock implements BuffEffect {
	private final int change; // Amount to add

	public ChangeEffectBlock(int change) {
		this.change = change;
	}

	@Override
	public void onGive(Unit self, int stacks) {
		int effectBlockOld = self.getEffectBlock();
		int effectBlockNew = effectBlockOld + (change * stacks);
		self.setEffectBlock(effectBlockNew);
		if (effectBlockNew > 0 && self.getShield() == 0 && self.getDefend() == 0)
			appendToLog(formatName(self.getUnitType().name(), self.getPos(), false) + " " + lang.get("log.is_now") + " "
					+ c_stat + lang.get("log.effect_block"));
	}

	@Override
	public void onRemove(Unit self, int stacks) {
		int effectBlockOld = self.getEffectBlock();
		int effectBlockNew = effectBlockOld - (change * stacks);
		self.setEffectBlock(effectBlockNew);
		if (effectBlockNew <= 0 && self.getShield() == 0 && self.getDefend() == 0)
			appendToLog(formatName(self.getUnitType().name(), self.getPos(), false) + " " + lang.get("log.is_no_longer")
					+ " " + c_stat + lang.get("log.effect_block"));
	}
}
