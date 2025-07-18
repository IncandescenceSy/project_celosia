package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.SkillEffect;
import io.github.celosia.sys.battle.StageType;

public class ChangeStage implements SkillEffect {

    private final StageType stageType; // Stage to change
    private final int change; // How much to change it by
    private final int turns; // How many turns to change it for

    public ChangeStage(StageType stageType, int change, int turns) {
        this.stageType = stageType;
        this.change = change;
        this.turns = turns;
    }

    @Override
    public Result apply(Combatant self, Combatant target, Result resultPrev) {
        int stageOld = target.getStage(stageType);
        target.setStage(stageType, stageOld + change);
        if ((stageOld >= 0 && change >= 0) || (stageOld <= 0 && change <= 0)) { // Refresh turns
            target.setStageTurns(stageType, Math.max(target.getStageTurns(stageType), turns));
        }

        return Result.SUCCESS;
    }
}
