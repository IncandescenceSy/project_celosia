package io.github.celosia.sys.battle;

import static io.github.celosia.sys.settings.Lang.lang;

public enum StageType {
    ATK(lang.get("stage.atk")),
    DEF(lang.get("stage.def")),
    AGI(lang.get("stat.agi")),
    FTH(lang.get("stat.fth"));

    private final String name;

    StageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
