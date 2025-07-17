package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.buff_effects.ChangeDefend;
import io.github.celosia.sys.battle.buff_effects.ChangeStat;
import io.github.celosia.sys.battle.buff_effects.Damage;

// todo: stacking, ailments, explicitly define whether pos/neg
public enum Buff {
    // Basic
    DEFEND("Defend", "todo", BuffType.BUFF, 1, new ChangeDefend(0.2f)),

    // Common ailments (elemental + poison)
    // todo: affinity conveys immunity to these
    POISON("Poison", "todo", BuffType.DEBUFF, 9, new Damage(0.03f)),
    BURN("Burn", "todo", BuffType.DEBUFF, 9, new Damage(0.02f), new ChangeStat(Stat.STR, -0.05f)),
    FROSTBITE("Frostbite", "todo", BuffType.DEBUFF, 9, new Damage(0.02f), new ChangeStat(Stat.MAG, -0.05f)),
    SHOCK("Shock", "todo", BuffType.DEBUFF, 9, new Damage(0.02f), new ChangeStat(Stat.AGI, -0.05f)), // todo
    WINDSWEPT("Windswept", "todo", BuffType.DEBUFF, 9, new Damage(0.02f), new ChangeStat(Stat.RES, -0.05f)), // todo
    TREMOR("Tremor", "todo", BuffType.DEBUFF, 9, new Damage(0.02f), new ChangeStat(Stat.AMR, -0.05f)), // todo
    CURSE("Curse", "todo",  BuffType.DEBUFF, 9, new Damage(0.02f), new ChangeStat(Stat.FTH, -0.05f)); // todo

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
