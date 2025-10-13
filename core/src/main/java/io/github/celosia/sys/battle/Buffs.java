package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.buff_effects.ChangeBooleanStat;
import io.github.celosia.sys.battle.buff_effects.ChangeDefend;
import io.github.celosia.sys.battle.buff_effects.ChangeExtraActions;
import io.github.celosia.sys.battle.buff_effects.ChangeHp;
import io.github.celosia.sys.battle.buff_effects.ChangeMult;
import io.github.celosia.sys.battle.buff_effects.ChangeShield;
import io.github.celosia.sys.battle.buff_effects.ChangeStat;

import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_NEG;
import static io.github.celosia.sys.util.TextLib.C_SHIELD;
import static io.github.celosia.sys.util.TextLib.C_STAT;

public class Buffs {

    // Temp testing

    /// Basic
    public static final Buff DEFEND = new Buff.Builder(lang.get("buff.defend"), "todo", "[azure][+shield]",
            BuffType.BUFF)
            .effects(new ChangeDefend(200), new ChangeMult(Mult.DMG_TAKEN, -200),
                    new ChangeBooleanStat(BooleanStat.EFFECT_BLOCK, 1))
            .build();

    // When setting stack amount in GiveBuff, 1 converts to 1% of user's Fth
    public static final Buff SHIELD = new Buff.Builder(lang.get("buff.shield"), "todo",
            C_SHIELD + "[+vibrating-shield]", BuffType.BUFF)
            .effects(new ChangeShield(), new ChangeBooleanStat(BooleanStat.EFFECT_BLOCK, 1)).build();

    // Decreases multDmgTaken by way more than necessary to be absolutely certain this minimizes it
    public static final Buff PROTECT = new Buff.Builder(lang.get("buff.protect"), "todo", "[azure][+shieldcomb]",
            BuffType.BUFF).effects(new ChangeMult(Mult.DMG_TAKEN, -10_000_000),
                    new ChangeBooleanStat(BooleanStat.EFFECT_BLOCK, 1))
            .build();

    public static final Buff STUNNED = new Buff.Builder(lang.get("buff.stunned"), "todo",
            "[YELLOW][+knocked-out-stars]", BuffType.DEBUFF)
            .effects(new ChangeBooleanStat(BooleanStat.UNABLE_TO_ACT, 1)).build();

    // todo: affinity should convey immunity to these
    // Elemental debuffs
    public static final Buff BURN = new Buff.Builder(lang.get("buff.burn"), "buff_desc.2.per_stack.hp",
            "[ORANGE][+small-fire]",
            BuffType.DEBUFF).maxStacks(5).effects(new ChangeHp.Builder(-20).build(), new ChangeStat(Stat.STR, -50))
            .descArgs(C_NEG + "-2%" + C_STAT, C_NEG + "-5% " + C_STAT + lang.get("stat.str")).build();
    public static final Buff FROSTBITE = new Buff.Builder(lang.get("buff.frostbite"), "buff_desc.2.per_stack.hp",
            "[azure][+snowflake-1]", BuffType.DEBUFF).maxStacks(5).effects(new ChangeHp.Builder(-20).build(),
                    new ChangeStat(Stat.MAG, -50))
            .descArgs(C_NEG + "-2%" + C_STAT, C_NEG + "-5% " + C_STAT + lang.get("stat.mag")).build();
    public static final Buff SHOCK = new Buff.Builder(lang.get("buff.shock"), "buff_desc.2.per_stack.hp",
            "[YELLOW][+power-lightning]",
            BuffType.DEBUFF).maxStacks(5).effects(new ChangeHp.Builder(-20).build(), new ChangeStat(Stat.AGI, -50))
            .descArgs(C_NEG + "-2%" + C_STAT, C_NEG + "-5% " + C_STAT + lang.get("stat.agi"))
            .build();
    public static final Buff WINDSWEPT = new Buff.Builder(lang.get("buff.windswept"), "buff_desc.2.per_stack",
            "[LIGHT GREEN][+whirlwind]", BuffType.DEBUFF).maxStacks(5).effects(new ChangeMult(Mult.SP_GAIN, -50),
                    new ChangeStat(Stat.RES, -50))
            .descArgs(C_NEG + "-5% " + C_STAT + lang.get("mult.sp_gain"),
                    C_NEG + "-5% " + C_STAT + lang.get("stat.res"))
            .build();
    public static final Buff TREMOR = new Buff.Builder(lang.get("buff.tremor"), "buff_desc.2.per_stack",
            "[LIGHT brown][+earth-spit]",
            BuffType.DEBUFF).maxStacks(5).effects(new ChangeMult(Mult.SP_GAIN, -50), new ChangeStat(Stat.AMR, -50))
            .descArgs(C_NEG + "-5% " + C_STAT + lang.get("mult.sp_gain"),
                    C_NEG + "-5% " + C_STAT + lang.get("stat.amr"))
            .build();
    public static final Buff DAZZLED = new Buff.Builder(lang.get("buff.dazzled"), "buff_desc.2.per_stack",
            "[+star-formation]",
            BuffType.DEBUFF).maxStacks(5).effects(new ChangeMult(Mult.SP_GAIN, -50), new ChangeStat(Stat.FTH, -50))
            .descArgs(C_NEG + "-5% " + C_STAT + lang.get("mult.sp_gain"),
                    C_NEG + "-5% " + C_STAT + lang.get("stat.fth"))
            .build();
    public static final Buff CURSE = new Buff.Builder(lang.get("buff.curse"), "buff_desc.2.per_stack.hp",
            "[RED][+dread-skull]",
            BuffType.DEBUFF).maxStacks(5).effects(new ChangeHp.Builder(-25).build(), new ChangeStat(Stat.FTH, -50))
            .descArgs(C_NEG + "-2.5%" + C_STAT, C_NEG + "-5% " + C_STAT + lang.get("stat.fth"))
            .build();
    public static final Buff POISON = new Buff.Builder(lang.get("buff.poison"), "buff_desc.2.per_stack.hp",
            "[PURPLE][+bubbles]",
            BuffType.DEBUFF).maxStacks(5)
            .effects(new ChangeHp.Builder(-25).build(), new ChangeMult(Mult.HEALING_TAKEN, -50))
            .descArgs(C_NEG + "-2.5%" + C_STAT, C_NEG + "-5% " + C_STAT + lang.get("mult.healing_taken"))
            .build();
    public static final Buff RADIATION = new Buff.Builder(lang.get("buff.radiation"), "buff_desc.2.per_stack.hp",
            "[CYAN][+nuclear]",
            BuffType.DEBUFF).maxStacks(5).effects(new ChangeHp.Builder(-30).build(), new ChangeMult(Mult.DMG_TAKEN, 5))
            .descArgs(C_NEG + "-3%" + C_STAT, C_NEG + "+5% " + C_STAT + lang.get("mult.dmg_taken"))
            .build();

    // Superior elemental debuffs
    public static final Buff BURNT_OUT = new Buff.Builder(lang.get("buff.burnt_out"), "todo",
            "[ORANGE][+fire-wave]", BuffType.DEBUFF).effects(new ChangeMult(Mult.IGNIS_DMG_TAKEN, 200),
                    new ChangeStat(Stat.STR, -100))
            .build();
    public static final Buff FROSTBOUND = new Buff.Builder(lang.get("buff.frostbound"), "buff_desc.2",
            "[azure][+ice-bolt]", BuffType.DEBUFF).effects(new ChangeMult(Mult.GLACIES_DMG_TAKEN, 200),
                    new ChangeStat(Stat.MAG, -100))
            .descArgs(C_NEG + "+20% " + C_STAT + Mult.GLACIES_DMG_TAKEN.getName(),
                    C_NEG + "-10% " + C_STAT + lang.get("stat.mag"))
            .build();
    public static final Buff OVERLOADED = new Buff.Builder(lang.get("buff.overloaded"), "todo",
            "[YELLOW][+lightning-tree]", BuffType.DEBUFF).effects(new ChangeMult(Mult.FULGUR_DMG_TAKEN, 200),
                    new ChangeStat(Stat.AGI, -100))
            .build();
    public static final Buff TEMPEST = new Buff.Builder(lang.get("buff.tempest"), "todo", "[LIGHT GREEN][+wind-hole]",
            BuffType.DEBUFF).effects(new ChangeMult(Mult.VENTUS_DMG_TAKEN, 200), new ChangeStat(Stat.RES, -100))
            .build();
    public static final Buff SHATTERED = new Buff.Builder(lang.get("buff.shattered"), "todo",
            "[LIGHT brown][+earth-crack]", BuffType.DEBUFF).effects(new ChangeMult(Mult.TERRA_DMG_TAKEN, 200),
                    new ChangeStat(Stat.AMR, -100))
            .build();
    public static final Buff STARSTRUCK = new Buff.Builder(lang.get("buff.starstruck"), "todo", "[+star-swirl]",
            BuffType.DEBUFF).effects(new ChangeMult(Mult.LUX_DMG_TAKEN, 200), new ChangeStat(Stat.FTH, -100)).build();
    public static final Buff DOOMED = new Buff.Builder(lang.get("buff.doomed"), "todo", "[RED][+skull-bolt]",
            BuffType.DEBUFF).effects(new ChangeMult(Mult.MALUM_DMG_TAKEN, 200), new ChangeStat(Stat.FTH, -100)).build();
    public static final Buff BLIGHTED = new Buff.Builder(lang.get("buff.blighted"), "todo", "[PURPLE][+poison-gas]",
            BuffType.DEBUFF).effects(new ChangeMult(Mult.DOT_DMG_TAKEN, 200), new ChangeMult(Mult.HEALING_TAKEN, -100))
            .build();
    public static final Buff DECAY = new Buff.Builder(lang.get("buff.decay"), "todo", "[CYAN][+nuclear-bomb]",
            BuffType.DEBUFF).effects(new ChangeMult(Mult.IGNIS_DMG_TAKEN, 150), new ChangeMult(Mult.DOT_DMG_TAKEN, 150),
                    new ChangeMult(Mult.HEALING_TAKEN, -100))
            .build();

    // Other
    public static final Buff REGENERATION = new Buff.Builder(lang.get("buff.regeneration"),
            lang.get("buff.regeneration.desc"),
            "[GREEN][+heart-plus]", BuffType.BUFF).maxStacks(5).effects(new ChangeHp.Builder(500).build()).build();
    public static final Buff EXTRA_ACTION = new Buff.Builder(lang.get("buff.extra_action"),
            lang.get("buff.extra_action.desc"),
            "[RED][+echo-ripples]", BuffType.BUFF).effects(new ChangeExtraActions(1)).build();

    /// Unique
}
