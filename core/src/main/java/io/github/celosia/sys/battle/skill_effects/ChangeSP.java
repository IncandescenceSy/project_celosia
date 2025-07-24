package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.ResultType;
import io.github.celosia.sys.battle.SkillEffect;

import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeSP implements SkillEffect {

    private final int change;

    public ChangeSP(int change) {
        this.change = change;
    }

    @Override
    public Result apply(Combatant self, Combatant target, ResultType resultPrev) {
        int spOld = target.getSp();
        int spNew = Math.clamp(spOld + change, 0, 100);
        String msg;

        if(spNew != spOld) {
            target.setSp(spNew);
            msg = target.getCmbType().getName() + "'s " + lang.get("sp") + " " + spOld + " -> " + spNew + "\n";
        } else msg = target.getCmbType().getName() + "'s " + lang.get("sp") + " " + ((spOld == 100) ? lang.get("log.max") : lang.get("log.min")) + "\n";

        return new Result(ResultType.SUCCESS, msg);
    }
}
