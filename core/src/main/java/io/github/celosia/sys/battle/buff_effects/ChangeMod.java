package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Mod;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.menu.TextLib.c_stat;
import static io.github.celosia.sys.menu.TextLib.formatMod;
import static io.github.celosia.sys.menu.TextLib.formatName;

public class ChangeMod implements BuffEffect {
	private final Mod mod;
	private final int change;

	public ChangeMod(Mod mod, int change) {
		this.mod = mod;
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
		int modOld = self.getMod(mod);
		int modNew = modOld + changeFull;

		self.setMod(mod, modNew);

		appendToLog(formatName(self.getUnitType().name(), self.getPos()) + " " + c_stat + mod.getName() + " "
				+ formatMod(modOld, mod) + "[WHITE] → " + formatMod(modNew, mod));
	}
}
