package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Buff;
import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.BuffInstance;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.Unit;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.battle.BuffEffectLib.notifyOnGiveBuff;
import static io.github.celosia.sys.menu.TextLib.c_buff;
import static io.github.celosia.sys.menu.TextLib.c_num;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.settings.Lang.lang;

public class GiveBuff implements SkillEffect {

	private final Buff buff;
	private final int turns;
	private final int stacks;
	private final ResultType minResult;
	private final boolean giveToSelf;
	private final boolean mainTargetOnly;
	private final boolean isInstant;

	public GiveBuff(Builder builder) {
		buff = builder.buff;
		turns = builder.turns;
		stacks = builder.stacks;
		minResult = builder.minResult;
		isInstant = builder.isInstant;
		giveToSelf = builder.giveToSelf;
		mainTargetOnly = builder.mainTargetOnly;
	}

	public static class Builder {
		private final Buff buff;
		private final int turns;
		private int stacks = 1;
		private ResultType minResult = ResultType.SUCCESS;
		private boolean giveToSelf = false;
		private boolean mainTargetOnly = false;
		private boolean isInstant = true;

		public Builder(Buff buff, int turns) {
			this.buff = buff;
			this.turns = turns;
		}

		public Builder stacks(int stacks) {
			this.stacks = stacks;
			return this;
		}

		public Builder minResult(ResultType minResult) {
			this.minResult = minResult;
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

		public Builder notInstant() {
			this.isInstant = false;
			return this;
		}

		public GiveBuff build() {
			return new GiveBuff(this);
		}
	}

	@Override
	public ResultType apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
		// Most attacks don't apply buffs if the previous hit was blocked by Shield or
		// immunity
		if (resultPrev.ordinal() >= minResult.ordinal() && (!mainTargetOnly || isMainTarget)) {
			List<String> msg = new ArrayList<>();

			Unit unit = (giveToSelf) ? self : target;

			// Apply durationMod
			int turnsMod = turns + self.getDurationModBuffTypeDealt(buff.getBuffType())
					+ unit.getDurationModBuffTypeTaken(buff.getBuffType());

			int stacksMod = stacks + self.getStacksModBuffTypeDealt(buff.getBuffType())
					+ unit.getStacksModBuffTypeTaken(buff.getBuffType());

			notifyOnGiveBuff(self, target, buff, turnsMod, stacksMod);

			List<BuffInstance> buffInstances = unit.getBuffInstances();
			BuffInstance buffInstance = unit.findBuff(buff);

			if (buffInstance != null) { // Already has buff
				String str = "";

				// Refresh turns
				int turnsOld = buffInstance.getTurns();
				if (turnsMod > turnsOld) {
					buffInstance.setTurns(turnsMod);
					str = formatName(unit.getUnitType().getName(), unit.getPos()) + " " + c_buff + buff.getName()
							+ "[WHITE] " + lang.format("turn_s", turnsMod) + " " + c_num + turnsOld + "[WHITE] → "
							+ c_num + turnsMod;
				}

				// Add stacks
				int stacksOld = buffInstance.getStacks();
				int stacksNew = Math.min(buffInstance.getBuff().getMaxStacks(), stacksOld + stacksMod);
				if (stacksNew != stacksOld) {
					buffInstance.setStacks(stacksNew);
					if (turnsMod > turnsOld)
						msg.add(str + "[WHITE], " + lang.format("stack_s", stacksNew) + " " + c_num + stacksOld
								+ "[WHITE] → " + c_num + stacksNew);
					else
						msg.add(formatName(unit.getUnitType().getName(), unit.getPos()) + " " + c_buff + buff.getName()
								+ " " + lang.get("stacks") + " " + c_num + stacksOld + "[WHITE] → " + c_num
								+ stacksNew);
				} else
					msg.add(str);

				appendToLog(msg);

				// Apply newly added stacks
				int stacksAdded = stacksNew - stacksOld;
				if (stacksAdded > 0)
					for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects())
						buffEffect.onGive(unit, stacksAdded);

			} else { // Doesn't have buff
				msg.add(formatName(unit.getUnitType().getName(), unit.getPos(), false) + " " + lang.get("log.gains")
						+ " " + c_buff + buff.getName() + "[WHITE] "
						+ ((buff.getMaxStacks() > 1)
								? (lang.get("log.with") + " " + c_num + stacks + " [WHITE]"
										+ lang.format("stack_s", stacksMod) + " " + lang.get("log.and")) + " "
								: lang.get("log.for") + " ")
						+ c_num + turnsMod + "[WHITE] " + lang.format("turn_s", turnsMod));
				unit.addBuffInstance(new BuffInstance(buff, turnsMod, stacksMod));
				buffInstance = buffInstances.getLast();

				appendToLog(msg);

				// Apply
				BuffEffect[] buffEffects = buffInstance.getBuff().getBuffEffects();
				for (BuffEffect buffEffect : buffEffects)
					buffEffect.onGive(unit, buffInstance.getStacks());

			}
		}

		// Returns success even if nothing happened because failure to apply a buff as a
		// secondary effect shouldn't fail the entire skill
		return ResultType.SUCCESS;
	}

	@Override
	public boolean isInstant() {
		return isInstant;
	}
}
