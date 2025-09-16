package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.Unit;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.menu.TextLib.c_buff;
import static io.github.celosia.sys.menu.TextLib.c_hp;
import static io.github.celosia.sys.menu.TextLib.c_num;
import static io.github.celosia.sys.menu.TextLib.c_shield;
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
			this.isInstant = true;
			return this;
		}

        public Builder giveToSelf() {
            this.giveToSelf = true;
            return this;
        }

		public Builder mainTargetOnly() {
			this.mainTargetOnly = true;
			return this;
		}

		public Heal build() {
			return new Heal(this);
		}
	}

	@Override
	public ResultType apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
		if (!mainTargetOnly || isMainTarget) {
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

				long hpMax = unit.getStatsDefault().getHp();
				long shieldCur = unit.getShield();
				long shieldNew = (shieldCur + unit.getDefend() + heal > hpMax)
						? hpMax - unit.getDefend()
						: shieldCur + heal;
				int turnsCur = unit.getShieldTurns();

				if (shieldNew > shieldCur) {
					long shieldCurDisp = unit.getDisplayShield();

                    unit.setShield(shieldNew);

					long hpMaxDisp = unit.getStatsDefault().getDisplayHp();
					long shieldNewDisp = unit.getDisplayShield();

					str = formatName(unit.getUnitType().name(), unit.getPos()) + " " + c_buff + lang.get("shield")
							+ " " + c_shield + formatNum((shieldCurDisp + unit.getDisplayDefend())) + "[WHITE] → "
							+ c_shield + formatNum((shieldNewDisp + unit.getDisplayDefend())) + "[WHITE]/" + c_shield
							+ formatNum(hpMaxDisp) + "[WHITE] (" + getColor(shieldNewDisp - shieldCurDisp) + "+"
							+ formatNum((shieldNewDisp - shieldCurDisp)) + "[WHITE])";
				}

				if (turnsMod > turnsCur) {
                    unit.setShieldTurns(turnsMod);
					if (shieldNew > shieldCur)
						msg.add(str + "[WHITE], " + lang.get("turns") + " " + c_num + turnsCur + "[WHITE] → " + c_num
								+ turnsMod);
					else
						msg.add(formatName(unit.getUnitType().name(), unit.getPos()) + " " + lang.get("shield")
								+ " " + lang.get("turns") + " " + c_num + turnsCur + "[WHITE] → " + c_num + turnsMod);
				}

				// Effect block message
				if (shieldCur == 0 && shieldNew > 0 && !unit.isEffectBlock() && unit.getDefend() == 0) {
					msg.add(formatName(unit.getUnitType().name(), unit.getPos(), false) + " "
							+ lang.get("log.is_now") + " " + c_buff + lang.get("log.effect_block"));
				}

			} else { // Heals
                self.onDealHeal(unit, heal, overHeal);
                unit.onTakeHeal(self, heal, overHeal);

				long hpCur = unit.getStatsCur().getHp();
				long hpMax = unit.getStatsDefault().getHp();
				// Picks the lower of (current HP + heal amount) and (maximum allowed overHeal
				// of this skill), and then the higher between that and current HP
				long hpNew = Math.max(hpCur, Math.min(hpCur + heal, (long) (hpMax * (1 + (overHeal / 1000d)))));

				if (hpNew > hpCur) {
					long hpCurDisp = unit.getStatsCur().getDisplayHp();

                    unit.getStatsCur().setHp(hpNew);

					long hpNewDisp = unit.getStatsCur().getDisplayHp();
					long hpMaxDisp = unit.getStatsDefault().getDisplayHp();

					msg.add(formatName(unit.getUnitType().name(), self.getPos()) + " " + lang.get("hp") + " " + c_hp
							+ formatNum(hpCurDisp) + "[WHITE] → " + c_hp + formatNum(hpNewDisp) + "[WHITE]/" + c_hp
							+ formatNum(hpMaxDisp) + getColor(hpNewDisp - hpCurDisp) + " (+"
							+ formatNum((hpNewDisp - hpCurDisp)) + ")");
				}
			}

			appendToLog(msg);
		}

		return ResultType.SUCCESS;
	}

	@Override
	public boolean isInstant() {
		return isInstant;
	}
}
