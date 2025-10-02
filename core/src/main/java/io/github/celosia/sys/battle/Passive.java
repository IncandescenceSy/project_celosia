package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.IconEntity;

public class Passive extends IconEntity {

    private final BuffEffect[] buffEffects;

    Passive(String name, String desc, String icon, BuffEffect... buffEffects) {
        super(name, desc, icon);
        this.buffEffects = buffEffects;
    }

    public BuffEffect[] getBuffEffects() {
        return buffEffects;
    }
}
