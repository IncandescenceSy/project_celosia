package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

import java.util.List;
import java.util.Objects;

import static io.github.celosia.sys.settings.Lang.lang;

public class GiveBuff implements SkillEffect {

    private final Buff buff;
    private final int turns;
    private final int stacks;
    private final ResultType minResult;
    private final boolean giveToSelf;
    private final boolean mainTargetOnly;
    private final boolean isInstant;

    public GiveBuff(Buff buff, int turns, int stacks, ResultType minResult, boolean isInstant, boolean giveToSelf, boolean mainTargetOnly) {
        this.buff = buff;
        this.turns = turns;
        this.stacks = stacks;
        this.minResult = minResult;
        this.isInstant = isInstant;
        this.giveToSelf = giveToSelf;
        this.mainTargetOnly = mainTargetOnly;
    }

    public GiveBuff(Buff buff, int turns, boolean isInstant, boolean giveToSelf, boolean mainTargetOnly) {
        this(buff, turns, 1, ResultType.SUCCESS, true, giveToSelf, mainTargetOnly);
    }

    public GiveBuff(Buff buff, int turns, boolean isInstant, boolean giveToSelf) {
        this(buff, turns, 1, ResultType.SUCCESS, isInstant, giveToSelf, false);
    }

    public GiveBuff(Buff buff, int turns, int stacks) {
        this(buff, turns, stacks, ResultType.SUCCESS, true, false, false);
    }

    public GiveBuff(Buff buff, int turns, boolean isInstant) {
        this(buff, turns, 1, ResultType.SUCCESS, isInstant, false, false);
    }

    public GiveBuff(Buff buff, int turns) {
        this(buff, turns, 1, ResultType.SUCCESS, true, false, false);
    }

    public GiveBuff(Buff buff, boolean isInstant) {
        this(buff, 1, 1, ResultType.SUCCESS, isInstant, false, false);
    }

    public GiveBuff(Buff buff) {
        this(buff, 1, 1, ResultType.SUCCESS, true, false, false);
    }

    @Override
    public Result apply(Combatant self, Combatant target, boolean isMainTarget, ResultType resultPrev) {
        // Most attacks don't apply buffs if the previous hit was blocked by Shield or immunity
        if (resultPrev.ordinal() >= minResult.ordinal() && (!mainTargetOnly || isMainTarget)) {
            Combatant cmb = (giveToSelf) ? self : target;
            List<BuffInstance> buffInstances = cmb.getBuffInstances();
            BuffInstance buffInstance = cmb.findBuff(buff);

            if(buffInstance != null) { // Already has buff
                StringBuilder msg = new StringBuilder();

                // Refresh turns
                int turnsOld = buffInstance.getTurns();
                if(turns > turnsOld) {
                    buffInstance.setTurns(turns);
                    msg.append(cmb.getCmbType().getName()).append("'s ").append(buff.getName()).append(" ").append(lang.format("turn_s", turns)).append(" ").append(turnsOld).append(" -> ").append(turns);
                } //else msg = ""; //target.getCmbType().getName() + "'s " + buff.getName() + " " + lang.get("log.duration_unchanged");

                // Add stacks
                int stacksOld = buffInstance.getStacks();
                int stacksNew = Math.min(buffInstance.getBuff().getMaxStacks(), stacksOld + stacks);
                if(stacksNew != stacksOld) {
                    buffInstance.setStacks(stacksNew);
                    if(turns > turnsOld) msg.append(", ").append(lang.format("stack_s", stacksNew)).append(" ").append(stacksOld).append(" -> ").append(stacksNew).append("\n");
                    else msg.append(cmb.getCmbType().getName()).append("'s ").append(buff.getName()).append(" ").append(lang.get("stacks")).append(" ").append(stacksOld).append(" -> ").append(stacksNew).append("\n");
                } else if(turns > turnsOld) msg.append("\n");

                // Apply once for each newly added stack
                int stacksAdded = stacksNew - stacksOld;
                for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                    for (int i = 1; i <= stacksAdded; i++) {
                        String onGive = buffEffect.onGive(cmb);
                        if(!Objects.equals(onGive, "")) msg.append(onGive).append("\n");
                    }
                }

                return new Result(ResultType.SUCCESS, msg.toString());
            } else { // Doesn't have buff
                String[] msg = new String[2];
                msg[0] = cmb.getCmbType().getName() + " " + lang.get("log.gains") + " " + buff.getName() + " " + lang.get("log.with") + " " + ((buff.getMaxStacks() > 1) ? (stacks + " " + lang.format("stack_s", stacks) + " " + lang.get("log.and")) + " " : "") + turns + " " + lang.format("turn_s", turns) + "\n";
                msg[1] = "";
                cmb.addBuffInstance(new BuffInstance(buff, turns, stacks));
                buffInstance = buffInstances.get(buffInstances.size() - 1);

                // Apply once for each stack
                BuffEffect[] buffEffects = buffInstance.getBuff().getBuffEffects();
                for (int i = 0; i < buffEffects.length; i++) {
                    for (int j = 0; j < buffInstance.getStacks(); j++) {
                        String onGive = buffEffects[i].onGive(cmb);
                        if(!Objects.equals(onGive, "")) msg[1] += onGive + "\n";
                    }
                }
                return new Result(ResultType.SUCCESS, msg);
            }
        }

        // Returns success even if nothing happened because failure to apply a buff as a secondary effect shouldn't fail the entire skill
        return new Result(ResultType.SUCCESS, "");
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }
}
