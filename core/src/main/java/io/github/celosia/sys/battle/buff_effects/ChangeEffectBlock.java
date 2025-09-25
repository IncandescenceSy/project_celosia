package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeEffectBlock implements BuffEffect {
	private final int change; // Amount to add

	public ChangeEffectBlock(int change) {
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
		int effectBlockOld = self.getEffectBlock();
		int effectBlockNew = effectBlockOld + changeFull;
		self.setEffectBlock(effectBlockNew);
		if (self.getShield() == 0 && self.getDefend() == 0) {
			appendToLog(lang.format("log.change_effect_block",
					formatName(self.getUnitType().name(), self.getPos(), false), effectBlockNew));
		}
	}
}
