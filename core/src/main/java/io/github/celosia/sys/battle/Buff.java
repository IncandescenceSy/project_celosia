package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.buff_effects.ChangeStat;

// todo: stacking, ailments, explicitly define whether pos/neg
public enum Buff {
    DEFEND("Defend", "todo", 1, new ChangeStat(Stat.AMR, 0.35f), new ChangeStat(Stat.RES, 0.35f)),
    ATK_UP("Atk Up", "todo", 9, new ChangeStat(Stat.STR, 0.1f), new ChangeStat(Stat.MAG, 0.1f)),
    DEF_DOWN("Def Down", "todo", 9, new ChangeStat(Stat.AMR, -0.1f), new ChangeStat(Stat.RES, -0.1f));

    private String name;
    private String desc;
    private int maxStacks;
    private BuffEffect[] buffEffects;

    Buff(String name, String desc, int maxStacks, BuffEffect... buffEffects) {
        this.name = name;
        this.desc = desc;
        this.maxStacks = maxStacks;
        this.buffEffects = buffEffects;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public BuffEffect[] getBuffEffects() {
        return buffEffects;
    }
}
