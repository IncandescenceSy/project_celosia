package io.github.celosia.sys.battle;

// Species and current stats
public class Combatant {
    CombatantType cmbType;
    int lvl; // Level

    // Current stats
    Stats statsDefault;
    Stats statsCur;

    int mp; // Mana
    // todo skillset, buffs, accessory

    public Combatant(CombatantType cmbType, int lvl, Stats stats, int mp) {
        this.cmbType = cmbType;
        this.lvl = lvl;
        this.statsDefault = stats;
        this.statsCur = stats;
        this.mp = mp;
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

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getMp() {
        return mp;
    }
}
