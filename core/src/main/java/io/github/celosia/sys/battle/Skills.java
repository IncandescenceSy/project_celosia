package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.skill_effects.*;

import static io.github.celosia.sys.settings.Lang.lang;

public class Skills {
    // Temp testing skills
    static Skill OVERHEAL = new Skill("Overheal", "", Element.VIS, Ranges.OTHER_1R_OR_SELF, 1, new Heal(500, 0.5));
    static Skill INFERNAL_PROVENANCE = new Skill("Infernal Provenance", "", Element.IGNIS, Ranges.OTHER_1R_OR_SELF, 1, new Damage(SkillType.MAG, Element.IGNIS, 100), new GiveBuff(Buffs.EXTRA_ACTION, 2, true, true));
    static Skill ULTRA_KILL = new Skill("Ultra Kill", "", Element.IGNIS, Ranges.COLUMN_OF_3_1R, 1, new Damage(SkillType.STR, Element.IGNIS, 100), new Damage(SkillType.STR, Element.IGNIS, 100, false, false, true));

    // Basic skills
    static Skill NOTHING = new Skill(lang.get("skill.nothing"), lang.get("skill.nothing.desc"), Element.VIS, Ranges.SELF, 0);
    static Skill ATTACK = new Skill(lang.get("skill.attack"), lang.get("skill.attack.desc"), Element.VIS, Ranges.OTHER_1R, 0, new Damage(SkillType.STR, Element.VIS, 55));
    static Skill DEFEND = new Skill(lang.get("skill.defend"), lang.get("skill.defend.desc"), Element.VIS, Ranges.SELF, 0, 3, new GiveBuff(Buffs.DEFEND), new ChangeSP(70, false, true)); // todo ChangeSP
    static Skill PROTECT = new Skill(lang.get("skill.protect"), lang.get("skill.protect.desc"), Element.VIS, Ranges.SELF, 40, 3, new GiveBuff(Buffs.PROTECT, false)); // todo disallow using twice in a row
    static Skill BLOSSOM = new Skill(lang.get("skill.blossom"), lang.get("skill.blossom.desc"), Element.VIS, Ranges.SELF, 300); // todo

    // Stage changers
    static Skill ATTACK_UP = new Skill(lang.get("skill.atk_up"), lang.get("skill.atk_up.desc"), Element.VIS, Ranges.OTHER_2R_OR_SELF, 50, new ChangeStage(StageType.ATK, 2, 5, false));
    static Skill DEFENSE_UP = new Skill(lang.get("skill.def_up"), lang.get("skill.def_up.desc"), Element.VIS, Ranges.OTHER_2R_OR_SELF, 50, new ChangeStage(StageType.DEF, 2, 5, false));
    static Skill FAITH_UP = new Skill(lang.get("skill.fth_up"), lang.get("skill.fth_up.desc"), Element.VIS, Ranges.OTHER_2R_OR_SELF, 50, new ChangeStage(StageType.FTH, 2, 5, false));
    static Skill AGILITY_UP = new Skill(lang.get("skill.agi_up"), lang.get("skill.agi_up.desc"), Element.VIS, Ranges.OTHER_2R_OR_SELF, 50, new ChangeStage(StageType.AGI, 2, 5, false));
    static Skill ATTACK_DOWN = new Skill(lang.get("skill.atk_down"), lang.get("skill.atk_down.desc"), Element.VIS, Ranges.OTHER_2R_OR_SELF, 50, new ChangeStage(StageType.ATK, -2, 5, false));
    static Skill DEFENSE_DOWN = new Skill(lang.get("skill.def_down"), lang.get("skill.def_down.desc"), Element.VIS, Ranges.OTHER_2R_OR_SELF, 50, new ChangeStage(StageType.DEF, -2, 5, false));
    static Skill FAITH_DOWN = new Skill(lang.get("skill.fth_down"), lang.get("skill.fth_down.desc"), Element.VIS, Ranges.OTHER_2R_OR_SELF, 50, new ChangeStage(StageType.FTH, -2, 5, false));
    static Skill AGILITY_DOWN = new Skill(lang.get("skill.agi_down"), lang.get("skill.agi_down.desc"), Element.VIS, Ranges.OTHER_2R_OR_SELF, 50, new ChangeStage(StageType.AGI, -2, 5, false));

    static Skill ATTACK_UP_GROUP = new Skill(lang.get("skill.atk_up_group"), lang.get("skill.atk_up_group.desc"), Element.VIS, Ranges.COLUMN_OF_3_1R, 150, new ChangeStage(StageType.ATK, 2, 5, false));
    static Skill DEFENSE_UP_GROUP = new Skill(lang.get("skill.def_up_group"), lang.get("skill.def_up_group.desc"), Element.VIS, Ranges.COLUMN_OF_3_1R, 150, new ChangeStage(StageType.DEF, 2, 5, false));
    static Skill FAITH_UP_GROUP = new Skill(lang.get("skill.fth_up_group"), lang.get("skill.fth_up_group.desc"), Element.VIS, Ranges.COLUMN_OF_3_1R, 150, new ChangeStage(StageType.FTH, 2, 5, false));
    static Skill AGILITY_UP_GROUP = new Skill(lang.get("skill.agi_up_group"), lang.get("skill.agi_up_group.desc"), Element.VIS, Ranges.COLUMN_OF_3_1R, 150, new ChangeStage(StageType.AGI, 2, 5, false));
    static Skill ATTACK_DOWN_GROUP = new Skill(lang.get("skill.atk_down_group"), lang.get("skill.atk_down_group.desc"), Element.VIS, Ranges.COLUMN_OF_3_1R, 150, new ChangeStage(StageType.ATK, -2, 5, false));
    static Skill DEFENSE_DOWN_GROUP = new Skill(lang.get("skill.def_down_group"), lang.get("skill.def_down_group.desc"), Element.VIS, Ranges.COLUMN_OF_3_1R, 150, new ChangeStage(StageType.DEF, -2, 5, false));
    static Skill FAITH_DOWN_GROUP = new Skill(lang.get("skill.fth_down_group"), lang.get("skill.fth_down_group.desc"), Element.VIS, Ranges.COLUMN_OF_3_1R, 150, new ChangeStage(StageType.FTH, -2, 5, false));
    static Skill AGILITY_DOWN_GROUP = new Skill(lang.get("skill.agi_down_group"), lang.get("skill.agi_down_group.desc"), Element.VIS, Ranges.COLUMN_OF_3_1R, 150, new ChangeStage(StageType.AGI, -2, 5, false));

    // Resists
    // todo

    // Breaks
    // todo

    // Heals
    static Skill HEAL = new Skill(lang.get("skill.heal"), lang.get("skill.heal.desc"), Element.VIS, Ranges.OTHER_1R_OR_SELF, 80, new Heal(210));
    static Skill AMBROSIA = new Skill(lang.get("skill.ambrosia"), lang.get("skill.ambrosia.desc"), Element.VIS, Ranges.OTHER_1R_OR_SELF, 125, new Heal(300));
    static Skill HEAL_GROUP = new Skill(lang.get("skill.heal_group"), lang.get("skill.heal_group.desc"), Element.VIS, Ranges.COLUMN_OF_3_1R, 260, new Heal(210));

    // Shields
    static Skill SHIELD = new Skill(lang.get("skill.shield"), lang.get("skill.shield.desc"), Element.VIS, Ranges.OTHER_1R_OR_SELF, 140, new Heal(300, 5));
    static Skill SHIELD_GROUP = new Skill(lang.get("skill.shield_group"), lang.get("skill.shield_group.desc"), Element.VIS, Ranges.COLUMN_OF_3_1R, 450, new Heal(300, 3));

    // Attacks
    // Ignis
    // Str
    // Mag
    static Skill FIREBALL = new Skill(lang.get("skill.fireball"), lang.get("skill.fireball.desc"), Element.IGNIS, Ranges.OTHER_1R, 50, new Damage(SkillType.MAG, Element.IGNIS, 50), new GiveBuff(Buffs.BURN, 3, 1));
    static Skill HEAT_WAVE = new Skill(lang.get("skill.heat_wave"), lang.get("skill.heat_wave.desc"), Element.IGNIS, Ranges.COLUMN_OF_3_1R, 180, new Damage(SkillType.MAG, Element.IGNIS, 60), new GiveBuff(Buffs.BURN, 2, 1));

    // Glacies
    // Str
    // Mag
    static Skill ICE_BEAM = new Skill(lang.get("skill.ice_beam"), lang.get("skill.ice_beam.desc"), Element.GLACIES, Ranges.OTHER_1R, 60, new Damage(SkillType.MAG, Element.GLACIES, 80), new GiveBuff(Buffs.FROSTBITE, 3, 1));

    // Fulgur
    // Str
    // Mag
    static Skill THUNDERBOLT = new Skill(lang.get("skill.thunderbolt"), lang.get("skill.thunderbolt.desc"), Element.FULGUR, Ranges.OTHER_1R, 80, new Damage(SkillType.MAG, Element.FULGUR, 100), new GiveBuff(Buffs.SHOCK, 3, 1));

    // Ventus
    // Str
    //static Skill ZEPHYR_LANCE = new Skill(lang.get("skill.zephyr_lance"), lang.get("skill.zephyr_lance.desc"), Element.VENTUS, Ranges.OTHER_1R, 200, new Damage(SkillType.STR, Element.VENTUS, 70, true));
    static Skill JET_STREAM = new Skill(lang.get("skill.jet_stream"), lang.get("skill.jet_stream.desc"), Element.VENTUS, Ranges.OTHER_1R, 120, 1, new Damage(SkillType.STR, Element.VENTUS, 55));
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
    static Skill DEMON_SCYTHE = new Skill(lang.get("skill.demon_scythe"), lang.get("skill.demon_scythe.desc"), Element.MALUM, Ranges.OTHER_2R, 110, new Damage(SkillType.MAG, Element.MALUM, 35), new GiveBuff(Buffs.CURSE, 2, 1),
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
    static Skill ICE_AGE = new Skill(lang.get("skill.ice_age"), lang.get("skill.ice_age.desc"), Element.GLACIES, Ranges.OTHER_2R, 600, true, new Damage(SkillType.STR, Element.GLACIES, 260, true), new GiveBuff(Buffs.FROSTBITE, 3, 3), new GiveBuff(Buffs.FROSTBOUND, 3));

    // Fulgur

    // Ventus

    // Terra

    // Lux

    // Malum

    // Unique
}
