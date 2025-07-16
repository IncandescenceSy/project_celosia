package io.github.celosia.sys.battle;

public enum StageType {
    ATK("Atk"),
    DEF("Def"),
    AGI("Agi"),
    FTH("Fth");

    private final String name;

    StageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
