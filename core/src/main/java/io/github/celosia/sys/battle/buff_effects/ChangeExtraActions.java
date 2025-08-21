package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;

import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeExtraActions implements BuffEffect {

    private final int change; // Amount to add

    public ChangeExtraActions(int change) {
        this.change = change;
    }

    @Override
    public String[] onGive(Combatant self) {
        int exAOld = self.getExtraActions();
        int exANew = exAOld + change;
        self.setExtraActions(exANew);
        return new String[]{self.getCmbType().getName() + "'s " + lang.get("extra_actions") + " " + Math.max(exAOld, 0) + " -> " + Math.max(exANew, 0)};
    }

    @Override
    public String[] onRemove(Combatant self) {
        int exAOld = self.getExtraActions();
        int exANew = exAOld - change;
        self.setExtraActions(exANew);
        return new String[]{self.getCmbType().getName() + "'s " + lang.get("extra_actions") + " " + Math.max(exAOld, 0) + " -> " + Math.max(exANew, 0)};
    }
}
