package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.SkillEffect;

public class ChangeSP implements SkillEffect {

    private final int change;

    public ChangeSP(int change) {
        this.change = change;
    }

    @Override
    public Result apply(Combatant self, Combatant target, Result resultPrev) {
        target.setSp(target.getSp() + change);
        return Result.SUCCESS;
    }
}
