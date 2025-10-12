package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.skill_effects.ChangeBloom;
import io.github.celosia.sys.battle.skill_effects.ChangeSp;
import io.github.celosia.sys.battle.skill_effects.ChangeStage;
import io.github.celosia.sys.battle.skill_effects.Damage;
import io.github.celosia.sys.battle.skill_effects.GiveBuff;
import io.github.celosia.sys.battle.skill_effects.Heal;

import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_BUFF;
import static io.github.celosia.sys.util.TextLib.C_NEG;

public class Skills {

    ///// Temp testing skills
    public static final Skill RASETU_FEAST = new Skill.Builder("Rasetu Feast", "todo", Elements.VIS,
            Ranges.OTHER_3R_OR_SELF, 1)
            .effects(new GiveBuff.Builder(Buffs.STUNNED, 3).build(), new GiveBuff.Builder(Buffs.FROSTBITE, 3).build(),
                    new GiveBuff.Builder(Buffs.SHOCK, 3).build(), new GiveBuff.Builder(Buffs.TREMOR, 3).build())
            .build();
    public static final Skill GET_EXA = new Skill.Builder("Get ExA", "todo", Elements.VIS, Ranges.OTHER_3R_OR_SELF, 1)
            .effects(new GiveBuff.Builder(Buffs.EXTRA_ACTION, 3).build()).build();

    ////////// Basic skills
    public static final Skill NOTHING = new Skill.Builder(lang.get("skill.nothing"), "todo",
            Elements.VIS,
            Ranges.SELF, 0).build();
    public static final Skill DEFEND = new Skill.Builder(lang.get("skill.defend"), "todo",
            Elements.VIS,
            Ranges.SELF, 0).prio(3).roles(SkillRole.BUFF_DEFENSIVE)
            .effects(new GiveBuff.Builder(Buffs.DEFEND, 1).build(),
                    new ChangeSp.Builder(70).giveToSelf().build())
            .build();
    public static final Skill PROTECT = new Skill.Builder(lang.get("skill.protect"), "todo",
            Elements.VIS,
            Ranges.SELF, 40).cooldown(2).prio(3).roles(SkillRole.BUFF_DEFENSIVE)
            .effects(new GiveBuff.Builder(Buffs.PROTECT, 1).notInstant().build()).build();
    public static final Skill BLOSSOM = new Skill.Builder(lang.get("skill.blossom"), "todo",
            Elements.VIS,
            Ranges.SELF, 300)
            // todo roles
            .effects(new ChangeBloom.Builder(300).notInstant().build()).build();

    // Stage changers
    public static final Skill ATTACK_UP = new Skill.Builder(lang.get("skill.atk_up"), "todo",
            Elements.VIS,
            Ranges.OTHER_2R_OR_SELF, 50).roles(SkillRole.BUFF_OFFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.ATK, 5, 2).notInstant().build()).build();
    public static final Skill DEFENSE_UP = new Skill.Builder(lang.get("skill.def_up"), "todo",
            Elements.VIS,
            Ranges.OTHER_2R_OR_SELF, 50).roles(SkillRole.BUFF_DEFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.DEF, 5, 2).notInstant().build()).build();
    public static final Skill FAITH_UP = new Skill.Builder(lang.get("skill.fth_up"), "todo",
            Elements.VIS,
            Ranges.OTHER_2R_OR_SELF, 50).roles(SkillRole.BUFF_DEFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.FTH, 5, 2).notInstant().build()).build();
    public static final Skill AGILITY_UP = new Skill.Builder(lang.get("skill.agi_up"), "todo",
            Elements.VIS,
            Ranges.OTHER_2R_OR_SELF, 50).roles(SkillRole.BUFF_OFFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.AGI, 5, 2).notInstant().build()).build();
    public static final Skill ATTACK_DOWN = new Skill.Builder(lang.get("skill.atk_down"),
            "todo",
            Elements.VIS, Ranges.OTHER_2R_OR_SELF, 50).roles(SkillRole.DEBUFF_DEFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.ATK, 5, -2).notInstant().build()).build();
    public static final Skill DEFENSE_DOWN = new Skill.Builder(lang.get("skill.def_down"),
            "todo",
            Elements.VIS, Ranges.OTHER_2R_OR_SELF, 50).roles(SkillRole.DEBUFF_OFFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.DEF, 5, -2).notInstant().build()).build();
    public static final Skill FAITH_DOWN = new Skill.Builder(lang.get("skill.fth_down"),
            "todo",
            Elements.VIS, Ranges.OTHER_2R_OR_SELF, 50).roles(SkillRole.DEBUFF_OFFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.FTH, 5, -2).notInstant().build()).build();
    public static final Skill AGILITY_DOWN = new Skill.Builder(lang.get("skill.agi_down"),
            "todo",
            Elements.VIS, Ranges.OTHER_2R_OR_SELF, 50).roles(SkillRole.DEBUFF_DEFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.AGI, 5, -2).notInstant().build()).build();

    public static final Skill ATTACK_UP_GROUP = new Skill.Builder(lang.get("skill.atk_up_group"),
            "todo", Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .roles(SkillRole.BUFF_OFFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.ATK, 5, 2).notInstant().build()).build();
    public static final Skill DEFENSE_UP_GROUP = new Skill.Builder(lang.get("skill.def_up_group"),
            "todo", Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .roles(SkillRole.BUFF_DEFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.DEF, 5, 2).notInstant().build()).build();
    public static final Skill FAITH_UP_GROUP = new Skill.Builder(lang.get("skill.fth_up_group"),
            "todo",
            Elements.VIS, Ranges.COLUMN_OF_3_1R, 150).roles(SkillRole.BUFF_DEFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.FTH, 5, 2).notInstant().build()).build();
    public static final Skill AGILITY_UP_GROUP = new Skill.Builder(lang.get("skill.agi_up_group"),
            "todo", Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .roles(SkillRole.BUFF_OFFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.AGI, 5, 2).notInstant().build()).build();
    public static final Skill ATTACK_DOWN_GROUP = new Skill.Builder(lang.get("skill.atk_down_group"),
            "todo", Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .roles(SkillRole.DEBUFF_DEFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.ATK, 5, -2).notInstant().build()).build();
    public static final Skill DEFENSE_DOWN_GROUP = new Skill.Builder(lang.get("skill.def_down_group"),
            "todo", Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .roles(SkillRole.DEBUFF_OFFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.DEF, 5, -2).notInstant().build()).build();
    public static final Skill FAITH_DOWN_GROUP = new Skill.Builder(lang.get("skill.fth_down_group"),
            "todo", Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .roles(SkillRole.DEBUFF_OFFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.FTH, 5, -2).notInstant().build()).build();
    public static final Skill AGILITY_DOWN_GROUP = new Skill.Builder(lang.get("skill.agi_down_group"),
            "todo", Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .roles(SkillRole.DEBUFF_DEFENSIVE)
            .effects(new ChangeStage.Builder(StageTypes.AGI, 5, -2).notInstant().build()).build();

    // Heals
    public static final Skill HEAL = new Skill.Builder(lang.get("skill.heal"), "todo",
            Elements.VIS,
            Ranges.OTHER_1R_OR_SELF, 80).roles(SkillRole.HEAL).effects(new Heal.Builder(40).build()).build();
    public static final Skill AMBROSIA = new Skill.Builder(lang.get("skill.ambrosia"), "todo",
            Elements.VIS,
            Ranges.OTHER_1R_OR_SELF, 125).roles(SkillRole.HEAL).effects(new Heal.Builder(60).build()).build();
    public static final Skill HEAL_GROUP = new Skill.Builder(lang.get("skill.heal_group"),
            "todo",
            Elements.VIS, Ranges.COLUMN_OF_3_1R, 260).roles(SkillRole.HEAL).effects(new Heal.Builder(40).build())
            .build();

    // Shields
    public static final Skill SHIELD = new Skill.Builder(lang.get("skill.shield"), "todo",
            Elements.VIS,
            Ranges.OTHER_1R_OR_SELF, 140).roles(SkillRole.SHIELD).effects(new GiveBuff.Builder(Buffs.SHIELD, 5).build())
            .build();
    public static final Skill SHIELD_GROUP = new Skill.Builder(lang.get("skill.shield_group"),
            "todo",
            Elements.VIS, Ranges.COLUMN_OF_3_1R, 450).roles(SkillRole.SHIELD)
            .effects(new GiveBuff.Builder(Buffs.SHIELD, 5).build()).build();

    /// Ignis
    // Str
    // Mag
    public static final Skill FIREBALL = new Skill.Builder(lang.get("skill.fireball"), "skill_desc.buff",
            Elements.IGNIS, Ranges.OTHER_1R, 50).roles(SkillRole.ATTACK)
            .effects(new Damage.Builder(SkillType.MAG, Elements.IGNIS, 50).build(),
                    new GiveBuff.Builder(Buffs.BURN, 3).build())
            .descArgs(C_NEG + "+1 ", Buffs.BURN.getNameWithIcon(C_BUFF), "3")
            .build();
    public static final Skill HEAT_WAVE = new Skill.Builder(lang.get("skill.heat_wave"),
            "skill_desc.buff",
            Elements.IGNIS, Ranges.COLUMN_OF_3_1R, 180).roles(SkillRole.ATTACK)
            .effects(new Damage.Builder(SkillType.MAG, Elements.IGNIS, 60).build(),
                    new GiveBuff.Builder(Buffs.BURN, 2).build())
            .descArgs(C_NEG + "+1 ", Buffs.BURN.getNameWithIcon(C_BUFF), "2")
            .build();

    /// Glacies
    // Str
    // Mag
    public static final Skill ICE_BEAM = new Skill.Builder(lang.get("skill.ice_beam"), "skill_desc.buff",
            Elements.GLACIES, Ranges.OTHER_1R, 60).roles(SkillRole.ATTACK)
            .effects(new Damage.Builder(SkillType.MAG, Elements.GLACIES, 80).build(),
                    new GiveBuff.Builder(Buffs.FROSTBITE, 3).build())
            .descArgs(C_NEG + "+1 ", Buffs.FROSTBITE.getNameWithIcon(C_BUFF), "3")
            .build();

    /// Fulgur
    // Str
    // Mag
    public static final Skill THUNDERBOLT = new Skill.Builder(lang.get("skill.thunderbolt"),
            "skill_desc.buff",
            Elements.FULGUR, Ranges.OTHER_1R, 80).roles(SkillRole.ATTACK)
            .effects(new Damage.Builder(SkillType.MAG, Elements.FULGUR, 100).build(),
                    new GiveBuff.Builder(Buffs.SHOCK, 3).build())
            .descArgs(C_NEG + "+1 ", Buffs.SHOCK.getNameWithIcon(C_BUFF), "3")
            .build();

    /// Ventus
    // Str
    public static final Skill JET_STREAM = new Skill.Builder(lang.get("skill.jet_stream"),
            "",
            Elements.VENTUS, Ranges.OTHER_1R, 120).roles(SkillRole.ATTACK).prio(1)
            .effects(new Damage.Builder(SkillType.STR, Elements.VENTUS, 55).build()).build();
    // Mag

    /// Terra
    // Str
    // Mag

    /// Lux
    // Str
    // Mag

    /// Malum
    // Str
    // Mag
    // todo a way to build multihits that's less repetitive?
    // Ref: Terraria
    public static final Skill DEMON_SCYTHE = new Skill.Builder(lang.get("skill.demon_scythe"),
            "skill_desc.multihit_buff",
            Elements.MALUM, Ranges.OTHER_2R, 120)
            .roles(SkillRole.ATTACK)
            .effects(new Damage.Builder(SkillType.MAG, Elements.MALUM, 50).build(),
                    new GiveBuff.Builder(Buffs.CURSE, 2).build(),
                    new Damage.Builder(SkillType.MAG, Elements.MALUM, 50).build(),
                    new GiveBuff.Builder(Buffs.CURSE, 2).build())
            .descArgs("2", C_NEG + "+1 ", Buffs.CURSE.getNameWithIcon(C_BUFF), "2")
            .build();

    ////////// Unique skills

    ///// Bloom skills
    // Stages

    // Heals

    // Shields

    ///// Attacks
    /// Ignis

    /// Glacies
    public static final Skill ICE_AGE = new Skill.Builder(lang.get("skill.ice_age"), "skill_desc.buff.2.same_turns",
            Elements.GLACIES,
            Ranges.OTHER_2R, 600)
            .bloom().roles(SkillRole.ATTACK)
            .effects(new Damage.Builder(SkillType.STR, Elements.GLACIES, 260).build(),
                    new GiveBuff.Builder(Buffs.FROSTBITE, 3).stacks(3).build(),
                    new GiveBuff.Builder(Buffs.FROSTBOUND, 3).build())
            .descArgs(C_NEG + "+3 ", Buffs.FROSTBITE.getNameWithIcon(C_BUFF), "",
                    Buffs.FROSTBOUND.getNameWithIcon(C_BUFF), "3")
            .build();

    /// Fulgur

    /// Ventus

    /// Terra

    /// Lux

    /// Malum

    /// Unique
}
