package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.BuffType;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.menu.TextLib.formatPossessive;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeDurationModBuffTypeDealt implements BuffEffect {

    private final BuffType buffType; // Type of buff to apply to
    private final int change; // Amount to change by

    public ChangeDurationModBuffTypeDealt(BuffType buffType, int change) {
        this.buffType = buffType;
        this.change = change;
    }

    @Override
    public String[] onGive(Unit self, int stacks) {
        int durOld = self.getDurationModBuffTypeDealt(buffType);
        int durNew = durOld + (change * stacks);
        self.setDurationModBuffTypeDealt(buffType, durNew);
        return new String[]{formatPossessive(self.getUnitType().getName()) + " " + ((buffType == BuffType.BUFF) ? lang.get("duration_mod_buff_dealt") : lang.get("duration_mod_debuff_dealt"))
            + ((durOld > 0) ? " +" : " ") + durOld + ((durNew > 0) ? " -> +" : " -> ") + durNew};
    }

    @Override
    public String[] onRemove(Unit self, int stacks) {
        int durOld = self.getDurationModBuffTypeDealt(buffType);
        int durNew = durOld - (change * stacks);
        self.setDurationModBuffTypeDealt(buffType, durNew);
        return new String[]{formatPossessive(self.getUnitType().getName()) + " " + ((buffType == BuffType.BUFF) ? lang.get("duration_mod_buff_dealt") : lang.get("duration_mod_debuff_dealt"))
            + ((durOld > 0) ? " +" : " ") + durOld + ((durNew > 0) ? " -> +" : " -> ") + durNew};
    }
}
