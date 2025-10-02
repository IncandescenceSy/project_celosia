package io.github.celosia.sys.battle;

import io.github.celosia.sys.battle.skill_effects.ChangeBloom;
import io.github.celosia.sys.battle.skill_effects.ChangeSp;
import io.github.celosia.sys.battle.skill_effects.ChangeStage;
import io.github.celosia.sys.battle.skill_effects.Damage;
import io.github.celosia.sys.battle.skill_effects.GiveBuff;
import io.github.celosia.sys.battle.skill_effects.Heal;

import static io.github.celosia.sys.save.Lang.lang;

public class Skills {

    ///// Temp testing skills
    public static final Skill RASETU_FEAST = new Skill.Builder("Rasetu Feast", "", Elements.VIS,
            Ranges.OTHER_3R_OR_SELF, 1)
            .effects(new GiveBuff.Builder(Buffs.BURN, 3).build(), new GiveBuff.Builder(Buffs.FROSTBITE, 3).build(),
                    new GiveBuff.Builder(Buffs.SHOCK, 3).build(), new GiveBuff.Builder(Buffs.TREMOR, 3).build())
            .build();
    public static final Skill GET_EXA = new Skill.Builder("Get ExA", "", Elements.VIS, Ranges.OTHER_3R_OR_SELF, 1)
            .effect(new GiveBuff.Builder(Buffs.EXTRA_ACTION, 3).build()).build();
    public static final Skill STAR_RULER = new Skill.Builder("Star Ruler", "", Elements.IGNIS, Ranges.OTHER_2R, 0)
            .cooldown(6).bloom()
            .effects(new Damage.Builder(SkillType.STR, Elements.IGNIS, 280).build(),
                    new GiveBuff.Builder(Buffs.STAR_RULER, 5).giveToSelf().build())
            .build();

    ////////// Basic skills
    public static final Skill NOTHING = new Skill.Builder(lang.get("skill.nothing"), lang.get("skill.nothing.desc"),
            Elements.VIS,
            Ranges.SELF, 0).build();
    public static final Skill DEFEND = new Skill.Builder(lang.get("skill.defend"), lang.get("skill.defend.desc"),
            Elements.VIS,
            Ranges.SELF, 0).prio(3).role(SkillRole.BUFF_DEFENSIVE)
            .effects(new GiveBuff.Builder(Buffs.DEFEND, 1).build(),
                    new ChangeSp.Builder(70).giveToSelf().build())
            .build();
    public static final Skill PROTECT = new Skill.Builder(lang.get("skill.protect"), lang.get("skill.protect.desc"),
            Elements.VIS,
            Ranges.SELF, 40).cooldown(2).prio(3).role(SkillRole.BUFF_DEFENSIVE)
            .effect(new GiveBuff.Builder(Buffs.PROTECT, 1).notInstant().build()).build();
    public static final Skill BLOSSOM = new Skill.Builder(lang.get("skill.blossom"), lang.get("skill.blossom.desc"),
            Elements.VIS,
            Ranges.SELF, 300)
            // todo roles
            .effects(new ChangeBloom.Builder(300).notInstant().build()).build();

    // Stage changers
    public static final Skill ATTACK_UP = new Skill.Builder(lang.get("skill.atk_up"), lang.get("skill.atk_up.desc"),
            Elements.VIS,
            Ranges.OTHER_2R_OR_SELF, 50).role(SkillRole.BUFF_OFFENSIVE)
            .effect(new ChangeStage.Builder(StageType.ATK, 5, 2).notInstant().build()).build();
    public static final Skill DEFENSE_UP = new Skill.Builder(lang.get("skill.def_up"), lang.get("skill.def_up.desc"),
            Elements.VIS,
            Ranges.OTHER_2R_OR_SELF, 50).role(SkillRole.BUFF_DEFENSIVE)
            .effect(new ChangeStage.Builder(StageType.DEF, 5, 2).notInstant().build()).build();
    public static final Skill FAITH_UP = new Skill.Builder(lang.get("skill.fth_up"), lang.get("skill.fth_up.desc"),
            Elements.VIS,
            Ranges.OTHER_2R_OR_SELF, 50).role(SkillRole.BUFF_DEFENSIVE)
            .effect(new ChangeStage.Builder(StageType.FTH, 5, 2).notInstant().build()).build();
    public static final Skill AGILITY_UP = new Skill.Builder(lang.get("skill.agi_up"), lang.get("skill.agi_up.desc"),
            Elements.VIS,
            Ranges.OTHER_2R_OR_SELF, 50).role(SkillRole.BUFF_OFFENSIVE)
            .effect(new ChangeStage.Builder(StageType.AGI, 5, 2).notInstant().build()).build();
    public static final Skill ATTACK_DOWN = new Skill.Builder(lang.get("skill.atk_down"),
            lang.get("skill.atk_down.desc"),
            Elements.VIS, Ranges.OTHER_2R_OR_SELF, 50).role(SkillRole.DEBUFF_DEFENSIVE)
            .effect(new ChangeStage.Builder(StageType.ATK, 5, -2).notInstant().build()).build();
    public static final Skill DEFENSE_DOWN = new Skill.Builder(lang.get("skill.def_down"),
            lang.get("skill.def_down.desc"),
            Elements.VIS, Ranges.OTHER_2R_OR_SELF, 50).role(SkillRole.DEBUFF_OFFENSIVE)
            .effect(new ChangeStage.Builder(StageType.DEF, 5, -2).notInstant().build()).build();
    public static final Skill FAITH_DOWN = new Skill.Builder(lang.get("skill.fth_down"),
            lang.get("skill.fth_down.desc"),
            Elements.VIS, Ranges.OTHER_2R_OR_SELF, 50).role(SkillRole.DEBUFF_OFFENSIVE)
            .effect(new ChangeStage.Builder(StageType.FTH, 5, -2).notInstant().build()).build();
    public static final Skill AGILITY_DOWN = new Skill.Builder(lang.get("skill.agi_down"),
            lang.get("skill.agi_down.desc"),
            Elements.VIS, Ranges.OTHER_2R_OR_SELF, 50).role(SkillRole.DEBUFF_DEFENSIVE)
            .effect(new ChangeStage.Builder(StageType.AGI, 5, -2).notInstant().build()).build();

    public static final Skill ATTACK_UP_GROUP = new Skill.Builder(lang.get("skill.atk_up_group"),
            lang.get("skill.atk_up_group.desc"), Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .role(SkillRole.BUFF_OFFENSIVE)
            .effect(new ChangeStage.Builder(StageType.ATK, 5, 2).notInstant().build()).build();
    public static final Skill DEFENSE_UP_GROUP = new Skill.Builder(lang.get("skill.def_up_group"),
            lang.get("skill.def_up_group.desc"), Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .role(SkillRole.BUFF_DEFENSIVE)
            .effect(new ChangeStage.Builder(StageType.DEF, 5, 2).notInstant().build()).build();
    public static final Skill FAITH_UP_GROUP = new Skill.Builder(lang.get("skill.fth_up_group"),
            lang.get("skill.fth_up_group.desc"),
            Elements.VIS, Ranges.COLUMN_OF_3_1R, 150).role(SkillRole.BUFF_DEFENSIVE)
            .effect(new ChangeStage.Builder(StageType.FTH, 5, 2).notInstant().build()).build();
    public static final Skill AGILITY_UP_GROUP = new Skill.Builder(lang.get("skill.agi_up_group"),
            lang.get("skill.agi_up_group.desc"), Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .role(SkillRole.BUFF_OFFENSIVE)
            .effect(new ChangeStage.Builder(StageType.AGI, 5, 2).notInstant().build()).build();
    public static final Skill ATTACK_DOWN_GROUP = new Skill.Builder(lang.get("skill.atk_down_group"),
            lang.get("skill.atk_down_group.desc"), Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .role(SkillRole.DEBUFF_DEFENSIVE)
            .effect(new ChangeStage.Builder(StageType.ATK, 5, -2).notInstant().build()).build();
    public static final Skill DEFENSE_DOWN_GROUP = new Skill.Builder(lang.get("skill.def_down_group"),
            lang.get("skill.def_down_group.desc"), Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .role(SkillRole.DEBUFF_OFFENSIVE)
            .effect(new ChangeStage.Builder(StageType.DEF, 5, -2).notInstant().build()).build();
    public static final Skill FAITH_DOWN_GROUP = new Skill.Builder(lang.get("skill.fth_down_group"),
            lang.get("skill.fth_down_group.desc"), Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .role(SkillRole.DEBUFF_OFFENSIVE)
            .effect(new ChangeStage.Builder(StageType.FTH, 5, -2).notInstant().build()).build();
    public static final Skill AGILITY_DOWN_GROUP = new Skill.Builder(lang.get("skill.agi_down_group"),
            lang.get("skill.agi_down_group.desc"), Elements.VIS, Ranges.COLUMN_OF_3_1R, 150)
            .role(SkillRole.DEBUFF_DEFENSIVE)
            .effect(new ChangeStage.Builder(StageType.AGI, 5, -2).notInstant().build()).build();

    // Heals
    public static final Skill HEAL = new Skill.Builder(lang.get("skill.heal"), lang.get("skill.heal.desc"),
            Elements.VIS,
            Ranges.OTHER_1R_OR_SELF, 80).role(SkillRole.HEAL).effect(new Heal.Builder(40).build()).build();
    public static final Skill AMBROSIA = new Skill.Builder(lang.get("skill.ambrosia"), lang.get("skill.ambrosia.desc"),
            Elements.VIS,
            Ranges.OTHER_1R_OR_SELF, 125).role(SkillRole.HEAL).effect(new Heal.Builder(60).build()).build();
    public static final Skill HEAL_GROUP = new Skill.Builder(lang.get("skill.heal_group"),
            lang.get("skill.heal_group.desc"),
            Elements.VIS, Ranges.COLUMN_OF_3_1R, 260).role(SkillRole.HEAL).effect(new Heal.Builder(40).build()).build();

    // Shields
    public static final Skill SHIELD = new Skill.Builder(lang.get("skill.shield"), lang.get("skill.shield.desc"),
            Elements.VIS,
            Ranges.OTHER_1R_OR_SELF, 140).role(SkillRole.SHIELD).effect(new Heal.Builder(60).shieldTurns(5).build())
            .build();
    public static final Skill SHIELD_GROUP = new Skill.Builder(lang.get("skill.shield_group"),
            lang.get("skill.shield_group.desc"),
            Elements.VIS, Ranges.COLUMN_OF_3_1R, 450).role(SkillRole.SHIELD)
            .effect(new Heal.Builder(60).shieldTurns(5).build()).build();

    /// Ignis
    // Str
    // Mag
    public static final Skill FIREBALL = new Skill.Builder(lang.get("skill.fireball"), lang.get("skill.fireball.desc"),
            Elements.IGNIS, Ranges.OTHER_1R, 50).role(SkillRole.ATTACK)
            .effects(new Damage.Builder(SkillType.MAG, Elements.IGNIS, 50).build(),
                    new GiveBuff.Builder(Buffs.BURN, 3).build())
            .build();
    public static final Skill HEAT_WAVE = new Skill.Builder(lang.get("skill.heat_wave"),
            lang.get("skill.heat_wave.desc"),
            Elements.IGNIS, Ranges.COLUMN_OF_3_1R, 180).role(SkillRole.ATTACK)
            .effects(new Damage.Builder(SkillType.MAG, Elements.IGNIS, 60).build(),
                    new GiveBuff.Builder(Buffs.BURN, 2).build())
            .build();

    /// Glacies
    // Str
    // Mag
    public static final Skill ICE_BEAM = new Skill.Builder(lang.get("skill.ice_beam"), lang.get("skill.ice_beam.desc"),
            Elements.GLACIES, Ranges.OTHER_1R, 60).role(SkillRole.ATTACK)
            .effects(new Damage.Builder(SkillType.MAG, Elements.GLACIES, 80).build(),
                    new GiveBuff.Builder(Buffs.FROSTBITE, 3).build())
            .build();

    /// Fulgur
    // Str
    // Mag
    public static final Skill THUNDERBOLT = new Skill.Builder(lang.get("skill.thunderbolt"),
            lang.get("skill.thunderbolt.desc"),
            Elements.FULGUR, Ranges.OTHER_1R, 80).role(SkillRole.ATTACK)
            .effects(new Damage.Builder(SkillType.MAG, Elements.FULGUR, 100).build(),
                    new GiveBuff.Builder(Buffs.SHOCK, 3).build())
            .build();

    /// Ventus
    // Str
    public static final Skill JET_STREAM = new Skill.Builder(lang.get("skill.jet_stream"),
            lang.get("skill.jet_stream.desc"),
            Elements.VENTUS, Ranges.OTHER_1R, 120).role(SkillRole.ATTACK).prio(1)
            .effect(new Damage.Builder(SkillType.STR, Elements.VENTUS, 55).build()).build();
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
            lang.get("skill.demon_scythe.desc"),
            Elements.MALUM, Ranges.OTHER_2R, 120)
            .role(SkillRole.ATTACK)
            .effects(new Damage.Builder(SkillType.MAG, Elements.MALUM, 50).build(),
                    new GiveBuff.Builder(Buffs.CURSE, 2).build(),
                    new Damage.Builder(SkillType.MAG, Elements.MALUM, 50).build(),
                    new GiveBuff.Builder(Buffs.CURSE, 2).build())
            .build();

    ////////// Unique skills

    ///// Bloom skills
    // Stages

    // Heals

    // Shields

    ///// Attacks
    /// Ignis

    /// Glacies
    public static final Skill ICE_AGE = new Skill.Builder(lang.get("skill.ice_age"), lang.get("skill.ice_age.desc"),
            Elements.GLACIES,
            Ranges.OTHER_2R, 600)
            .bloom().role(SkillRole.ATTACK)
            .effects(new Damage.Builder(SkillType.STR, Elements.GLACIES, 260).build(),
                    new GiveBuff.Builder(Buffs.FROSTBITE, 3).stacks(3).build(),
                    new GiveBuff.Builder(Buffs.FROSTBOUND, 3).build())
            .build();

    /// Fulgur

    /// Ventus

    /// Terra

    /// Lux

    /// Malum

    /// Unique
}
