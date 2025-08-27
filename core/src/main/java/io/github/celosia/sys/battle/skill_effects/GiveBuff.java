package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

import java.util.ArrayList;
import java.util.List;

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

    public GiveBuff(Builder builder) {
        buff = builder.buff;
        turns = builder.turns;
        stacks = builder.stacks;
        minResult = builder.minResult;
        isInstant = builder.isInstant;
        giveToSelf = builder.giveToSelf;
        mainTargetOnly = builder.mainTargetOnly;
    }

    public static class Builder {
        private final Buff buff;
        private final int turns;
        private int stacks = 1;
        private ResultType minResult = ResultType.SUCCESS;
        private boolean giveToSelf = false;
        private boolean mainTargetOnly = false;
        private boolean isInstant = true;

        public Builder(Buff buff, int turns) {
            this.buff = buff;
            this.turns = turns;
        }

        public Builder stacks(int stacks) {
            this.stacks = stacks;
            return this;
        }

        public Builder minResult(ResultType minResult) {
            this.minResult = minResult;
            return this;
        }

        public Builder giveToSelf() {
            this.giveToSelf = true;
            return this;
        }

        public Builder mainTargetOnly() {
            this.mainTargetOnly = true;
            return this;
        }

        public Builder notInstant() {
            this.isInstant = false;
            return this;
        }

        public GiveBuff build() {
            return new GiveBuff(this);
        }
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
                    for(String effectMsg : effectMsgs) if(!effectMsg.isEmpty()) msg.add(effectMsg);
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
                    for (String effectMsg : effectMsgs) if (!effectMsg.isEmpty()) msg.add(effectMsg);
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
