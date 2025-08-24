package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Unit;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;

import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeSP implements SkillEffect {

    private final int change;
    private final boolean isInstant;
    private final boolean giveToSelf;
    private final boolean mainTargetOnly;

    public ChangeSP(int change, boolean isInstant, boolean giveToSelf, boolean mainTargetOnly) {
        this.change = change;
        this.isInstant = isInstant;
        this.giveToSelf = giveToSelf;
        this.mainTargetOnly = mainTargetOnly;
    }

    public ChangeSP(int change, boolean isInstant, boolean giveToSelf) {
        this(change, isInstant, giveToSelf, false);
    }

    public ChangeSP(int change, boolean giveToSelf) {
        this(change, true, giveToSelf, false);
    }

    public ChangeSP(int change) {
        this(change, true, false, false);
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
                msg = unit.getUnitType().getName() + "'s " + lang.get("sp") + " " + String.format("%,d", spOld) + " -> " + String.format("%,d", spNew);
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
