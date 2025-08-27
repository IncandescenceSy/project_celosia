package io.github.celosia.sys.battle;

// A set of stats
public class Stats {

    private int hp;  // Health
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

    public void setStat(Stat stat, int set) {
        switch(stat) {
            case STR:
                this.str = set;
                break;
            case MAG:
                this.mag = set;
                break;
            case FTH:
                this.fth = set;
                break;
            case AMR:
                this.amr = set;
                break;
            case RES:
                this.res = set;
                break;
            case AGI:
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

    public Stats getRealStats(int lvl) {
        return new Stats((hp + (hp / 2) * lvl) * 5, str + (str / 2) * lvl, mag + (mag / 2) * lvl, fth + (fth / 2) * lvl, amr + (amr / 2) * lvl, res + (res / 2) * lvl, agi + (agi / 2) * lvl);
    }
}
