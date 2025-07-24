package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Mult;

public class ChangeMult implements BuffEffect {

    private Mult mult;
    private float change; // Amount to add

    public ChangeMult(Mult mult, float change) {
        this.mult = mult;
        this.change = change;
    }

    @Override
    public String onGive(Combatant self) {
        float multOld = self.getMult(mult);
        self.setMult(mult, multOld + change);
        float changeDisplay = Math.round((Math.max(multOld + change, 0.1f) - Math.max(multOld, 0.1f)) * 100f) / 100f;
        return self.getCmbType().getName() + "'s " + mult.getName() + " " + Math.max(multOld, 0.1f) + " -> " + Math.max(multOld + change, 0.1f) + ((change > 0) ? " (+" : " (") + changeDisplay + ")";
    }

    @Override
    public String onRemove(Combatant self) {
        float multOld = self.getMult(mult);
        self.setMult(mult, multOld - change);
        float changeDisplay = Math.round((Math.max(multOld + change, 0.1f) - Math.max(multOld, 0.1f)) * 100f) / 100f;
        return self.getCmbType().getName() + "'s " + mult.getName() + " " + Math.max(multOld, 0.1f) + " -> " + Math.max(multOld - change, 0.1f) + ((change < 0) ? " (+" : " (") + (changeDisplay * -1) + ")";
    }

    @Override
    public String onUseSkill(Combatant self, Combatant target) {
        return "";
    }

    @Override
    public String onTakeDamage(Combatant self) {
        return "";
    }

    @Override
    public String onTurnEnd(Combatant self) {
        return "";
    }
}
