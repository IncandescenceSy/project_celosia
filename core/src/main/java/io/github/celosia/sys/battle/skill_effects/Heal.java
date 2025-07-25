package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;

import static io.github.celosia.sys.settings.Lang.lang;

public class Heal implements SkillEffect {

    private final int pow;
    private final float overHeal; // Amount to heal over max HP. 1f = 100%
    private final int barrierTurns;

    public Heal(int pow, float overHeal, int barrierTurns) {
        this.pow = pow;
        this.overHeal = overHeal;
        this.barrierTurns = barrierTurns;
    }

    public Heal(int pow, int barrierTurns) {
        this(pow, 0f, barrierTurns);
    }

    public Heal(int pow, float overHeal) {
        this(pow, overHeal, 0);
    }

    public Heal(int pow) {
        this(pow, 0f, 0);
    }

    @Override
    public Result apply(Combatant self, Combatant target, ResultType resultPrev) {
        int heal = (int) (((float) self.getFthWithStage() / 50) * pow);

        if (barrierTurns > 0) { // Adds barrier (barrier + defend cannot exceed max HP)
            int hpMax = target.getStatsDefault().getHp();
            int barrierCur = target.getBarrier();
            int barrierNew = (barrierCur + target.getDefend() + heal > hpMax) ? hpMax - target.getDefend() : barrierCur + heal;
            int turnsCur = target.getBarrierTurns();

            String[] msg = new String[2];
            if(barrierNew > barrierCur) {
                target.setBarrier(barrierNew);
                msg[0] = target.getCmbType().getName() + "'s " + lang.get("barrier") + " " + String.format("%,d", (barrierCur + target.getDefend())) + " -> " + String.format("%,d", (barrierNew + target.getDefend())) + "/" + String.format("%,d", hpMax) + " (+" + String.format("%,d", (barrierNew - barrierCur)) + ")";
            } else msg[0] = ""; //target.getCmbType().getName() + "'s " + lang.get("barrier") + " " + lang.get("log.max"); // todo properly choose between 's and '

            if(barrierTurns > turnsCur) {
                target.setBarrierTurns(barrierTurns);
                if(barrierNew > barrierCur) msg[0] += ", " + lang.get("turns") + " " + turnsCur + " -> " + barrierTurns + "\n";
                else msg[1] = target.getCmbType().getName() + " " + lang.get("barrier") + " " + lang.get("turns") + " " + turnsCur + " -> " + barrierTurns + "\n";
            } else {
                if(barrierNew > barrierCur) msg[0] += "\n";
                msg[1] = ""; //target.getCmbType().getName() + "'s " + lang.get("barrier") + " " + lang.get("log.duration_unchanged");
            }

            return new Result(ResultType.SUCCESS, msg);
        } else { // Heals
            int hpCur = target.getStatsCur().getHp();
            int hpMax = target.getStatsDefault().getHp();
            // Picks the lower of (current HP + heal amount) and (maximum allowed overHeal of this skill), and then the higher between that and current HP
            int hpNew = (int) Math.max(hpCur, Math.min(hpCur + heal, hpMax * (1 + overHeal)));

            if(hpNew > hpCur) {
                target.getStatsCur().setHp(hpNew);
                return new Result(ResultType.SUCCESS, target.getCmbType().getName() + "'s " + lang.get("hp") + " " + String.format("%,d", hpCur) + " -> " + String.format("%,d", hpNew) + "/" + String.format("%,d", hpMax) + " (+" + String.format("%,d", (hpNew - hpCur)) + ")" + "\n");
            } else return new Result(ResultType.SUCCESS, ""/*target.getCmbType().getName() + "'s " + lang.get("hp") + " " + lang.get("log.max")*/);
        }
    }
}
