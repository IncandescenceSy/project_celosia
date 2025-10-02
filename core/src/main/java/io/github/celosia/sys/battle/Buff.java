package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.IconEntity;

public class Buff extends IconEntity {

    private final BuffType buffType;
    private final int maxStacks;
    private final BuffEffect[] buffEffects;

    Buff(String name, String desc, String icon, BuffType buffType, int maxStacks, BuffEffect... buffEffects) {
        super(name, desc, icon);
        this.buffType = buffType;
        this.maxStacks = maxStacks;
        this.buffEffects = buffEffects;
    }

    Buff(String name, String desc, String icon, BuffType buffType, BuffEffect... buffEffects) {
        this(name, desc, icon, buffType, 1, buffEffects);
    }

    public BuffType getBuffType() {
        return buffType;
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public BuffEffect[] getBuffEffects() {
        return buffEffects;
    }
}
