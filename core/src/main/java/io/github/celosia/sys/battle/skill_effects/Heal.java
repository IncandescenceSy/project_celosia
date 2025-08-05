package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;

import static io.github.celosia.sys.settings.Lang.lang;

public class Heal implements SkillEffect {

    private final int pow;
    private final float overHeal; // Amount to heal over max HP. 1f = 100%
    private final int shieldTurns;

    public Heal(int pow, float overHeal, int shieldTurns) {
        this.pow = pow;
        this.overHeal = overHeal;
        this.shieldTurns = shieldTurns;
    }

    public Heal(int pow, int shieldTurns) {
        this(pow, 0f, shieldTurns);
    }

    public Heal(int pow, float overHeal) {
        this(pow, overHeal, 0);
    }

    public Heal(int pow) {
        this(pow, 0f, 0);
    }

    @Override
    public Result apply(Combatant self, Combatant target, ResultType resultPrev) {
        // Heals by pow% of user's Fth
        int heal = (int) (self.getFthWithStage() * (pow / 100f) * (Math.max(self.getMultHealingDealt(), 10) / 100f) * (Math.max(target.getMultHealingTaken(), 10) / 100f));

        if (shieldTurns > 0) { // Adds shield (shield + defend cannot exceed max HP)
            int hpMax = target.getStatsDefault().getHp();
            int shieldCur = target.getShield();
            int shieldNew = (shieldCur + target.getDefend() + heal > hpMax) ? hpMax - target.getDefend() : shieldCur + heal;
            int turnsCur = target.getShieldTurns();

            String[] msg = new String[2];
            if(shieldNew > shieldCur) {
                target.setShield(shieldNew);
                msg[0] = target.getCmbType().getName() + "'s " + lang.get("shield") + " " + String.format("%,d", (shieldCur + target.getDefend())) + " -> " + String.format("%,d", (shieldNew + target.getDefend())) + "/" + String.format("%,d", hpMax) + " (+" + String.format("%,d", (shieldNew - shieldCur)) + ")";
            } else msg[0] = ""; //target.getCmbType().getName() + "'s " + lang.get("shield") + " " + lang.get("log.max"); // todo properly choose between 's and '

            if(shieldTurns > turnsCur) {
                target.setShieldTurns(shieldTurns);
                if(shieldNew > shieldCur) msg[0] += ", " + lang.get("turns") + " " + turnsCur + " -> " + shieldTurns + "\n";
                else msg[1] = target.getCmbType().getName() + " " + lang.get("shield") + " " + lang.get("turns") + " " + turnsCur + " -> " + shieldTurns + "\n";
            } else {
                if(shieldNew > shieldCur) msg[0] += "\n";
                msg[1] = ""; //target.getCmbType().getName() + "'s " + lang.get("shield") + " " + lang.get("log.duration_unchanged");
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
