package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

public class ChangeSp implements SkillEffect {

    private final int change;
    private final boolean isInstant;
    private final boolean giveToSelf;
    private final boolean mainTargetOnly;

    public ChangeSp(Builder builder) {
        change = builder.change;
        isInstant = builder.isInstant;
        giveToSelf = builder.giveToSelf;
        mainTargetOnly = builder.mainTargetOnly;
    }

    public static class Builder {
        private final int change;
        private boolean isInstant = true;
        private boolean giveToSelf = false;
        private boolean mainTargetOnly = false;

        public Builder(int change) {
            this.change = change;
        }

        public Builder notInstant() {
            this.isInstant = false;
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

        public ChangeSp build() {
            return new ChangeSp(this);
        }
    }

    @Override
    public Result apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
        if(!mainTargetOnly || isMainTarget) {
            return new Result(ResultType.SUCCESS, Calcs.changeSp((giveToSelf) ? self : target, change));
        } else return new Result(ResultType.SUCCESS, "");
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }
}
