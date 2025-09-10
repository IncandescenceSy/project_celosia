package io.github.celosia.sys.battle;

import static io.github.celosia.sys.battle.BattleLib.STAT_FACTOR;

// A set of stats
public class Stats {

	private int hp; // Health
	private int str; // Strength
	private int mag; // Magic
	private int fth; // Faith
	private int amr; // Armor
	private int res; // Resilience
	private int agi; // Agility

	public Stats(int hp, int str, int mag, int fth, int amr, int res, int agi) {
		this.hp = hp;
		this.str = str;
		this.mag = mag;
		this.fth = fth;
		this.amr = amr;
		this.res = res;
		this.agi = agi;
	}

	public Stats(Stats stats) {
		this.hp = stats.getHp();
		this.str = stats.getStr();
		this.mag = stats.getMag();
		this.fth = stats.getFth();
		this.amr = stats.getAmr();
		this.res = stats.getRes();
		this.agi = stats.getAgi();
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getHp() {
		return hp;
	}

	public int getDisplayHp() {
		return hp / STAT_FACTOR;
	}

	public void setStr(int str) {
		this.str = str;
	}

	public int getStr() {
		return str;
	}

	public int getDisplayStr() {
		return str / STAT_FACTOR;
	}

	public void setMag(int mag) {
		this.mag = mag;
	}

	public int getMag() {
		return mag;
	}

	public int getDisplayMag() {
		return mag / STAT_FACTOR;
	}

	public void setFth(int fth) {
		this.fth = fth;
	}

	public int getFth() {
		return fth;
	}

	public int getDisplayFth() {
		return fth / STAT_FACTOR;
	}

	public void setAmr(int amr) {
		this.amr = amr;
	}

	public int getAmr() {
		return amr;
	}

	public int getDisplayAmr() {
		return amr / STAT_FACTOR;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public int getRes() {
		return res;
	}

	public int getDisplayRes() {
		return res / STAT_FACTOR;
	}

	public void setAgi(int agi) {
		this.agi = agi;
	}

	public int getAgi() {
		return agi;
	}

	public int getDisplayAgi() {
		return agi / STAT_FACTOR;
	}

	public void setStat(Stat stat, int set) {
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

	public int getStat(Stat stat) {
		return switch (stat) {
			case STR -> str;
			case MAG -> mag;
			case FTH -> fth;
			case AMR -> amr;
			case RES -> res;
			case AGI -> agi;
		};
	}

	public int getDisplayStat(Stat stat) {
		return switch (stat) {
			case STR -> str / STAT_FACTOR;
			case MAG -> mag / STAT_FACTOR;
			case FTH -> fth / STAT_FACTOR;
			case AMR -> amr / STAT_FACTOR;
			case RES -> res / STAT_FACTOR;
			case AGI -> agi / STAT_FACTOR;
		};
	}

	public Stats getRealStats(int lvl) {
		return new Stats((hp + (hp / 2) * lvl) * 10 * STAT_FACTOR, (str + (str / 2) * lvl) * 10 * STAT_FACTOR,
				(mag + (mag / 2) * lvl) * 10 * STAT_FACTOR, (fth + (fth / 2) * lvl) * 10 * STAT_FACTOR,
				(amr + (amr / 2) * lvl) * 10 * STAT_FACTOR, (res + (res / 2) * lvl) * 10 * STAT_FACTOR,
				(agi + (agi / 2) * lvl) * 10 * STAT_FACTOR);
	}
}
