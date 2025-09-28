package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Buff;
import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.BuffInstance;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.Unit;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.menu.TextLib.C_BUFF;
import static io.github.celosia.sys.menu.TextLib.C_NUM;
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
			giveToSelf = true;
			return this;
		}

		public Builder mainTargetOnly() {
			mainTargetOnly = true;
			return this;
		}

		public Builder notInstant() {
			isInstant = false;
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
			int turnsMod = turns + self.getDurationModBuffTypeDealt(buff.buffType())
					+ unit.getDurationModBuffTypeTaken(buff.buffType());

			int stacksMod = stacks + self.getStacksModBuffTypeDealt(buff.buffType())
					+ unit.getStacksModBuffTypeTaken(buff.buffType());

			self.onGiveBuff(target, buff, turnsMod, stacksMod);

			List<BuffInstance> buffInstances = unit.getBuffInstances();
			BuffInstance buffInstance = unit.findBuff(buff);

			String buffName = buff.icon() + C_BUFF + buff.name();

			if (buffInstance != null) { // Already has buff
				String str = "";
				// Add stacks
				int stacksOld = buffInstance.getStacks();
				int stacksNew = Math.min(buffInstance.getBuff().maxStacks(), stacksOld + stacksMod);
				if (stacksNew != stacksOld) {
					buffInstance.setStacks(stacksNew);
					str = lang.format("log.give_buff.stacks", formatName(unit.getUnitType().name(), unit.getPos()),
							buffName, C_NUM + stacksOld, C_NUM + stacksNew);
				}

				// Refresh turns
				int turnsOld = buffInstance.getTurns();
				if (turnsMod > turnsOld) {
					buffInstance.setTurns(turnsMod);

					if (stacksNew != stacksOld) {
						msg.add(str + lang.format("log.turns.nameless", C_NUM + turnsOld, C_NUM + turnsMod));
					} else {
						msg.add(lang.format("log.give_buff.turns", formatName(unit.getUnitType().name(), unit.getPos()),
								buffName, C_NUM + turnsOld, C_NUM + turnsMod));
					}
				}

				appendToLog(msg);

				// Apply newly added stacks
				int stacksAdded = stacksNew - stacksOld;
				if (stacksAdded > 0)
					for (BuffEffect buffEffect : buffInstance.getBuff().buffEffects())
						buffEffect.onGive(unit, stacksAdded);

			} else { // Doesn't have buff
				msg.add(lang.format("log.give_buff.gain", formatName(unit.getUnitType().name(), unit.getPos(), false),
						buffName, buff.maxStacks(), stacksMod, lang.format("log.stack_s", stacksMod), turnsMod));
				unit.addBuffInstance(new BuffInstance(buff, turnsMod, stacksMod));
				buffInstance = buffInstances.getLast();

				appendToLog(msg);

				// Apply
				BuffEffect[] buffEffects = buffInstance.getBuff().buffEffects();
				for (BuffEffect buffEffect : buffEffects) {
					buffEffect.onGive(unit, buffInstance.getStacks());
				}

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
