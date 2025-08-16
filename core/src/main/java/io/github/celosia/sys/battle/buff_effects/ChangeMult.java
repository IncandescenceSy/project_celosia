package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Mult;

public class ChangeMult implements BuffEffect {

    private final Mult mult;
    private final int change; // Amount to add in %

    public ChangeMult(Mult mult, int change) {
        this.mult = mult;
        this.change = change;
    }

    @Override
    public String onGive(Combatant self) {
        int multOld = self.getMult(mult);
        self.setMult(mult, multOld + change);
        double changeDisplay = Math.max(multOld + change, 10) - Math.max(multOld, 10);
        return self.getCmbType().getName() + "'s " + mult.getName() + " " + Math.max(multOld, 10) + "% -> " + Math.max(multOld + change, 10) + ((change > 0) ? "% (+" : "% (") + changeDisplay + "%)";
    }

    @Override
    public String onRemove(Combatant self) {
        int multOld = self.getMult(mult);
        self.setMult(mult, multOld - change);
        double changeDisplay = Math.max(multOld + change, 10) - Math.max(multOld, 10);
        return self.getCmbType().getName() + "'s " + mult.getName() + " " + Math.max(multOld, 10) + "% -> " + (Math.max(multOld - change, 10)) + ((change < 0) ? "% (+" : "% (") + (changeDisplay * -1) + "%)";
    }
}
