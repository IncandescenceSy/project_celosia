package io.github.celosia.sys.battle.skill_effects;

import com.badlogic.gdx.math.MathUtils;
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
        target.setSp(MathUtils.clamp(target.getSp() + change, 0, 100));
        return Result.SUCCESS;
    }
}
