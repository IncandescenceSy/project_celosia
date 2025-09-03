package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.menu.TextLib.*;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeExtraActions implements BuffEffect {

    private final int change; // Amount to add

    public ChangeExtraActions(int change) {
        this.change = change;
    }

    @Override
    public void onGive(Unit self, int stacks) {
        int exAOld = self.getExtraActions();
        int exANew = exAOld + (change * stacks);
        self.setExtraActions(exANew);
        appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + lang.get("extra_actions") + " " +
            getColor(exAOld) + Math.max(exAOld, 0) + "[WHITE] → " + getColor(exANew) + Math.max(exANew, 0));
    }

    @Override
    public void onRemove(Unit self, int stacks) {
        int exAOld = self.getExtraActions();
        int exANew = exAOld - (change * stacks);
        self.setExtraActions(exANew);
        appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + lang.get("extra_actions") + " " +
            getColor(exAOld) + Math.max(exAOld, 0) + "[WHITE] → " + getColor(exANew) + Math.max(exANew, 0));
    }
}
