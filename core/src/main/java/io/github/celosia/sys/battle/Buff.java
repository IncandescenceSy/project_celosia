package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.buff_effects.ChangeDefend;
import io.github.celosia.sys.battle.buff_effects.ChangeHP;
import io.github.celosia.sys.battle.buff_effects.ChangeMult;
import io.github.celosia.sys.battle.buff_effects.ChangeStat;

import static io.github.celosia.sys.settings.Lang.lang;

public enum Buff {
    // Basic
    DEFEND(lang.get("skill.defend"), lang.get("buff.defend.desc"), BuffType.BUFF, 1, new ChangeDefend(0.2f), new ChangeMult(Mult.DEF, -0.2f)),

    // Decreases multDef by way more than necessary so that (multDef < -500f) can be used as a check for Protect
    // todo: is it fine that having less multDef makes this proportionately worse?
    PROTECT(lang.get("buff.protect"), lang.get("buff.protect.desc"), BuffType.BUFF, 1, new ChangeMult(Mult.DEF, -1000f)),

    // Common ailments (poison + elemental)
    // todo: affinity should convey immunity to these
    POISON(lang.get("buff.poison"), lang.get("buff.poison.desc"), BuffType.DEBUFF, new ChangeHP(0.03f)),
    BURN(lang.get("buff.burn"), lang.get("buff.burn.desc"), BuffType.DEBUFF, new ChangeHP(0.02f), new ChangeStat(Stat.STR, -0.05f)),
    FROSTBITE(lang.get("buff.frostbite"), lang.get("buff.frostbite.desc"), BuffType.DEBUFF, new ChangeHP(0.02f), new ChangeStat(Stat.MAG, -0.05f)),
    SHOCK(lang.get("buff.shock"), lang.get("buff.shock.desc"), BuffType.DEBUFF, new ChangeHP(0.02f), new ChangeStat(Stat.AGI, -0.05f)), // todo
    WINDSWEPT(lang.get("buff.windswept"), lang.get("buff.windswept.desc"), BuffType.DEBUFF, new ChangeHP(0.02f), new ChangeStat(Stat.RES, -0.05f)), // todo
    TREMOR(lang.get("buff.tremor"), lang.get("buff.tremor.desc"), BuffType.DEBUFF, new ChangeHP(0.02f), new ChangeStat(Stat.AMR, -0.05f)), // todo
    CURSE(lang.get("buff.curse"), lang.get("buff.curse.desc"),  BuffType.DEBUFF, new ChangeHP(0.02f), new ChangeStat(Stat.FTH, -0.05f)), // todo

    // Other
    EXTRA_ACTION(lang.get("buff.extra_action"), lang.get("buff.extra_action.desc"), BuffType.BUFF);

    private final String name;
    private final String desc;
    private final BuffType buffType;
    private final int maxStacks;
    private final BuffEffect[] buffEffects;

    Buff(String name, String desc, BuffType buffType, int maxStacks, BuffEffect... buffEffects) {
        this.name = name;
        this.desc = desc;
        this.buffType = buffType;
        this.maxStacks = maxStacks;
        this.buffEffects = buffEffects;
    }

    Buff(String name, String desc, BuffType buffType, BuffEffect... buffEffects) {
        this(name, desc, buffType, 9, buffEffects);
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public BuffType getBuffType() {
        return buffType;
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public BuffEffect[] getBuffEffects() {
        return buffEffects;
    }
}
