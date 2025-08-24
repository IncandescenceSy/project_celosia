package io.github.celosia.sys.battle;

public class Passive {
    private final String name;
    private final String desc;
    private final PassiveEffect[] passiveEffects;

    Passive(String name, String desc, PassiveEffect... passiveEffects) {
        this.name = name;
        this.desc = desc;
        this.passiveEffects = passiveEffects;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public PassiveEffect[] getPassiveEffects() {
        return passiveEffects;
    }
}
