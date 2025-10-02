package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.buff_effects.ChangeAff;
import io.github.celosia.sys.battle.buff_effects.ChangeHp;
import io.github.celosia.sys.battle.buff_effects.ChangeInfiniteSp;
import io.github.celosia.sys.battle.buff_effects.ChangeMod;
import io.github.celosia.sys.battle.buff_effects.ChangeMult;

import static io.github.celosia.sys.save.Lang.lang;

public class Passives {

    public static final Passive RESTORATION = new Passive(lang.get("passive.restoration"),
            lang.get("passive.restoration.desc"),
            "[+todo icon]",
            new ChangeHp.Builder(1000).build());

    // Change Affinities
    public static final Passive IGNIS_AFF_UP = new Passive(lang.get("passive.ignis_aff_up"),
            lang.get("passive.ignis_aff_up.desc"), "[+todo]", new ChangeAff(Elements.IGNIS, 1));

    // Change Mults
    public static final Passive PERCENTAGE_DMG_TAKEN_DOWN_50 = new Passive(
            lang.get("passive.percentage_dmg_taken_down_50"),
            lang.get("passive.percentage_dmg_taken_down_50.desc"), "[+todo icon]",
            new ChangeMult(Mult.PERCENTAGE_DMG_TAKEN, -500));
    public static final Passive PERCENTAGE_DMG_TAKEN_DOWN_999 = new Passive(
            lang.get("passive.percentage_dmg_taken_down_999"),
            lang.get("passive.percentage_dmg_taken_down_999.desc"), "[+todo icon]",
            new ChangeMult(Mult.PERCENTAGE_DMG_TAKEN, -999));

    // Change Mods
    public static final Passive BUFF_DURATION_UP = new Passive(lang.get("passive.buff_duration_up"),
            lang.get("passive.buff_duration_up.desc"), "[+todo icon]", new ChangeMod(Mod.DURATION_BUFF_DEALT, 1));
    public static final Passive DEBUFF_DURATION_UP = new Passive(lang.get("passive.debuff_duration_up"),
            lang.get("passive.debuff_duration_up.desc"), "[+todo icon]", new ChangeMod(Mod.DURATION_DEBUFF_DEALT, 1));

    // Change other stats
    public static final Passive ETERNAL_WELLSPRING = new Passive(lang.get("passive.eternal_wellspring"),
            lang.get("passive.eternal_wellspring.desc"), "[+todo icon]", new ChangeInfiniteSp(1));
}
