package io.github.celosia.sys.battle.passive_effects;

import io.github.celosia.sys.battle.BuffType;
import io.github.celosia.sys.battle.PassiveEffect;
import io.github.celosia.sys.battle.Unit;

public class ChangeDurationModBuffTypeGiven implements PassiveEffect {

    private final BuffType buffType; // Type of buff to apply to
    private final int change; // Amount to change by

    public ChangeDurationModBuffTypeGiven(BuffType buffType, int change) {
        this.buffType = buffType;
        this.change = change;
    }

    @Override
    public String[] onBattleStart(Unit self) {
        self.setDurationModBuffTypeDealt(buffType, self.getDurationModBuffTypeDealt(buffType) + change);
        return new String[]{""};
    }
}
