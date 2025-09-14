package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.Unit;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.battle.BuffEffectLib.notifyOnGiveShield;
import static io.github.celosia.sys.battle.BuffEffectLib.notifyOnHeal;
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
	private final boolean mainTargetOnly;

	public Heal(Builder builder) {
		pow = builder.pow;
		overHeal = builder.overHeal;
		shieldTurns = builder.shieldTurns;
		isInstant = builder.isInstant;
		mainTargetOnly = builder.mainTargetOnly;
	}

	public static class Builder {
		private final int pow;
		private int overHeal = 0;
		private int shieldTurns = 0;
		private boolean isInstant = false;
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

			// Heals by pow% of user's Fth
			long heal = (long) (self.getFthWithStage() * (pow / 100d) * self.getMultWithExpHealingDealt()
					* target.getMultWithExpHealingTaken());

			// Adds shield (shield + defend cannot exceed max HP)
			if (shieldTurns > 0) {
				String str = "";

				// Apply self's durationMod
				int turnsMod = shieldTurns + self.getModDurationBuffDealt() + target.getModDurationBuffTaken();

				notifyOnGiveShield(self, target, turnsMod, heal);

				long hpMax = target.getStatsDefault().getHp();
				long shieldCur = target.getShield();
				long shieldNew = (shieldCur + target.getDefend() + heal > hpMax)
						? hpMax - target.getDefend()
						: shieldCur + heal;
				int turnsCur = target.getShieldTurns();

				if (shieldNew > shieldCur) {
					long shieldCurDisp = target.getDisplayShield();

					target.setShield(shieldNew);

					long hpMaxDisp = target.getStatsDefault().getDisplayHp();
					long shieldNewDisp = target.getDisplayShield();

					str = formatName(target.getUnitType().name(), target.getPos()) + " " + c_buff + lang.get("shield")
							+ " " + c_shield + formatNum((shieldCurDisp + target.getDisplayDefend())) + "[WHITE] → "
							+ c_shield + formatNum((shieldNewDisp + target.getDisplayDefend())) + "[WHITE]/" + c_shield
							+ formatNum(hpMaxDisp) + "[WHITE] (" + getColor(shieldNewDisp - shieldCurDisp) + "+"
							+ formatNum((shieldNewDisp - shieldCurDisp)) + "[WHITE])";
				}

				if (turnsMod > turnsCur) {
					target.setShieldTurns(turnsMod);
					if (shieldNew > shieldCur)
						msg.add(str + "[WHITE], " + lang.get("turns") + " " + c_num + turnsCur + "[WHITE] → " + c_num
								+ turnsMod);
					else
						msg.add(formatName(target.getUnitType().name(), target.getPos()) + " " + lang.get("shield")
								+ " " + lang.get("turns") + " " + c_num + turnsCur + "[WHITE] → " + c_num + turnsMod);
				}

				// Effect block message
				if (shieldCur == 0 && shieldNew > 0 && !target.isEffectBlock() && target.getDefend() == 0) {
					msg.add(formatName(target.getUnitType().name(), target.getPos(), false) + " "
							+ lang.get("log.is_now") + " " + c_buff + lang.get("log.effect_block"));
				}

			} else { // Heals
				notifyOnHeal(self, target, heal, overHeal);

				long hpCur = target.getStatsCur().getHp();
				long hpMax = target.getStatsDefault().getHp();
				// Picks the lower of (current HP + heal amount) and (maximum allowed overHeal
				// of this skill), and then the higher between that and current HP
				long hpNew = Math.max(hpCur, Math.min(hpCur + heal, (long) (hpMax * (1 + (overHeal / 1000d)))));

				if (hpNew > hpCur) {
					long hpCurDisp = target.getStatsCur().getDisplayHp();

					target.getStatsCur().setHp(hpNew);

					long hpNewDisp = target.getStatsCur().getDisplayHp();
					long hpMaxDisp = target.getStatsDefault().getDisplayHp();

					msg.add(formatName(target.getUnitType().name(), self.getPos()) + " " + lang.get("hp") + " " + c_hp
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
