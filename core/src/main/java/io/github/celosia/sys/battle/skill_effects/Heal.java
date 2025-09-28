package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.Unit;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.menu.TextLib.C_HP;
import static io.github.celosia.sys.menu.TextLib.C_NUM;
import static io.github.celosia.sys.menu.TextLib.C_POS;
import static io.github.celosia.sys.menu.TextLib.C_SHIELD;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.formatNum;
import static io.github.celosia.sys.menu.TextLib.getColor;
import static io.github.celosia.sys.settings.Lang.lang;

public class Heal implements SkillEffect {
	private final int pow;
	private final int overHeal; // Amount to heal over max HP in 10ths of a % (1000 = 100%)
	private final int shieldTurns;
	private final boolean isInstant;
	private final boolean giveToSelf;
	private final boolean mainTargetOnly;

	public Heal(Builder builder) {
		pow = builder.pow;
		overHeal = builder.overHeal;
		shieldTurns = builder.shieldTurns;
		isInstant = builder.isInstant;
		giveToSelf = builder.giveToSelf;
		mainTargetOnly = builder.mainTargetOnly;
	}

	public static class Builder {
		private final int pow;
		private int overHeal = 0;
		private int shieldTurns = 0;
		private boolean isInstant = false;
		private boolean giveToSelf = false;
		private boolean mainTargetOnly = false;

		public Builder(int pow) {
			this.pow = pow;
		}

		public Builder overHeal(int overHeal) {
			this.overHeal = overHeal;
			return this;
		}

		public Builder shieldTurns(int shieldTurns) {
			this.shieldTurns = shieldTurns;
			return this;
		}

		public Builder instant() {
			isInstant = true;
			return this;
		}

		public Builder giveToSelf() {
			giveToSelf = true;
			return this;
		}

		public Builder mainTargetOnly() {
			mainTargetOnly = true;
			return this;
		}

		public Heal build() {
			return new Heal(this);
		}
	}

	@Override
	public ResultType apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
		if (mainTargetOnly && !isMainTarget) {
			return ResultType.SUCCESS;
		}

		List<String> msg = new ArrayList<>();

		Unit unit = (giveToSelf) ? self : target;

		// Heals by pow% of user's Fth
		long heal = (long) (self.getFthWithStage() * (pow / 100d) * self.getMultWithExpHealingDealt()
				* unit.getMultWithExpHealingTaken());

		// Adds shield (shield + defend cannot exceed max HP)
		if (shieldTurns > 0) {
			String str = "";

			// Apply self's durationMod
			int turnsMod = shieldTurns + self.getModDurationBuffDealt() + unit.getModDurationBuffTaken();

			self.onDealShield(unit, turnsMod, heal);
			unit.onTakeShield(unit, turnsMod, heal);

			long hpMax = unit.getMaxHp();
			long shieldOld = unit.getShield();
			long shieldNew = (shieldOld + unit.getDefend() + heal > hpMax)
					? hpMax - unit.getDefend()
					: shieldOld + heal;
			int turnsOld = unit.getShieldTurns();

			if (shieldNew > shieldOld) {
				long shieldOldDisp = unit.getDisplayShield();

				unit.setShield(shieldNew);

				long hpMaxDisp = unit.getDisplayMaxHp();
				long shieldNewDisp = unit.getDisplayShield();

				str = lang.format("log.change_shield", formatName(unit.getUnitType().name(), unit.getPos()),
						C_SHIELD + formatNum((shieldOldDisp + unit.getDisplayDefend())),
						C_SHIELD + formatNum((shieldNewDisp + unit.getDisplayDefend())), C_HP + formatNum(hpMaxDisp),
						getColor(shieldNewDisp - shieldOldDisp) + "+" + formatNum((shieldNewDisp - shieldOldDisp)));
			}

			if (turnsMod > turnsOld) {
				unit.setShieldTurns(turnsMod);
				if (shieldNew > shieldOld) {
					msg.add(str + lang.format("log.turns.nameless", C_NUM + turnsOld, C_NUM + turnsMod));
				} else {
					msg.add(lang.format("log.shield.turns", formatName(unit.getUnitType().name(), unit.getPos()),
							C_NUM + turnsOld, C_NUM + turnsMod));
				}
			}

			// Effect block message
			if (shieldOld == 0 && shieldNew > 0 && !unit.isEffectBlock() && unit.getDefend() == 0) {
				msg.add(lang.format("log.change_effect_block",
						formatName(unit.getUnitType().name(), unit.getPos(), false), 1));
			}

		} else { // Heals
			self.onDealHeal(unit, heal, overHeal);
			unit.onTakeHeal(self, heal, overHeal);

			long hpCur = unit.getHp();
			long hpMax = unit.getMaxHp();
			// Picks the lower of (current HP + heal amount) and (maximum allowed overHeal
			// of this skill), and then the higher between that and current HP
			long hpNew = Math.max(hpCur, Math.min(hpCur + heal, (long) (hpMax * (1 + (overHeal / 1000d)))));

			if (hpNew > hpCur) {
				long hpOldDisp = unit.getDisplayHp();

				unit.setHp(hpNew);

				long hpNewDisp = unit.getDisplayHp();
				long hpMaxDisp = unit.getDisplayMaxHp();

				msg.add(lang.format("log.change_hp", formatName(unit.getUnitType().name(), self.getPos()),
						C_HP + formatNum(hpOldDisp), C_HP + formatNum(hpNewDisp), C_HP + formatNum(hpMaxDisp),
						C_POS + "+" + formatNum((hpNewDisp - hpOldDisp))));
			}
		}

		appendToLog(msg);
		return ResultType.SUCCESS;
	}

	@Override
	public boolean isInstant() {
		return isInstant;
	}
}
