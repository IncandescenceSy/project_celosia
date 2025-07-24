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
        /*if(Math.max(multOld + change, 0.1f) == Math.max(multOld, 0.1f)) { // No change due to minimum
            return self.getCmbType().getName() + "'s " + mult.getName() + " " + lang.get("log.min");
        } else if(multOld + change > 0.1f) { // Above minimum
            return self.getCmbType().getName() + "'s " + mult.getName() + " " + multOld + " -> " + (multOld + change) + ((change > 0) ? " (+" : " (-") + change + ")";
        } else if(multOld > 0.1f && multOld + change < 0.1f) { // Above minimum and decreasing to below it
            return self.getCmbType().getName() + "'s " + mult.getName() + " " + multOld + " -> " + (multOld + change) + ((change > 0) ? " (+" : " (-") + change + ")\n" + self.getCmbType().getName() + "'s " + mult.getName() + " " + lang.get("log.min");
        }*/
        // todo more complex logic to not let the player know it's decreasing below 0.1
        return self.getCmbType().getName() + "'s " + mult.getName() + " " + Math.max(multOld, 0.1f) + " -> " + Math.max(multOld + change, 0.1f) + ((change > 0) ? " (+" : " (") + change + ")";
    }

    @Override
    public String onRemove(Combatant self) {
        float multOld = self.getMult(mult);
        self.setMult(mult, multOld - change);
        return self.getCmbType().getName() + "'s " + mult.getName() + " " + Math.max(multOld, 0.1f) + " -> " + Math.max(multOld - change, 0.1f) + ((change < 0) ? " (+" : " (") + (change * -1) + ")";
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
