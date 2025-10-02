package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Buff;
import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.BuffInstance;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.Unit;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_BUFF;
import static io.github.celosia.sys.util.TextLib.C_NUM;
import static io.github.celosia.sys.util.TextLib.formatName;

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
            giveToSelf = true;
            return this;
        }

        public Builder mainTargetOnly() {
            mainTargetOnly = true;
            return this;
        }

        public Builder notInstant() {
            isInstant = false;
            return this;
        }

        public GiveBuff build() {
            return new GiveBuff(this);
        }
    }

    @Override
    public ResultType apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
        // Most attacks don't apply buffs if the previous hit was blocked by Shield or
        // immunity
        // Returns success even if nothing happened because failure to apply a buff as a
        // secondary effect shouldn't fail the entire skill
        if (resultPrev.ordinal() < minResult.ordinal() || (mainTargetOnly && !isMainTarget)) {
            return ResultType.SUCCESS;
        }

        List<String> msg = new ArrayList<>();

        Unit unit = (giveToSelf) ? self : target;

        int turnsMod = turns + self.getDurationModBuffTypeDealt(buff.getBuffType()) +
                unit.getDurationModBuffTypeTaken(buff.getBuffType());

        int stacksMod = stacks + self.getStacksModBuffTypeDealt(buff.getBuffType()) +
                unit.getStacksModBuffTypeTaken(buff.getBuffType());

        self.onGiveBuff(target, buff, turnsMod, stacksMod);

        List<BuffInstance> buffInstances = unit.getBuffInstances();
        BuffInstance buffInstance = unit.getBuffInstance(buff);

        String buffName = buff.getIcon() + C_BUFF + buff.getName();

        // Already has buff
        if (buffInstance != null) {
            String str = "";

            int stacksOld = buffInstance.getStacks();
            int stacksNew = Math.min(buffInstance.getBuff().getMaxStacks(), stacksOld + stacksMod);
            if (stacksNew != stacksOld) {
                buffInstance.setStacks(stacksNew);
                str = lang.format("log.give_buff.stacks", formatName(unit.getUnitType().getName(), unit.getPos()),
                        buffName, C_NUM + stacksOld, C_NUM + stacksNew);
            }

            int turnsOld = buffInstance.getTurns();
            if (turnsMod > turnsOld) {
                buffInstance.setTurns(turnsMod);

                if (stacksNew != stacksOld) {
                    msg.add(str + lang.format("log.turns.nameless", C_NUM + turnsOld, C_NUM + turnsMod));
                } else {
                    msg.add(lang.format("log.give_buff.turns", formatName(unit.getUnitType().getName(), unit.getPos()),
                            buffName, C_NUM + turnsOld, C_NUM + turnsMod));
                }
            }

            appendToLog(msg);

            int stacksAdded = stacksNew - stacksOld;
            if (stacksAdded > 0) {
                for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects()) {
                    buffEffect.onGive(unit, stacksAdded);
                }
            }

        } else {
            msg.add(lang.format("log.give_buff.gain", formatName(unit.getUnitType().getName(), unit.getPos(), false),
                    buffName, buff.getMaxStacks(), stacksMod, lang.format("log.stack_s", stacksMod), turnsMod));
            unit.addBuffInstance(new BuffInstance(buff, turnsMod, stacksMod));
            buffInstance = buffInstances.getLast();

            appendToLog(msg);

            BuffEffect[] buffEffects = buffInstance.getBuff().getBuffEffects();
            for (BuffEffect buffEffect : buffEffects) {
                buffEffect.onGive(unit, buffInstance.getStacks());
            }

        }

        return ResultType.SUCCESS;
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }
}
