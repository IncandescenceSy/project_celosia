package io.github.celosia.sys.battle;

import static io.github.celosia.sys.battle.BattleLib.STAT_MULT_HIDDEN;
import static io.github.celosia.sys.battle.BattleLib.STAT_MULT_VISIBLE;

// A set of stats
public class Stats {

	private long hp; // Health
	private long str; // Strength (physical attack)
	private long mag; // Magic (magical attack)
	private long fth; // Faith (healing/shielding power)
	private long amr; // Armor (physical defense)
	private long res; // Resilience (magical defense)
	private long agi; // Agility (turn order)

	public Stats(long hp, long str, long mag, long fth, long amr, long res, long agi) {
		this.hp = hp;
		this.str = str;
		this.mag = mag;
		this.fth = fth;
		this.amr = amr;
		this.res = res;
		this.agi = agi;
	}

	public Stats(Stats stats) {
		this.hp = stats.hp;
		this.str = stats.str;
		this.mag = stats.mag;
		this.fth = stats.fth;
		this.amr = stats.amr;
		this.res = stats.res;
		this.agi = stats.agi;
	}

	public Stats(long stats) {
		hp = stats;
		str = stats;
		mag = stats;
		fth = stats;
		amr = stats;
		res = stats;
		agi = stats;
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

	public void setStr(long str) {
		this.str = str;
	}

	public long getStr() {
		return str;
	}

	public long getDisplayStr() {
		return str / STAT_MULT_HIDDEN;
	}

	public void setMag(long mag) {
		this.mag = mag;
	}

	public long getMag() {
		return mag;
	}

	public long getDisplayMag() {
		return mag / STAT_MULT_HIDDEN;
	}

	public void setFth(long fth) {
		this.fth = fth;
	}

	public long getFth() {
		return fth;
	}

	public long getDisplayFth() {
		return fth / STAT_MULT_HIDDEN;
	}

	public void setAmr(long amr) {
		this.amr = amr;
	}

	public long getAmr() {
		return amr;
	}

	public long getDisplayAmr() {
		return amr / STAT_MULT_HIDDEN;
	}

	public void setRes(long res) {
		this.res = res;
	}

	public long getRes() {
		return res;
	}

	public long getDisplayRes() {
		return res / STAT_MULT_HIDDEN;
	}

	public void setAgi(long agi) {
		this.agi = agi;
	}

	public long getAgi() {
		return agi;
	}

	public long getDisplayAgi() {
		return agi / STAT_MULT_HIDDEN;
	}

	public void setStat(Stat stat, long set) {
		switch (stat) {
			case STR :
				this.str = set;
				break;
			case MAG :
				this.mag = set;
				break;
			case FTH :
				this.fth = set;
				break;
			case AMR :
				this.amr = set;
				break;
			case RES :
				this.res = set;
				break;
			case AGI :
				this.agi = set;
				break;
		}
	}

	public long getStat(Stat stat) {
		return switch (stat) {
			case STR -> str;
			case MAG -> mag;
			case FTH -> fth;
			case AMR -> amr;
			case RES -> res;
			case AGI -> agi;
		};
	}

	public long getDisplayStat(Stat stat) {
		return switch (stat) {
			case STR -> str / STAT_MULT_HIDDEN;
			case MAG -> mag / STAT_MULT_HIDDEN;
			case FTH -> fth / STAT_MULT_HIDDEN;
			case AMR -> amr / STAT_MULT_HIDDEN;
			case RES -> res / STAT_MULT_HIDDEN;
			case AGI -> agi / STAT_MULT_HIDDEN;
		};
	}

	public long getRealHp(long lvl) {
		return (hp + (hp / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN;
	}

	public Stats getRealStats(long lvl) {
		return new Stats((hp + (hp / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN,
				(str + (str / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN,
				(mag + (mag / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN,
				(fth + (fth / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN,
				(amr + (amr / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN,
				(res + (res / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN,
				(agi + (agi / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN);
	}

	public void addToStat(Stat stat, long change) {
		switch (stat) {
			case STR :
				this.str += change;
				break;
			case MAG :
				this.mag += change;
				break;
			case FTH :
				this.fth += change;
				break;
			case AMR :
				this.amr += change;
				break;
			case RES :
				this.res += change;
				break;
			case AGI :
				this.agi += change;
				break;
		}
	}
}
