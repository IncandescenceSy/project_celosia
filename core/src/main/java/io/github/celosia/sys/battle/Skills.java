package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.skill_effects.Damage;

// Skills (any action that is attributed to a Combatant and has impact on the battle)
// todo lang
public enum Skills {
    ATTACK("Attack", 0, "", new Damage(55, Element.VIS)),
    FIREBALL("Fireball", 4, "", new Damage(60, Element.IGNIS));

    int cost;
    String name;
    String desc;
    boolean isBloom;
    SkillEffect[] skillEffects;

    Skills(String name, int cost, String desc, SkillEffect... skillEffects) {
        this.name = name;
        this.cost = cost;
        this.desc = desc;
        this.isBloom = false;
        this.skillEffects = skillEffects;
    }

    Skills(String name, int cost, String desc, boolean isBloom, SkillEffect... skillEffects) {
        this.name = name;
        this.cost = cost;
        this.desc = desc;
        this.isBloom = isBloom;
        this.skillEffects = skillEffects;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isBloom() {
        return isBloom;
    }

    public SkillEffect[] getSkillEffects() {
        return skillEffects;
    }
}
