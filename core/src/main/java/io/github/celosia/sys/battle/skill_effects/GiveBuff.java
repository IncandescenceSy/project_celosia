package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.celosia.sys.battle.BuffEffectLib.notifyOnGiveBuff;
import static io.github.celosia.sys.menu.TextLib.formatPossessive;
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
    public Result apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
        // Most attacks don't apply buffs if the previous hit was blocked by Shield or immunity
        if (resultPrev.ordinal() >= minResult.ordinal() && (!mainTargetOnly || isMainTarget)) {
            List<String> msg = new ArrayList<>();

            // Apply durationMod
            int turnsMod = turns + self.getDurationModBuffTypeDealt(buff.getBuffType()) + target.getDurationModBuffTypeTaken(buff.getBuffType());

            notifyOnGiveBuff(self, target, buff, turnsMod, stacks);

            Unit unit = (giveToSelf) ? self : target;
            List<BuffInstance> buffInstances = unit.getBuffInstances();
            BuffInstance buffInstance = unit.findBuff(buff);

            if(buffInstance != null) { // Already has buff
                String str = "";

                // Refresh turns
                int turnsOld = buffInstance.getTurns();
                if(turnsMod > turnsOld) {
                    buffInstance.setTurns(turnsMod);
                    str = formatPossessive(unit.getUnitType().getName()) + " " + buff.getName() + " " + lang.format("turn_s", turnsMod) + " " + turnsOld + " -> " + turnsMod;
                }

                // Add stacks
                int stacksOld = buffInstance.getStacks();
                int stacksNew = Math.min(buffInstance.getBuff().getMaxStacks(), stacksOld + stacks);
                if(stacksNew != stacksOld) {
                    buffInstance.setStacks(stacksNew);
                    if(turnsMod > turnsOld) msg.add(str + ", " + lang.format("stack_s", stacksNew) + " " + stacksOld + " -> " + stacksNew);
                    else msg.add(formatPossessive(unit.getUnitType().getName()) + " " + buff.getName() + " " + lang.get("stacks") + " " + stacksOld + " -> " + stacksNew);
                }

                // Apply once for each newly added stack
                int stacksAdded = stacksNew - stacksOld;
                for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                    String[] effectMsgs = buffEffect.onGive(unit, stacksAdded);
                    for(String effectMsg : effectMsgs) if(!Objects.equals(effectMsg, "")) msg.add(effectMsg);
                }

                return new Result(ResultType.SUCCESS, msg);
            } else { // Doesn't have buff
                msg.add(unit.getUnitType().getName() + " " + lang.get("log.gains") + " " + buff.getName() + " " + lang.get("log.with") + " " + ((buff.getMaxStacks() > 1) ?
                    (stacks + " " + lang.format("stack_s", stacks) + " " + lang.get("log.and")) + " " : "") + turnsMod + " " + lang.format("turn_s", turnsMod));
                unit.addBuffInstance(new BuffInstance(buff, turnsMod, stacks));
                buffInstance = buffInstances.getLast();

                // Apply
                BuffEffect[] buffEffects = buffInstance.getBuff().getBuffEffects();
                for (BuffEffect buffEffect : buffEffects) {
                    String[] effectMsgs = buffEffect.onGive(unit, buffInstance.getStacks());
                    for (String effectMsg : effectMsgs) if (!Objects.equals(effectMsg, "")) msg.add(effectMsg);
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
