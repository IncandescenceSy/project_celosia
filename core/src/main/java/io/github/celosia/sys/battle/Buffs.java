package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.buff_effects.ChangeDefend;
import io.github.celosia.sys.battle.buff_effects.ChangeHP;
import io.github.celosia.sys.battle.buff_effects.ChangeMult;
import io.github.celosia.sys.battle.buff_effects.ChangeStat;

import static io.github.celosia.sys.settings.Lang.lang;

public class Buffs {
    // Basic
    static Buff DEFEND = new Buff(lang.get("skill.defend"), lang.get("buff.defend.desc"), BuffType.BUFF, 1, new ChangeDefend(0.2f), new ChangeMult(Mult.DMG_TAKEN, -20));

    // Decreases multDef by way more than necessary so that (multDef < -500f) can be used as a check for Protect
    // todo: is it fine that having less multDef makes this proportionately worse?
    static Buff PROTECT = new Buff(lang.get("buff.protect"), lang.get("buff.protect.desc"), BuffType.BUFF, 1, new ChangeMult(Mult.DMG_TAKEN, -100000));

    // Common ailments (poison + elemental)
    // todo: affinity should convey immunity to these
    static Buff POISON = new Buff(lang.get("buff.poison"), lang.get("buff.poison.desc"), BuffType.DEBUFF, 5, new ChangeHP(-0.03f));
    static Buff BURN = new Buff(lang.get("buff.burn"), lang.get("buff.burn.desc"), BuffType.DEBUFF, 5, new ChangeHP(-0.02f), new ChangeStat(Stat.STR, -0.05f));
    static Buff FROSTBITE = new Buff(lang.get("buff.frostbite"), lang.get("buff.frostbite.desc"), BuffType.DEBUFF, 5, new ChangeHP(-0.02f), new ChangeStat(Stat.MAG, -0.05f));
    static Buff SHOCK = new Buff(lang.get("buff.shock"), lang.get("buff.shock.desc"), BuffType.DEBUFF, 5, new ChangeHP(-0.02f), new ChangeStat(Stat.AGI, -0.05f)); // todo
    static Buff WINDSWEPT = new Buff(lang.get("buff.windswept"), lang.get("buff.windswept.desc"), BuffType.DEBUFF, 5, new ChangeHP(-0.02f), new ChangeStat(Stat.RES, -0.05f)); // todo
    static Buff TREMOR = new Buff(lang.get("buff.tremor"), lang.get("buff.tremor.desc"), BuffType.DEBUFF, 5, new ChangeHP(-0.02f), new ChangeStat(Stat.AMR, -0.05f)); // todo
    // todo dazzled
    static Buff CURSE = new Buff(lang.get("buff.curse"), lang.get("buff.curse.desc"),  BuffType.DEBUFF, 5, new ChangeHP(-0.02f), new ChangeStat(Stat.FTH, -0.05f)); // todo

    // Other
    // todo regen
    static Buff EXTRA_ACTION = new Buff(lang.get("buff.extra_action"), lang.get("buff.extra_action.desc"), BuffType.BUFF, 1);
}
