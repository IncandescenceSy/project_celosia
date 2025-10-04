package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.BoolLib.isBothTruthy;
import static io.github.celosia.sys.util.TextLib.formatName;

public class ChangeEquipDisabled implements BuffEffect {

    private final int change;

    public ChangeEquipDisabled(int change) {
        this.change = change;
    }

    @Override
    public void onGive(Unit self, int stacks) {
        calc(self, change * stacks);
    }

    @Override
    public void onRemove(Unit self, int stacks) {
        calc(self, (change * stacks) * -1);
    }

    public void calc(Unit self, int changeFull) {
        int equipDisabledOld = self.getEffectBlock();
        int equipDisabledNew = equipDisabledOld + changeFull;
        self.setEffectBlock(equipDisabledNew);
        if (!isBothTruthy(equipDisabledOld, equipDisabledNew)) {
            appendToLog(lang.format("log.change_equip_disabled",
                    formatName(self.getUnitType().getName(), self.getPos()), equipDisabledNew));
        }
    }
}
