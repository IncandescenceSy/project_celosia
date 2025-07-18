package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

import java.util.List;

public class GiveBuff implements SkillEffect {

    private final Buff buff;
    private final int turns;
    private final int stacks;
    private final Result minResult;

    public GiveBuff(Buff buff, int turns, int stacks, Result minResult) {
        this.buff = buff;
        this.turns = turns;
        this.stacks = stacks;
        this.minResult = minResult;
    }

    public GiveBuff(Buff buff, int turns, int stacks) {
        this(buff, turns, stacks, Result.SUCCESS);
    }

    public GiveBuff(Buff buff, int turns) {
        this(buff, turns, 1, Result.SUCCESS);
    }

    public GiveBuff(Buff buff) {
        this(buff, 1, 1, Result.SUCCESS);
    }

    @Override
    public Result apply(Combatant self, Combatant target, Result resultPrev) {
        // Most attacks don't apply buffs if the previous hit was blocked by Barrier or immunity
        if(resultPrev.ordinal() >= minResult.ordinal()) {
            List<BuffInstance> buffInstances = target.getBuffInstances();
            boolean hasBuff = false;
            for (BuffInstance buffInstance : buffInstances) {
                if (buffInstance.getBuff() == buff) { // Already has buff
                    hasBuff = true;
                    buffInstance.setTurns(turns); // Refresh turns

                    // Add stacks
                    int stacksOld = buffInstance.getStacks();
                    buffInstance.setStacks(Math.min(buffInstance.getBuff().getMaxStacks(), stacksOld + stacks));

                    // Apply once for each newly added stack
                    int stacksNew = buffInstance.getStacks() - stacksOld;
                    for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                        for (int i = 1; i <= stacksNew; i++) {
                            buffEffect.onGive(target);
                        }
                    }

                    break;
                }
            }
            if (!hasBuff) {
                target.addBuffInstance(new BuffInstance(buff, turns, stacks));
                BuffInstance buffInstance = buffInstances.get(buffInstances.size() - 1);

                // Apply once for each stack
                for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                    for (int i = 1; i <= buffInstance.getStacks(); i++) {
                        buffEffect.onGive(target);
                    }
                }
            }
        }

        // Returns success even if nothing happened because failure to apply a buff as a secondary effect shouldn't fail the skill
        return Result.SUCCESS;
    }
}
