package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.SkillEffect;

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
    public Result apply(Combatant self, Combatant target, Result resultPrev) {
        int heal = (int) (((float) self.getFthWithStage() / 50) * pow);

        if(barrierTurns > 0) { // Adds barrier (barrier + defend cannot exceed max HP)
            int maxHp = target.getStatsDefault().getHp();
            target.setBarrier((target.getBarrier() + target.getDefend() + heal > maxHp) ? maxHp - target.getDefend() : target.getBarrier() + heal);
            target.setBarrierTurns(Math.max(target.getBarrierTurns(), barrierTurns));
        } else { // Heals
            int hpCur = target.getStatsCur().getHp();
            // Picks the lower of (current HP + heal amount) and (maximum allowed overHeal of this skill), and then the higher between that and current HP
            target.getStatsCur().setHp((int) Math.max(hpCur, Math.min(hpCur + heal, target.getStatsDefault().getHp() * (1 + overHeal))));
        }

        return Result.SUCCESS;
    }
}
