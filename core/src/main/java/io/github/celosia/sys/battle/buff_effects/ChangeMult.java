package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;
import io.github.celosia.sys.battle.Mult;

public class ChangeMult implements BuffEffect {

    private final Mult mult;
    private final int change; // Amount to add in %

    public ChangeMult(Mult mult, int change) {
        this.mult = mult;
        this.change = change;
    }

    @Override
    public String[] onGive(Unit self, int stacks) {
        int multOld = self.getMult(mult);
        int changeFull = change * stacks;
        self.setMult(mult, multOld + changeFull);
        double changeDisplay = Math.max(multOld + changeFull, 10) - Math.max(multOld, 10);
        return new String[]{self.getUnitType().getName() + "'s " + mult.getName() + " " + Math.max(multOld, 10) + "% -> " + Math.max(multOld + changeFull, 10) + ((changeFull > 0) ? "% (+" : "% (") + changeDisplay + "%)"};
    }

    @Override
    public String[] onRemove(Unit self, int stacks) {
        int multOld = self.getMult(mult);
        int changeFull = change * stacks;
        self.setMult(mult, multOld - changeFull);
        double changeDisplay = Math.max(multOld + changeFull, 10) - Math.max(multOld, 10);
        return new String[]{self.getUnitType().getName() + "'s " + mult.getName() + " " + Math.max(multOld, 10) + "% -> " + (Math.max(multOld - changeFull, 10)) + ((changeFull < 0) ? "% (+" : "% (") + (changeDisplay * -1) + "%)"};
    }
}
