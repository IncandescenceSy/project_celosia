package io.github.celosia.sys.battle;

// Species and current statsL
public class Combatant {
    CombatantType cmbType;
    int lvl; // Level

    // Current stats
    Stats statsDefault;
    Stats statsCur;

    // Skills
    Skill[] skills;

    int mp; // Mana
    int pos; // Position on the battlefield
    // todo skillset, buffs, accessory

    public Combatant(CombatantType cmbType, int lvl, Stats stats, Skill[] skills, int mp, int pos) {
        this.cmbType = cmbType;
        this.lvl = lvl;
        this.statsDefault = stats;
        this.statsCur = new Stats(stats.getHp(), stats.getStr(), stats.getMag(), stats.getFth(), stats.getAmr(), stats.getRes(), stats.getAgi());
        this.skills = skills;
        this.mp = mp;
        this.pos = pos;
    }

    public CombatantType getCmbType() {
        return cmbType;
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

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getMp() {
        return mp;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
