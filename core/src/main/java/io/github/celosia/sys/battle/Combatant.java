package io.github.celosia.sys.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.celosia.sys.settings.Lang.lang;

// Species and current stats
public class Combatant {
    private final CombatantType cmbType;
    private final int lvl; // Level

    // Current stats
    private final Stats statsDefault;
    private final Stats statsCur;

    // Skills
    private final Skill[] skills;

    private int sp; // Skill Points
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

    // Multiplies damage dealt/taken
    private float multAtk;
    private float multDef;

    // Barrier
    private int barrier;
    private int barrierTurns;

    // Defend (essentially a 2nd Barrier with higher priority)
    private int defend;

    private List<BuffInstance> buffInstances = new ArrayList<>();

    public Combatant(CombatantType cmbType, int lvl, Stats stats, Skill[] skills, int pos) {
        this.cmbType = cmbType;
        this.lvl = lvl;
        this.statsDefault = stats;
        this.statsCur = new Stats(stats.getHp(), stats.getStr(), stats.getMag(), stats.getFth(), stats.getAmr(), stats.getRes(), stats.getAgi());
        this.skills = skills;
        this.sp = 10;
        this.pos = pos;
        stageAtk = 0;
        stageAtkTurns = 0;
        stageDef = 0;
        stageDefTurns = 0;
        stageFth = 0;
        stageFthTurns = 0;
        stageAgi = 0;
        stageAgiTurns = 0;
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

    public void setSp(int sp) {
        this.sp = sp;
    }

    public int getSp() {
        return sp;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public boolean isPlayerTeam() {
        return pos < 4;
    }

    public void setStageAtk(int stageAtk) {
        this.stageAtk = stageAtk;
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
        this.stageDef = stageDef;
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
        this.stageFth = stageFth;
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
        this.stageAgi = stageAgi;
    }

    public int getStageAgi() {
        return stageAgi;
    }

    public void setStage(StageType stageType, int stage) {
        switch (stageType) {
            case ATK:
                stageAtk = stage;
                break;
            case DEF:
                stageDef = stage;
                break;
            case FTH:
                stageFth = stage;
                break;
            case AGI:
                stageAgi = stage;
                break;
        }
    }

    public int getStage(StageType stageType) {
        return switch (stageType) {
            case ATK -> stageAtk;
            case DEF -> stageDef;
            case FTH -> stageFth;
            case AGI -> stageAgi;
        };
    }

    public void setStageTurns(StageType stageType, int turns) {
        switch (stageType) {
            case ATK:
                stageAtkTurns = turns;
                break;
            case DEF:
                stageDefTurns = turns;
                break;
            case FTH:
                stageFthTurns = turns;
                break;
            case AGI:
                stageAgiTurns = turns;
                break;
        }
    }

    public int getStageTurns(StageType stageType) {
        switch (stageType) {
            case ATK:
                return stageAtkTurns;
            case DEF:
                return stageDefTurns;
            case FTH:
                return stageFthTurns;
            case AGI:
                return stageAgiTurns;
        }
        return -1;
    }

    public int getStrWithStage() {
        return this.getStatsCur().getStr() + (int) (this.getStatsDefault().getStr() * (((float) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1)));
    }

    public int getMagWithStage() {
        return this.getStatsCur().getMag() + (int) (this.getStatsDefault().getMag() * (((float) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1)));
    }

    public int getFthWithStage() {
        return this.getStatsCur().getFth() + (int) (this.getStatsDefault().getFth() * (((float) stageFth / 10) / ((stageFth < 0) ? 2 : 1)));
    }

    public int getAmrWithStage() {
        return this.getStatsCur().getAmr() + (int) (this.getStatsDefault().getAmr() * (((float) stageDef / 10) / ((stageDef < 0) ? 2 : 1)));
    }

    public int getResWithStage() {
        return this.getStatsCur().getRes() + (int) (this.getStatsDefault().getRes() * (((float) stageDef / 10) / ((stageDef < 0) ? 2 : 1)));
    }

    public int getAgiWithStage() {
        return this.getStatsCur().getAgi() + (int) (this.getStatsDefault().getAgi() * (((float) stageAgi / 10) / ((stageAgi < 0) ? 2 : 1)));
    }

    public int getStatWithStage(Stat stat) {
        switch(stat) {
            case STR:
                return this.getStatsCur().getStr() + (int) (this.getStatsDefault().getStr() * (((float) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1)));
            case MAG:
                return this.getStatsCur().getMag() + (int) (this.getStatsDefault().getMag() * (((float) stageAtk / 10) / ((stageAtk < 0) ? 2 : 1)));
            case FTH:
                return this.getStatsCur().getFth() + (int) (this.getStatsDefault().getFth() * (((float) stageFth / 10) / ((stageFth < 0) ? 2 : 1)));
            case AMR:
                return this.getStatsCur().getAmr() + (int) (this.getStatsDefault().getAmr() * (((float) stageDef / 10) / ((stageDef < 0) ? 2 : 1)));
            case RES:
                return this.getStatsCur().getRes() + (int) (this.getStatsDefault().getRes() * (((float) stageDef / 10) / ((stageDef < 0) ? 2 : 1)));
            case AGI:
                return this.getStatsCur().getAgi() + (int) (this.getStatsDefault().getAgi() * (((float) stageAgi / 10) / ((stageAgi < 0) ? 2 : 1)));
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

    public void setMult(Mult mult, float set) {
        if (mult == Mult.ATK) multAtk = set;
        else multDef = set;
    }

    public float getMult(Mult mult) {
        if (mult == Mult.ATK) return multAtk;
        else return multDef;
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

    // Returns the requested BuffInstance if present
    public BuffInstance findBuff(Buff buff) {
        for(BuffInstance buffInstance : buffInstances) {
            if(buffInstance.getBuff() == buff) return buffInstance;
        }
        return null;
    }

    public String decrementTurns() {
        StringBuilder msg = new StringBuilder();

        // Stages
        if (stageAtk != 0 && --stageAtkTurns <= 0) {
            // todo stage(s)
            msg.append(this.getCmbType().getName()).append(" ").append(lang.get("log.loses")).append(" ").append(stageAtk).append(lang.get("stages")).append(" ").append(StageType.ATK.getName()).append("\n");
            stageAtk = 0; // Remove stages
        }
        if (stageDef != 0 && --stageDefTurns <= 0) {
            msg.append(this.getCmbType().getName()).append(" ").append(lang.get("log.loses")).append(" ").append(stageDef).append(lang.get("stages")).append(" ").append(StageType.DEF.getName()).append("\n");
            stageDef = 0;
        }
        if (stageFth != 0 && --stageFthTurns <= 0) {
            msg.append(this.getCmbType().getName()).append(" ").append(lang.get("log.loses")).append(" ").append(stageFth).append(lang.get("stages")).append(" ").append(StageType.FTH.getName()).append("\n");
            stageFth = 0;
        }
        if (stageAgi != 0 && --stageAgiTurns <= 0) {
            msg.append(this.getCmbType().getName()).append(" ").append(lang.get("log.loses")).append(" ").append(stageAgi).append(lang.get("stages")).append(" ").append(StageType.AGI.getName()).append("\n");
            stageAgi = 0;
        }

        // Barrier
        if (barrier != 0 && --barrierTurns <= 0) {
            msg.append(this.getCmbType().getName()).append(" ").append(lang.get("log.loses")).append(" ").append(barrier).append(" ").append(lang.get("barrier")).append("\n");
            barrier = 0;
        }

        // Buffs
        for(int i = buffInstances.size() - 1; i >= 0; i--) {
            BuffInstance buffInstance = buffInstances.get(i);
            int turns = buffInstance.getTurns();
            if (turns >= 2 && turns < 1000) { // 1000+ turns = infinite
                buffInstance.setTurns(turns - 1);
            } else {
                int maxStacks = buffInstance.getBuff().getMaxStacks();
                msg.append(this.getCmbType().getName()).append(" ").append(lang.get("log.loses")).append(" ");
                if(maxStacks > 1) msg.append(buffInstance.getStacks()).append(" ");
                msg.append(buffInstance.getBuff().getName());
                if(maxStacks > 1) msg.append(lang.get("stacks"));
                msg.append("\n");

                // todo condense lines when giving/removing multiple stacks at once
                for(BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                    for(int j = 1; j <= buffInstance.getStacks(); j++) { // Remove all stacks
                        String onRemove = buffEffect.onRemove(this);
                        if(!Objects.equals(onRemove, "")) msg.append(onRemove).append("\n");
                    }
                }
                buffInstances.remove(buffInstance);
            }
        }

        return msg.toString();
    }

    // Damage Combatant, taking into account Defend and Barrier. Returns false if HP was lowered
    public Result damage(int dmg, boolean pierce) {
        dmg *= Math.max(multDef, 0.1f);
        int dmgFull = dmg;
        int defendOld = defend;

        String[] msg = new String[2];

        if (!pierce) { // Pierce skips Defend and Barrier
            if (defend > 0 && dmg > 0) { // There's Defend and dmg
                if (defend > dmg) { // Only hit Defend
                    defend -= dmg;
                    return new Result(ResultType.HIT_BARRIER, this.getCmbType().getName() + "'s " + lang.get("barrier") + " " + String.format("%,d", (defendOld + barrier)) + " -> " + String.format("%,d", (defend + barrier)) + "/" + String.format("%,d", this.getStatsDefault().getHp()) + " (-" + String.format("%,d", dmgFull) + ")" + "\n");
                } else { // Destroy Defend and proceed to Barrier
                    dmg -= defend;
                    defend = 0;
                }
            }

            if (barrier > 0 && dmg > 0) { // There's Barrier and dmg
                if (barrier > dmg) { // Only hit Barrier
                    barrier -= dmg;
                    return new Result(ResultType.HIT_BARRIER, this.getCmbType().getName() + "'s " + lang.get("barrier") + " " + String.format("%,d", (defendOld + barrier)) + " -> " + String.format("%,d", barrier) + "/" + String.format("%,d", this.getStatsDefault().getHp()) + " (-" + String.format("%,d", dmgFull) + ")" + "\n");
                } else { // Destroy Barrier and proceed to HP
                    msg[0] = this.getCmbType().getName() + "'s " + lang.get("barrier") + " " + String.format("%,d", (defendOld + barrier)) + " -> " + 0 + "/" + this.getStatsDefault().getHp() + " (-" + String.format("%,d", (defendOld + barrier)) + ")" + "\n";
                    dmg -= barrier;
                    barrier = 0;
                    barrierTurns = 0;
                }
            }
        }

        // Lower HP
        int hpOld = this.getStatsCur().getHp();
        int hpNew = Math.clamp(hpOld - dmg, 0, this.getStatsDefault().getHp());
        msg[1] = this.getCmbType().getName() + "'s " + lang.get("hp") + " " + String.format("%,d", hpOld) + " -> " + String.format("%,d", hpNew) + " (-" + String.format("%,d", dmg) + ")" + "\n";
        this.getStatsCur().setHp(hpNew);

        if (multDef <= -500f) { // Hit Protect
            return new Result(ResultType.HIT_BARRIER, msg);
        } else if (dmg > 0) { // Did damage
            return new Result(ResultType.SUCCESS, msg);
        } else return new Result(ResultType.FAIL, lang.get("log.no_effect") + " " + lang.get("log.on") + " " + this.getCmbType().getName() + "\n"); // Did no damage
    }

    public Result damage(int dmg) {
        return this.damage(dmg, false);
    }
}
