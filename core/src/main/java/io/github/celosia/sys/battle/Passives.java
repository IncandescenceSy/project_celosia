package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.buff_effects.ChangeAffinity;
import io.github.celosia.sys.battle.buff_effects.ChangeBooleanStat;
import io.github.celosia.sys.battle.buff_effects.ChangeHp;
import io.github.celosia.sys.battle.buff_effects.ChangeMod;
import io.github.celosia.sys.battle.buff_effects.ChangeMult;

import static io.github.celosia.sys.save.Lang.lang;

public class Passives {

    // Change Affinities
    public static final Passive IGNIS_AFF_UP = new Passive.Builder(lang.get("passive.ignis_aff_up"),
            lang.get("passive.ignis_aff_up.desc"), "[+todo]").effects(new ChangeAffinity(Elements.IGNIS, 1)).build();

    // Change Mults
    public static final Passive PERCENTAGE_DMG_TAKEN_DOWN_50 = new Passive.Builder(
            lang.get("passive.percentage_dmg_taken_down_50"), lang.get("passive.percentage_dmg_taken_down_50.desc"),
            "[+todo icon]").effects(new ChangeMult(Mult.PERCENTAGE_DMG_TAKEN, -500)).build();
    public static final Passive PERCENTAGE_DMG_TAKEN_DOWN_999 = new Passive.Builder(
            lang.get("passive.percentage_dmg_taken_down_999"), lang.get("passive.percentage_dmg_taken_down_999.desc"),
            "[+todo icon]").effects(new ChangeMult(Mult.PERCENTAGE_DMG_TAKEN, -999)).build();

    // Change Mods
    public static final Passive BUFF_DURATION_UP = new Passive.Builder(lang.get("passive.buff_duration_up"),
            lang.get("passive.buff_duration_up.desc"), "[+todo icon]")
            .effects(new ChangeMod(Mod.DURATION_BUFF_DEALT, 1))
            .build();
    public static final Passive DEBUFF_DURATION_UP = new Passive.Builder(lang.get("passive.debuff_duration_up"),
            lang.get("passive.debuff_duration_up.desc"), "[+todo icon]")
            .effects(new ChangeMod(Mod.DURATION_DEBUFF_DEALT, 1)).build();

    // Change bool stats
    public static final Passive ETERNAL_WELLSPRING = new Passive.Builder(lang.get("passive.eternal_wellspring"),
            lang.get("passive.eternal_wellspring.desc"), "[+todo icon]")
            .effects(new ChangeBooleanStat(BooleanStat.INFINITE_SP, 1)).build();

    // Misc
    public static final Passive RESTORATION = new Passive.Builder(lang.get("passive.restoration"),
            lang.get("passive.restoration.desc"), "[+todo icon]").effects(new ChangeHp.Builder(1000).build()).build();
}
