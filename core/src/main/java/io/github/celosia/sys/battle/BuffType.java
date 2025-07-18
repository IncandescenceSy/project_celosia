package io.github.celosia.sys.battle;

import static io.github.celosia.sys.settings.Lang.lang;

public enum BuffType {
    BUFF(lang.get("buff")),
    DEBUFF(lang.get("debuff"));

    private final String name;

    BuffType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
