package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;

import static io.github.celosia.sys.settings.Lang.lang;

public class Heal implements SkillEffect {

    private final int pow;
    private final double overHeal; // Amount to heal over max HP. 1f = 100%
    private final int shieldTurns;
    private final boolean isInstant;
    private final boolean mainTargetOnly;

    public Heal(int pow, double overHeal, int shieldTurns, boolean isInstant, boolean mainTargetOnly) {
        this.pow = pow;
        this.overHeal = overHeal;
        this.shieldTurns = shieldTurns;
        this.isInstant = isInstant;
        this.mainTargetOnly = mainTargetOnly;
    }

    public Heal(int pow, int shieldTurns) {
        this(pow, 0, shieldTurns, false, false);
    }

    public Heal(int pow, double overHeal) {
        this(pow, overHeal, 0, false, false);
    }

    public Heal(int pow) {
        this(pow, 0, 0, false, false);
    }

    @Override
    public Result apply(Combatant self, Combatant target, boolean isMainTarget, ResultType resultPrev) {
        if(!mainTargetOnly || isMainTarget) {
            // Heals by pow% of user's Fth
            int heal = (int) (self.getFthWithStage() * (pow / 100d) * (Math.max(self.getMultHealingDealt(), 10) / 100d) * (Math.max(target.getMultHealingTaken(), 10) / 100d));

            if (shieldTurns > 0) { // Adds shield (shield + defend cannot exceed max HP)
                int hpMax = target.getStatsDefault().getHp();
                int shieldCur = target.getShield();
                int shieldNew = (shieldCur + target.getDefend() + heal > hpMax) ? hpMax - target.getDefend() : shieldCur + heal;
                int turnsCur = target.getShieldTurns();

                String[] msg = new String[2];
                if (shieldNew > shieldCur) {
                    target.setShield(shieldNew);
                    msg[0] = target.getCmbType().getName() + "'s " + lang.get("shield") + " " + String.format("%,d", (shieldCur + target.getDefend())) + " -> " + String.format("%,d", (shieldNew +
                        target.getDefend())) + "/" + String.format("%,d", hpMax) + " (+" + String.format("%,d", (shieldNew - shieldCur)) + ")";
                } else
                    msg[0] = "";

                if (shieldTurns > turnsCur) {
                    target.setShieldTurns(shieldTurns);
                    if (shieldNew > shieldCur)
                        msg[0] += ", " + lang.get("turns") + " " + turnsCur + " -> " + shieldTurns + "\n";
                    else
                        msg[1] = target.getCmbType().getName() + " " + lang.get("shield") + " " + lang.get("turns") + " " + turnsCur + " -> " + shieldTurns + "\n";
                } else {
                    if (shieldNew > shieldCur) msg[0] += "\n";
                    msg[1] = "";
                }

                // Effect block message
                if(shieldCur == 0 && shieldNew > 0 && target.getEffectBlock() <= 0 && target.getDefend() == 0) {
                    msg[1] += self.getCmbType().getName() + " " + lang.get("log.is_now") + " " + lang.get("log.effect_block") + "\n";
                }

                return new Result(ResultType.SUCCESS, msg);
            } else { // Heals
                int hpCur = target.getStatsCur().getHp();
                int hpMax = target.getStatsDefault().getHp();
                // Picks the lower of (current HP + heal amount) and (maximum allowed overHeal of this skill), and then the higher between that and current HP
                int hpNew = (int) Math.max(hpCur, Math.min(hpCur + heal, hpMax * (1 + overHeal)));

                if (hpNew > hpCur) {
                    target.getStatsCur().setHp(hpNew);
                    return new Result(ResultType.SUCCESS, target.getCmbType().getName() + "'s " + lang.get("hp") + " " + String.format("%,d", hpCur) + " -> " + String.format("%,d", hpNew)
                        + "/" + String.format("%,d", hpMax) + " (+" + String.format("%,d", (hpNew - hpCur)) + ")" + "\n");
                } else return new Result(ResultType.SUCCESS, "");
            }
        } else return new Result(ResultType.SUCCESS, "");
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }
}
