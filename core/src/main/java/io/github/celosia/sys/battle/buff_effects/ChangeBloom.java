package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Calcs;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.battle.BattleControllerLib.battle;

public class ChangeBloom implements BuffEffect {

    private final int change;
    private final boolean isImmediate;

    public ChangeBloom(int change, boolean isImmediate) {
        this.change = change;
        this.isImmediate = isImmediate;
    }

    public ChangeBloom(int change) {
        this(change, false);
    }

    @Override
    public void onGive(Unit self, int stacks) {
        if (isImmediate) {
            appendToLog(Calcs.changeBloom(battle.getTeamAtPos(self.getPos()), self.getSide(), change));
        }
    }

    @Override
    public String[] onTurnEnd(Unit self, int stacks) {
        if (isImmediate) {
            return new String[] { Calcs.changeBloom(battle.getTeamAtPos(self.getPos()), self.getSide(), change) };
        }

        return new String[] { "" };
    }
}
