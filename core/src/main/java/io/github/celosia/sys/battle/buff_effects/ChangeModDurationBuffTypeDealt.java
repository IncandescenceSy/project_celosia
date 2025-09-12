package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.BuffType;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.menu.TextLib.c_stat;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.getColor;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeModDurationBuffTypeDealt implements BuffEffect {

	private final BuffType buffType; // Type of buff to apply to
	private final int change; // Amount to change by

	public ChangeModDurationBuffTypeDealt(BuffType buffType, int change) {
		this.buffType = buffType;
		this.change = change;
	}

	@Override
	public void onGive(Unit self, int stacks) {
		int durOld = self.getDurationModBuffTypeDealt(buffType);
		int durNew = durOld + (change * stacks);
		self.setDurationModBuffTypeDealt(buffType, durNew);
		appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat
				+ ((buffType == BuffType.BUFF)
						? lang.get("mod_duration_buff_dealt")
						: lang.get("mod_duration_debuff_dealt"))
				+ getColor(durOld) + ((durOld > 0) ? " +" : " ") + durOld + "[WHITE] → " + getColor(durNew)
				+ ((durNew > 0) ? "+" : "") + durNew);
	}

	@Override
	public void onRemove(Unit self, int stacks) {
		int durOld = self.getDurationModBuffTypeDealt(buffType);
		int durNew = durOld - (change * stacks);
		self.setDurationModBuffTypeDealt(buffType, durNew);
		appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat
				+ ((buffType == BuffType.BUFF)
						? lang.get("mod_duration_buff_dealt")
						: lang.get("mod_duration_debuff_dealt"))
				+ getColor(durOld) + ((durOld > 0) ? " +" : " ") + durOld + "[WHITE] → " + getColor(durNew)
				+ ((durNew > 0) ? "+" : "") + durNew);
	}
}
