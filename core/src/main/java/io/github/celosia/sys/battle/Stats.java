package io.github.celosia.sys.battle;

// A set of statsL
public class Stats {

    int hp;  // Health
    int str; // Strength
    int mag; // Magic
    int fth; // Faith
    int amr; // Armor
    int res; // Resistance
    int agi; // Agility

    public Stats(int hp, int str, int mag, int fth, int amr, int res, int agi) {
        this.hp = hp;
        this.str = str;
        this.mag = mag;
        this.fth = fth;
        this.amr = amr;
        this.res = res;
        this.agi = agi;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getHp() {
        return hp;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getStr() {
        return str;
    }

    public void setMag(int mag) {
        this.mag = mag;
    }

    public int getMag() {
        return mag;
    }

    public void setFth(int fth) {
        this.fth = fth;
    }

    public int getFth() {
        return fth;
    }

    public void setAmr(int amr) {
        this.amr = amr;
    }

    public int getAmr() {
        return amr;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getRes() {
        return res;
    }

    public void setAgi(int agi) {
        this.agi = agi;
    }

    public int getAgi() {
        return agi;
    }

    public Stats getRealStats(int lvl) {
        return new Stats(hp * lvl, str * lvl, mag * lvl, fth * lvl, amr * lvl, res * lvl, agi * lvl);
    }
}
