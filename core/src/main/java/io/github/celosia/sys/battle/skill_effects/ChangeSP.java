package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Unit;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;

import static io.github.celosia.sys.menu.TextLib.formatPossessive;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeSP implements SkillEffect {

    private final int change;
    private final boolean isInstant;
    private final boolean giveToSelf;
    private final boolean mainTargetOnly;

    public ChangeSP(Builder builder) {
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

        public ChangeSP build() {
            return new ChangeSP(this);
        }
    }

    @Override
    public Result apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
        if(!mainTargetOnly || isMainTarget) {
            int spOld = target.getSp();
            int spNew = Math.clamp(spOld + (int) (change * (Math.max(self.getMultSpGain(), 10) / 100d)), 0, 1000);
            String msg;

            if (spNew != spOld) {
                Unit unit = (giveToSelf) ? self : target;
                unit.setSp(spNew);
                msg = formatPossessive(unit.getUnitType().getName()) + " " + lang.get("sp") + " " + String.format("%,d", spOld) + " -> " + String.format("%,d", spNew);
            } else
                msg = "";

            return new Result(ResultType.SUCCESS, msg);
        } else return new Result(ResultType.SUCCESS, "");
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }
}
