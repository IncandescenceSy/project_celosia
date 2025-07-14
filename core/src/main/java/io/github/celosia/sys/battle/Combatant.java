package io.github.celosia.sys.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Species and current stats
public class Combatant {
    private CombatantType cmbType;
    private int lvl; // Level

    // Current stats
    private Stats statsDefault;
    private Stats statsCur;

    // Skills
    private Skill[] skills;

    private int mp; // Mana
    private int pos; // Position on the battlefield

    private List<BuffInstance> buffInstances = new ArrayList<>();

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

    public void setBuffInstances(List<BuffInstance> buffInstances) {
        this.buffInstances = buffInstances;
    }

    public List<BuffInstance> getBuffInstances() {
        return buffInstances;
    }

    public void addBuffInstance(BuffInstance buffInstance) {
        buffInstances.add(buffInstance);
    }

    public void decrementBuffTurns() {
        for(int i = buffInstances.size() - 1; i >= 0; i--) {
            BuffInstance buffInstance = buffInstances.get(i);
            int turns = buffInstance.getTurns();
            if(turns >= 2) {
                buffInstance.setTurns(turns - 1);
            } else {
                for(BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                    for(int j = 1; j <= buffInstance.getStacks(); j++) { // Remove all stacks
                        buffEffect.onRemove(this);
                    }
                }
                buffInstances.remove(buffInstance);
            }
        }
    }
}
