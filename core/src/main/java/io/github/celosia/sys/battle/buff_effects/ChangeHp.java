package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.menu.TextLib.c_hp;
import static io.github.celosia.sys.menu.TextLib.formatNum;
import static io.github.celosia.sys.menu.TextLib.getColor;
import static io.github.celosia.sys.menu.TextLib.getSign;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeHp implements BuffEffect {
	private final int change; // Amount to change HP by. If isPercentage, 1000 = +100%
	private final boolean isImmediate; // If true, happens onGive. If false, happens onTurnEnd
	private final boolean isPercentage; // If false, uses the raw number of change instead
	private final boolean isPierce;

	public ChangeHp(Builder builder) {
		change = builder.change;
		isImmediate = builder.isImmediate;
		isPercentage = builder.isPercentage;
		isPierce = builder.isPierce;
	}

	public static class Builder {
		private final int change;
		private boolean isImmediate = false;
		private boolean isPercentage = true;
		private boolean isPierce = false;

		public Builder(int change) {
			this.change = change;
		}

		public Builder immediate() {
			this.isImmediate = true;
			return this;
		}

		public Builder notPercentage() {
			this.isPercentage = false;
			return this;
		}

		public Builder pierce() {
			this.isPierce = true;
			return this;
		}

		public ChangeHp build() {
			return new ChangeHp(this);
		}
	}

	// todo this might need to display the name if immediate
	@Override
	public void onGive(Unit self, int stacks) {
		if (isImmediate) {
			appendToLog(calc(self, stacks));
		}
	}

	@Override
	public String[] onTurnEnd(Unit self, int stacks) {
		if (!isImmediate) {
			return calc(self, stacks);
		} else {
			return new String[]{""};
		}
	}

	private String[] calc(Unit self, int stacks) {
		if (change < 0) { // Damage
			double multDoTDmgTaken = ((isImmediate) ? 1 : self.getMultWithExpDoTDmgTaken());
			long dmg = (isPercentage)
					? (long) Math.abs(((self.getMaxHp() * (change / 1000d)) * stacks)
							* self.getMultWithExpDmgTaken() * multDoTDmgTaken * self.getMultWithExpPercentageDmgTaken())
					: (long) (change * self.getMultWithExpDmgTaken() * multDoTDmgTaken);
			self.onTakeDamage(self, dmg, Element.VIS);
			Result result = self.damage(dmg, isPierce, false);
			return result.messages().toArray(String[]::new);
		} else { // Healing
			long hpOld = self.getHp();
			long hpMax = self.getMaxHp();
			long heal = (long) (change * ((isPercentage) ? hpMax : 1) * stacks * self.getMultWithExpHealingTaken());
			long hpNew = Math.max(hpOld, Math.min(hpOld + heal, hpMax));
			if (hpNew > hpOld) {
				self.onTakeHeal(self, heal, 0);

				long hpOldDisp = self.getDisplayHp();
				self.setHp(hpNew);
				long hpNewDisp = self.getDisplayHp();
				long hpMaxDisp = self.getDisplayMaxHp();
                long changeFullDisp = Math.max(hpNewDisp - hpOldDisp, 0);

				return new String[]{lang.format("log.change_hp", "", c_hp + formatNum(hpOldDisp), c_hp + formatNum(hpNewDisp), c_hp + formatNum(hpMaxDisp),
						getColor(changeFullDisp) + getSign(changeFullDisp) + formatNum(changeFullDisp))};
			} else
				return new String[]{""};
		}
	}
}
