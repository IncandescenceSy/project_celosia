package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeInfiniteSp implements BuffEffect {

    private final int change; // Amount to add

    public ChangeInfiniteSp(int change) {
        this.change = change;
    }

    @Override
    public String[] onGive(Unit self, int stacks) {
        int infiniteSpOld = self.getInfiniteSp();
        int infiniteSpNew = infiniteSpOld + (change * stacks);
        self.setInfiniteSp(infiniteSpNew);
        return new String[]{(infiniteSpNew > 0 && self.getShield() == 0 && self.getDefend() == 0) ? self.getUnitType().getName() + " " + lang.get("log.now_has") + " " + lang.get("log.infinite_sp") : ""};
    }

    @Override
    public String[] onRemove(Unit self, int stacks) {
        int infiniteSpOld = self.getInfiniteSp();
        int infiniteSpNew = infiniteSpOld - (change * stacks);
        self.setInfiniteSp(infiniteSpNew);
        return new String[]{(infiniteSpNew <= 0 && self.getShield() == 0 && self.getDefend() == 0) ? self.getUnitType().getName() + " " + lang.get("log.no_longer_has") + " " + lang.get("log.infinite_sp") : ""};
    }
}
