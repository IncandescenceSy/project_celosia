package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;

import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeDefend implements BuffEffect {

    private final float change; // Defend to add in % of max HP

    public ChangeDefend(float change) {
        this.change = change;
    }

    @Override
    public String onGive(Combatant self) {
        int hpMax = self.getStatsDefault().getHp();
        // Add defend (barrier + defend cannot exceed max HP)
        int defendOld = self.getDefend();
        int defendNew = (self.getBarrier() + defendOld + (change * hpMax) > hpMax) ? hpMax - self.getBarrier() : (int) (change * hpMax);
        self.setDefend(defendNew);
        return self.getCmbType().getName() + "'s " + lang.get("barrier") + " " + String.format("%,d", (self.getBarrier() + defendOld)) + " -> " + String.format("%,d", (self.getBarrier() + defendNew)) + "/" + String.format("%,d", hpMax) + " (+" + String.format("%,d", ((self.getBarrier() + defendNew) - (self.getBarrier() + defendOld))) + ")";
    }

    @Override
    public String onRemove(Combatant self) {
        int defendOld = self.getDefend();
        self.setDefend(0);
        return self.getCmbType().getName() + "'s " + lang.get("barrier") + " " + String.format("%,d", (self.getBarrier() + defendOld)) + " -> " + String.format("%,d", (self.getBarrier() + 0)) + "/" + String.format("%,d", self.getStatsDefault().getHp()) + " (" + String.format("%,d", ((self.getBarrier() + 0) - (self.getBarrier() + defendOld))) + ")";
    }

    @Override
    public String onUseSkill(Combatant self, Combatant target) {
        return "";
    }

    // Todo implement
    @Override
    public String onTakeDamage(Combatant self) {
        return "";
    }

    @Override
    public String onTurnEnd(Combatant self) {
        return "";
    }
}
