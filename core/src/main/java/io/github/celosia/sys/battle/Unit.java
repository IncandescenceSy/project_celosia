package io.github.celosia.sys.battle;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.celosia.Main.ELEMENTS;
import static io.github.celosia.Main.STAGE_TYPES;
import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.battle.BattleLib.STAGE_TYPE_NOT_FOUND;
import static io.github.celosia.sys.battle.BattleLib.STAT_MULT_HIDDEN;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_BUFF;
import static io.github.celosia.sys.util.TextLib.C_HP;
import static io.github.celosia.sys.util.TextLib.C_NEG;
import static io.github.celosia.sys.util.TextLib.C_NUM;
import static io.github.celosia.sys.util.TextLib.C_SHIELD;
import static io.github.celosia.sys.util.TextLib.formatName;
import static io.github.celosia.sys.util.TextLib.formatNum;
import static io.github.celosia.sys.util.TextLib.getColor;
import static io.github.celosia.sys.util.TextLib.getSign;
import static io.github.celosia.sys.util.TextLib.getStageStatString;

// Species and current stats
public class Unit {

    private final UnitType unitType;
    private final long lvl;

    // Stats
    // statsMult is treated as multipliers applied to statsDefault, in 10ths of a % (1000 = 100%)
    // Is never treated as less than 10%
    // HP, Str, Mag, Amr, Res, and Agi
    private final Stats statsDefault;
    private final Stats statsMult;

    // Current HP needs to be separate
    private long hp;

    private final List<SkillInstance> skillInstances = new ArrayList<>();
    private final List<Passive> passives;

    // Equipped item (Accessory or Weapon)
    private EquippableEntity equipped;

    /// Affinities
    // Multiplies the damage dealt, damage taken, and SP cost for their corresponding element
    private final Map<Element, Integer> affinities;

    // Skill Points
    private int sp;

    /// Position on the battlefield
    // 0 4
    // 1 5
    // 2 6
    // 3 7
    private int pos;

    /// Stat stages
    // Increases/decreases corresponding stats by +10/-5% each level, between -5 and
    // +5 levels
    private int stageAtk; // Str and Mag
    private int stageAtkTurns;
    private int stageDef; // Amr and Res
    private int stageDefTurns;
    private int stageFth;
    private int stageFthTurns;
    private int stageAgi;
    private int stageAgiTurns;

    /// Multipliers
    // Multiplies the corresponding numbers
    // In 10ths of a % (1000 = *100%)
    // Raised to the corresponding exponent
    // Exps are in hundredths (100 = ^1, 150 = ^1.5, etc)
    // Calculations are performed on max(mult^exp, 10%)

    private int multDmgDealt;
    private int expDmgDealt;
    private int multDmgTaken;
    private int expDmgTaken;

    private final Map<Element, Integer> multElementDmgDealt = new HashMap<>();
    private final Map<Element, Integer> expElementDmgDealt = new HashMap<>();
    private final Map<Element, Integer> multElementDmgTaken = new HashMap<>();
    private final Map<Element, Integer> expElementDmgTaken = new HashMap<>();

    private int multWeakDmgDealt;
    private int expWeakDmgDealt;
    private int multWeakDmgTaken;
    private int expWeakDmgTaken;

    private int multFollowUpDmgDealt;
    private int expFollowUpDmgDealt;
    private int multFollowUpDmgTaken;
    private int expFollowUpDmgTaken;

    // For exclusively DoT damage (negative ChangeHp)
    private int multDoTDmgTaken;
    private int expDoTDmgTaken;

    // For %-based damage. Primarily reserved for bosses
    // Unlike other Mults, the minimum is 0.1%
    private int multPercentageDmgTaken;
    private int expPercentageDmgTaken;

    private int multHealingDealt;
    private int expHealingDealt;
    private int multHealingTaken;
    private int expHealingTaken;

    private int multSpGain;
    private int expSpGain;
    private int multSpUse;
    private int expSpUse;

    private long shield;

    // Defend (essentially a 2nd Shield with higher priority)
    private long defend;

    private int extraActions;

    /// Boolean stats
    // Secondary effect block; >= 1 blocks secondary effects the same as Shield
    private int effectBlock;

    // >= 1 means SP is infinite
    private int infiniteSp;

    // >= 1 removes ability to move
    private int unableToAct;

    // >= 1 conveys immunity to unableToAct
    private int unableToActImmunity;

    // >= 1 disables equipped
    private int equipDisabled;

    // >= 1 conveys immunity to equipDisabled
    private int equipDisabledImmunity;

    /// Modifiers
    private int modDurationBuffDealt;
    private int modDurationBuffTaken;
    private int modDurationDebuffDealt;
    private int modDurationDebuffTaken;

    private int modStacksBuffDealt;
    private int modStacksBuffTaken;
    private int modStacksDebuffDealt;
    private int modStacksDebuffTaken;

    private int modRange;

    private List<BuffInstance> buffInstances = new ArrayList<>();

    public Unit(UnitType unitType, long lvl, Skill[] skills, EquippableEntity equipped, int pos) {
        this.unitType = unitType;
        this.lvl = lvl;
        statsDefault = unitType.getStatsBase().getRealStats(lvl);
        statsMult = new Stats(1000);
        hp = statsDefault.getHp();
        for (Skill i : skills) skillInstances.add(i.toSkillInstance());
        passives = new ArrayList<>(List.of(unitType.getPassives()));
        affinities = unitType.getAffinities();
        this.equipped = equipped;
        equipped.equip(this);
        sp = 200;
        this.pos = pos;
        stageAtk = 0;
        stageAtkTurns = 0;
        stageDef = 0;
        stageDefTurns = 0;
        stageFth = 0;
        stageFthTurns = 0;
        stageAgi = 0;
        stageAgiTurns = 0;
        multDmgDealt = 1000;
        multDmgTaken = 1000;
        multWeakDmgDealt = 1000;
        multWeakDmgTaken = 1000;
        multFollowUpDmgDealt = 1000;
        multFollowUpDmgTaken = 1000;
        multDoTDmgTaken = 1000;
        multPercentageDmgTaken = 1000;
        multHealingDealt = 1000;
        multHealingTaken = 1000;
        multSpGain = 1000;
        multSpUse = 1000;
        expDmgDealt = 100;
        expDmgTaken = 100;
        expWeakDmgDealt = 100;
        expWeakDmgTaken = 100;
        expFollowUpDmgDealt = 100;
        expFollowUpDmgTaken = 100;
        expDoTDmgTaken = 100;
        expPercentageDmgTaken = 100;
        expHealingDealt = 100;
        expHealingTaken = 100;
        expSpGain = 100;
        expSpUse = 100;
        shield = 0;
        defend = 0;
        extraActions = 0;
        effectBlock = 0;
        infiniteSp = 0;
        unableToAct = 0;
        equipDisabled = 0;
        modDurationBuffDealt = 0;
        modDurationBuffTaken = 0;
        modDurationDebuffDealt = 0;
        modDurationDebuffTaken = 0;
        modStacksBuffDealt = 0;
        modStacksBuffTaken = 0;
        modStacksDebuffDealt = 0;
        modStacksDebuffTaken = 0;
        modRange = 0;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public long getLvl() {
        return lvl;
    }

    public Stats getStatsDefault() {
        return statsDefault;
    }

    public Stats getStatsMult() {
        return statsMult;
    }

    public void setHp(long hp) {
        this.hp = hp;
    }

    public long getHp() {
        return hp;
    }

    public long getDisplayHp() {
        return hp / STAT_MULT_HIDDEN;
    }

    public long getMaxHp() {
        return statsDefault.getHp();
    }

    public long getDisplayMaxHp() {
        return statsDefault.getDisplayHp();
    }

    public long getStr() {
        return this.getStat(Stat.STR);
    }

    public long getMag() {
        return this.getStat(Stat.MAG);
    }

    public long getFth() {
        return this.getStat(Stat.FTH);
    }

    public long getAmr() {
        return this.getStat(Stat.AMR);
    }

    public long getRes() {
        return this.getStat(Stat.RES);
    }

    public long getAgi() {
        return this.getStat(Stat.AGI);
    }

    public long getStat(Stat stat) {
        long val = (long) (statsDefault.getStat(stat) * (Math.max(statsMult.getStat(stat), 100) / 1000d));

        // Only way for val to be negative is if an overflow happened, so set it to max long just to be safe
        if (val < 0) {
            val = Long.MAX_VALUE;
        } else if (val == 0) {
            val = 1;
        }

        return val;
    }

    public long getDisplayStr() {
        return this.getStr() / STAT_MULT_HIDDEN;
    }

    public long getDisplayMag() {
        return this.getMag() / STAT_MULT_HIDDEN;
    }

    public long getDisplayFth() {
        return this.getFth() / STAT_MULT_HIDDEN;
    }

    public long getDisplayAmr() {
        return this.getAmr() / STAT_MULT_HIDDEN;
    }

    public long getDisplayRes() {
        return this.getRes() / STAT_MULT_HIDDEN;
    }

    public long getDisplayAgi() {
        return this.getAgi() / STAT_MULT_HIDDEN;
    }

    public long getDisplayStat(Stat stat) {
        return this.getStat(stat) / STAT_MULT_HIDDEN;
    }

    public List<SkillInstance> getSkillInstances() {
        return skillInstances;
    }

    public Skill getSkill(int index) {
        return skillInstances.get(index).getSkill();
    }

    public int getSkillCount() {
        return skillInstances.size();
    }

    public void addSkills(Skill... skills) {
        for (Skill skill : skills) skillInstances.add(skill.toSkillInstance());
    }

    public void removeSkills(Skill... skills) {
        for (Skill skill : skills) skillInstances.remove(skill.toSkillInstance());
    }

    public List<Passive> getPassives() {
        return passives;
    }

    public Passive getPassive(int index) {
        return passives.get(index);
    }

    public int getPassiveCount() {
        return passives.size();
    }

    public void addPassives(Passive... passives) {
        for (Passive passive : passives) {
            this.passives.add(passive);
            for (BuffEffect buffEffect : passive.getBuffEffects()) buffEffect.onGive(this, 1);
        }
    }

    public void removePassives(Passive... passives) {
        for (Passive passive : passives) {
            this.passives.remove(passive);
            for (BuffEffect buffEffect : passive.getBuffEffects()) buffEffect.onRemove(this, 1);
        }
    }

    public void setEquipped(EquippableEntity equipped) {
        equipped.unequip(this);
        this.equipped = equipped;
        equipped.equip(this);
    }

    public EquippableEntity getEquipped() {
        return equipped;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public int getSp() {
        return sp;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setStage(StageType stageType, int stage) {
        if (stageType == StageTypes.ATK) {
            stageAtk = stage;
            return;
        }
        if (stageType == StageTypes.DEF) {
            stageDef = stage;
            return;
        }
        if (stageType == StageTypes.FTH) {
            stageFth = stage;
            return;
        }
        if (stageType == StageTypes.AGI) {
            stageAgi = stage;
        }
    }

    public int getStage(StageType stageType) {
        if (stageType == StageTypes.ATK) return stageAtk;
        if (stageType == StageTypes.DEF) return stageDef;
        if (stageType == StageTypes.FTH) return stageFth;
        if (stageType == StageTypes.AGI) return stageAgi;

        return STAGE_TYPE_NOT_FOUND;
    }

    public void setStageTurns(StageType stageType, int turns) {
        if (stageType == StageTypes.ATK) {
            stageAtkTurns = turns;
            return;
        }
        if (stageType == StageTypes.DEF) {
            stageDefTurns = turns;
            return;
        }
        if (stageType == StageTypes.FTH) {
            stageFthTurns = turns;
            return;
        }
        if (stageType == StageTypes.AGI) {
            stageAgiTurns = turns;
        }
    }

    public int getStageTurns(StageType stageType) {
        if (stageType == StageTypes.ATK) return stageAtkTurns;
        if (stageType == StageTypes.DEF) return stageDefTurns;
        if (stageType == StageTypes.FTH) return stageFthTurns;
        if (stageType == StageTypes.AGI) return stageAgiTurns;

        return STAGE_TYPE_NOT_FOUND;
    }

    public List<StageType> getAlteredStages() {
        List<StageType> alteredStages = new ArrayList<>(4);

        for (StageType stageType : STAGE_TYPES) {
            if (this.getStage(stageType) != 0) {
                alteredStages.add(stageType);
            }
        }

        return alteredStages;
    }

    public long getStrWithStage() {
        return this.getStatWithStage(Stat.STR);
    }

    public long getMagWithStage() {
        return this.getStatWithStage(Stat.MAG);
    }

    public long getFthWithStage() {
        return this.getStatWithStage(Stat.FTH);
    }

    public long getAmrWithStage() {
        return this.getStatWithStage(Stat.AMR);
    }

    public long getResWithStage() {
        return this.getStatWithStage(Stat.RES);
    }

    public long getAgiWithStage() {
        return this.getStatWithStage(Stat.AGI);
    }

    public long getStatWithStage(Stat stat) {
        return switch (stat) {
            case STR -> Math.max(
                    this.getStr() +
                            (long) (statsDefault.getStr() * (((double) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1))),
                    1);
            case MAG -> Math.max(
                    this.getMag() +
                            (long) (statsDefault.getMag() * (((double) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1))),
                    1);
            case FTH -> Math.max(
                    this.getFth() +
                            (long) (statsDefault.getFth() * (((double) stageFth / 10) / ((stageFth < 0) ? 2 : 1))),
                    1);
            case AMR -> Math.max(
                    this.getAmr() +
                            (long) (statsDefault.getAmr() * (((double) stageDef / 10) / ((stageDef < 0) ? 2 : 1))),
                    1);
            case RES -> Math.max(
                    this.getRes() +
                            (long) (statsDefault.getRes() * (((double) stageDef / 10) / ((stageDef < 0) ? 2 : 1))),
                    1);
            case AGI -> Math.max(
                    this.getAgi() +
                            (long) (statsDefault.getAgi() * (((double) stageAgi / 10) / ((stageAgi < 0) ? 2 : 1))),
                    1);
        };
    }

    public long getStatWithStage(Stat stat, int stage) {
        return switch (stat) {
            case STR -> Math.max(
                    this.getStr() + (long) (statsDefault.getStr() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
                    1);
            case MAG -> Math.max(
                    this.getMag() + (long) (statsDefault.getMag() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
                    1);
            case FTH -> Math.max(
                    this.getFth() + (long) (statsDefault.getFth() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
                    1);
            case AMR -> Math.max(
                    this.getAmr() + (long) (statsDefault.getAmr() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
                    1);
            case RES -> Math.max(
                    this.getRes() + (long) (statsDefault.getRes() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
                    1);
            case AGI -> Math.max(
                    this.getAgi() + (long) (statsDefault.getAgi() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
                    1);
        };
    }

    public long getDisplayStatWithStage(Stat stat) {
        return switch (stat) {
            case STR -> Math.max(this.getDisplayStr() +
                    (long) (statsDefault.getDisplayStr() * (((double) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1))), 1);
            case MAG -> Math.max(this.getDisplayMag() +
                    (long) (statsDefault.getDisplayMag() * (((double) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1))), 1);
            case FTH -> Math.max(this.getDisplayFth() +
                    (long) (statsDefault.getDisplayFth() * (((double) stageFth / 10) / ((stageFth < 0) ? 2 : 1))), 1);
            case AMR -> Math.max(this.getDisplayAmr() +
                    (long) (statsDefault.getDisplayAmr() * (((double) stageDef / 10) / ((stageDef < 0) ? 2 : 1))), 1);
            case RES -> Math.max(this.getDisplayRes() +
                    (long) (statsDefault.getDisplayRes() * (((double) stageDef / 10) / ((stageDef < 0) ? 2 : 1))), 1);
            case AGI -> Math.max(this.getDisplayAgi() +
                    (long) (statsDefault.getDisplayAgi() * (((double) stageAgi / 10) / ((stageAgi < 0) ? 2 : 1))), 1);
        };
    }

    public long getDisplayStatWithStage(Stat stat, int stage) {
        return switch (stat) {
            case STR -> Math.max(
                    this.getDisplayStr() +
                            (long) (statsDefault.getDisplayStr() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
                    1);
            case MAG -> Math.max(
                    this.getDisplayMag() +
                            (long) (statsDefault.getDisplayMag() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
                    1);
            case FTH -> Math.max(
                    this.getDisplayFth() +
                            (long) (statsDefault.getDisplayFth() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
                    1);
            case AMR -> Math.max(
                    this.getDisplayAmr() +
                            (long) (statsDefault.getDisplayAmr() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
                    1);
            case RES -> Math.max(
                    this.getDisplayRes() +
                            (long) (statsDefault.getDisplayRes() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
                    1);
            case AGI -> Math.max(
                    this.getDisplayAgi() +
                            (long) (statsDefault.getDisplayAgi() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
                    1);
        };
    }

    public Map<Element, Integer> getAffinities() {
        return affinities;
    }

    public int getAffinity(Element element) {
        return affinities.getOrDefault(element, 0);
    }

    public String getAffinitiesString() {
        StringBuilder str = new StringBuilder();

        for (Element element : ELEMENTS) {
            int aff = affinities.getOrDefault(element, 0);
            str.append(element.getIcon()).append(getColor(aff)).append(getSign(aff)).append(aff).append("[WHITE]  ");
        }

        return str.toString();
    }

    public void setMult(Mult mult, int set) {
        switch (mult) {
            case DMG_DEALT -> multDmgDealt = set;
            case DMG_TAKEN -> multDmgTaken = set;
            case IGNIS_DMG_DEALT -> multElementDmgDealt.put(Elements.IGNIS, set);
            case IGNIS_DMG_TAKEN -> multElementDmgTaken.put(Elements.IGNIS, set);
            case GLACIES_DMG_DEALT -> multElementDmgDealt.put(Elements.GLACIES, set);
            case GLACIES_DMG_TAKEN -> multElementDmgTaken.put(Elements.GLACIES, set);
            case FULGUR_DMG_DEALT -> multElementDmgDealt.put(Elements.FULGUR, set);
            case FULGUR_DMG_TAKEN -> multElementDmgTaken.put(Elements.FULGUR, set);
            case VENTUS_DMG_DEALT -> multElementDmgDealt.put(Elements.VENTUS, set);
            case VENTUS_DMG_TAKEN -> multElementDmgTaken.put(Elements.VENTUS, set);
            case TERRA_DMG_DEALT -> multElementDmgDealt.put(Elements.TERRA, set);
            case TERRA_DMG_TAKEN -> multElementDmgTaken.put(Elements.TERRA, set);
            case LUX_DMG_DEALT -> multElementDmgDealt.put(Elements.LUX, set);
            case LUX_DMG_TAKEN -> multElementDmgTaken.put(Elements.LUX, set);
            case MALUM_DMG_DEALT -> multElementDmgDealt.put(Elements.MALUM, set);
            case MALUM_DMG_TAKEN -> multElementDmgTaken.put(Elements.MALUM, set);
            case WEAK_DMG_DEALT -> multWeakDmgDealt = set;
            case WEAK_DMG_TAKEN -> multWeakDmgTaken = set;
            case FOLLOW_UP_DMG_DEALT -> multFollowUpDmgDealt = set;
            case FOLLOW_UP_DMG_TAKEN -> multFollowUpDmgTaken = set;
            case DOT_DMG_TAKEN -> multDoTDmgTaken = set;
            case PERCENTAGE_DMG_TAKEN -> multPercentageDmgTaken = set;
            case HEALING_DEALT -> multHealingDealt = set;
            case HEALING_TAKEN -> multHealingTaken = set;
            case SP_GAIN -> multSpGain = set;
            case SP_USE -> multSpUse = set;
        }
    }

    public int getMult(Mult mult) {
        return switch (mult) {
            case DMG_DEALT -> multDmgDealt;
            case DMG_TAKEN -> multDmgTaken;
            case IGNIS_DMG_DEALT -> getMultElementDmgDealt(Elements.IGNIS);
            case IGNIS_DMG_TAKEN -> getMultElementDmgTaken(Elements.IGNIS);
            case GLACIES_DMG_DEALT -> getMultElementDmgDealt(Elements.GLACIES);
            case GLACIES_DMG_TAKEN -> getMultElementDmgTaken(Elements.GLACIES);
            case FULGUR_DMG_DEALT -> getMultElementDmgDealt(Elements.FULGUR);
            case FULGUR_DMG_TAKEN -> getMultElementDmgTaken(Elements.FULGUR);
            case VENTUS_DMG_DEALT -> getMultElementDmgDealt(Elements.VENTUS);
            case VENTUS_DMG_TAKEN -> getMultElementDmgTaken(Elements.VENTUS);
            case TERRA_DMG_DEALT -> getMultElementDmgDealt(Elements.TERRA);
            case TERRA_DMG_TAKEN -> getMultElementDmgTaken(Elements.TERRA);
            case LUX_DMG_DEALT -> getMultElementDmgDealt(Elements.LUX);
            case LUX_DMG_TAKEN -> getMultElementDmgTaken(Elements.LUX);
            case MALUM_DMG_DEALT -> getMultElementDmgDealt(Elements.MALUM);
            case MALUM_DMG_TAKEN -> getMultElementDmgTaken(Elements.MALUM);
            case WEAK_DMG_DEALT -> multWeakDmgDealt;
            case WEAK_DMG_TAKEN -> multWeakDmgTaken;
            case FOLLOW_UP_DMG_DEALT -> multFollowUpDmgDealt;
            case FOLLOW_UP_DMG_TAKEN -> multFollowUpDmgTaken;
            case DOT_DMG_TAKEN -> multDoTDmgTaken;
            case PERCENTAGE_DMG_TAKEN -> multPercentageDmgTaken;
            case HEALING_DEALT -> multHealingDealt;
            case HEALING_TAKEN -> multHealingTaken;
            case SP_GAIN -> multSpGain;
            case SP_USE -> multSpUse;
        };
    }

    public int getMultElementDmgDealt(Element element) {
        return multElementDmgDealt.getOrDefault(element, 1000);
    }

    public int getMultElementDmgTaken(Element element) {
        return multElementDmgTaken.getOrDefault(element, 1000);
    }

    public Map<Element, Integer> getMultElementDmgDealt() {
        return multElementDmgDealt;
    }

    public Map<Element, Integer> getMultElementDmgTaken() {
        return multElementDmgTaken;
    }

    public void setExp(Mult mult, int set) {
        switch (mult) {
            case DMG_DEALT -> expDmgDealt = set;
            case DMG_TAKEN -> expDmgTaken = set;
            case IGNIS_DMG_DEALT -> expElementDmgDealt.put(Elements.IGNIS, set);
            case IGNIS_DMG_TAKEN -> expElementDmgTaken.put(Elements.IGNIS, set);
            case GLACIES_DMG_DEALT -> expElementDmgDealt.put(Elements.GLACIES, set);
            case GLACIES_DMG_TAKEN -> expElementDmgTaken.put(Elements.GLACIES, set);
            case FULGUR_DMG_DEALT -> expElementDmgDealt.put(Elements.FULGUR, set);
            case FULGUR_DMG_TAKEN -> expElementDmgTaken.put(Elements.FULGUR, set);
            case VENTUS_DMG_DEALT -> expElementDmgDealt.put(Elements.VENTUS, set);
            case VENTUS_DMG_TAKEN -> expElementDmgTaken.put(Elements.VENTUS, set);
            case TERRA_DMG_DEALT -> expElementDmgDealt.put(Elements.TERRA, set);
            case TERRA_DMG_TAKEN -> expElementDmgTaken.put(Elements.TERRA, set);
            case LUX_DMG_DEALT -> expElementDmgDealt.put(Elements.LUX, set);
            case LUX_DMG_TAKEN -> expElementDmgTaken.put(Elements.LUX, set);
            case MALUM_DMG_DEALT -> expElementDmgDealt.put(Elements.MALUM, set);
            case MALUM_DMG_TAKEN -> expElementDmgTaken.put(Elements.MALUM, set);
            case WEAK_DMG_DEALT -> expWeakDmgDealt = set;
            case WEAK_DMG_TAKEN -> expWeakDmgTaken = set;
            case FOLLOW_UP_DMG_DEALT -> expFollowUpDmgDealt = set;
            case FOLLOW_UP_DMG_TAKEN -> expFollowUpDmgTaken = set;
            case DOT_DMG_TAKEN -> expDoTDmgTaken = set;
            case PERCENTAGE_DMG_TAKEN -> expPercentageDmgTaken = set;
            case HEALING_DEALT -> expHealingDealt = set;
            case HEALING_TAKEN -> expHealingTaken = set;
            case SP_GAIN -> expSpGain = set;
            case SP_USE -> expSpUse = set;
        }
    }

    public int getExp(Mult mult) {
        return switch (mult) {
            case DMG_DEALT -> expDmgDealt;
            case DMG_TAKEN -> expDmgTaken;
            case IGNIS_DMG_DEALT -> getExpElementDmgDealt(Elements.IGNIS);
            case IGNIS_DMG_TAKEN -> getExpElementDmgTaken(Elements.IGNIS);
            case GLACIES_DMG_DEALT -> getExpElementDmgDealt(Elements.GLACIES);
            case GLACIES_DMG_TAKEN -> getExpElementDmgTaken(Elements.GLACIES);
            case FULGUR_DMG_DEALT -> getExpElementDmgDealt(Elements.FULGUR);
            case FULGUR_DMG_TAKEN -> getExpElementDmgTaken(Elements.FULGUR);
            case VENTUS_DMG_DEALT -> getExpElementDmgDealt(Elements.VENTUS);
            case VENTUS_DMG_TAKEN -> getExpElementDmgTaken(Elements.VENTUS);
            case TERRA_DMG_DEALT -> getExpElementDmgDealt(Elements.TERRA);
            case TERRA_DMG_TAKEN -> getExpElementDmgTaken(Elements.TERRA);
            case LUX_DMG_DEALT -> getExpElementDmgDealt(Elements.LUX);
            case LUX_DMG_TAKEN -> getExpElementDmgTaken(Elements.LUX);
            case MALUM_DMG_DEALT -> getExpElementDmgDealt(Elements.MALUM);
            case MALUM_DMG_TAKEN -> getExpElementDmgTaken(Elements.MALUM);
            case WEAK_DMG_DEALT -> expWeakDmgDealt;
            case WEAK_DMG_TAKEN -> expWeakDmgTaken;
            case FOLLOW_UP_DMG_DEALT -> expFollowUpDmgDealt;
            case FOLLOW_UP_DMG_TAKEN -> expFollowUpDmgTaken;
            case DOT_DMG_TAKEN -> expDoTDmgTaken;
            case PERCENTAGE_DMG_TAKEN -> expPercentageDmgTaken;
            case HEALING_DEALT -> expHealingDealt;
            case HEALING_TAKEN -> expHealingTaken;
            case SP_GAIN -> expSpGain;
            case SP_USE -> expSpUse;
        };
    }

    public int getExpElementDmgDealt(Element element) {
        return expElementDmgDealt.getOrDefault(element, 100);
    }

    public int getExpElementDmgTaken(Element element) {
        return expElementDmgTaken.getOrDefault(element, 100);
    }

    public Map<Element, Integer> getExpElementDmgDealt() {
        return expElementDmgDealt;
    }

    public Map<Element, Integer> getExpElementDmgTaken() {
        return expElementDmgTaken;
    }

    public double getMultWithExp(Mult mult) {
        return switch (mult) {
            case DMG_DEALT -> Math.max(Math.pow(multDmgDealt / 1000d, expDmgDealt / 100d), 0.1);
            case DMG_TAKEN -> Math.max(Math.pow(multDmgTaken / 1000d, expDmgTaken / 100d), 0.1);
            case IGNIS_DMG_DEALT -> getMultWithExpElementDmgDealt(Elements.IGNIS);
            case IGNIS_DMG_TAKEN -> getMultWithExpElementDmgTaken(Elements.IGNIS);
            case GLACIES_DMG_DEALT -> getMultWithExpElementDmgDealt(Elements.GLACIES);
            case GLACIES_DMG_TAKEN -> getMultWithExpElementDmgTaken(Elements.GLACIES);
            case FULGUR_DMG_DEALT -> getMultWithExpElementDmgDealt(Elements.FULGUR);
            case FULGUR_DMG_TAKEN -> getMultWithExpElementDmgTaken(Elements.FULGUR);
            case VENTUS_DMG_DEALT -> getMultWithExpElementDmgDealt(Elements.VENTUS);
            case VENTUS_DMG_TAKEN -> getMultWithExpElementDmgTaken(Elements.VENTUS);
            case TERRA_DMG_DEALT -> getMultWithExpElementDmgDealt(Elements.TERRA);
            case TERRA_DMG_TAKEN -> getMultWithExpElementDmgTaken(Elements.TERRA);
            case LUX_DMG_DEALT -> getMultWithExpElementDmgDealt(Elements.LUX);
            case LUX_DMG_TAKEN -> getMultWithExpElementDmgTaken(Elements.LUX);
            case MALUM_DMG_DEALT -> getMultWithExpElementDmgDealt(Elements.MALUM);
            case MALUM_DMG_TAKEN -> getMultWithExpElementDmgTaken(Elements.MALUM);
            case WEAK_DMG_DEALT -> Math.max(Math.pow(multWeakDmgDealt / 1000d, expWeakDmgDealt / 100d), 0.1);
            case WEAK_DMG_TAKEN -> Math.max(Math.pow(multWeakDmgTaken / 1000d, expWeakDmgTaken / 100d), 0.1);
            case FOLLOW_UP_DMG_DEALT -> Math.max(Math.pow(multFollowUpDmgDealt / 1000d, expFollowUpDmgDealt / 100d),
                    0.1);
            case FOLLOW_UP_DMG_TAKEN -> Math.max(Math.pow(multFollowUpDmgTaken / 1000d, expFollowUpDmgTaken / 100d),
                    0.1);
            case DOT_DMG_TAKEN -> Math.max(Math.pow(multDoTDmgTaken / 1000d, expDoTDmgTaken / 100d), 0.1);
            case PERCENTAGE_DMG_TAKEN -> Math
                    .max(Math.pow(multPercentageDmgTaken / 1000d, expPercentageDmgTaken / 100d), 0.001);
            case HEALING_DEALT -> Math.max(Math.pow(multHealingDealt / 1000d, expHealingDealt / 100d), 0.1);
            case HEALING_TAKEN -> Math.max(Math.pow(multHealingTaken / 1000d, expHealingTaken / 100d), 0.1);
            case SP_GAIN -> Math.max(Math.pow(multSpGain / 1000d, expSpGain / 100d), 0.1);
            case SP_USE -> Math.max(Math.pow(multSpUse / 1000d, expSpUse / 100d), 0.1);
        };
    }

    public double getMultWithExpElementDmgDealt(Element element) {
        return Math.max(Math.pow(multElementDmgDealt.getOrDefault(element, 1000) / 1000d,
                expElementDmgDealt.getOrDefault(element, 100)), 0.1);
    }

    public double getMultWithExpElementDmgTaken(Element element) {
        return Math.max(Math.pow(multElementDmgTaken.getOrDefault(element, 1000) / 1000d,
                expElementDmgDealt.getOrDefault(element, 100)), 0.1);
    }

    public void setShield(long shield) {
        this.shield = shield;
    }

    public long getShield() {
        return shield;
    }

    public long getDisplayShield() {
        return shield / STAT_MULT_HIDDEN;
    }

    public void setDefend(long defend) {
        this.defend = defend;
    }

    public long getDefend() {
        return defend;
    }

    public long getDisplayDefend() {
        return defend / STAT_MULT_HIDDEN;
    }

    public void setExtraActions(int extraActions) {
        this.extraActions = extraActions;
    }

    public int getExtraActions() {
        return extraActions;
    }

    public void setBooleanStat(BooleanStat stat, int set) {
        switch (stat) {
            case EFFECT_BLOCK -> effectBlock = set;
            case INFINITE_SP -> infiniteSp = set;
            case UNABLE_TO_ACT -> unableToAct = set;
            case UNABLE_TO_ACT_IMMUNITY -> unableToActImmunity = set;
            case EQUIP_DISABLED -> equipDisabled = set;
            case EQUIP_DISABLED_IMMUNITY -> equipDisabledImmunity = set;
        }
    }

    public int getBooleanStat(BooleanStat stat) {
        return switch (stat) {
            case EFFECT_BLOCK -> effectBlock;
            case INFINITE_SP -> infiniteSp;
            case UNABLE_TO_ACT -> unableToAct;
            case UNABLE_TO_ACT_IMMUNITY -> unableToActImmunity;
            case EQUIP_DISABLED -> equipDisabled;
            case EQUIP_DISABLED_IMMUNITY -> equipDisabledImmunity;
        };
    }

    public boolean isBooleanStat(BooleanStat stat) {
        return switch (stat) {
            case EFFECT_BLOCK -> effectBlock > 0;
            case INFINITE_SP -> infiniteSp > 0;
            case UNABLE_TO_ACT -> unableToAct > 0 && unableToActImmunity <= 0;
            case UNABLE_TO_ACT_IMMUNITY -> unableToActImmunity > 0;
            case EQUIP_DISABLED -> equipDisabled > 0 && equipDisabledImmunity <= 0;
            case EQUIP_DISABLED_IMMUNITY -> equipDisabledImmunity > 0;
        };
    }

    public int getDurationModBuffTypeDealt(BuffType buffType) {
        return (buffType == BuffType.BUFF) ? modDurationBuffDealt : modDurationDebuffDealt;
    }

    public int getDurationModBuffTypeTaken(BuffType buffType) {
        return (buffType == BuffType.BUFF) ? modDurationBuffTaken : modDurationDebuffTaken;
    }

    public int getStacksModBuffTypeDealt(BuffType buffType) {
        return (buffType == BuffType.BUFF) ? modStacksBuffDealt : modStacksDebuffDealt;
    }

    public int getStacksModBuffTypeTaken(BuffType buffType) {
        return (buffType == BuffType.BUFF) ? modStacksBuffTaken : modStacksDebuffTaken;
    }

    public void setMod(Mod mod, int set) {
        switch (mod) {
            case DURATION_BUFF_DEALT -> modDurationBuffDealt = set;
            case DURATION_BUFF_TAKEN -> modDurationBuffTaken = set;
            case DURATION_DEBUFF_DEALT -> modDurationDebuffDealt = set;
            case DURATION_DEBUFF_TAKEN -> modDurationDebuffTaken = set;
            case STACKS_BUFF_DEALT -> modStacksBuffDealt = set;
            case STACKS_BUFF_TAKEN -> modStacksBuffTaken = set;
            case STACKS_DEBUFF_DEALT -> modStacksDebuffDealt = set;
            case STACKS_DEBUFF_TAKEN -> modStacksDebuffTaken = set;
            case RANGE -> modRange = set;
        }
    }

    public int getMod(Mod mod) {
        return switch (mod) {
            case DURATION_BUFF_DEALT -> modDurationBuffDealt;
            case DURATION_BUFF_TAKEN -> modDurationBuffTaken;
            case DURATION_DEBUFF_DEALT -> modDurationDebuffDealt;
            case DURATION_DEBUFF_TAKEN -> modDurationDebuffTaken;
            case STACKS_BUFF_DEALT -> modStacksBuffDealt;
            case STACKS_BUFF_TAKEN -> modStacksBuffTaken;
            case STACKS_DEBUFF_DEALT -> modStacksDebuffDealt;
            case STACKS_DEBUFF_TAKEN -> modStacksDebuffTaken;
            case RANGE -> modRange;
        };
    }

    public void setBuffInstances(List<BuffInstance> buffInstances) {
        this.buffInstances = buffInstances;
    }

    public List<BuffInstance> getBuffInstances() {
        return buffInstances;
    }

    public Buff getBuff(int index) {
        return buffInstances.get(index).getBuff();
    }

    public int getBuffCount() {
        return buffInstances.size();
    }

    public void addBuffInstances(BuffInstance... buffInstances) {
        this.buffInstances.addAll(List.of(buffInstances));
    }

    public boolean isProtected() {
        return multDmgTaken <= -5_000_000;
    }

    public boolean isWeakTo(Element element) {
        return this.getAffinity(element) < 0;
    }

    public boolean resists(Element element) {
        return this.getAffinity(element) > 0;
    }

    public boolean isImmuneTo(Element element) {
        return this.getAffinity(element) >= 5;
    }

    public boolean isNeutralTo(Element element) {
        return this.getAffinity(element) == 0;
    }

    // Returns if the requested Passive is present
    public boolean hasPassive(Passive passive) {
        return passives.contains(passive);
    }

    // Returns the requested BuffInstance if present
    public @Nullable BuffInstance getBuffInstance(Buff buff) {
        for (BuffInstance buffInstance : buffInstances) {
            if (buffInstance.getBuff() == buff) {
                return buffInstance;
            }
        }

        return null;
    }

    private void notifyBuffEffects(Unit target, BuffEffectNotifier notifier) {
        // Handle Passives
        for (Passive passive : this.getPassives()) {
            for (BuffEffect buffEffect : passive.getBuffEffects()) {
                notifier.notify(buffEffect, this, target, 1);
            }
        }

        // Handle Buffs
        for (BuffInstance buffInstance : this.getBuffInstances()) {
            for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                notifier.notify(buffEffect, this, target, buffInstance.getStacks());
            }
        }
    }

    public void onUseSkill(Unit target, Skill skill) {
        notifyBuffEffects(target, (effect, s, t, stacks) -> effect.onUseSkill(s, t, stacks, skill));
    }

    public void onTargetedBySkill(Unit target, Skill skill) {
        notifyBuffEffects(target, (effect, s, t, stacks) -> effect.onTargetedBySkill(s, t, stacks, skill));
    }

    public void onDealDamage(Unit target, long damage, Element element) {
        notifyBuffEffects(target, (effect, s, t, stacks) -> effect.onDealDamage(s, t, stacks, damage, element));
    }

    public void onTakeDamage(Unit target, long damage, Element element) {
        notifyBuffEffects(target, (effect, s, t, stacks) -> effect.onDealDamage(s, t, stacks, damage, element));
    }

    public void onDealHeal(Unit target, long heal, int overHeal) {
        notifyBuffEffects(target, (effect, s, t, stacks) -> effect.onDealHeal(s, t, stacks, heal, overHeal));
    }

    public void onTakeHeal(Unit target, long heal, int overHeal) {
        notifyBuffEffects(target, (effect, s, t, stacks) -> effect.onTakeHeal(s, t, stacks, heal, overHeal));
    }

    public void onDealShield(Unit target, int turns, long heal) {
        notifyBuffEffects(target, (effect, s, t, stacks) -> effect.onDealShield(s, t, stacks, turns, heal));
    }

    public void onTakeShield(Unit target, int turns, long heal) {
        notifyBuffEffects(target, (effect, s, t, stacks) -> effect.onTakeShield(s, t, stacks, turns, heal));
    }

    public void onGiveBuff(Unit target, Buff buff, int turnsMod, int stacksChange) {
        notifyBuffEffects(target,
                (effect, s, t, stacks) -> effect.onGiveBuff(s, t, stacks, buff, turnsMod, stacksChange));
    }

    public void onChangeStage(Unit target, StageType stageType, int turnsMod, int stacksChange) {
        notifyBuffEffects(target,
                (effect, s, t, stacks) -> effect.onChangeStage(s, t, stacks, stageType, turnsMod, stacksChange));
    }

    // Returns the Unit's Side
    public Side getSide() {
        return (pos < 4) ? Side.ALLY : Side.OPPONENT;
    }

    public void decrementTurns() {
        // Stages
        if (stageAtk != 0 && --stageAtkTurns <= 0) {
            appendToLog(lang.format("log.lose.stage", formatName(unitType.getName(), pos, false), stageAtk,
                    getColor(stageAtk) + stageAtk, StageTypes.ATK.getIcon() + C_BUFF + StageTypes.ATK.getName(),
                    getStageStatString(this, StageTypes.ATK, 0)));
            // Remove stages
            stageAtk = 0;
        }
        if (stageDef != 0 && --stageDefTurns <= 0) {
            appendToLog(lang.format("log.lose.stage", formatName(unitType.getName(), pos, false), stageDef,
                    getColor(stageDef) + stageDef, StageTypes.DEF.getIcon() + C_BUFF + StageTypes.DEF.getName(),
                    getStageStatString(this, StageTypes.DEF, 0)));
            stageDef = 0;
        }
        if (stageFth != 0 && --stageFthTurns <= 0) {
            appendToLog(lang.format("log.lose.stage", formatName(unitType.getName(), pos, false), stageFth,
                    getColor(stageFth) + stageFth, StageTypes.FTH.getIcon() + C_BUFF + StageTypes.FTH.getName(),
                    getStageStatString(this, StageTypes.FTH, 0)));
            stageFth = 0;
        }
        if (stageAgi != 0 && --stageAgiTurns <= 0) {
            appendToLog(lang.format("log.lose.stage", formatName(unitType.getName(), pos, false), stageAgi,
                    getColor(stageAgi) + stageAgi, StageTypes.AGI.getIcon() + C_BUFF + StageTypes.AGI.getName(),
                    getStageStatString(this, StageTypes.AGI, 0)));
            stageAgi = 0;
        }

        // Buffs
        // Iterate backwards so they can be removed
        for (int i = buffInstances.size() - 1; i >= 0; i--) {
            BuffInstance buffInstance = buffInstances.get(i);
            int turns = buffInstance.getTurns();
            // 1000+ turns = infinite
            if (turns >= 2 && turns < 1000) {
                buffInstance.setTurns(turns - 1);
            } else {
                appendToLog(lang.format("log.lose.buff", formatName(unitType.getName(), pos, false),
                        buffInstance.getBuff().getMaxStacks(), C_NUM + buffInstance.getStacks(),
                        buffInstance.getBuff().getIcon() + C_BUFF + buffInstance.getBuff().getName(),
                        lang.format("log.stack_s", buffInstance.getStacks())));

                // Remove effects
                for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                    buffEffect.onRemove(this, buffInstance.getStacks());
                }
                buffInstances.remove(i);
            }
        }

        // Skill cooldowns
        for (SkillInstance skillInstance : skillInstances) {
            skillInstance.decrementCooldown();
        }
    }

    // Damage Unit, taking into account Defend and Shield
    public Result damage(long dmg, boolean pierce, boolean useName) {
        long dmgFull = dmg;
        long defendOld = defend;

        List<String> msg = new ArrayList<>();

        String name = (useName) ? formatName(unitType.getName(), pos, false) + " " : "";
        String name_s = (useName) ? formatName(unitType.getName(), pos) + " " : "";

        // Pierce skips Defend and Shield
        if (!pierce) {
            if (defend > 0 && dmg > 0) {
                // Only hit Defend
                if (defend > dmg) {
                    defend -= dmg;
                    return new Result(ResultType.HIT_SHIELD,
                            lang.format("log.change_shield", name_s,
                                    C_SHIELD + formatNum((defendOld + shield) / STAT_MULT_HIDDEN),
                                    C_SHIELD + formatNum((defend + shield) / STAT_MULT_HIDDEN),
                                    C_HP + formatNum(statsDefault.getDisplayHp()),
                                    C_NEG + "-" + formatNum(dmgFull / STAT_MULT_HIDDEN)));
                }
                // Destroy Defend and proceed to Shield
                else {
                    dmg -= defend;
                    defend = 0;
                    // todo this should come after the dmg message
                    if (shield == 0 && effectBlock <= 0) {
                        msg.add(lang.format("log.change_boolean_stat.effect_block", name, 0));
                    }
                }
            }

            if (shield > 0 && dmg > 0) {
                // Only hit Shield
                if (shield > dmg) {
                    long shieldOld = shield;
                    shield -= dmg;
                    return new Result(ResultType.HIT_SHIELD,
                            lang.format("log.change_shield", name_s,
                                    C_SHIELD + formatNum((defendOld + shieldOld) / STAT_MULT_HIDDEN),
                                    C_SHIELD + formatNum(shield / STAT_MULT_HIDDEN),
                                    C_HP + formatNum(statsDefault.getDisplayHp()),
                                    C_NEG + "-" + formatNum(dmgFull / STAT_MULT_HIDDEN)));
                }
                // Destroy Shield and proceed to HP
                else {
                    msg.add(lang.format("log.change_shield", name_s,
                            C_SHIELD + formatNum((defendOld + shield) / STAT_MULT_HIDDEN), C_SHIELD + 0,
                            C_HP + formatNum(statsDefault.getDisplayHp()),
                            C_NEG + "-" + formatNum((defendOld + shield) / STAT_MULT_HIDDEN)));
                    dmg -= shield;
                    shield = 0;
                    if (effectBlock <= 0) {
                        msg.add(lang.format("log.change_boolean_stat.effect_block", name, 0));
                    }
                }
            }
        }

        long hpOldDisp = this.getDisplayHp();
        hp = Math.clamp(hp - dmg, 0, statsDefault.getHp());
        long hpNewDisp = this.getDisplayHp();

        msg.add(lang.format("log.change_hp", name_s, C_HP + formatNum(hpOldDisp), C_HP + formatNum(hpNewDisp),
                C_HP + formatNum(statsDefault.getDisplayHp()), C_NEG + "-" + formatNum(dmg / STAT_MULT_HIDDEN)));

        // todo should this be a separate result from hitting shield
        if (effectBlock > 0) {
            return new Result(ResultType.HIT_SHIELD, msg);
        }

        if (dmg > 0) {
            return new Result(ResultType.SUCCESS, msg);
        }

        return new Result(ResultType.FAIL, lang.format("log.no_effect", name));
    }

    public Result damage(long dmg, boolean pierce) {
        return this.damage(dmg, pierce, true);
    }

    public Result damage(long dmg) {
        return this.damage(dmg, false, true);
    }
}
