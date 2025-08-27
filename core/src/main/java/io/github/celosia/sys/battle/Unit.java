package io.github.celosia.sys.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.celosia.sys.menu.TextLib.formatPossessive;
import static io.github.celosia.sys.settings.Lang.lang;

// Species and current stats
public class Unit {
    private final UnitType unitType;
    private final int lvl; // Level

    // Current stats
    private final Stats statsDefault;
    private final Stats statsCur;

    // Skills
    private final Skill[] skills;

    // Passives
    private List<Passive> passives = new ArrayList<>();

    private int sp; // Skill Points
    private int pos; // Position on the battlefield

    // Stat stages
    private int stageAtk;
    private int stageAtkTurns;
    private int stageDef;
    private int stageDefTurns;
    private int stageFth;
    private int stageFthTurns;
    private int stageAgi;
    private int stageAgiTurns;

    // Current affinities
    private int affIgnis;
    private int affGlacies;
    private int affFulgur;
    private int affVentus;
    private int affTerra;
    private int affLux;
    private int affMalum;

    // Multiplies damage dealt/taken. All mults are percentages
    private int multDmgDealt;
    private int multDmgTaken;

    // For each element
    private int multIgnisDmgDealt;
    private int multIgnisDmgTaken;
    private int multGlaciesDmgDealt;
    private int multGlaciesDmgTaken;
    private int multFulgurDmgDealt;
    private int multFulgurDmgTaken;
    private int multVentusDmgDealt;
    private int multVentusDmgTaken;
    private int multTerraDmgDealt;
    private int multTerraDmgTaken;
    private int multLuxDmgDealt;
    private int multLuxDmgTaken;
    private int multMalumDmgDealt;
    private int multMalumDmgTaken;

    // For exclusively weakness damage
    private int multWeakDmgDealt;
    private int multWeakDmgTaken;

    // For exclusively Follow-Up damage
    private int multFollowUpDmgDealt;
    private int multFollowUpDmgTaken;

    // For exclusively DoT damage (negative ChangeHP)
    private int multDoTDmgTaken;

    // For healing and shield
    private int multHealingDealt;
    private int multHealingTaken;

    // Multiplies all SP gain/use
    private int multSpGain;
    private int multSpUse;

    // Shield
    private int shield;
    private int shieldTurns;

    // Defend (essentially a 2nd Shield with higher priority)
    private int defend;

    // Extra actions
    private int extraActions;

    // Secondary effect block; >=1 means blocks secondary effects the same as Barrier
    private int effectBlock;

    // Buff/debuff duration modifiers
    private int durationModBuffDealt;
    private int durationModBuffTaken;
    private int durationModDebuffDealt;
    private int durationModDebuffTaken;

    private List<BuffInstance> buffInstances = new ArrayList<>();

    public Unit(UnitType unitType, int lvl, Skill[] skills, int pos) {
        this.unitType = unitType;
        this.lvl = lvl;
        statsDefault = unitType.getStatsBase().getRealStats(lvl);
        statsCur = new Stats(statsDefault);
        this.skills = skills;
        this.passives = List.of(unitType.getPassives());
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
        affIgnis = unitType.getAffIgnis();
        affGlacies = unitType.getAffGlacies();
        affFulgur = unitType.getAffFulgur();
        affVentus = unitType.getAffVentus();
        affTerra = unitType.getAffTerra();
        affLux = unitType.getAffLux();
        affMalum = unitType.getAffMalum();
        multDmgDealt = 100;
        multDmgTaken = 100;
        multIgnisDmgDealt = 100;
        multIgnisDmgTaken = 100;
        multGlaciesDmgDealt = 100;
        multGlaciesDmgTaken = 100;
        multFulgurDmgDealt = 100;
        multFulgurDmgTaken = 100;
        multVentusDmgDealt = 100;
        multVentusDmgTaken = 100;
        multTerraDmgDealt = 100;
        multTerraDmgTaken = 100;
        multLuxDmgDealt = 100;
        multLuxDmgTaken = 100;
        multMalumDmgDealt = 100;
        multMalumDmgTaken = 100;
        multWeakDmgDealt = 100;
        multWeakDmgTaken = 100;
        multFollowUpDmgDealt = 100;
        multFollowUpDmgTaken = 100;
        multDoTDmgTaken = 100;
        multHealingDealt = 100;
        multHealingTaken = 100;
        multSpGain = 100;
        multSpUse = 100;
        shield = 0;
        shieldTurns = 0;
        defend = 0;
        extraActions = 0;
        effectBlock = 0;
        durationModBuffDealt = 0;
        durationModBuffTaken = 0;
        durationModDebuffDealt = 0;
        durationModDebuffTaken = 0;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public int getLvl() {
        return lvl;
    }

    public Stats getStatsDefault() {
        return statsDefault;
    }

    public Stats getStatsCur() {
        return statsCur;
    }

    public Skill[] getSkills() {
        return skills;
    }

    public void setPassives(List<Passive> passives) {
        this.passives = passives;
    }

    public List<Passive> getPassives() {
        return passives;
    }

    public void addPassive(Passive passive) {
        passives.add(passive);
    }

    public void addPassives(Passive... passives) {
        this.passives.addAll(List.of(passives));
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

    public void setStageAtk(int stageAtk) {
        this.stageAtk = stageAtk;
    }

    public int getStageAtk() {
        return stageAtk;
    }

    public void setStageAtkTurns(int stageAtkTurns) {
        this.stageAtkTurns = stageAtkTurns;
    }

    public int getStageAtkTurns() {
        return stageAtkTurns;
    }

    public void setStageDef(int stageDef) {
        this.stageDef = stageDef;
    }

    public int getStageDef() {
        return stageDef;
    }

    public void setStageDefTurns(int stageDefTurns) {
        this.stageDefTurns = stageDefTurns;
    }

    public int getStageDefTurns() {
        return stageDefTurns;
    }

    public void setStageFth(int stageFth) {
        this.stageFth = stageFth;
    }

    public int getStageFth() {
        return stageFth;
    }

    public void setStageFthTurns(int stageFthTurns) {
        this.stageFthTurns = stageFthTurns;
    }

    public int getStageFthTurns() {
        return stageFthTurns;
    }

    public void setStageAgi(int stageAgi) {
        this.stageAgi = stageAgi;
    }

    public int getStageAgi() {
        return stageAgi;
    }

    public void setStageAgiTurns(int stageAgiTurns) {
        this.stageAgiTurns = stageAgiTurns;
    }

    public int getStageAgiTurns() {
        return stageAgiTurns;
    }

    public void setStage(StageType stageType, int stage) {
        switch (stageType) {
            case ATK -> stageAtk = stage;
            case DEF -> stageDef = stage;
            case FTH -> stageFth = stage;
            case AGI -> stageAgi = stage;
        }
    }

    public int getStage(StageType stageType) {
        return switch (stageType) {
            case ATK -> stageAtk;
            case DEF -> stageDef;
            case FTH -> stageFth;
            case AGI -> stageAgi;
        };
    }

    public void setStageTurns(StageType stageType, int turns) {
        switch (stageType) {
            case ATK -> stageAtkTurns = turns;
            case DEF -> stageDefTurns = turns;
            case FTH -> stageFthTurns = turns;
            case AGI -> stageAgiTurns = turns;
        }
    }

    public int getStageTurns(StageType stageType) {
        return switch (stageType) {
            case ATK -> stageAtkTurns;
            case DEF -> stageDefTurns;
            case FTH -> stageFthTurns;
            case AGI -> stageAgiTurns;
        };
    }

    public int getStrWithStage() {
        return this.getStatsCur().getStr() + (int) (this.getStatsDefault().getStr() * (((double) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1)));
    }

    public int getMagWithStage() {
        return this.getStatsCur().getMag() + (int) (this.getStatsDefault().getMag() * (((double) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1)));
    }

    public int getFthWithStage() {
        return this.getStatsCur().getFth() + (int) (this.getStatsDefault().getFth() * (((double) stageFth / 10) / ((stageFth < 0) ? 2 : 1)));
    }

    public int getAmrWithStage() {
        return this.getStatsCur().getAmr() + (int) (this.getStatsDefault().getAmr() * (((double) stageDef / 10) / ((stageDef < 0) ? 2 : 1)));
    }

    public int getResWithStage() {
        return this.getStatsCur().getRes() + (int) (this.getStatsDefault().getRes() * (((double) stageDef / 10) / ((stageDef < 0) ? 2 : 1)));
    }

    public int getAgiWithStage() {
        return this.getStatsCur().getAgi() + (int) (this.getStatsDefault().getAgi() * (((double) stageAgi / 10) / ((stageAgi < 0) ? 2 : 1)));
    }

    public int getStatWithStage(Stat stat) {
        return switch (stat) {
            case STR -> this.getStatsCur().getStr() + (int) (this.getStatsDefault().getStr() * (((double) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1)));
            case MAG -> this.getStatsCur().getMag() + (int) (this.getStatsDefault().getMag() * (((double) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1)));
            case FTH -> this.getStatsCur().getFth() + (int) (this.getStatsDefault().getFth() * (((double) stageFth / 10) / ((stageFth < 0) ? 2 : 1)));
            case AMR -> this.getStatsCur().getAmr() + (int) (this.getStatsDefault().getAmr() * (((double) stageDef / 10) / ((stageDef < 0) ? 2 : 1)));
            case RES -> this.getStatsCur().getRes() + (int) (this.getStatsDefault().getRes() * (((double) stageDef / 10) / ((stageDef < 0) ? 2 : 1)));
            case AGI -> this.getStatsCur().getAgi() + (int) (this.getStatsDefault().getAgi() * (((double) stageAgi / 10) / ((stageAgi < 0) ? 2 : 1)));
        };
    }

    public void setAffIgnis(int affIgnis) {
        this.affIgnis = affIgnis;
    }

    public int getAffIgnis() {
        return affIgnis;
    }

    public void setAffGlacies(int affGlacies) {
        this.affGlacies = affGlacies;
    }

    public int getAffGlacies() {
        return affGlacies;
    }

    public void setAffFulgur(int affFulgur) {
        this.affFulgur = affFulgur;
    }

    public int getAffFulgur() {
        return affFulgur;
    }

    public void setAffVentus(int affVentus) {
        this.affVentus = affVentus;
    }

    public int getAffVentus() {
        return affVentus;
    }

    public void setAffTerra(int affTerra) {
        this.affTerra = affTerra;
    }

    public int getAffTerra() {
        return affTerra;
    }

    public void setAffLux(int affLux) {
        this.affLux = affLux;
    }

    public int getAffLux() {
        return affLux;
    }

    public void setAffMalum(int affMalum) {
        this.affMalum = affMalum;
    }

    public int getAffMalum() {
        return affMalum;
    }

    public int[] getAffs() {
        return new int[]{affIgnis, affGlacies, affFulgur, affVentus, affTerra, affLux, affMalum};
    }

    public void setAff(Element element, int aff) {
        switch (element) {
            case IGNIS -> affIgnis = aff;
            case GLACIES -> affGlacies = aff;
            case FULGUR -> affFulgur = aff;
            case VENTUS -> affVentus = aff;
            case TERRA -> affTerra = aff;
            case LUX -> affLux = aff;
            case MALUM -> affMalum = aff;
        }
    }

    public int getAff(Element element) {
        return switch (element) {
            case VIS -> 0;
            case IGNIS -> affIgnis;
            case GLACIES -> affGlacies;
            case FULGUR -> affFulgur;
            case VENTUS -> affVentus;
            case TERRA -> affTerra;
            case LUX -> affLux;
            case MALUM -> affMalum;
            //case FULGUR_MALUM -> affFulgur + affMalum;
        };
    }

    public void setMultDmgDealt(int multDmgDealt) {
        this.multDmgDealt = multDmgDealt;
    }

    public int getMultDmgDealt() {
        return multDmgDealt;
    }

    public void setMultDmgTaken(int multDmgTaken) {
        this.multDmgTaken = multDmgTaken;
    }

    public int getMultDmgTaken() {
        return multDmgTaken;
    }

    public void setMultIgnisDmgDealt(int multIgnisDmgDealt) {
        this.multIgnisDmgDealt = multIgnisDmgDealt;
    }

    public int getMultIgnisDmgDealt() {
        return multIgnisDmgDealt;
    }

    public void setMultIgnisDmgTaken(int multIgnisDmgTaken) {
        this.multIgnisDmgTaken = multIgnisDmgTaken;
    }

    public int getMultIgnisDmgTaken() {
        return multIgnisDmgTaken;
    }

    public void setMultGlaciesDmgDealt(int multGlaciesDmgDealt) {
        this.multGlaciesDmgDealt = multGlaciesDmgDealt;
    }

    public int getMultGlaciesDmgDealt() {
        return multGlaciesDmgDealt;
    }

    public void setMultGlaciesDmgTaken(int multGlaciesDmgTaken) {
        this.multGlaciesDmgTaken = multGlaciesDmgTaken;
    }

    public int getMultGlaciesDmgTaken() {
        return multGlaciesDmgTaken;
    }

    public void setMultFulgurDmgDealt(int multFulgurDmgDealt) {
        this.multFulgurDmgDealt = multFulgurDmgDealt;
    }

    public int getMultFulgurDmgDealt() {
        return multFulgurDmgDealt;
    }

    public void setMultFulgurDmgTaken(int multFulgurDmgTaken) {
        this.multFulgurDmgTaken = multFulgurDmgTaken;
    }

    public int getMultFulgurDmgTaken() {
        return multFulgurDmgTaken;
    }

    public void setMultVentusDmgDealt(int multVentusDmgDealt) {
        this.multVentusDmgDealt = multVentusDmgDealt;
    }

    public int getMultVentusDmgDealt() {
        return multVentusDmgDealt;
    }

    public void setMultVentusDmgTaken(int multVentusDmgTaken) {
        this.multVentusDmgTaken = multVentusDmgTaken;
    }

    public int getMultVentusDmgTaken() {
        return multVentusDmgTaken;
    }

    public void setMultTerraDmgDealt(int multTerraDmgDealt) {
        this.multTerraDmgDealt = multTerraDmgDealt;
    }

    public int getMultTerraDmgDealt() {
        return multTerraDmgDealt;
    }

    public void setMultTerraDmgTaken(int multTerraDmgTaken) {
        this.multTerraDmgTaken = multTerraDmgTaken;
    }

    public int getMultTerraDmgTaken() {
        return multTerraDmgTaken;
    }

    public void setMultLuxDmgDealt(int multLuxDmgDealt) {
        this.multLuxDmgDealt = multLuxDmgDealt;
    }

    public int getMultLuxDmgDealt() {
        return multLuxDmgDealt;
    }

    public void setMultLuxDmgTaken(int multLuxDmgTaken) {
        this.multLuxDmgTaken = multLuxDmgTaken;
    }

    public int getMultLuxDmgTaken() {
        return multLuxDmgTaken;
    }

    public void setMultMalumDmgDealt(int multMalumDmgDealt) {
        this.multMalumDmgDealt = multMalumDmgDealt;
    }

    public int getMultMalumDmgDealt() {
        return multMalumDmgDealt;
    }

    public void setMultMalumDmgTaken(int multMalumDmgTaken) {
        this.multMalumDmgTaken = multMalumDmgTaken;
    }

    public int getMultMalumDmgTaken() {
        return multMalumDmgTaken;
    }

    public void setMultWeakDmgDealt(int multWeakDmgDealt) {
        this.multWeakDmgDealt = multWeakDmgDealt;
    }

    public int getMultWeakDmgDealt() {
        return multWeakDmgDealt;
    }

    public void setMultWeakDmgTaken(int multWeakDmgTaken) {
        this.multWeakDmgTaken = multWeakDmgTaken;
    }

    public int getMultWeakDmgTaken() {
        return multWeakDmgTaken;
    }

    public void setMultFollowUpDmgDealt(int multFollowUpDmgDealt) {
        this.multFollowUpDmgDealt = multFollowUpDmgDealt;
    }

    public int getMultFollowUpDmgDealt() {
        return multFollowUpDmgDealt;
    }

    public void setMultFollowUpDmgTaken(int multFollowUpDmgTaken) {
        this.multFollowUpDmgTaken = multFollowUpDmgTaken;
    }

    public int getMultFollowUpDmgTaken() {
        return multFollowUpDmgTaken;
    }

    public void setMultDoTDmgTaken(int multDoTDmgTaken) {
        this.multDoTDmgTaken = multDoTDmgTaken;
    }

    public int getMultDoTDmgTaken() {
        return multDoTDmgTaken;
    }

    public void setMultHealingDealt(int multHealingDealt) {
        this.multHealingDealt = multHealingDealt;
    }

    public int getMultHealingDealt() {
        return multHealingDealt;
    }

    public void setMultHealingTaken(int multHealingTaken) {
        this.multHealingTaken = multHealingTaken;
    }

    public int getMultHealingTaken() {
        return multHealingTaken;
    }

    public void setMultSpGain(int multSpGain) {
        this.multSpGain = multSpGain;
    }

    public int getMultSpGain() {
        return multSpGain;
    }

    public void setMultSpUse(int multSpUse) {
        this.multSpUse = multSpUse;
    }

    public int getMultSpUse() {
        return multSpUse;
    }

    public void setMult(Mult mult, int set) {
        switch (mult) {
            case DMG_DEALT -> multDmgDealt = set;
            case DMG_TAKEN -> multDmgTaken = set;
            case IGNIS_DMG_DEALT -> multIgnisDmgDealt = set;
            case IGNIS_DMG_TAKEN -> multIgnisDmgTaken = set;
            case GLACIES_DMG_DEALT -> multGlaciesDmgDealt = set;
            case GLACIES_DMG_TAKEN -> multGlaciesDmgTaken = set;
            case FULGUR_DMG_DEALT -> multFulgurDmgDealt = set;
            case FULGUR_DMG_TAKEN -> multFulgurDmgTaken = set;
            case VENTUS_DMG_DEALT -> multVentusDmgDealt = set;
            case VENTUS_DMG_TAKEN -> multVentusDmgTaken = set;
            case TERRA_DMG_DEALT -> multTerraDmgDealt = set;
            case TERRA_DMG_TAKEN -> multTerraDmgTaken = set;
            case LUX_DMG_DEALT -> multLuxDmgDealt = set;
            case LUX_DMG_TAKEN -> multLuxDmgTaken = set;
            case MALUM_DMG_DEALT -> multMalumDmgDealt = set;
            case MALUM_DMG_TAKEN -> multMalumDmgTaken = set;
            case WEAK_DMG_DEALT -> multWeakDmgDealt = set;
            case WEAK_DMG_TAKEN -> multWeakDmgTaken = set;
            case FOLLOW_UP_DMG_DEALT -> multFollowUpDmgDealt = set;
            case FOLLOW_UP_DMG_TAKEN -> multFollowUpDmgTaken = set;
            case DOT_DMG_TAKEN -> multDoTDmgTaken = set;
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
            case IGNIS_DMG_DEALT -> multIgnisDmgDealt;
            case IGNIS_DMG_TAKEN -> multIgnisDmgTaken;
            case GLACIES_DMG_DEALT -> multGlaciesDmgDealt;
            case GLACIES_DMG_TAKEN -> multGlaciesDmgTaken;
            case FULGUR_DMG_DEALT -> multFulgurDmgDealt;
            case FULGUR_DMG_TAKEN -> multFulgurDmgTaken;
            case VENTUS_DMG_DEALT -> multVentusDmgDealt;
            case VENTUS_DMG_TAKEN -> multVentusDmgTaken;
            case TERRA_DMG_DEALT -> multTerraDmgDealt;
            case TERRA_DMG_TAKEN -> multTerraDmgTaken;
            case LUX_DMG_DEALT -> multLuxDmgDealt;
            case LUX_DMG_TAKEN -> multLuxDmgTaken;
            case MALUM_DMG_DEALT -> multMalumDmgDealt;
            case MALUM_DMG_TAKEN -> multMalumDmgTaken;
            case WEAK_DMG_DEALT -> multWeakDmgDealt;
            case WEAK_DMG_TAKEN -> multWeakDmgTaken;
            case FOLLOW_UP_DMG_DEALT -> multFollowUpDmgDealt;
            case FOLLOW_UP_DMG_TAKEN -> multFollowUpDmgTaken;
            case DOT_DMG_TAKEN -> multDoTDmgTaken;
            case HEALING_DEALT -> multHealingDealt;
            case HEALING_TAKEN -> multHealingTaken;
            case SP_GAIN -> multSpGain;
            case SP_USE -> multSpUse;
        };
    }

    public int getMultElementDmgDealt(Element element) {
        return switch (element) {
            case VIS -> 1;
            case IGNIS -> multIgnisDmgDealt;
            case GLACIES -> multGlaciesDmgDealt;
            case FULGUR -> multFulgurDmgDealt;
            case VENTUS -> multVentusDmgDealt;
            case TERRA -> multTerraDmgDealt;
            case LUX -> multLuxDmgDealt;
            case MALUM -> multMalumDmgDealt;
            //case FULGUR_MALUM -> 100 + ((multFulgurDmgDealt - 100) + (multMalumDmgDealt - 100));
        };
    }

    public int getMultElementDmgTaken(Element element) {
        return switch (element) {
            case VIS -> 1;
            case IGNIS -> multIgnisDmgTaken;
            case GLACIES -> multGlaciesDmgTaken;
            case FULGUR -> multFulgurDmgTaken;
            case VENTUS -> multVentusDmgTaken;
            case TERRA -> multTerraDmgTaken;
            case LUX -> multLuxDmgTaken;
            case MALUM -> multMalumDmgTaken;
            //case FULGUR_MALUM -> 100 + ((multFulgurDmgTaken - 100) + (multMalumDmgTaken - 100));
        };
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public int getShield() {
        return shield;
    }

    public void setShieldTurns(int shieldTurns) {
        this.shieldTurns = shieldTurns;
    }

    public int getShieldTurns() {
        return shieldTurns;
    }

    public void setDefend(int defend) {
        this.defend = defend;
    }

    public int getDefend() {
        return defend;
    }

    public void setExtraActions(int extraActions) {
        this.extraActions = extraActions;
    }

    public int getExtraActions() {
        return extraActions;
    }

    public void setEffectBlock(int effectBlock) {
        this.effectBlock = effectBlock;
    }

    public int getEffectBlock() {
        return effectBlock;
    }

    public void setDurationModBuffDealt(int durationModBuffDealt) {
        this.durationModBuffDealt = durationModBuffDealt;
    }

    public int getDurationModBuffDealt() {
        return durationModBuffDealt;
    }

    public void setDurationModBuffTaken(int durationModBuffTaken) {
        this.durationModBuffTaken = durationModBuffTaken;
    }

    public int getDurationModBuffTaken() {
        return durationModBuffTaken;
    }

    public void setDurationModDebuffDealt(int durationModDebuffDealt) {
        this.durationModDebuffDealt = durationModDebuffDealt;
    }

    public int getDurationModDebuffDealt() {
        return durationModDebuffDealt;
    }

    public void setDurationModDebuffTaken(int durationModDebuffTaken) {
        this.durationModDebuffTaken = durationModDebuffTaken;
    }

    public int getDurationModDebuffTaken() {
        return durationModDebuffTaken;
    }

    public void setDurationModBuffTypeDealt(BuffType buffType, int durationModBuffTypeDealt) {
        if(buffType == BuffType.BUFF) durationModBuffDealt = durationModBuffTypeDealt;
        else durationModDebuffDealt = durationModBuffTypeDealt;
    }

    public int getDurationModBuffTypeDealt(BuffType buffType) {
        return (buffType == BuffType.BUFF) ? durationModBuffDealt : durationModDebuffDealt;
    }

    public void setDurationModBuffTypeTaken(BuffType buffType, int durationModBuffTypeTaken) {
        if(buffType == BuffType.BUFF) durationModBuffTaken = durationModBuffTypeTaken;
        else durationModDebuffTaken = durationModBuffTypeTaken;
    }

    public int getDurationModBuffTypeTaken(BuffType buffType) {
        return (buffType == BuffType.BUFF) ? durationModBuffTaken : durationModDebuffTaken;
    }

    public void setBuffInstances(List<BuffInstance> buffInstances) {
        this.buffInstances = buffInstances;
    }

    public List<BuffInstance> getBuffInstances() {
        return buffInstances;
    }

    public void addBuffInstance(BuffInstance buffInstance) {
        buffInstances.add(buffInstance);
    }

    public void addBuffInstances(BuffInstance... buffInstances) {
        this.buffInstances.addAll(List.of(buffInstances));
    }

    public boolean isProtected() {
        return multDmgTaken <= -50000;
    }

    public boolean isWeakTo(Element element) {
        return this.getAff(element) < 0;
    }

    public boolean isResists(Element element) {
        return this.getAff(element) > 0;
    }

    public boolean isImmuneTo(Element element) {
        return this.getAff(element) >= 5;
    }

    public boolean isNeutralTo(Element element) {
        return this.getAff(element) == 0;
    }

    // Returns if the requested Passive is present
    public boolean findPassive(Passive passiveTarget) {
        for(Passive passive : passives) {
            if(passive == passiveTarget) return true;
        }
        return false;
    }

    // Returns the requested BuffInstance if present
    public BuffInstance findBuff(Buff buff) {
        for(BuffInstance buffInstance : buffInstances) {
            if(buffInstance.getBuff() == buff) return buffInstance;
        }
        return null;
    }

    public List<String> decrementTurns() {
        List<String> msg = new ArrayList<>();

        // Stages
        if (stageAtk != 0 && --stageAtkTurns <= 0) {
            msg.add(unitType.getName() + " " + lang.get("log.loses") + " " + stageAtk + lang.format("stage_s", stageAtk) + " " + StageType.ATK.getName());
            stageAtk = 0; // Remove stages
        }
        if (stageDef != 0 && --stageDefTurns <= 0) {
            msg.add(unitType.getName() + " " + lang.get("log.loses") + " " + stageDef + lang.format("stage_s", stageDef) + " " + StageType.DEF.getName());
            stageDef = 0;
        }
        if (stageFth != 0 && --stageFthTurns <= 0) {
            msg.add(unitType.getName() + " " + lang.get("log.loses") + " " + stageFth + lang.format("stage_s", stageFth) + " " + StageType.FTH.getName());
            stageFth = 0;
        }
        if (stageAgi != 0 && --stageAgiTurns <= 0) {
            msg.add(unitType.getName() + " " + lang.get("log.loses") + " " + stageAgi + lang.format("stage_s", stageAgi) + " " + StageType.AGI.getName());
            stageAgi = 0;
        }

        // Shield
        if (shield != 0 && --shieldTurns <= 0) {
            if(defend == 0) msg.add(unitType.getName() + " " + lang.get("log.loses") + " " + String.format("%,d", shield) + " " + lang.get("shield"));
            else msg.add(formatPossessive(unitType.getName()) + " " + lang.get("shield") + " " + String.format("%,d", (shield + defend)) + " -> " + String.format("%,d", defend));
            shield = 0;
        }

        // Buffs
        for(int i = buffInstances.size() - 1; i >= 0; i--) {
            BuffInstance buffInstance = buffInstances.get(i);
            int turns = buffInstance.getTurns();
            if (turns >= 2 && turns < 1000) { // 1000+ turns = infinite
                buffInstance.setTurns(turns - 1);
            } else {
                StringBuilder str = new StringBuilder();
                int maxStacks = buffInstance.getBuff().getMaxStacks();
                str.append(unitType.getName()).append(" ").append(lang.get("log.loses")).append(" ");
                if(maxStacks > 1) str.append(buffInstance.getStacks()).append(" ");
                str.append(buffInstance.getBuff().getName());
                if(maxStacks > 1) str.append(" ").append(lang.get("stacks"));
                msg.add(str.toString());

                // Remove effects
                for(BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                    String[] effectMsgs = buffEffect.onRemove(this, buffInstance.getStacks());
                    for(String effectMsg : effectMsgs) if(!effectMsg.isEmpty()) msg.add(effectMsg);
                }
                buffInstances.remove(buffInstance);
            }
        }

        return msg;
    }

    // Damage Unit, taking into account Defend and Shield. Returns false if HP was lowered
    public Result damage(int dmg, boolean pierce, boolean useName) {
        int dmgFull = dmg;
        int defendOld = defend;

        List<String> msg = new ArrayList<>();

        String name = (useName) ? unitType.getName() + " " : "";
        String name_s = (useName) ? formatPossessive(unitType.getName()) + " " : "";

        if (!pierce) { // Pierce skips Defend and Shield
            if (defend > 0 && dmg > 0) { // There's Defend and dmg
                if (defend > dmg) { // Only hit Defend
                    defend -= dmg;
                    return new Result(ResultType.HIT_SHIELD, name_s + lang.get("shield") + " " + String.format("%,d", (defendOld + shield)) + " -> "
                        + String.format("%,d", (defend + shield)) + "/" + String.format("%,d", this.getStatsDefault().getHp()) + " (-" + String.format("%,d", dmgFull) + ")");
                } else { // Destroy Defend and proceed to Shield
                    dmg -= defend;
                    defend = 0;
                    if(shield == 0 && effectBlock <= 0) msg.add(name + lang.get("log.is_no_longer") + " " + lang.get("log.effect_block"));
                }
            }

            if (shield > 0 && dmg > 0) { // There's Shield and dmg
                if (shield > dmg) { // Only hit Shield
                    int shieldOld = shield;
                    shield -= dmg;
                    return new Result(ResultType.HIT_SHIELD, name_s + lang.get("shield") + " " + String.format("%,d", (defendOld + shieldOld)) + " -> "
                        + String.format("%,d", shield) + "/" + String.format("%,d", this.getStatsDefault().getHp()) + " (-" + String.format("%,d", dmgFull) + ")");
                } else { // Destroy Shield and proceed to HP
                    msg.add(name_s + lang.get("shield") + " " + String.format("%,d", (defendOld + shield)) + " -> " + 0 + "/" + this.getStatsDefault().getHp() +
                        " (-" + String.format("%,d", (defendOld + shield)) + ")");
                    dmg -= shield;
                    shield = 0;
                    shieldTurns = 0;
                    if(effectBlock <= 0) msg.add(name+ lang.get("log.is_no_longer") + " " + lang.get("log.effect_block"));
                }
            }
        }

        // Lower HP
        int hpOld = this.getStatsCur().getHp();
        int hpNew = Math.clamp(hpOld - dmg, 0, this.getStatsDefault().getHp());
        msg.add(name_s + lang.get("hp") + " " + String.format("%,d", hpOld) + " -> " + String.format("%,d", hpNew) + " (-" + String.format("%,d", dmg) + ")");
        this.getStatsCur().setHp(hpNew);

        if (effectBlock > 0) { // todo should this be a separate result from hitting shield
            return new Result(ResultType.HIT_SHIELD, msg);
        } else if (dmg > 0) { // Did damage
            return new Result(ResultType.SUCCESS, msg);
        } else return new Result(ResultType.FAIL, lang.get("log.no_effect") + " " + lang.get("log.on") + " " + unitType.getName()); // Did no damage
    }

    public Result damage(int dmg, boolean pierce) {
        return this.damage(dmg, pierce, true);
    }
    public Result damage(int dmg) {
        return this.damage(dmg, false, true);
    }
}
