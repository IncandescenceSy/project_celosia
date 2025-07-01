package io.github.celosia.sys.battle;

// Species and current stats
public class Combatant {
    CombatantType cmbType;
    int lvl; // Level

    // Current stats
    Stats stats;

    int mp; // Mana
    // todo skillset, buffs, accessory

    public Combatant(CombatantType cmbType, int lvl, Stats stats, int mp) {
        this.cmbType = cmbType;
        this.lvl = lvl;
        this.stats = stats;
        this.mp = mp;
    }

    public CombatantType getCmbType() {
        return cmbType;
    }

    public int getLvl() {
        return lvl;
    }

    public Stats getStats() {
        return stats;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getMp() {
        return mp;
    }
}
