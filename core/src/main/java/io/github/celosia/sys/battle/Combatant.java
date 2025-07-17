package io.github.celosia.sys.battle;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

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

    // Stat stages
    private int stageAtk;
    private int stageAtkTurns;
    private int stageDef;
    private int stageDefTurns;
    private int stageFth;
    private int stageFthTurns;
    private int stageAgi;
    private int stageAgiTurns;

    // Atk/Def multipliers
    private float multAtk;
    private float multDef;

    // Barrier
    private int barrier;
    private int barrierTurns;

    // Defend (essentially a 2nd Barrier with higher priority)
    private int defend;

    private List<BuffInstance> buffInstances = new ArrayList<>();

    public Combatant(CombatantType cmbType, int lvl, Stats stats, Skill[] skills, int mp, int pos) {
        this.cmbType = cmbType;
        this.lvl = lvl;
        this.statsDefault = stats;
        this.statsCur = new Stats(stats.getHp(), stats.getStr(), stats.getMag(), stats.getFth(), stats.getAmr(), stats.getRes(), stats.getAgi());
        this.skills = skills;
        this.mp = mp;
        this.pos = pos;
        this.stageAtk = 0;
        this.stageAtkTurns = 0;
        this.stageDef = 0;
        this.stageDefTurns = 0;
        this.stageFth = 0;
        this.stageFthTurns = 0;
        this.stageAgi = 0;
        this.stageAgiTurns = 0;
        this.multAtk = 1;
        this.multDef = 1;
        this.barrier = 0;
        this.barrierTurns = 0;
        this.defend = 0;
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

    public void setStageAtk(int stageAtk) {
        this.stageAtk = MathUtils.clamp(stageAtk, -9, 9);
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
        this.stageDef = MathUtils.clamp(stageDef, -9, 9);
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
        this.stageFth = MathUtils.clamp(stageFth, -9, 9);
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
        this.stageAgi = MathUtils.clamp(stageAgi, -9, 9);
    }

    public int getStageAgi() {
        return stageAgi;
    }

    public void setStage(StageType stageType, int stage) {
        switch (stageType) {
            case ATK:
                this.stageAtk = MathUtils.clamp(stage, -9, 9);
                break;
            case DEF:
                this.stageDef = MathUtils.clamp(stage, -9, 9);
                break;
            case FTH:
                this.stageFth = MathUtils.clamp(stage, -9, 9);
                break;
            case AGI:
                this.stageAgi = MathUtils.clamp(stage, -9, 9);
                break;
        }
    }

    public int getStage(StageType stageType) {
        switch (stageType) {
            case ATK:
                return this.stageAtk;
            case DEF:
                return this.stageDef;
            case FTH:
                return this.stageFth;
            case AGI:
                return this.stageAgi;
        }
        return -10;
    }

    public void setStageTurns(StageType stageType, int turns) {
        switch (stageType) {
            case ATK:
                this.stageAtkTurns = turns;
                break;
            case DEF:
                this.stageDefTurns = turns;
                break;
            case FTH:
                this.stageFthTurns = turns;
                break;
            case AGI:
                this.stageAgiTurns = turns;
                break;
        }
    }

    public int getStageTurns(StageType stageType) {
        switch (stageType) {
            case ATK:
                return this.stageAtkTurns;
            case DEF:
                return this.stageDefTurns;
            case FTH:
                return this.stageFthTurns;
            case AGI:
                return this.stageAgiTurns;
        }
        return -1;
    }

    public int getStrWithStage() {
        return this.getStatsCur().getStr() + (int) (this.getStatsDefault().getStr() * (((float) this.stageAtk / 10) / ((this.stageAtk < 0) ? 2 : 1)));
    }

    public int getMagWithStage() {
        return this.getStatsCur().getMag() + (int) (this.getStatsDefault().getMag() * (((float) this.stageAtk / 10) / ((this.stageAtk < 0) ? 2 : 1)));
    }

    public int getFthWithStage() {
        return this.getStatsCur().getFth() + (int) (this.getStatsDefault().getFth() * (((float) this.stageFth / 10) / ((this.stageFth < 0) ? 2 : 1)));
    }

    public int getAmrWithStage() {
        return this.getStatsCur().getAmr() + (int) (this.getStatsDefault().getAmr() * (((float) this.stageDef / 10) / ((this.stageDef < 0) ? 2 : 1)));
    }

    public int getResWithStage() {
        return this.getStatsCur().getRes() + (int) (this.getStatsDefault().getRes() * (((float) this.stageDef / 10) / ((this.stageDef < 0) ? 2 : 1)));
    }

    public int getAgiWithStage() {
        return this.getStatsCur().getAgi() + (int) (this.getStatsDefault().getAgi() * (((float) this.stageAgi / 10) / ((this.stageAgi < 0) ? 2 : 1)));
    }

    public int getStatWithStage(Stat stat) {
        switch(stat) {
            case STR:
                return this.getStatsCur().getStr() + (int) (this.getStatsDefault().getStr() * (((float) this.stageAtk / 10) / ((this.stageAtk < 0) ? 2 : 1)));
            case MAG:
                return this.getStatsCur().getMag() + (int) (this.getStatsDefault().getMag() * (((float) this.stageAtk / 10) / ((this.stageAtk < 0) ? 2 : 1)));
            case FTH:
                return this.getStatsCur().getFth() + (int) (this.getStatsDefault().getFth() * (((float) this.stageFth / 10) / ((this.stageFth < 0) ? 2 : 1)));
            case AMR:
                return this.getStatsCur().getAmr() + (int) (this.getStatsDefault().getAmr() * (((float) this.stageDef / 10) / ((this.stageDef < 0) ? 2 : 1)));
            case RES:
                return this.getStatsCur().getRes() + (int) (this.getStatsDefault().getRes() * (((float) this.stageDef / 10) / ((this.stageDef < 0) ? 2 : 1)));
            case AGI:
                return this.getStatsCur().getAgi() + (int) (this.getStatsDefault().getAgi() * (((float) this.stageAgi / 10) / ((this.stageAgi < 0) ? 2 : 1)));
        }
        return -1;
    }

    public void setMultAtk(float multAtk) {
        this.multAtk = multAtk;
    }

    public float getMultAtk() {
        return multAtk;
    }

    public void setMultDef(float multDef) {
        this.multDef = multDef;
    }

    public float getMultDef() {
        return multDef;
    }

    public void setBarrier(int barrier) {
        this.barrier = barrier;
    }

    public int getBarrier() {
        return barrier;
    }

    public void setBarrierTurns(int barrierTurns) {
        this.barrierTurns = barrierTurns;
    }

    public int getBarrierTurns() {
        return barrierTurns;
    }

    public void setDefend(int defend) {
        this.defend = defend;
    }

    public int getDefend() {
        return defend;
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

    public void decrementTurns() {
        // Stages
        if(--this.stageAtkTurns <= 0) {
            this.stageAtk = 0; // Remove stages
        }
        if(--this.stageDefTurns <= 0) {
            this.stageDef = 0;
        }
        if(--this.stageFthTurns <= 0) {
            this.stageFth = 0;
        }
        if(--this.stageAgiTurns <= 0) {
            this.stageAgi = 0;
        }

        // Barrier
        if(--this.barrierTurns <= 0) {
            this.barrier = 0;
        }

        // Buffs
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

    // Damage Combatant, taking into account Defend and Barrier. Returns false if HP was lowered
    public boolean damage(int dmg, boolean pierce) {
        if(!pierce) { // Pierce skips Defend and Barrier
            if (this.defend > 0 && dmg > 0) { // There's Defend and dmg
                if (this.defend > dmg) { // Only hit Defend
                    this.defend -= dmg;
                    return false;
                } else { // Destroy Defend and proceed to Barrier
                    dmg -= this.defend;
                    this.defend = 0;
                }
            }

            if (this.barrier > 0 && dmg > 0) { // There's Barrier and dmg
                if (this.barrier > dmg) { // Only hit Barrier
                    this.barrier -= dmg;
                    return false;
                } else { // Destroy Barrier and proceed to HP
                    dmg -= this.barrier;
                    this.barrier = 0;
                    this.barrierTurns = 0;
                }
            }
        }

        // Lower HP
        this.getStatsCur().setHp(MathUtils.clamp(this.getStatsCur().getHp() - dmg, 0, this.getStatsDefault().getHp()));

        return dmg > 0;
    }

    public boolean damage(int dmg) {
        return this.damage(dmg, false);
    }
}
