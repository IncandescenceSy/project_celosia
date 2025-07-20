package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

import java.util.List;

public class GiveBuff implements SkillEffect {

    private final Buff buff;
    private final int turns;
    private final int stacks;
    private final Result minResult;
    private final boolean giveToSelf;

    public GiveBuff(Buff buff, int turns, int stacks, Result minResult, boolean giveToSelf) {
        this.buff = buff;
        this.turns = turns;
        this.stacks = stacks;
        this.minResult = minResult;
        this.giveToSelf = giveToSelf;
    }

    public GiveBuff(Buff buff, int turns, int stacks) {
        this(buff, turns, stacks, Result.SUCCESS, false);
    }

    public GiveBuff(Buff buff, int turns) {
        this(buff, turns, 1, Result.SUCCESS, false);
    }

    public GiveBuff(Buff buff, int turns, boolean giveToSelf) {
        this(buff, turns, 1, Result.SUCCESS, giveToSelf);
    }

    public GiveBuff(Buff buff) {
        this(buff, 1, 1, Result.SUCCESS, false);
    }

    @Override
    public Result apply(Combatant self, Combatant target, Result resultPrev) {
        // Most attacks don't apply buffs if the previous hit was blocked by Barrier or immunity
        if (resultPrev.ordinal() >= minResult.ordinal()) {
            Combatant cmb = (giveToSelf) ? self : target;
            List<BuffInstance> buffInstances = cmb.getBuffInstances();
            BuffInstance buffInstance = cmb.findBuff(buff);

            if(buffInstance != null) { // Already has buff
                // Refresh turns
                buffInstance.setTurns(turns);

                // Add stacks
                int stacksOld = buffInstance.getStacks();
                buffInstance.setStacks(Math.min(buffInstance.getBuff().getMaxStacks(), stacksOld + stacks));

                // Apply once for each newly added stack
                int stacksNew = buffInstance.getStacks() - stacksOld;
                for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                    for (int i = 1; i <= stacksNew; i++) {
                        buffEffect.onGive(cmb);
                    }
                }
            } else { // Doesn't have buff
                cmb.addBuffInstance(new BuffInstance(buff, turns, stacks));
                buffInstance = buffInstances.get(buffInstances.size() - 1);

                // Apply once for each stack
                for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                    for (int i = 1; i <= buffInstance.getStacks(); i++) {
                        buffEffect.onGive(cmb);
                    }
                }
            }
        }

        // Returns success even if nothing happened because failure to apply a buff as a secondary effect shouldn't fail the skill
        return Result.SUCCESS;
    }
}
