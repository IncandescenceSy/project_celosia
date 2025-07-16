package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

import java.util.List;

public class GiveBuff implements SkillEffect {

    private final Buff buff;
    private final int turns;
    private final int stacks;

    public GiveBuff(Buff buff, int turns, int stacks) {
        this.buff = buff;
        this.turns = turns;
        this.stacks = stacks;
    }

    public GiveBuff(Buff buff, int turns) {
        this(buff, turns, 1);
    }

    public GiveBuff(Buff buff) {
        this(buff, 1, 1);
    }

    @Override
    public boolean apply(Combatant self, Combatant target) {
        List<BuffInstance> buffInstances = target.getBuffInstances();
        boolean hasBuff = false;
        for(BuffInstance buffInstance : buffInstances) {
            if(buffInstance.getBuff() == buff) { // Already has buff
                hasBuff = true;
                buffInstance.setTurns(turns); // Refresh turns

                // Add stacks
                int stacksOld = buffInstance.getStacks();
                buffInstance.setStacks(Math.min(buffInstance.getBuff().getMaxStacks(), stacksOld + stacks));

                // Apply once for each newly added stack
                int stacksNew = buffInstance.getStacks() - stacksOld;
                for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                    for(int i = 1; i <= stacksNew; i++) {
                        buffEffect.onGive(target);
                    }
                }

                break;
            }
        }
        if(!hasBuff) {
            target.addBuffInstance(new BuffInstance(buff, turns, stacks));
            BuffInstance buffInstance = buffInstances.get(buffInstances.size() - 1);

            // Apply once for each stack
            for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                for(int i = 1; i <= buffInstance.getStacks(); i++) {
                    buffEffect.onGive(target);
                }
            }
        }

        return true;
    }
}
