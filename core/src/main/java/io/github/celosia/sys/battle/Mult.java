package io.github.celosia.sys.battle;

import static io.github.celosia.sys.settings.Lang.lang;

public enum Mult {
    DMG_DEALT(lang.get("mult.dmg_dealt")),
    DMG_TAKEN(lang.get("mult.dmg_taken")),
    IGNIS_DMG_DEALT(lang.get("mult.ignis_dmg_dealt")),
    IGNIS_DMG_TAKEN(lang.get("mult.ignis_dmg_taken")),
    GLACIES_DMG_DEALT(lang.get("mult.glacies_dmg_dealt")),
    GLACIES_DMG_TAKEN(lang.get("mult.glacies_dmg_taken")),
    FULGUR_DMG_DEALT(lang.get("mult.fulgur_dmg_dealt")),
    FULGUR_DMG_TAKEN(lang.get("mult.fulgur_dmg_taken")),
    VENTUS_DMG_DEALT(lang.get("mult.ventus_dmg_dealt")),
    VENTUS_DMG_TAKEN(lang.get("mult.ventus_dmg_taken")),
    TERRA_DMG_DEALT(lang.get("mult.terra_dmg_dealt")),
    TERRA_DMG_TAKEN(lang.get("mult.terra_dmg_taken")),
    LUX_DMG_DEALT(lang.get("mult.lux_dmg_dealt")),
    LUX_DMG_TAKEN(lang.get("mult.lux_dmg_taken")),
    MALUM_DMG_DEALT(lang.get("mult.malum_dmg_dealt")),
    MALUM_DMG_TAKEN(lang.get("mult.malum_dmg_taken")),
    WEAK_DMG_DEALT(lang.get("mult.weak_dmg_dealt")),
    WEAK_DMG_TAKEN(lang.get("mult.weak_dmg_taken")),
    FOLLOW_UP_DMG_DEALT(lang.get("mult.follow_up_dmg_dealt")),
    FOLLOW_UP_DMG_TAKEN(lang.get("mult.follow_up_dmg_taken")),
    DOT_DMG_TAKEN(lang.get("mult.dot_dmg_taken")),
    HEALING_DEALT(lang.get("mult.healing_dealt")),
    HEALING_TAKEN(lang.get("mult.healing_taken")),
    SP_GAIN(lang.get("mult.sp_gain"));

    private final String name;

    Mult(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
