package io.github.celosia.sys.battle.skill_effects;

import io.github.celosia.sys.battle.*;

import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeStage implements SkillEffect {

    private final StageType stageType; // Stage to change
    private final int change; // How much to change it by
    private final int turns; // How many turns to change it for
    private final boolean isInstant;
    private final boolean mainTargetOnly;

    public ChangeStage(StageType stageType, int change, int turns, boolean isInstant, boolean mainTargetOnly) {
        this.stageType = stageType;
        this.change = change;
        this.turns = turns;
        this.isInstant = isInstant;
        this.mainTargetOnly = mainTargetOnly;
    }

    public ChangeStage(StageType stageType, int change, int turns, boolean isInstant) {
        this(stageType, change, turns, isInstant, false);
    }

    public ChangeStage(StageType stageType, int change, int turns) {
        this(stageType, change, turns, true, false);
    }

    @Override
    public Result apply(Combatant self, Combatant target, boolean isMainTarget, ResultType resultPrev) {
        if(!mainTargetOnly || isMainTarget) {
            int stageOld = target.getStage(stageType);
            int stageNew = Math.clamp(stageOld + change, -5, 5);

            String[] msg = new String[2];
            if (stageNew != stageOld) {
                target.setStage(stageType, stageNew);
                // todo is the (+/-X) at the end needed?
                msg[0] = target.getCmbType().getName() + "'s " + stageType.getName() + " " + lang.get("stage") + " " + stageOld + " -> " + stageNew; /*+ ((change > 0) ? " (+" : " (") + (stageNew - stageOld) + ")"*/
            } else
                msg[0] = ""; //target.getCmbType().getName() + "'s " + stageType.getName() + " " + lang.get("stage") + " " + ((stageNew == 5) ? lang.get("log.max") : lang.get("log.min")) + "\n";

            //String durationUnchanged = target.getCmbType().getName() + "'s " + stageType.getName() + " " + lang.get("stage") + " " + lang.get("log.duration_unchanged");

            if ((stageOld >= 0 && change >= 0) || (stageOld <= 0 && change <= 0)) { // Refresh turns
                int turnsOld = target.getStageTurns(stageType);
                if (turns > turnsOld) {
                    target.setStageTurns(stageType, turns);
                    // todo (+/-X) at the end?
                    if (stageNew != stageOld)
                        msg[0] += ", " + lang.get("turns") + " " + turnsOld + " -> " + turns + "\n";
                    else
                        msg[1] = target.getCmbType().getName() + "'s " + stageType.getName() + " " + lang.get("stage") + " " + lang.get("turns") + " " + turnsOld + " -> " + turns + "\n";
                } else {
                    if (stageNew != stageOld) msg[0] += "\n";
                    msg[1] = ""; //durationUnchanged;
                }
            } else {
                if (stageNew != stageOld) msg[0] += "\n";
                msg[1] = ""; //durationUnchanged;
            }

            return new Result(ResultType.SUCCESS, msg);
        } else return new Result(ResultType.SUCCESS, "");
    }

    @Override
    public boolean isInstant() {
        return isInstant;
    }
}
