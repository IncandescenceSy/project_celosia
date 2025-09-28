package io.github.celosia.sys.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.battle.BattleLib.STAT_MULT_HIDDEN;
import static io.github.celosia.sys.menu.TextLib.C_BUFF;
import static io.github.celosia.sys.menu.TextLib.C_HP;
import static io.github.celosia.sys.menu.TextLib.C_NEG;
import static io.github.celosia.sys.menu.TextLib.C_NUM;
import static io.github.celosia.sys.menu.TextLib.C_SHIELD;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.formatNum;
import static io.github.celosia.sys.menu.TextLib.getColor;
import static io.github.celosia.sys.menu.TextLib.getStageStatString;
import static io.github.celosia.sys.settings.Lang.lang;

// Species and current stats
public class Unit {
	private final UnitType unitType;
	private final long lvl; // Level

	// Stats
	// statsMult is treated as multipliers applied to statsDefault, in 10ths of a %
	// (1000 = 100%). Is never treated as less than 10%
	// HP, Str, Mag, Amr, Res, and Agi
	private final Stats statsDefault;
	private final Stats statsMult;

	// Current HP needs to be separate
	private long hp;

	private final SkillInstance[] skillInstances;
	private List<Passive> passives;

	// Skill Points
	private int sp;

	// Position on the battlefield
	// 0 4
	// 1 5
	// 2 6
	// 3 7
	private int pos;

	// Stat stages
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

	// Current and default affinities
	// Multiplies the damage dealt, damage taken, and SP cost for their
	// corresponding element
	private final Affinities affsDefault;
	private final Affinities affsCur;

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

	private int multIgnisDmgDealt;
	private int expIgnisDmgDealt;
	private int multIgnisDmgTaken;
	private int expIgnisDmgTaken;
	private int multGlaciesDmgDealt;
	private int expGlaciesDmgDealt;
	private int multGlaciesDmgTaken;
	private int expGlaciesDmgTaken;
	private int multFulgurDmgDealt;
	private int expFulgurDmgDealt;
	private int multFulgurDmgTaken;
	private int expFulgurDmgTaken;
	private int multVentusDmgDealt;
	private int expVentusDmgDealt;
	private int multVentusDmgTaken;
	private int expVentusDmgTaken;
	private int multTerraDmgDealt;
	private int expTerraDmgDealt;
	private int multTerraDmgTaken;
	private int expTerraDmgTaken;
	private int multLuxDmgDealt;
	private int expLuxDmgDealt;
	private int multLuxDmgTaken;
	private int expLuxDmgTaken;
	private int multMalumDmgDealt;
	private int expMalumDmgDealt;
	private int multMalumDmgTaken;
	private int expMalumDmgTaken;

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
	// Unlike other mults, the minimum is 0.1%
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
	private int shieldTurns;

	// Defend (essentially a 2nd Shield with higher priority)
	private long defend;

	private int extraActions;

	// Secondary effect block; >= 1 blocks secondary effects the same as Shield
	private int effectBlock;

	// >= 1 means SP is infinite
	private int infiniteSp;

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

	public Unit(UnitType unitType, long lvl, Skill[] skills, int pos) {
		this.unitType = unitType;
		this.lvl = lvl;
		statsDefault = unitType.statsBase().getRealStats(lvl);
		statsMult = new Stats(1000);
		hp = statsDefault.getHp();
		skillInstances = Arrays.stream(skills).map(Skill::toSkillInstance).toArray(SkillInstance[]::new);
		passives = List.of(unitType.passives());
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
		affsDefault = unitType.affsBase();
		affsCur = new Affinities(affsDefault);
		multDmgDealt = 1000;
		multDmgTaken = 1000;
		multIgnisDmgDealt = 1000;
		multIgnisDmgTaken = 1000;
		multGlaciesDmgDealt = 1000;
		multGlaciesDmgTaken = 1000;
		multFulgurDmgDealt = 1000;
		multFulgurDmgTaken = 1000;
		multVentusDmgDealt = 1000;
		multVentusDmgTaken = 1000;
		multTerraDmgDealt = 1000;
		multTerraDmgTaken = 1000;
		multLuxDmgDealt = 1000;
		multLuxDmgTaken = 1000;
		multMalumDmgDealt = 1000;
		multMalumDmgTaken = 1000;
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
		expIgnisDmgDealt = 100;
		expIgnisDmgTaken = 100;
		expGlaciesDmgDealt = 100;
		expGlaciesDmgTaken = 100;
		expFulgurDmgDealt = 100;
		expFulgurDmgTaken = 100;
		expVentusDmgDealt = 100;
		expVentusDmgTaken = 100;
		expTerraDmgDealt = 100;
		expTerraDmgTaken = 100;
		expLuxDmgDealt = 100;
		expLuxDmgTaken = 100;
		expMalumDmgDealt = 100;
		expMalumDmgTaken = 100;
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
		shieldTurns = 0;
		defend = 0;
		extraActions = 0;
		effectBlock = 0;
		infiniteSp = 0;
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

		// Only way for val to be negative is if an overflow happened, so set it to max
		// long just to be safe
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

	public SkillInstance[] getSkillInstances() {
		return skillInstances;
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
					this.getStr()
							+ (long) (statsDefault.getStr() * (((double) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1))),
					1);
			case MAG -> Math.max(
					this.getMag()
							+ (long) (statsDefault.getMag() * (((double) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1))),
					1);
			case FTH -> Math.max(
					this.getFth()
							+ (long) (statsDefault.getFth() * (((double) stageFth / 10) / ((stageFth < 0) ? 2 : 1))),
					1);
			case AMR -> Math.max(
					this.getAmr()
							+ (long) (statsDefault.getAmr() * (((double) stageDef / 10) / ((stageDef < 0) ? 2 : 1))),
					1);
			case RES -> Math.max(
					this.getRes()
							+ (long) (statsDefault.getRes() * (((double) stageDef / 10) / ((stageDef < 0) ? 2 : 1))),
					1);
			case AGI -> Math.max(
					this.getAgi()
							+ (long) (statsDefault.getAgi() * (((double) stageAgi / 10) / ((stageAgi < 0) ? 2 : 1))),
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
			case STR -> Math.max(this.getDisplayStr()
					+ (long) (statsDefault.getDisplayStr() * (((double) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1))), 1);
			case MAG -> Math.max(this.getDisplayMag()
					+ (long) (statsDefault.getDisplayMag() * (((double) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1))), 1);
			case FTH -> Math.max(this.getDisplayFth()
					+ (long) (statsDefault.getDisplayFth() * (((double) stageFth / 10) / ((stageFth < 0) ? 2 : 1))), 1);
			case AMR -> Math.max(this.getDisplayAmr()
					+ (long) (statsDefault.getDisplayAmr() * (((double) stageDef / 10) / ((stageDef < 0) ? 2 : 1))), 1);
			case RES -> Math.max(this.getDisplayRes()
					+ (long) (statsDefault.getDisplayRes() * (((double) stageDef / 10) / ((stageDef < 0) ? 2 : 1))), 1);
			case AGI -> Math.max(this.getDisplayAgi()
					+ (long) (statsDefault.getDisplayAgi() * (((double) stageAgi / 10) / ((stageAgi < 0) ? 2 : 1))), 1);
		};
	}

	public long getDisplayStatWithStage(Stat stat, int stage) {
		return switch (stat) {
			case STR -> Math.max(
					this.getDisplayStr()
							+ (long) (statsDefault.getDisplayStr() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
					1);
			case MAG -> Math.max(
					this.getDisplayMag()
							+ (long) (statsDefault.getDisplayMag() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
					1);
			case FTH -> Math.max(
					this.getDisplayFth()
							+ (long) (statsDefault.getDisplayFth() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
					1);
			case AMR -> Math.max(
					this.getDisplayAmr()
							+ (long) (statsDefault.getDisplayAmr() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
					1);
			case RES -> Math.max(
					this.getDisplayRes()
							+ (long) (statsDefault.getDisplayRes() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
					1);
			case AGI -> Math.max(
					this.getDisplayAgi()
							+ (long) (statsDefault.getDisplayAgi() * (((double) stage / 10) / ((stage < 0) ? 2 : 1))),
					1);
		};
	}

	public Affinities getAffsDefault() {
		return affsCur;
	}

	public Affinities getAffsCur() {
		return affsCur;
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

	public void setMultPercentageDmgTaken(int multPercentageDmgTaken) {
		this.multPercentageDmgTaken = multPercentageDmgTaken;
	}

	public int getMultPercentageDmgTaken() {
		return multPercentageDmgTaken;
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
			case PERCENTAGE_DMG_TAKEN -> multPercentageDmgTaken;
			case HEALING_DEALT -> multHealingDealt;
			case HEALING_TAKEN -> multHealingTaken;
			case SP_GAIN -> multSpGain;
			case SP_USE -> multSpUse;
		};
	}

	public int getMultElementDmgDealt(Element element) {
		return switch (element) {
			case VIS -> 1;
			case IGNIS -> (int) Math.pow(multIgnisDmgDealt, expIgnisDmgDealt);
			case GLACIES -> multGlaciesDmgDealt;
			case FULGUR -> multFulgurDmgDealt;
			case VENTUS -> multVentusDmgDealt;
			case TERRA -> multTerraDmgDealt;
			case LUX -> multLuxDmgDealt;
			case MALUM -> multMalumDmgDealt;
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
		};
	}

	public void setExpDmgDealt(int expDmgDealt) {
		this.expDmgDealt = expDmgDealt;
	}

	public int getExpDmgDealt() {
		return expDmgDealt;
	}

	public void setExpDmgTaken(int expDmgTaken) {
		this.expDmgTaken = expDmgTaken;
	}

	public int getExpDmgTaken() {
		return expDmgTaken;
	}

	public void setExpIgnisDmgDealt(int expIgnisDmgDealt) {
		this.expIgnisDmgDealt = expIgnisDmgDealt;
	}

	public int getExpIgnisDmgDealt() {
		return expIgnisDmgDealt;
	}

	public void setExpIgnisDmgTaken(int expIgnisDmgTaken) {
		this.expIgnisDmgTaken = expIgnisDmgTaken;
	}

	public int getExpIgnisDmgTaken() {
		return expIgnisDmgTaken;
	}

	public void setExpGlaciesDmgDealt(int expGlaciesDmgDealt) {
		this.expGlaciesDmgDealt = expGlaciesDmgDealt;
	}

	public int getExpGlaciesDmgDealt() {
		return expGlaciesDmgDealt;
	}

	public void setExpGlaciesDmgTaken(int expGlaciesDmgTaken) {
		this.expGlaciesDmgTaken = expGlaciesDmgTaken;
	}

	public int getExpGlaciesDmgTaken() {
		return expGlaciesDmgTaken;
	}

	public void setExpFulgurDmgDealt(int expFulgurDmgDealt) {
		this.expFulgurDmgDealt = expFulgurDmgDealt;
	}

	public int getExpFulgurDmgDealt() {
		return expFulgurDmgDealt;
	}

	public void setExpFulgurDmgTaken(int expFulgurDmgTaken) {
		this.expFulgurDmgTaken = expFulgurDmgTaken;
	}

	public int getExpFulgurDmgTaken() {
		return expFulgurDmgTaken;
	}

	public void setExpVentusDmgDealt(int expVentusDmgDealt) {
		this.expVentusDmgDealt = expVentusDmgDealt;
	}

	public int getExpVentusDmgDealt() {
		return expVentusDmgDealt;
	}

	public void setExpVentusDmgTaken(int expVentusDmgTaken) {
		this.expVentusDmgTaken = expVentusDmgTaken;
	}

	public int getExpVentusDmgTaken() {
		return expVentusDmgTaken;
	}

	public void setExpTerraDmgDealt(int expTerraDmgDealt) {
		this.expTerraDmgDealt = expTerraDmgDealt;
	}

	public int getExpTerraDmgDealt() {
		return expTerraDmgDealt;
	}

	public void setExpTerraDmgTaken(int expTerraDmgTaken) {
		this.expTerraDmgTaken = expTerraDmgTaken;
	}

	public int getExpTerraDmgTaken() {
		return expTerraDmgTaken;
	}

	public void setExpLuxDmgDealt(int expLuxDmgDealt) {
		this.expLuxDmgDealt = expLuxDmgDealt;
	}

	public int getExpLuxDmgDealt() {
		return expLuxDmgDealt;
	}

	public void setExpLuxDmgTaken(int expLuxDmgTaken) {
		this.expLuxDmgTaken = expLuxDmgTaken;
	}

	public int getExpLuxDmgTaken() {
		return expLuxDmgTaken;
	}

	public void setExpMalumDmgDealt(int expMalumDmgDealt) {
		this.expMalumDmgDealt = expMalumDmgDealt;
	}

	public int getExpMalumDmgDealt() {
		return expMalumDmgDealt;
	}

	public void setExpMalumDmgTaken(int expMalumDmgTaken) {
		this.expMalumDmgTaken = expMalumDmgTaken;
	}

	public int getExpMalumDmgTaken() {
		return expMalumDmgTaken;
	}

	public void setExpWeakDmgDealt(int expWeakDmgDealt) {
		this.expWeakDmgDealt = expWeakDmgDealt;
	}

	public int getExpWeakDmgDealt() {
		return expWeakDmgDealt;
	}

	public void setExpWeakDmgTaken(int expWeakDmgTaken) {
		this.expWeakDmgTaken = expWeakDmgTaken;
	}

	public int getExpWeakDmgTaken() {
		return expWeakDmgTaken;
	}

	public void setExpFollowUpDmgDealt(int expFollowUpDmgDealt) {
		this.expFollowUpDmgDealt = expFollowUpDmgDealt;
	}

	public int getExpFollowUpDmgDealt() {
		return expFollowUpDmgDealt;
	}

	public void setExpFollowUpDmgTaken(int expFollowUpDmgTaken) {
		this.expFollowUpDmgTaken = expFollowUpDmgTaken;
	}

	public int getExpFollowUpDmgTaken() {
		return expFollowUpDmgTaken;
	}

	public void setExpDoTDmgTaken(int expDoTDmgTaken) {
		this.expDoTDmgTaken = expDoTDmgTaken;
	}

	public int getExpDoTDmgTaken() {
		return expDoTDmgTaken;
	}

	public void setExpPercentageDmgTaken(int expPercentageDmgTaken) {
		this.expPercentageDmgTaken = expPercentageDmgTaken;
	}

	public int getExpPercentageDmgTaken() {
		return expPercentageDmgTaken;
	}

	public void setExpHealingDealt(int expHealingDealt) {
		this.expHealingDealt = expHealingDealt;
	}

	public int getExpHealingDealt() {
		return expHealingDealt;
	}

	public void setExpHealingTaken(int expHealingTaken) {
		this.expHealingTaken = expHealingTaken;
	}

	public int getExpHealingTaken() {
		return expHealingTaken;
	}

	public void setExpSpGain(int expSpGain) {
		this.expSpGain = expSpGain;
	}

	public int getExpSpGain() {
		return expSpGain;
	}

	public void setExpSpUse(int expSpUse) {
		this.expSpUse = expSpUse;
	}

	public int getExpSpUse() {
		return expSpUse;
	}

	public void setExp(Mult mult, int set) {
		switch (mult) {
			case DMG_DEALT -> expDmgDealt = set;
			case DMG_TAKEN -> expDmgTaken = set;
			case IGNIS_DMG_DEALT -> expIgnisDmgDealt = set;
			case IGNIS_DMG_TAKEN -> expIgnisDmgTaken = set;
			case GLACIES_DMG_DEALT -> expGlaciesDmgDealt = set;
			case GLACIES_DMG_TAKEN -> expGlaciesDmgTaken = set;
			case FULGUR_DMG_DEALT -> expFulgurDmgDealt = set;
			case FULGUR_DMG_TAKEN -> expFulgurDmgTaken = set;
			case VENTUS_DMG_DEALT -> expVentusDmgDealt = set;
			case VENTUS_DMG_TAKEN -> expVentusDmgTaken = set;
			case TERRA_DMG_DEALT -> expTerraDmgDealt = set;
			case TERRA_DMG_TAKEN -> expTerraDmgTaken = set;
			case LUX_DMG_DEALT -> expLuxDmgDealt = set;
			case LUX_DMG_TAKEN -> expLuxDmgTaken = set;
			case MALUM_DMG_DEALT -> expMalumDmgDealt = set;
			case MALUM_DMG_TAKEN -> expMalumDmgTaken = set;
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
			case IGNIS_DMG_DEALT -> expIgnisDmgDealt;
			case IGNIS_DMG_TAKEN -> expIgnisDmgTaken;
			case GLACIES_DMG_DEALT -> expGlaciesDmgDealt;
			case GLACIES_DMG_TAKEN -> expGlaciesDmgTaken;
			case FULGUR_DMG_DEALT -> expFulgurDmgDealt;
			case FULGUR_DMG_TAKEN -> expFulgurDmgTaken;
			case VENTUS_DMG_DEALT -> expVentusDmgDealt;
			case VENTUS_DMG_TAKEN -> expVentusDmgTaken;
			case TERRA_DMG_DEALT -> expTerraDmgDealt;
			case TERRA_DMG_TAKEN -> expTerraDmgTaken;
			case LUX_DMG_DEALT -> expLuxDmgDealt;
			case LUX_DMG_TAKEN -> expLuxDmgTaken;
			case MALUM_DMG_DEALT -> expMalumDmgDealt;
			case MALUM_DMG_TAKEN -> expMalumDmgTaken;
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
		return switch (element) {
			case VIS -> 1;
			case IGNIS -> expIgnisDmgDealt;
			case GLACIES -> expGlaciesDmgDealt;
			case FULGUR -> expFulgurDmgDealt;
			case VENTUS -> expVentusDmgDealt;
			case TERRA -> expTerraDmgDealt;
			case LUX -> expLuxDmgDealt;
			case MALUM -> expMalumDmgDealt;
		};
	}

	public int getExpElementDmgTaken(Element element) {
		return switch (element) {
			case VIS -> 1;
			case IGNIS -> expIgnisDmgTaken;
			case GLACIES -> expGlaciesDmgTaken;
			case FULGUR -> expFulgurDmgTaken;
			case VENTUS -> expVentusDmgTaken;
			case TERRA -> expTerraDmgTaken;
			case LUX -> expLuxDmgTaken;
			case MALUM -> expMalumDmgTaken;
		};
	}

	public double getMultWithExpDmgDealt() {
		return Math.max(Math.pow(multDmgDealt / 1000d, expDmgDealt / 100d), 0.1);
	}

	public double getMultWithExpDmgTaken() {
		return Math.max(Math.pow(multDmgTaken / 1000d, expDmgTaken / 100d), 0.1);
	}

	public double getMultWithExpIgnisDmgDealt() {
		return Math.max(Math.pow(multIgnisDmgDealt / 1000d, expIgnisDmgDealt / 100d), 0.1);
	}

	public double getMultWithExpIgnisDmgTaken() {
		return Math.max(Math.pow(multIgnisDmgTaken / 1000d, expIgnisDmgTaken / 100d), 0.1);
	}

	public double getMultWithExpGlaciesDmgDealt() {
		return Math.max(Math.pow(multGlaciesDmgDealt / 1000d, expGlaciesDmgDealt / 100d), 0.1);
	}

	public double getMultWithExpGlaciesDmgTaken() {
		return Math.max(Math.pow(multGlaciesDmgTaken / 1000d, expGlaciesDmgTaken / 100d), 0.1);
	}

	public double getMultWithExpFulgurDmgDealt() {
		return Math.max(Math.pow(multFulgurDmgDealt / 1000d, expFulgurDmgDealt / 100d), 0.1);
	}

	public double getMultWithExpFulgurDmgTaken() {
		return Math.max(Math.pow(multFulgurDmgTaken / 1000d, expFulgurDmgTaken / 100d), 0.1);
	}

	public double getMultWithExpVentusDmgDealt() {
		return Math.max(Math.pow(multVentusDmgDealt / 1000d, expVentusDmgDealt / 100d), 0.1);
	}

	public double getMultWithExpVentusDmgTaken() {
		return Math.max(Math.pow(multVentusDmgTaken / 1000d, expVentusDmgTaken / 100d), 0.1);
	}

	public double getMultWithExpTerraDmgDealt() {
		return Math.max(Math.pow(multTerraDmgDealt / 1000d, expTerraDmgDealt / 100d), 0.1);
	}

	public double getMultWithExpTerraDmgTaken() {
		return Math.max(Math.pow(multTerraDmgTaken / 1000d, expTerraDmgTaken / 100d), 0.1);
	}

	public double getMultWithExpLuxDmgDealt() {
		return Math.max(Math.pow(multLuxDmgDealt / 1000d, expLuxDmgDealt / 100d), 0.1);
	}

	public double getMultWithExpLuxDmgTaken() {
		return Math.max(Math.pow(multLuxDmgTaken / 1000d, expLuxDmgTaken / 100d), 0.1);
	}

	public double getMultWithExpMalumDmgDealt() {
		return Math.max(Math.pow(multMalumDmgDealt / 1000d, expMalumDmgDealt / 100d), 0.1);
	}

	public double getMultWithExpMalumDmgTaken() {
		return Math.max(Math.pow(multMalumDmgTaken / 1000d, expMalumDmgTaken / 100d), 0.1);
	}

	public double getMultWithExpWeakDmgDealt() {
		return Math.max(Math.pow(multWeakDmgDealt / 1000d, expWeakDmgDealt / 100d), 0.1);
	}

	public double getMultWithExpWeakDmgTaken() {
		return Math.max(Math.pow(multWeakDmgTaken / 1000d, expWeakDmgTaken / 100d), 0.1);
	}

	public double getMultWithExpFollowUpDmgDealt() {
		return Math.max(Math.pow(multFollowUpDmgDealt / 1000d, expFollowUpDmgDealt / 100d), 0.1);
	}

	public double getMultWithExpFollowUpDmgTaken() {
		return Math.max(Math.pow(multFollowUpDmgTaken / 1000d, expFollowUpDmgTaken / 100d), 0.1);
	}

	public double getMultWithExpDoTDmgTaken() {
		return Math.max(Math.pow(multDoTDmgTaken / 1000d, expDoTDmgTaken / 100d), 0.1);
	}

	public double getMultWithExpPercentageDmgTaken() {
		return Math.max(Math.pow(multPercentageDmgTaken / 1000d, expPercentageDmgTaken / 100d), 0.001);
	}

	public double getMultWithExpHealingDealt() {
		return Math.max(Math.pow(multHealingDealt / 1000d, expHealingDealt / 100d), 0.1);
	}

	public double getMultWithExpHealingTaken() {
		return Math.max(Math.pow(multHealingTaken / 1000d, expHealingTaken / 100d), 0.1);
	}

	public double getMultWithExpSpGain() {
		return Math.max(Math.pow(multSpGain / 1000d, expSpGain / 100d), 0.1);
	}

	public double getMultWithExpSpUse() {
		return Math.max(Math.pow(multSpUse / 1000d, expSpUse / 100d), 0.1);
	}

	public double getMultWithExp(Mult mult) {
		return switch (mult) {
			case DMG_DEALT -> Math.max(Math.pow(multDmgDealt / 1000d, expDmgDealt / 100d), 0.1);
			case DMG_TAKEN -> Math.max(Math.pow(multDmgTaken / 1000d, expDmgTaken / 100d), 0.1);
			case IGNIS_DMG_DEALT -> Math.max(Math.pow(multIgnisDmgDealt / 1000d, expIgnisDmgDealt / 100d), 0.1);
			case IGNIS_DMG_TAKEN -> Math.max(Math.pow(multIgnisDmgTaken / 1000d, expIgnisDmgTaken / 100d), 0.1);
			case GLACIES_DMG_DEALT -> Math.max(Math.pow(multGlaciesDmgDealt / 1000d, expGlaciesDmgDealt / 100d), 0.1);
			case GLACIES_DMG_TAKEN -> Math.max(Math.pow(multGlaciesDmgTaken / 1000d, expGlaciesDmgTaken / 100d), 0.1);
			case FULGUR_DMG_DEALT -> Math.max(Math.pow(multFulgurDmgDealt / 1000d, expFulgurDmgDealt / 100d), 0.1);
			case FULGUR_DMG_TAKEN -> Math.max(Math.pow(multFulgurDmgTaken / 1000d, expFulgurDmgTaken / 100d), 0.1);
			case VENTUS_DMG_DEALT -> Math.max(Math.pow(multVentusDmgDealt / 1000d, expVentusDmgDealt / 100d), 0.1);
			case VENTUS_DMG_TAKEN -> Math.max(Math.pow(multVentusDmgTaken / 1000d, expVentusDmgTaken / 100d), 0.1);
			case TERRA_DMG_DEALT -> Math.max(Math.pow(multTerraDmgDealt / 1000d, expTerraDmgDealt / 100d), 0.1);
			case TERRA_DMG_TAKEN -> Math.max(Math.pow(multTerraDmgTaken / 1000d, expTerraDmgTaken / 100d), 0.1);
			case LUX_DMG_DEALT -> Math.max(Math.pow(multLuxDmgDealt / 1000d, expLuxDmgDealt / 100d), 0.1);
			case LUX_DMG_TAKEN -> Math.max(Math.pow(multLuxDmgTaken / 1000d, expLuxDmgTaken / 100d), 0.1);
			case MALUM_DMG_DEALT -> Math.max(Math.pow(multMalumDmgDealt / 1000d, expMalumDmgDealt / 100d), 0.1);
			case MALUM_DMG_TAKEN -> Math.max(Math.pow(multMalumDmgTaken / 1000d, expMalumDmgTaken / 100d), 0.1);
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
		return switch (element) {
			case VIS -> 1;
			case IGNIS -> Math.max(Math.pow(multIgnisDmgDealt / 1000d, expIgnisDmgDealt / 100d), 0.1);
			case GLACIES -> Math.max(Math.pow(multGlaciesDmgDealt / 1000d, expGlaciesDmgDealt / 100d), 0.1);
			case FULGUR -> Math.max(Math.pow(multFulgurDmgDealt / 1000d, expFulgurDmgDealt / 100d), 0.1);
			case VENTUS -> Math.max(Math.pow(multVentusDmgDealt / 1000d, expVentusDmgDealt / 100d), 0.1);
			case TERRA -> Math.max(Math.pow(multTerraDmgDealt / 1000d, expTerraDmgDealt / 100d), 0.1);
			case LUX -> Math.max(Math.pow(multLuxDmgDealt / 1000d, expLuxDmgDealt / 100d), 0.1);
			case MALUM -> Math.max(Math.pow(multMalumDmgDealt / 1000d, expMalumDmgDealt / 100d), 0.1);
		};
	}

	public double getMultWithExpElementDmgTaken(Element element) {
		return switch (element) {
			case VIS -> 1;
			case IGNIS -> Math.max(Math.pow(multIgnisDmgTaken / 1000d, expIgnisDmgTaken / 100d), 0.1);
			case GLACIES -> Math.max(Math.pow(multGlaciesDmgTaken / 1000d, expGlaciesDmgTaken / 100d), 0.1);
			case FULGUR -> Math.max(Math.pow(multFulgurDmgTaken / 1000d, expFulgurDmgTaken / 100d), 0.1);
			case VENTUS -> Math.max(Math.pow(multVentusDmgTaken / 1000d, expVentusDmgTaken / 100d), 0.1);
			case TERRA -> Math.max(Math.pow(multTerraDmgTaken / 1000d, expTerraDmgTaken / 100d), 0.1);
			case LUX -> Math.max(Math.pow(multLuxDmgTaken / 1000d, expLuxDmgTaken / 100d), 0.1);
			case MALUM -> Math.max(Math.pow(multMalumDmgTaken / 1000d, expMalumDmgTaken / 100d), 0.1);
		};
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

	public void setShieldTurns(int shieldTurns) {
		this.shieldTurns = shieldTurns;
	}

	public int getShieldTurns() {
		return shieldTurns;
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

	public void setEffectBlock(int effectBlock) {
		this.effectBlock = effectBlock;
	}

	public int getEffectBlock() {
		return effectBlock;
	}

	public boolean isEffectBlock() {
		return effectBlock > 0;
	}

	public void setInfiniteSp(int infiniteSp) {
		this.infiniteSp = infiniteSp;
	}

	public int getInfiniteSp() {
		return infiniteSp;
	}

	public boolean isInfiniteSp() {
		return infiniteSp > 0;
	}

	public void setModDurationBuffDealt(int modDurationBuffDealt) {
		this.modDurationBuffDealt = modDurationBuffDealt;
	}

	public int getModDurationBuffDealt() {
		return modDurationBuffDealt;
	}

	public void setModDurationBuffTaken(int modDurationBuffTaken) {
		this.modDurationBuffTaken = modDurationBuffTaken;
	}

	public int getModDurationBuffTaken() {
		return modDurationBuffTaken;
	}

	public void setModDurationDebuffDealt(int modDurationDebuffDealt) {
		this.modDurationDebuffDealt = modDurationDebuffDealt;
	}

	public int getModDurationDebuffDealt() {
		return modDurationDebuffDealt;
	}

	public void setModDurationDebuffTaken(int modDurationDebuffTaken) {
		this.modDurationDebuffTaken = modDurationDebuffTaken;
	}

	public int getModDurationDebuffTaken() {
		return modDurationDebuffTaken;
	}

	public void setDurationModBuffTypeDealt(BuffType buffType, int durationModBuffTypeDealt) {
		if (buffType == BuffType.BUFF) {
			modDurationBuffDealt = durationModBuffTypeDealt;
		} else {
			modDurationDebuffDealt = durationModBuffTypeDealt;
		}
	}

	public int getDurationModBuffTypeDealt(BuffType buffType) {
		return (buffType == BuffType.BUFF) ? modDurationBuffDealt : modDurationDebuffDealt;
	}

	public void setDurationModBuffTypeTaken(BuffType buffType, int durationModBuffTypeTaken) {
		if (buffType == BuffType.BUFF) {
			modDurationBuffTaken = durationModBuffTypeTaken;
		} else {
			modDurationDebuffTaken = durationModBuffTypeTaken;
		}
	}

	public int getDurationModBuffTypeTaken(BuffType buffType) {
		return (buffType == BuffType.BUFF) ? modDurationBuffTaken : modDurationDebuffTaken;
	}

	public void setModStacksBuffDealt(int modStacksBuffDealt) {
		this.modStacksBuffDealt = modStacksBuffDealt;
	}

	public int getModStacksBuffDealt() {
		return modStacksBuffDealt;
	}

	public void setModStacksBuffTaken(int modStacksBuffTaken) {
		this.modStacksBuffTaken = modStacksBuffTaken;
	}

	public int getModStacksBuffTaken() {
		return modStacksBuffTaken;
	}

	public void setModStacksDebuffDealt(int modStacksDebuffDealt) {
		this.modStacksDebuffDealt = modStacksDebuffDealt;
	}

	public int getModStacksDebuffDealt() {
		return modStacksDebuffDealt;
	}

	public void setModStacksDebuffTaken(int modStacksDebuffTaken) {
		this.modStacksDebuffTaken = modStacksDebuffTaken;
	}

	public int getModStacksDebuffTaken() {
		return modStacksDebuffTaken;
	}

	public void setStacksModBuffTypeDealt(BuffType buffType, int StacksModBuffTypeDealt) {
		if (buffType == BuffType.BUFF) {
			modStacksBuffDealt = StacksModBuffTypeDealt;
		} else {
			modStacksDebuffDealt = StacksModBuffTypeDealt;
		}
	}

	public int getStacksModBuffTypeDealt(BuffType buffType) {
		return (buffType == BuffType.BUFF) ? modStacksBuffDealt : modStacksDebuffDealt;
	}

	public void setStacksModBuffTypeTaken(BuffType buffType, int StacksModBuffTypeTaken) {
		if (buffType == BuffType.BUFF) {
			modStacksBuffTaken = StacksModBuffTypeTaken;
		} else {
			modStacksDebuffTaken = StacksModBuffTypeTaken;
		}
	}

	public int getStacksModBuffTypeTaken(BuffType buffType) {
		return (buffType == BuffType.BUFF) ? modStacksBuffTaken : modStacksDebuffTaken;
	}

	public void setModRange(int modRange) {
		this.modRange = modRange;
	}

	public int getModRange() {
		return modRange;
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

	public void addBuffInstance(BuffInstance buffInstance) {
		buffInstances.add(buffInstance);
	}

	public void addBuffInstances(BuffInstance... buffInstances) {
		this.buffInstances.addAll(List.of(buffInstances));
	}

	public boolean isProtected() {
		return multDmgTaken <= -5_000_000;
	}

	public boolean isWeakTo(Element element) {
		return affsCur.getAff(element) < 0;
	}

	public boolean resists(Element element) {
		return affsCur.getAff(element) > 0;
	}

	public boolean isImmuneTo(Element element) {
		return affsCur.getAff(element) >= 5;
	}

	public boolean isNeutralTo(Element element) {
		return affsCur.getAff(element) == 0;
	}

	// Returns if the requested Passive is present
	public boolean findPassive(Passive passiveTarget) {
		for (Passive passive : passives) {
			if (passive == passiveTarget) {
				return true;
			}
		}
		return false;
	}

	// Returns the requested BuffInstance if present
	public BuffInstance findBuff(Buff buff) {
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
			for (BuffEffect buffEffect : passive.buffEffects()) {
				notifier.notify(buffEffect, this, target, 1);
			}
		}

		// Handle Buffs
		for (BuffInstance buffInstance : this.getBuffInstances()) {
			for (BuffEffect buffEffect : buffInstance.getBuff().buffEffects()) {
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
			appendToLog(lang.format("log.lose.stage", formatName(unitType.name(), pos, false), stageAtk,
					getColor(stageAtk) + stageAtk, StageType.ATK.getIcon() + C_BUFF + StageType.ATK.getName(),
					getStageStatString(this, StageType.ATK, 0)));
			// Remove stages
			stageAtk = 0;
		}
		if (stageDef != 0 && --stageDefTurns <= 0) {
			appendToLog(lang.format("log.lose.stage", formatName(unitType.name(), pos, false), stageDef,
					getColor(stageDef) + stageDef, StageType.DEF.getIcon() + C_BUFF + StageType.DEF.getName(),
					getStageStatString(this, StageType.DEF, 0)));
			stageDef = 0;
		}
		if (stageFth != 0 && --stageFthTurns <= 0) {
			appendToLog(lang.format("log.lose.stage", formatName(unitType.name(), pos, false), stageFth,
					getColor(stageFth) + stageFth, StageType.FTH.getIcon() + C_BUFF + StageType.FTH.getName(),
					getStageStatString(this, StageType.FTH, 0)));
			stageFth = 0;
		}
		if (stageAgi != 0 && --stageAgiTurns <= 0) {
			appendToLog(lang.format("log.lose.stage", formatName(unitType.name(), pos, false), stageAgi,
					getColor(stageAgi) + stageAgi, StageType.AGI.getIcon() + C_BUFF + StageType.AGI.getName(),
					getStageStatString(this, StageType.AGI, 0)));
			stageAgi = 0;
		}

		// Shield
		if (shield != 0 && --shieldTurns <= 0) {
			if (defend == 0) {
				appendToLog(lang.format("log.lose.shield", formatName(unitType.name(), pos, false),
						C_SHIELD + formatNum(shield / STAT_MULT_HIDDEN)));
				if (effectBlock <= 0) {
					appendToLog(lang.format("log.change_effect_block", formatName(unitType.name(), pos, false), 0));
				}
			} else {
				appendToLog(lang.format("log.change_shield", formatName(unitType.name(), pos),
						C_SHIELD + formatNum((shield + defend) / STAT_MULT_HIDDEN),
						C_SHIELD + formatNum(defend / STAT_MULT_HIDDEN), C_HP + formatNum(statsDefault.getHp()),
						C_NEG + "-" + formatNum(shield / STAT_MULT_HIDDEN)));
			}
			shield = 0;
		}

		// Buffs
		// Iterate backwards so they can be removed
		for (int i = buffInstances.size() - 1; i >= 0; i--) {
			BuffInstance buffInstance = buffInstances.get(i);
			int turns = buffInstance.getTurns();
			if (turns >= 2 && turns < 1000) { // 1000+ turns = infinite
				buffInstance.setTurns(turns - 1);
			} else {
				appendToLog(lang.format("log.lose.buff", formatName(unitType.name(), pos, false),
						buffInstance.getBuff().maxStacks(), C_NUM + buffInstance.getStacks(),
						buffInstance.getBuff().icon() + C_BUFF + buffInstance.getBuff().name(),
						lang.format("log.stack_s", buffInstance.getStacks())));

				// Remove effects
				for (BuffEffect buffEffect : buffInstance.getBuff().buffEffects()) {
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

		String name = (useName) ? formatName(unitType.name(), pos, false) + " " : "";
		String name_s = (useName) ? formatName(unitType.name(), pos) + " " : "";

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
						msg.add(lang.format("log.change_effect_block", name, 0));
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
					shieldTurns = 0;
					if (effectBlock <= 0) {
						msg.add(lang.format("log.change_effect_block", name, 0));
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
