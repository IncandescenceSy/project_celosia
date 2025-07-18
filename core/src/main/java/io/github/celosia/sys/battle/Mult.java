package io.github.celosia.sys.battle;

import static io.github.celosia.sys.settings.Lang.lang;

public enum Mult {
    ATK(lang.get("mult.atk")),
    DEF(lang.get("mult.def"));

    private final String name;

    Mult(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
