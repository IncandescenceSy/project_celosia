package io.github.celosia.sys.battle;

import static io.github.celosia.sys.settings.Lang.lang;

public enum Mult {
    DMG_DEALT(lang.get("mult.dmg_dealt"), true),
    DMG_TAKEN(lang.get("mult.dmg_taken"), false),
    IGNIS_DMG_DEALT(lang.get("mult.ignis_dmg_dealt"), true),
    IGNIS_DMG_TAKEN(lang.get("mult.ignis_dmg_taken"), false),
    GLACIES_DMG_DEALT(lang.get("mult.glacies_dmg_dealt"), true),
    GLACIES_DMG_TAKEN(lang.get("mult.glacies_dmg_taken"), false),
    FULGUR_DMG_DEALT(lang.get("mult.fulgur_dmg_dealt"), true),
    FULGUR_DMG_TAKEN(lang.get("mult.fulgur_dmg_taken"), false),
    VENTUS_DMG_DEALT(lang.get("mult.ventus_dmg_dealt"), true),
    VENTUS_DMG_TAKEN(lang.get("mult.ventus_dmg_taken"), false),
    TERRA_DMG_DEALT(lang.get("mult.terra_dmg_dealt"), true),
    TERRA_DMG_TAKEN(lang.get("mult.terra_dmg_taken"), false),
    LUX_DMG_DEALT(lang.get("mult.lux_dmg_dealt"), true),
    LUX_DMG_TAKEN(lang.get("mult.lux_dmg_taken"), false),
    MALUM_DMG_DEALT(lang.get("mult.malum_dmg_dealt"), true),
    MALUM_DMG_TAKEN(lang.get("mult.malum_dmg_taken"), false),
    WEAK_DMG_DEALT(lang.get("mult.weak_dmg_dealt"), true),
    WEAK_DMG_TAKEN(lang.get("mult.weak_dmg_taken"), false),
    FOLLOW_UP_DMG_DEALT(lang.get("mult.follow_up_dmg_dealt"), true),
    FOLLOW_UP_DMG_TAKEN(lang.get("mult.follow_up_dmg_taken"), false),
    DOT_DMG_TAKEN(lang.get("mult.dot_dmg_taken"), false),
    HEALING_DEALT(lang.get("mult.healing_dealt"), true),
    HEALING_TAKEN(lang.get("mult.healing_taken"), true),
    SP_GAIN(lang.get("mult.sp_gain"), true),
    SP_USE(lang.get("mult.sp_use"), false);

    private final String name;
    private final boolean isPositive; // true means more = better

    Mult(String name, boolean isPositive) {
        this.name = name;
        this.isPositive = isPositive;
    }

    public String getName() {
        return name;
    }

    public boolean isPositive() {
        return isPositive;
    }
}
