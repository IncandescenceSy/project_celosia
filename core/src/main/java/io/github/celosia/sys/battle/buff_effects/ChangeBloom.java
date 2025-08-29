package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Calcs;
import io.github.celosia.sys.battle.Team;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleController.battle;

// Todo heavily limit %-based damage on bosses
public class ChangeBloom implements BuffEffect {
    private final int change; // Amount to change SP by
    private final boolean isImmediate; // If true, happens onGive. If false, happens onTurnEnd

    public ChangeBloom(int change, boolean isImmediate) {
        this.change = change;
        this.isImmediate = isImmediate;
    }

    public ChangeBloom(int change) {
        this(change, false);
    }

    @Override
    public String[] onGive(Unit self, int stacks) {
        if(isImmediate) {
            Team team = battle.getTeamAtPos(self.getPos());
            return new String[] {Calcs.changeBloom(team, self.getSide(), change)};
        } else return new String[]{""};
    }

    @Override
    public String[] onTurnEnd(Unit self, int stacks) {
        if(!isImmediate) {
            Team team = battle.getTeamAtPos(self.getPos());
            return new String[] {Calcs.changeBloom(team, self.getSide(), change)};
        } else return new String[]{""};
    }
}
