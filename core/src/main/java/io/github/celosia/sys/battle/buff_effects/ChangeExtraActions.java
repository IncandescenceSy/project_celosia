package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.menu.TextLib.formatPossessive;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeExtraActions implements BuffEffect {

    private final int change; // Amount to add

    public ChangeExtraActions(int change) {
        this.change = change;
    }

    @Override
    public String[] onGive(Unit self, int stacks) {
        int exAOld = self.getExtraActions();
        int exANew = exAOld + (change * stacks);
        self.setExtraActions(exANew);
        return new String[]{formatPossessive(self.getUnitType().getName()) + " " + lang.get("extra_actions") + " " + Math.max(exAOld, 0) + " -> " + Math.max(exANew, 0)};
    }

    @Override
    public String[] onRemove(Unit self, int stacks) {
        int exAOld = self.getExtraActions();
        int exANew = exAOld - (change * stacks);
        self.setExtraActions(exANew);
        return new String[]{formatPossessive(self.getUnitType().getName()) + " " + lang.get("extra_actions") + " " + Math.max(exAOld, 0) + " -> " + Math.max(exANew, 0)};
    }
}
