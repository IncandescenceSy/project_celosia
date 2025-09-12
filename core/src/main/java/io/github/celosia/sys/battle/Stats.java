package io.github.celosia.sys.battle;

import static io.github.celosia.sys.battle.BattleLib.STAT_MULT_HIDDEN;
import static io.github.celosia.sys.battle.BattleLib.STAT_MULT_VISIBLE;

// A set of stats
public class Stats {

	private int hp; // Health
	private int str; // Strength (physical attack)
	private int mag; // Magic (magical attack)
	private int fth; // Faith (healing/shielding power)
	private int amr; // Armor (physical defense)
	private int res; // Resilience (magical defense)
	private int agi; // Agility (turn order)

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
		return hp / STAT_MULT_HIDDEN;
	}

	public void setStr(int str) {
		this.str = str;
	}

	public int getStr() {
		return str;
	}

	public int getDisplayStr() {
		return str / STAT_MULT_HIDDEN;
	}

	public void setMag(int mag) {
		this.mag = mag;
	}

	public int getMag() {
		return mag;
	}

	public int getDisplayMag() {
		return mag / STAT_MULT_HIDDEN;
	}

	public void setFth(int fth) {
		this.fth = fth;
	}

	public int getFth() {
		return fth;
	}

	public int getDisplayFth() {
		return fth / STAT_MULT_HIDDEN;
	}

	public void setAmr(int amr) {
		this.amr = amr;
	}

	public int getAmr() {
		return amr;
	}

	public int getDisplayAmr() {
		return amr / STAT_MULT_HIDDEN;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public int getRes() {
		return res;
	}

	public int getDisplayRes() {
		return res / STAT_MULT_HIDDEN;
	}

	public void setAgi(int agi) {
		this.agi = agi;
	}

	public int getAgi() {
		return agi;
	}

	public int getDisplayAgi() {
		return agi / STAT_MULT_HIDDEN;
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
			case STR -> str / STAT_MULT_HIDDEN;
			case MAG -> mag / STAT_MULT_HIDDEN;
			case FTH -> fth / STAT_MULT_HIDDEN;
			case AMR -> amr / STAT_MULT_HIDDEN;
			case RES -> res / STAT_MULT_HIDDEN;
			case AGI -> agi / STAT_MULT_HIDDEN;
		};
	}

	public Stats getRealStats(int lvl) {
		return new Stats((hp + (hp / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN, (str + (str / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN,
				(mag + (mag / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN, (fth + (fth / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN,
				(amr + (amr / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN, (res + (res / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN,
				(agi + (agi / 2) * lvl) * STAT_MULT_VISIBLE * STAT_MULT_HIDDEN);
	}
}
