package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.skill_effects.ChangeMP;
import io.github.celosia.sys.battle.skill_effects.ChangeStage;
import io.github.celosia.sys.battle.skill_effects.Damage;
import io.github.celosia.sys.battle.skill_effects.GiveBuff;

// Skills (any action that is attributed to a Combatant and has impact on the battle)
// todo lang, display element/type even for non-damaging skills, explicitly define which are "for allies" and which are "for opponents" for autotargeting
public enum Skill {
    // Basic
    ATTACK("Attack", Element.VIS, Targeting.OTHER_1R, 0, "", new Damage(SkillType.STR, Element.VIS, 55)),
    DEFEND("Defend", Element.VIS, Targeting.SELF, 0, "", new GiveBuff(Buff.DEFEND), new ChangeMP(10)),

    // Basic buffs/debuffs
    ATTACK_UP("Attack Up", Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, "", new ChangeStage(StageType.ATK, 3, 5)),
    DEFENSE_UP("Defense Up", Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, "", new ChangeStage(StageType.DEF, 3, 5)),
    FAITH_UP("Faith Up", Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, "", new ChangeStage(StageType.FTH, 3, 5)),
    AGILITY_UP("Agility Up", Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, "", new ChangeStage(StageType.AGI, 3, 5)),
    ATTACK_DOWN("Attack Down", Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, "", new ChangeStage(StageType.ATK, -3, 5)),
    DEFENSE_DOWN("Defense Down", Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, "", new ChangeStage(StageType.DEF, -3, 5)),
    FAITH_DOWN("Faith Down", Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, "", new ChangeStage(StageType.FTH, -3, 5)),
    AGILITY_DOWN("Agility Down", Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, "", new ChangeStage(StageType.AGI, -3, 5)),

    ATTACK_UP_GROUP("Group Attack Up", Element.VIS, Targeting.COLUMN_OF_3_1R, 12, "", new ChangeStage(StageType.ATK, 3, 3)),
    DEFENSE_UP_GROUP("Group Defense Up", Element.VIS, Targeting.COLUMN_OF_3_1R, 12, "", new ChangeStage(StageType.DEF, 3, 3)),
    FAITH_UP_GROUP("Group Faith Up", Element.VIS, Targeting.COLUMN_OF_3_1R, 12, "", new ChangeStage(StageType.FTH, 3, 3)),
    AGILITY_UP_GROUP("Group Agility Up", Element.VIS, Targeting.COLUMN_OF_3_1R, 12, "", new ChangeStage(StageType.AGI, 3, 3)),
    ATTACK_DOWN_GROUP("Group Attack Down", Element.VIS, Targeting.COLUMN_OF_3_1R, 12, "", new ChangeStage(StageType.ATK, -3, 3)),
    DEFENSE_DOWN_GROUP("Group Defense Down", Element.VIS, Targeting.COLUMN_OF_3_1R, 12, "", new ChangeStage(StageType.DEF, -3, 3)),
    FAITH_DOWN_GROUP("Group Faith Down", Element.VIS, Targeting.COLUMN_OF_3_1R, 12, "", new ChangeStage(StageType.FTH, -3, 3)),
    AGILITY_DOWN_GROUP("Group Agility Down", Element.VIS, Targeting.COLUMN_OF_3_1R, 12, "", new ChangeStage(StageType.AGI, -3, 3)),

    // Heals
    HEAL("Heal", Element.VIS, Targeting.OTHER_2R, 8, "", new Damage(SkillType.FTH, Element.VIS, -100)),

    // Barriers

    // Resists

    // Breaks

    // Common skills
    // Ignis
    FIREBALL("Fireball", Element.IGNIS, Targeting.OTHER_1R, 4, "", new Damage(SkillType.MAG, Element.IGNIS, 60), new GiveBuff(Buff.BURN, 3, 1)),

    // Glacies
    ICE_BEAM("Ice Beam", Element.GLACIES, Targeting.OTHER_1R, 6, "", new Damage(SkillType.MAG, Element.GLACIES, 80), new GiveBuff(Buff.FROSTBITE, 3, 1)),

    // Fulgur
    THUNDERBOLT("Thunderbolt", Element.FULGUR, Targeting.OTHER_1R, 8, "", new Damage(SkillType.MAG, Element.FULGUR, 100), new GiveBuff(Buff.SHOCK, 3, 1)),

    // Common bloom skills
    // Glacies
    ICE_AGE("Ice Age \uD83C\uDFF5", Element.GLACIES, Targeting.OTHER_2R, 60, "", true, new Damage(SkillType.MAG, Element.GLACIES, 260), new GiveBuff(Buff.FROSTBITE, 3, 3));

    private final String name;
    private final Element element;
    private final Targeting targeting;
    private final int cost;
    private final String desc;
    private final boolean isBloom;
    private final SkillEffect[] skillEffects;

    Skill(String name, Element element, Targeting targeting, int cost, String desc, boolean isBloom, SkillEffect... skillEffects) {
        this.name = name;
        this.element = element;
        this.targeting = targeting;
        this.cost = cost;
        this.desc = desc;
        this.isBloom = isBloom;
        this.skillEffects = skillEffects;
    }

    Skill(String name, Element element, Targeting targeting, int cost, String desc, SkillEffect... skillEffects) {
        this(name, element, targeting, cost, desc, false, skillEffects);
    }

    public String getName() {
        return name;
    }

    public Element getElement() {
        return element;
    }

    public Targeting getTargeting() {
        return targeting;
    }

    public int getCost() {
        return cost;
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
