package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Mod;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_STAT;
import static io.github.celosia.sys.util.TextLib.formatMod;
import static io.github.celosia.sys.util.TextLib.formatName;

public record ChangeMod(Mod mod, int change) implements BuffEffect {

    @Override
    public void onGive(Unit self, int stacks) {
        calc(self, change * stacks);
    }

    @Override
    public void onRemove(Unit self, int stacks) {
        calc(self, (change * stacks) * -1);
    }

    public void calc(Unit self, int changeFull) {
        int modOld = self.getMod(mod);
        int modNew = modOld + changeFull;

        self.setMod(mod, modNew);
        appendToLog(lang.format("log.change_mod", formatName(self.getUnitType().getName(), self.getPos()),
                C_STAT + mod.getName(), formatMod(modOld, mod), formatMod(modNew, mod)));
    }
}
