package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.skill_effects.Damage;

// Skills (any action that is attributed to a Combatant and has impact on the battle)
// todo lang
public enum Skill {
    ATTACK("Attack", 0, "", new Damage(55, Element.VIS)),
    DEFEND("Defend", 0, "", new Damage(0, Element.VIS)),
    FIREBALL("Fireball", 4, "", new Damage(60, Element.IGNIS)),
    ICE_BEAM("Ice Beam", 6, "", new Damage(80, Element.GLACIES)),
    THUNDERBOLT("Thunderbolt", 8, "", new Damage(100, Element.FULGUR)),
    HEAL("Heal", 8, "", new Damage(-100, Element.VIS)),
    ICE_AGE("Ice Age \uD83C\uDFF5", 60, "", true, new Damage(260, Element.GLACIES));

    final int cost;
    final String name;
    final String desc;
    final boolean isBloom;
    final SkillEffect[] skillEffects;

    Skill(String name, int cost, String desc, SkillEffect... skillEffects) {
        this.name = name;
        this.cost = cost;
        this.desc = desc;
        this.isBloom = false;
        this.skillEffects = skillEffects;
    }

    Skill(String name, int cost, String desc, boolean isBloom, SkillEffect... skillEffects) {
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
