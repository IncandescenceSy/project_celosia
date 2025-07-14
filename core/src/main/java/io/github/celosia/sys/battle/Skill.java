package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.skill_effects.Damage;
import io.github.celosia.sys.battle.skill_effects.GiveBuff;

// Skills (any action that is attributed to a Combatant and has impact on the battle)
// todo lang, display element/type even for non-damaging skills, explicitly define which are "for allies" and which are "for opponents" for autotargeting
public enum Skill {
    ATTACK("Attack", Targeting.OTHER_1R, 0, "", new Damage(SkillType.STR, Element.VIS, 55)),
    DEFEND("Defend", Targeting.SELF, 0, "", new GiveBuff(Buff.DEFEND)),
    FIREBALL("Fireball", Targeting.OTHER_1R, 4, "", new Damage(SkillType.MAG, Element.IGNIS, 60)),
    ICE_BEAM("Ice Beam", Targeting.OTHER_1R, 6, "", new Damage(SkillType.MAG, Element.GLACIES, 80)),
    THUNDERBOLT("Thunderbolt", Targeting.OTHER_1R, 8, "", new Damage(SkillType.MAG, Element.FULGUR, 100)),
    HEAL("Heal", Targeting.OTHER_2R, 8, "", new Damage(SkillType.FTH, Element.VIS, -100)),
    ICE_AGE("Ice Age \uD83C\uDFF5", Targeting.OTHER_2R, 60, "", true, new Damage(SkillType.MAG, Element.GLACIES, 260)),
    TARUKAJA("Tarukaja", Targeting.OTHER_2R, 10, "todo", new GiveBuff(Buff.ATK_UP, 3, 3)),
    RAKUNDA("Rakunda", Targeting.OTHER_2R, 10, "todo", new GiveBuff(Buff.DEF_DOWN, 3, 3));

    final Targeting targeting;
    final int cost;
    final String name;
    final String desc;
    final boolean isBloom;
    final SkillEffect[] skillEffects;

    Skill(String name, Targeting targeting, int cost, String desc, SkillEffect... skillEffects) {
        this.name = name;
        this.targeting = targeting;
        this.cost = cost;
        this.desc = desc;
        this.isBloom = false;
        this.skillEffects = skillEffects;
    }

    Skill(String name, Targeting targeting, int cost, String desc, boolean isBloom, SkillEffect... skillEffects) {
        this.name = name;
        this.targeting = targeting;
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
