package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BooleanStat;
import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Calcs;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;

// Todo heavily limit %-based damage on bosses
public record ChangeSp(int change, boolean isImmediate) implements BuffEffect {

    public ChangeSp(int change) {
        this(change, false);
    }

    @Override
    public void onGive(Unit self, int stacks) {
        if (isImmediate) {
            String changeStr = Calcs.changeSp(self, change * stacks);
            if (!self.isBooleanStat(BooleanStat.INFINITE_SP)) {
                appendToLog(changeStr);
            }
        }
    }

    @Override
    public String[] onTurnEnd(Unit self, int stacks) {
        if (!isImmediate) {
            String changeStr = Calcs.changeSp(self, change * stacks);
            if (!self.isBooleanStat(BooleanStat.INFINITE_SP)) {
                return new String[] { changeStr };
            }
        }

        return new String[] { "" };
    }
}
