package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Mult;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.menu.TextLib.c_exp;
import static io.github.celosia.sys.menu.TextLib.c_stat;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.formatNum;
import static io.github.celosia.sys.menu.TextLib.getExpChangeColor;
import static io.github.celosia.sys.menu.TextLib.getExpColor;
import static io.github.celosia.sys.menu.TextLib.getMultChangeColor;
import static io.github.celosia.sys.menu.TextLib.getMultColor;
import static io.github.celosia.sys.menu.TextLib.getMultWithExpChangeColor;
import static io.github.celosia.sys.menu.TextLib.getMultWithExpColor;

public class ChangeMult implements BuffEffect {
	private final Mult mult;
	private final int changeMult; // Amount to add to mult in 10ths of a % (1000 = +100%)
	private final int changeExp; // Amount to add to exp in 100ths (100 = +1)

	public ChangeMult(Mult mult, int changeMult, int changeExp) {
		this.mult = mult;
		this.changeMult = changeMult;
		this.changeExp = changeExp;
	}

	public ChangeMult(Mult mult, int changeMult) {
		this(mult, changeMult, 0);
	}

	@Override
	public void onGive(Unit self, int stacks) {
		calc(self, changeMult * stacks, changeExp * stacks, true);
	}

	@Override
	public void onRemove(Unit self, int stacks) {
		calc(self, (changeMult * stacks) * -1, (changeExp * stacks) * -1, false);
	}

	public void calc(Unit self, int changeMultFull, int changeExpFull, boolean giving) {
		int multOld = self.getMult(mult);
		int multNew = multOld + changeMultFull;
		int changeMultDisplay = (Math.max(multNew, 100) - Math.max(multOld, 100)) / 10;
		int expOld = self.getExp(mult);
		int expNew = expOld + changeExpFull;
		double changeExpDisplay = (Math.max(expNew, 10) - Math.max(expOld, 10)) / 100d;
		double multWithExpOld = self.getMultWithExp(mult);

		self.setMult(mult, multNew);
		self.setExp(mult, expNew);

		double multWithExpNew = self.getMultWithExp(mult);
		double changeMultWithExpDisplay = Math.max(multWithExpNew, 0.1) - Math.max(multWithExpOld, 0.1);

		// The final mult with the exponent calculated as displays
		String calcedOld = "";
		String calcedNew = "";
		String calcedChange = "[WHITE])";

		if (expOld != 100) {
			calcedOld = getExpColor(expOld, multOld, mult) + "^" + formatNum(expOld / 100d) + " " + c_exp + "("
					+ getMultWithExpColor(multWithExpOld, mult) + formatNum(multWithExpOld * 100) + "%[WHITE]" + c_exp
					+ ")[WHITE]";
		}

		if (expNew != 100) {
			calcedNew = getExpColor(expNew, multNew, mult) + "^" + formatNum(expNew / 100d) + " " + c_exp + "("
					+ getMultWithExpColor(multWithExpNew, mult) + formatNum(multWithExpNew * 100) + "%[WHITE]" + c_exp
					+ ")[WHITE]";
		}

		if (expOld != expNew) {
			calcedChange = getExpChangeColor(changeExpFull, (giving) ? multNew : multOld, mult) + "^"
					+ ((changeExpDisplay > 0) ? "+" : "") + formatNum(changeExpDisplay) + "[WHITE]) " + c_exp + "("
					+ getMultWithExpChangeColor(changeMultWithExpDisplay, mult)
					+ ((changeMultWithExpDisplay > 0) ? "+" : "") + formatNum(changeMultWithExpDisplay * 100)
					+ "%[WHITE]" + c_exp + ")[WHITE]";
		}

		appendToLog(formatName(self.getUnitType().name(), self.getPos()) + " " + c_stat + mult.getName() + " "
				+ getMultColor(multOld, mult) + formatNum(Math.max(multOld, 100) / 10) + "%" + calcedOld + "[WHITE] → "
				+ getMultColor(multNew, mult) + formatNum(Math.max(multNew, 100) / 10) + "%" + calcedNew + "[WHITE] ("
				+ getMultChangeColor(changeMultFull, mult) + ((changeMultDisplay > 0) ? "+" : "")
				+ formatNum(changeMultDisplay) + "%" + calcedChange);
	}
}
