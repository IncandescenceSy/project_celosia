package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.StageType;
import io.github.celosia.sys.battle.Unit;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.battle.BattleLib.getStageBuffType;
import static io.github.celosia.sys.battle.BuffEffectLib.notifyOnChangeStage;
import static io.github.celosia.sys.menu.TextLib.getStageStatString;
import static io.github.celosia.sys.menu.TextLib.c_buff;
import static io.github.celosia.sys.menu.TextLib.c_num;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.getColor;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeStage implements SkillEffect {

	private final StageType stageType; // Stage to change
	private final int turns; // How many turns to change it for
	private final int stacks; // How much to change it by
	private final boolean isInstant;
	private final boolean giveToSelf;
	private final boolean mainTargetOnly;

	public ChangeStage(Builder builder) {
		this.stageType = builder.stageType;
		this.turns = builder.turns;
		this.stacks = builder.stacks;
		this.isInstant = builder.isInstant;
		this.giveToSelf = builder.giveToSelf;
		this.mainTargetOnly = builder.mainTargetOnly;
	}

	public static class Builder {
		private final StageType stageType;
		private final int turns;
		private final int stacks;
		private boolean isInstant;
		private boolean giveToSelf;
		private boolean mainTargetOnly;

		public Builder(StageType stageType, int turns, int stacks) {
			this.stageType = stageType;
			this.turns = turns;
			this.stacks = stacks;
		}

		public Builder notInstant() {
			this.isInstant = false;
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

		public ChangeStage build() {
			return new ChangeStage(this);
		}
	}

	@Override
	public ResultType apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
		if (!mainTargetOnly || isMainTarget) {
			List<String> msg = new ArrayList<>();
			String str = "";
			String str2 = "";

			// Apply self's durationMod
			// Sometimes this is wrong about the BuffType, but it shouldn't really matter
			// since it's only applied if it's correct
			// Unless some Passive has a weird interaction with it. Just keep it in mind
			// todo fix just in case
            Unit unit = (giveToSelf) ? self : target;

			int turnsMod = turns
					+ self.getDurationModBuffTypeDealt(getStageBuffType(target.getStage(stageType) + stacks))
					+ unit.getDurationModBuffTypeTaken(getStageBuffType(target.getStage(stageType) + stacks));

            int stacksMod = stacks + self.getStacksModBuffTypeDealt(getStageBuffType(unit.getStage(stageType) + stacks)) + unit.getStacksModBuffTypeTaken(getStageBuffType(unit.getStage(stageType) + stacks));

			notifyOnChangeStage(self, target, stageType, turnsMod, stacksMod);

			int stageOld = target.getStage(stageType);
			int stageNew = Math.clamp(stageOld + stacksMod, -5, 5);

			if (stageNew != stageOld) {
				String signOld = (stageOld > 0) ? "+" : "";
				String signNew = (stageNew > 0) ? "+" : "";

				str = formatName(unit.getUnitType().getName(), unit.getPos()) + " " + c_buff + stageType.getName()
						+ "[WHITE] " + lang.get("stage") + " " + getColor(stageOld) + signOld + stageOld + "[WHITE] → "
						+ getColor(stageNew) + signNew + stageNew;
				str2 = getStageStatString(unit, stageType, stageNew);

				unit.setStage(stageType, stageNew);
			}

			// Refresh turns
			if ((stageOld >= 0 && stacksMod >= 0) || (stageOld <= 0 && stacksMod <= 0)) {
				int turnsOld = unit.getStageTurns(stageType);
				if (turnsMod > turnsOld) {
					unit.setStageTurns(stageType, turnsMod);
					if (stageNew != stageOld)
						msg.add(str + "[WHITE], " + lang.get("turns") + " " + c_num + turnsOld + "[WHITE] → " + c_num
								+ turnsMod + str2);
					else
						msg.add(formatName(unit.getUnitType().getName(), unit.getPos()) + " " + c_buff
								+ stageType.getName() + "[WHITE] " + lang.get("stage") + " " + lang.get("turns") + " "
								+ c_num + turnsOld + "[WHITE] → " + c_num + turnsMod);
				} else if (stageNew != stageOld)
					msg.add(str + str2);
			} else if (stageNew != stageOld)
				msg.add(str + str2);

			if (!msg.isEmpty())
				appendToLog(msg);
		}

		return ResultType.SUCCESS;
	}

	@Override
	public boolean isInstant() {
		return isInstant;
	}
}
