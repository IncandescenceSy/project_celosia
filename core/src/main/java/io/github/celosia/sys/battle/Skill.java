package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.skill_effects.*;

import static io.github.celosia.sys.settings.Lang.lang;

// Skills (any action that is attributed to a Combatant and has impact on the battle)
// todo lang, functional targeting, display type even for non-damaging skills, explicitly define which are "for allies" and which are "for opponents" for autotargeting and AI
public enum Skill {
    // Basic skills
    NOTHING(lang.get("skill.nothing"), lang.get("skill.nothing.desc"), Element.VIS, Targeting.SELF, 0),
    ATTACK(lang.get("skill.attack"), lang.get("skill.attack.desc"), Element.VIS, Targeting.OTHER_1R, 0, new Damage(SkillType.STR, Element.VIS, 55)),
    DEFEND(lang.get("skill.defend"), lang.get("skill.defend.desc"), Element.VIS, Targeting.SELF, 0, 3, new GiveBuff(Buff.DEFEND), new ChangeSP(8)),
    PROTECT(lang.get("skill.protect"), lang.get("skill.protect.desc"), Element.VIS, Targeting.SELF, 4, 3, new GiveBuff(Buff.PROTECT)), // todo disallow using twice in a row
    BLOSSOM(lang.get("skill.blossom"), lang.get("skill.blossom.desc"), Element.VIS, Targeting.SELF, 30), // todo

    // Stage changers
    ATTACK_UP(lang.get("skill.atk_up"), lang.get("skill.atk_up.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, new ChangeStage(StageType.ATK, 3, 5)),
    DEFENSE_UP(lang.get("skill.def_up"), lang.get("skill.def_up.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, new ChangeStage(StageType.DEF, 3, 5)),
    FAITH_UP(lang.get("skill.fth_up"), lang.get("skill.fth_up.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, new ChangeStage(StageType.FTH, 3, 5)),
    AGILITY_UP(lang.get("skill.agi_up"), lang.get("skill.agi_up.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, new ChangeStage(StageType.AGI, 3, 5)),
    ATTACK_DOWN(lang.get("skill.atk_down"), lang.get("skill.atk_down.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, new ChangeStage(StageType.ATK, -3, 5)),
    DEFENSE_DOWN(lang.get("skill.def_down"), lang.get("skill.def_down.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, new ChangeStage(StageType.DEF, -3, 5)),
    FAITH_DOWN(lang.get("skill.fth_down"), lang.get("skill.fth_down.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, new ChangeStage(StageType.FTH, -3, 5)),
    AGILITY_DOWN(lang.get("skill.agi_down"), lang.get("skill.agi_down.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 4, new ChangeStage(StageType.AGI, -3, 5)),

    ATTACK_UP_GROUP(lang.get("skill.atk_up_group"), lang.get("skill.atk_up_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 4, new ChangeStage(StageType.ATK, 3, 5)),
    DEFENSE_UP_GROUP(lang.get("skill.def_up_group"), lang.get("skill.def_up_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 4, new ChangeStage(StageType.DEF, 3, 5)),
    FAITH_UP_GROUP(lang.get("skill.fth_up_group"), lang.get("skill.fth_up_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 4, new ChangeStage(StageType.FTH, 3, 5)),
    AGILITY_UP_GROUP(lang.get("skill.agi_up_group"), lang.get("skill.agi_up_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 4, new ChangeStage(StageType.AGI, 3, 5)),
    ATTACK_DOWN_GROUP(lang.get("skill.atk_down_group"), lang.get("skill.atk_down_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 4, new ChangeStage(StageType.ATK, -3, 5)),
    DEFENSE_DOWN_GROUP(lang.get("skill.def_down_group"), lang.get("skill.def_down_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 4, new ChangeStage(StageType.DEF, -3, 5)),
    FAITH_DOWN_GROUP(lang.get("skill.fth_down_group"), lang.get("skill.fth_down_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 4, new ChangeStage(StageType.FTH, -3, 5)),
    AGILITY_DOWN_GROUP(lang.get("skill.agi_down_group"), lang.get("skill.agi_down_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 4, new ChangeStage(StageType.AGI, -3, 5)),

    // Resists
    // todo

    // Breaks
    // todo

    // Heals
    HEAL(lang.get("skill.heal"), lang.get("skill.heal.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 6, new Heal(160)),
    HEAL_GROUP(lang.get("skill.heal_group"), lang.get("skill.heal_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 18, new Heal(140)),
    OVERHEAL("Overheal", lang.get("skill.heal.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 15, new Heal(240, 0.5f)), // temp

    // Barriers
    BARRIER(lang.get("skill.barrier"), lang.get("skill.barrier.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 8, new Heal(140, 5)),
    BARRIER_GROUP(lang.get("skill.barrier_group"), lang.get("skill.barrier_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 24, new Heal(120, 3)),

    // Attacks
    // Ignis
    // Str
    // Mag
    FIREBALL(lang.get("skill.fireball"), lang.get("skill.fireball.desc"), Element.IGNIS, Targeting.OTHER_1R, 4, new Damage(SkillType.MAG, Element.IGNIS, 60), new GiveBuff(Buff.BURN, 3, 1)),

    // Glacies
    // Str
    // Mag
    ICE_BEAM(lang.get("skill.ice_beam"), lang.get("skill.ice_beam.desc"), Element.GLACIES, Targeting.OTHER_1R, 6, new Damage(SkillType.MAG, Element.GLACIES, 80), new GiveBuff(Buff.FROSTBITE, 3, 1)),

    // Fulgur
    // Str
    // Mag
    THUNDERBOLT(lang.get("skill.thunderbolt"), lang.get("skill.thunderbolt.desc"), Element.FULGUR, Targeting.OTHER_1R, 8, new Damage(SkillType.MAG, Element.FULGUR, 100), new GiveBuff(Buff.SHOCK, 3, 1)),

    // Ventus
    // Str
    ZEPHYR_LANCE(lang.get("skill.zephyr_lance"), lang.get("skill.zephyr_lance.desc"), Element.VENTUS, Targeting.OTHER_1R, 20, new Damage(SkillType.STR, Element.VENTUS, 70, true)),
    JET_STREAM(lang.get("skill.jet_stream"), lang.get("skill.jet_stream.desc"), Element.VENTUS, Targeting.OTHER_1R, 12, 1, new Damage(SkillType.STR, Element.VENTUS, 55)),
    // Mag

    // Terra
    // Str
    // Mag

    // Lux
    // Str
    // Mag

    // Malum
    // Str
    // Mag
    // todo a way to build multihits that's less repetitive?
    // Ref: Terraria
    DEMON_SCYTHE(lang.get("skill.demon_scythe"), lang.get("skill.demon_scythe.desc"), Element.MALUM, Targeting.OTHER_2R, 11, new Damage(SkillType.MAG, Element.MALUM, 35), new GiveBuff(Buff.CURSE, 2, 1),
        new Damage(SkillType.MAG, Element.MALUM, 35), new GiveBuff(Buff.CURSE, 2, 1),
        new Damage(SkillType.MAG, Element.MALUM, 35), new GiveBuff(Buff.CURSE, 2, 1)),

    // Unique skills

    // Bloom skills
    // Stages

    // Resists

    // Breaks

    // Heals

    // Barriers

    // Attacks
    // Ignis

    // Glacies
    ICE_AGE(lang.get("skill.ice_age"), lang.get("skill.ice_age.desc"), Element.GLACIES, Targeting.OTHER_2R, 60, true, new Damage(SkillType.STR, Element.GLACIES, 260, true), new GiveBuff(Buff.FROSTBITE, 3, 3, Result.SUCCESS));

    // Fulgur

    // Ventus

    // Terra

    // Lux

    // Malum

    // Unique

    private final String name;
    private final String desc;
    private final Element element;
    private final Targeting targeting;
    private final int cost;

    // todo: NYI
    // Skills happen in order of prio, and in order of user Agi within each prio
    // Prio brackets:
    // -9: Always happens last
    // -1: Happens late
    // 0: Normal
    // +1: Happens early (most prio skills)
    // +2: Happens very early (certain special prio skills?)
    // +3: Happens before attacks (Defend, Protect, Ally Switch)
    // +9: Always happens immediately (Follow Ups)
    private final int prio;

    private final boolean isBloom;
    private final SkillEffect[] skillEffects;

    Skill(String name, String desc, Element element, Targeting targeting, int cost, int prio, boolean isBloom, SkillEffect... skillEffects) {
        this.name = name;
        this.desc = desc;
        this.element = element;
        this.targeting = targeting;
        this.cost = cost;
        this.prio = prio;
        this.isBloom = isBloom;
        this.skillEffects = skillEffects;
    }

    Skill(String name, String desc, Element element, Targeting targeting, int cost, int prio, SkillEffect... skillEffects) {
        this(name, desc, element, targeting, cost, prio, false, skillEffects);
    }

    Skill(String name, String desc, Element element, Targeting targeting, int cost, boolean isBloom, SkillEffect... skillEffects) {
        this(name, desc, element, targeting, cost, 0, isBloom, skillEffects);
    }

    Skill(String name, String desc, Element element, Targeting targeting, int cost, SkillEffect... skillEffects) {
        this(name, desc, element, targeting, cost, 0, false, skillEffects);
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
