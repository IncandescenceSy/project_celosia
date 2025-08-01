package io.github.celosia.sys.battle;

import static io.github.celosia.sys.settings.Lang.lang;

public enum Mult {
    DMG_DEALT(lang.get("mult.dmg_dealt")),
    DMG_TAKEN(lang.get("mult.dmg_taken")),
    WEAK_DMG_DEALT(lang.get("mult.weak_dmg_dealt")),
    WEAK_DMG_TAKEN(lang.get("mult.weak_dmg_taken")),
    FOLLOW_UP_DMG_DEALT(lang.get("mult.follow_up_dmg_dealt")),
    FOLLOW_UP_DMG_TAKEN(lang.get("mult.follow_up_dmg_taken")),
    DOT_DMG_TAKEN(lang.get("mult.dot_dmg_taken")),
    HEALING_DEALT(lang.get("mult.healing_dealt")),
    HEALING_TAKEN(lang.get("mult.healing_taken"));

    private final String name;

    Mult(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
