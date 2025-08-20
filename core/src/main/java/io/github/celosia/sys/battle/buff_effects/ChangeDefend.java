package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;

import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeDefend implements BuffEffect {

    private final double change; // Defend to add in % of max HP

    public ChangeDefend(double change) {
        this.change = change;
    }

    @Override
    public String onGive(Combatant self) {
        int hpMax = self.getStatsDefault().getHp();
        // Add defend (shield + defend cannot exceed max HP)
        int defendOld = self.getDefend();
        int defendNew = (self.getShield() + defendOld + (change * hpMax) > hpMax) ? hpMax - self.getShield() : (int) (change * hpMax);
        self.setDefend(defendNew);
        return self.getCmbType().getName() + "'s " + lang.get("shield") + " " + String.format("%,d", (self.getShield() + defendOld)) + " -> " + String.format("%,d", (self.getShield() + defendNew)) +
            "/" + String.format("%,d", hpMax) + " (+" + String.format("%,d", ((self.getShield() + defendNew) - (self.getShield() + defendOld))) + ")" +
            ((self.getEffectBlock() <= 0 && self.getShield() == 0 && defendOld == 0) ? "\n" + self.getCmbType().getName() + " " + lang.get("log.is_now") + " " + lang.get("log.effect_block") : "");
    }

    @Override
    public String onRemove(Combatant self) {
        int defendOld = self.getDefend();
        self.setDefend(0);
        return self.getCmbType().getName() + "'s " + lang.get("shield") + " " + String.format("%,d", (self.getShield() + defendOld)) + " -> " + String.format("%,d", (self.getShield() + 0)) + "/" +
            String.format("%,d", self.getStatsDefault().getHp()) + " (" + String.format("%,d", ((self.getShield() + 0) - (self.getShield() + defendOld))) + ")" +
        ((self.getEffectBlock() <= 0 && self.getShield() == 0) ? "\n" + self.getCmbType().getName() + " " + lang.get("log.is_no_longer") + " " + lang.get("log.effect_block") : "");
    }
}
