package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.skill_effects.ChangeStage;
import io.github.celosia.sys.battle.skill_effects.Damage;
import io.github.celosia.sys.battle.skill_effects.GiveBuff;
import io.github.celosia.sys.battle.skill_effects.Heal;

import static io.github.celosia.sys.settings.Lang.lang;

public class Skills {
    // Temp testing skills
    static Skill OVERHEAL = new Skill("Overheal", "", Element.VIS, Targeting.OTHER_1R_OR_SELF, 150, new Heal(240, 0.5f)); // temp
    static Skill INFERNAL_PROVENANCE = new Skill("Infernal Provenance", "", Element.IGNIS, Targeting.OTHER_1R_OR_SELF, 150, new Damage(SkillType.MAG, Element.IGNIS, 100), new GiveBuff(Buffs.EXTRA_ACTION, 2, true)); // temp

    // Basic skills
    static Skill NOTHING = new Skill(lang.get("skill.nothing"), lang.get("skill.nothing.desc"), Element.VIS, Targeting.SELF, 0);
    static Skill ATTACK = new Skill(lang.get("skill.attack"), lang.get("skill.attack.desc"), Element.VIS, Targeting.OTHER_1R, 0, new Damage(SkillType.STR, Element.VIS, 55));
    static Skill DEFEND = new Skill(lang.get("skill.defend"), lang.get("skill.defend.desc"), Element.VIS, Targeting.SELF, -70, 3, new GiveBuff(Buffs.DEFEND)); // todo ChangeSP
    static Skill PROTECT = new Skill(lang.get("skill.protect"), lang.get("skill.protect.desc"), Element.VIS, Targeting.SELF, 40, 3, new GiveBuff(Buffs.PROTECT)); // todo disallow using twice in a row
    static Skill BLOSSOM = new Skill(lang.get("skill.blossom"), lang.get("skill.blossom.desc"), Element.VIS, Targeting.SELF, 300); // todo

    // Stage changers
    static Skill ATTACK_UP = new Skill(lang.get("skill.atk_up"), lang.get("skill.atk_up.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 40, new ChangeStage(StageType.ATK, 2, 5));
    static Skill DEFENSE_UP = new Skill(lang.get("skill.def_up"), lang.get("skill.def_up.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 40, new ChangeStage(StageType.DEF, 2, 5));
    static Skill FAITH_UP = new Skill(lang.get("skill.fth_up"), lang.get("skill.fth_up.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 40, new ChangeStage(StageType.FTH, 2, 5));
    static Skill AGILITY_UP = new Skill(lang.get("skill.agi_up"), lang.get("skill.agi_up.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 40, new ChangeStage(StageType.AGI, 2, 5));
    static Skill ATTACK_DOWN = new Skill(lang.get("skill.atk_down"), lang.get("skill.atk_down.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 40, new ChangeStage(StageType.ATK, -2, 5));
    static Skill DEFENSE_DOWN = new Skill(lang.get("skill.def_down"), lang.get("skill.def_down.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 40, new ChangeStage(StageType.DEF, -2, 5));
    static Skill FAITH_DOWN = new Skill(lang.get("skill.fth_down"), lang.get("skill.fth_down.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 40, new ChangeStage(StageType.FTH, -2, 5));
    static Skill AGILITY_DOWN = new Skill(lang.get("skill.agi_down"), lang.get("skill.agi_down.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 40, new ChangeStage(StageType.AGI, -2, 5));

    static Skill ATTACK_UP_GROUP = new Skill(lang.get("skill.atk_up_group"), lang.get("skill.atk_up_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 120, new ChangeStage(StageType.ATK, 2, 3));
    static Skill DEFENSE_UP_GROUP = new Skill(lang.get("skill.def_up_group"), lang.get("skill.def_up_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 120, new ChangeStage(StageType.DEF, 2, 3));
    static Skill FAITH_UP_GROUP = new Skill(lang.get("skill.fth_up_group"), lang.get("skill.fth_up_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 120, new ChangeStage(StageType.FTH, 2, 3));
    static Skill AGILITY_UP_GROUP = new Skill(lang.get("skill.agi_up_group"), lang.get("skill.agi_up_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 120, new ChangeStage(StageType.AGI, 2, 3));
    static Skill ATTACK_DOWN_GROUP = new Skill(lang.get("skill.atk_down_group"), lang.get("skill.atk_down_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 120, new ChangeStage(StageType.ATK, -2, 3));
    static Skill DEFENSE_DOWN_GROUP = new Skill(lang.get("skill.def_down_group"), lang.get("skill.def_down_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 120, new ChangeStage(StageType.DEF, -2, 3));
    static Skill FAITH_DOWN_GROUP = new Skill(lang.get("skill.fth_down_group"), lang.get("skill.fth_down_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 120, new ChangeStage(StageType.FTH, -2, 3));
    static Skill AGILITY_DOWN_GROUP = new Skill(lang.get("skill.agi_down_group"), lang.get("skill.agi_down_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 120, new ChangeStage(StageType.AGI, -2, 3));

    // Resists
    // todo

    // Breaks
    // todo

    // Heals
    static Skill HEAL = new Skill(lang.get("skill.heal"), lang.get("skill.heal.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 80, new Heal(210));
    static Skill AMBROSIA = new Skill(lang.get("skill.ambrosia"), lang.get("skill.ambrosia.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 125, new Heal(300));
    static Skill HEAL_GROUP = new Skill(lang.get("skill.heal_group"), lang.get("skill.heal_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 260, new Heal(210));

    // Shields
    static Skill SHIELD = new Skill(lang.get("skill.shield"), lang.get("skill.shield.desc"), Element.VIS, Targeting.OTHER_1R_OR_SELF, 140, new Heal(300, 5));
    static Skill SHIELD_GROUP = new Skill(lang.get("skill.shield_group"), lang.get("skill.shield_group.desc"), Element.VIS, Targeting.COLUMN_OF_3_1R, 450, new Heal(300, 3));

    // Attacks
    // Ignis
    // Str
    // Mag
    static Skill FIREBALL = new Skill(lang.get("skill.fireball"), lang.get("skill.fireball.desc"), Element.IGNIS, Targeting.OTHER_1R, 40, new Damage(SkillType.MAG, Element.IGNIS, 60), new GiveBuff(Buffs.BURN, 3, 1));

    // Glacies
    // Str
    // Mag
    static Skill ICE_BEAM = new Skill(lang.get("skill.ice_beam"), lang.get("skill.ice_beam.desc"), Element.GLACIES, Targeting.OTHER_1R, 60, new Damage(SkillType.MAG, Element.GLACIES, 80), new GiveBuff(Buffs.FROSTBITE, 3, 1));

    // Fulgur
    // Str
    // Mag
    static Skill THUNDERBOLT = new Skill(lang.get("skill.thunderbolt"), lang.get("skill.thunderbolt.desc"), Element.FULGUR, Targeting.OTHER_1R, 80, new Damage(SkillType.MAG, Element.FULGUR, 100), new GiveBuff(Buffs.SHOCK, 3, 1));

    // Ventus
    // Str
    //static Skill ZEPHYR_LANCE = new Skill(lang.get("skill.zephyr_lance"), lang.get("skill.zephyr_lance.desc"), Element.VENTUS, Targeting.OTHER_1R, 200, new Damage(SkillType.STR, Element.VENTUS, 70, true));
    static Skill JET_STREAM = new Skill(lang.get("skill.jet_stream"), lang.get("skill.jet_stream.desc"), Element.VENTUS, Targeting.OTHER_1R, 120, 1, new Damage(SkillType.STR, Element.VENTUS, 55));
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
    static Skill DEMON_SCYTHE = new Skill(lang.get("skill.demon_scythe"), lang.get("skill.demon_scythe.desc"), Element.MALUM, Targeting.OTHER_2R, 110, new Damage(SkillType.MAG, Element.MALUM, 35), new GiveBuff(Buffs.CURSE, 2, 1),
        new Damage(SkillType.MAG, Element.MALUM, 35), new GiveBuff(Buffs.CURSE, 2, 1),
        new Damage(SkillType.MAG, Element.MALUM, 35), new GiveBuff(Buffs.CURSE, 2, 1));

    // Unique skills

    // Bloom skills
    // Stages

    // Resists

    // Breaks

    // Heals

    // Shields

    // Attacks
    // Ignis

    // Glacies
    static Skill ICE_AGE = new Skill(lang.get("skill.ice_age"), lang.get("skill.ice_age.desc"), Element.GLACIES, Targeting.OTHER_2R, 600, true, new Damage(SkillType.STR, Element.GLACIES, 260, true), new GiveBuff(Buffs.FROSTBITE, 3, 3));

    // Fulgur

    // Ventus

    // Terra

    // Lux

    // Malum

    // Unique
}
