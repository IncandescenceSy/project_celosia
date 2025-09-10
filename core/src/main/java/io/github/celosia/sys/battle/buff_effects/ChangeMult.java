package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Mult;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.menu.TextLib.c_stat;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.getMultChangeColor;
import static io.github.celosia.sys.menu.TextLib.getMultColor;

public class ChangeMult implements BuffEffect {

	private final Mult mult;
	private final int change; // Amount to add in 100ths of a % (10000 = +100%)

	public ChangeMult(Mult mult, int change) {
		this.mult = mult;
		this.change = change;
	}

	@Override
	public void onGive(Unit self, int stacks) {
		int multOld = self.getMult(mult);
		int changeFull = change * stacks;
		self.setMult(mult, multOld + changeFull);
		int changeDisplay = (Math.max(multOld + changeFull, 1000) - Math.max(multOld, 1000)) / 100;
		appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + mult.getName() + " "
				+ getMultColor(multOld, mult) + (Math.max(multOld, 1000) / 100) + "%[WHITE] → "
				+ getMultColor(multOld + changeFull, mult) + (Math.max(multOld + changeFull, 1000) / 100) + "% [WHITE]("
				+ getMultChangeColor(changeFull, mult) + ((changeDisplay > 0) ? "+" : "") + changeDisplay
				+ "%[WHITE])");
	}

	@Override
	public void onRemove(Unit self, int stacks) {
		int multOld = self.getMult(mult);
		int changeFull = (change * stacks) * -1;
		self.setMult(mult, multOld + changeFull);
		int changeDisplay = (Math.max(multOld + changeFull, 1000) - Math.max(multOld, 1000)) / 100;
		appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + mult.getName() + " "
				+ getMultColor(multOld, mult) + (Math.max(multOld, 1000) / 100) + "%[WHITE] → "
				+ getMultColor(multOld + changeFull, mult) + (Math.max(multOld + changeFull, 1000) / 100) + "% [WHITE]("
				+ getMultChangeColor(changeDisplay, mult) + ((changeDisplay > 0) ? "+" : "") + changeDisplay
				+ "%[WHITE])");
	}
}
