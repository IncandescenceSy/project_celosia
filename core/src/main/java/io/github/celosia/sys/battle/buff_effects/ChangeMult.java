package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Mult;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.menu.TextLib.*;

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
        int changeDisplay = Math.max(multOld + changeFull, 10) - Math.max(multOld, 10);
        return new String[]{formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + mult.getName() + " " + getMultColor(multOld, mult) + Math.max(multOld, 10) + "%[WHITE] → " +
            getMultColor(multOld + changeFull, mult) + (Math.max(multOld + changeFull, 10)) + "% [WHITE](" + getMultChangeColor(changeFull, mult) + ((changeFull < 0) ? "+" : "") + (changeDisplay * -1) + "%[WHITE])"};
    }

    @Override
    public String[] onRemove(Unit self, int stacks) {
        int multOld = self.getMult(mult);
        int changeFull = change * stacks;
        self.setMult(mult, multOld - changeFull);
        int changeDisplay = Math.max(multOld + changeFull, 10) - Math.max(multOld, 10);
        return new String[]{formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + mult.getName() + " " + getMultColor(multOld, mult) + Math.max(multOld, 10) + "%[WHITE] → " +
            getMultColor(multOld - changeFull, mult) + (Math.max(multOld - changeFull, 10)) + "% [WHITE](" + getMultChangeColor(changeFull, mult) + ((changeFull < 0) ? "+" : "") + (changeDisplay * -1) + "%[WHITE])"};
    }
}
