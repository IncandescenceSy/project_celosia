package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.celosia.sys.battle.BattleLib.getStageBuffType;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeStage implements SkillEffect {

    private final StageType stageType; // Stage to change
    private final int turns; // How many turns to change it for
    private final int stacks; // How much to change it by
    private final boolean isInstant;
    private final boolean mainTargetOnly;

    public ChangeStage(StageType stageType, int turns, int stacks, boolean isInstant, boolean mainTargetOnly) {
        this.stageType = stageType;
        this.turns = turns;
        this.stacks = stacks;
        this.isInstant = isInstant;
        this.mainTargetOnly = mainTargetOnly;
    }

    public ChangeStage(StageType stageType, int turns, int stacks, boolean isInstant) {
        this(stageType, turns, stacks, isInstant, false);
    }

    public ChangeStage(StageType stageType, int change, int turns) {
        this(stageType, change, turns, true, false);
    }

    @Override
    public Result apply(Unit self, Unit target, boolean isMainTarget, ResultType resultPrev) {
        if(!mainTargetOnly || isMainTarget) {
            List<String> msg = new ArrayList<>();
            String str = "";

            // Apply self's durationMod
            // Sometimes this is wrong about the BuffType, but it shouldn't really matter since it's only applied if it's correct
            // Unless some Passive has a weird interaction with it. Just keep it in mind
            // todo fix just in case
            int turnsMod = turns + self.getDurationModBuffTypeDealt(getStageBuffType(target.getStage(stageType) + stacks)) +
                target.getDurationModBuffTypeTaken(getStageBuffType(target.getStage(stageType) + stacks));

            // Notify Passives onGiveBuff
            for(Passive passive : self.getUnitType().getPassives()) {
                for(PassiveEffect passiveEffect : passive.getPassiveEffects()) {
                    String[] effectMsgs = passiveEffect.onChangeStage(self, target, stageType, turnsMod, stacks);
                    for(String effectMsg : effectMsgs) if(!Objects.equals(effectMsg, "")) msg.add(effectMsg);
                }
            }

            int stageOld = target.getStage(stageType);
            int stageNew = Math.clamp(stageOld + stacks, -5, 5);

            if (stageNew != stageOld) {
                target.setStage(stageType, stageNew);
                str = target.getUnitType().getName() + "'s " + stageType.getName() + " " + lang.get("stage") + " " + stageOld + " -> " + stageNew;
            }

            if ((stageOld >= 0 && stacks >= 0) || (stageOld <= 0 && stacks <= 0)) { // Refresh turns
                int turnsOld = target.getStageTurns(stageType);
                if (turnsMod > turnsOld) {
                    target.setStageTurns(stageType, turnsMod);
                    if (stageNew != stageOld)
                        msg.add(str + ", " + lang.get("turns") + " " + turnsOld + " -> " + turnsMod);
                    else
                        msg.add(target.getUnitType().getName() + "'s " + stageType.getName() + " " + lang.get("stage") + " " + lang.get("turns") + " " + turnsOld + " -> " + turnsMod);
                }
            } else msg.add(str);

            return new Result(ResultType.SUCCESS, msg);
        } else return new Result(ResultType.SUCCESS, "");
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }
}
