package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Calcs;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;

// Todo heavily limit %-based damage on bosses
public class ChangeSp implements BuffEffect {
	private final int change; // Amount to change SP by
	private final boolean isImmediate; // If true, happens onGive. If false, happens onTurnEnd

	public ChangeSp(int change, boolean isImmediate) {
		this.change = change;
		this.isImmediate = isImmediate;
	}

	public ChangeSp(int change) {
		this(change, false);
	}

	@Override
	public void onGive(Unit self, int stacks) {
		if (isImmediate) {
			appendToLog(Calcs.changeSp(self, change * stacks));
		}
	}

	@Override
	public String[] onTurnEnd(Unit self, int stacks) {
		if (!isImmediate) {
			return new String[]{Calcs.changeSp(self, change * stacks)};
		}

		return new String[]{""};
	}
}
