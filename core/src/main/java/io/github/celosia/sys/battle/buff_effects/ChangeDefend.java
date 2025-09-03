package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.menu.TextLib.*;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeDefend implements BuffEffect {

    private final double change; // Defend to add in % of max HP

    public ChangeDefend(double change) {
        this.change = change;
    }

    @Override
    public String[] onGive(Unit self, int stacks) {
        int hpMax = self.getStatsDefault().getHp();
        // Add defend (shield + defend cannot exceed max HP)
        int defendOld = self.getDefend();
        int defendNew = (self.getShield() + defendOld + ((change * hpMax) * stacks) > hpMax) ? hpMax - self.getShield() : (int) ((change * hpMax) * stacks);
        self.setDefend(defendNew);
        return new String[]{formatName(self.getUnitType().getName(), self.getPos()) + " " + c_buff + lang.get("shield") + " " + c_shield + String.format("%,d", (self.getShield() + defendOld))
            + "[WHITE]" + " → " + c_shield + String.format("%,d", (self.getShield() + defendNew)) + "[WHITE]/" + c_shield +
            String.format("%,d", hpMax) + " (" + c_pos + "+" + String.format("%,d", ((self.getShield() + defendNew) - (self.getShield() + defendOld))) + "[WHITE])",
            ((self.isEffectBlock() && self.getShield() == 0 && defendOld == 0) ? self.getUnitType().getName() + " " + lang.get("log.is_now") + " " + c_stat + lang.get("log.effect_block") : "")};
    }

    @Override
    public String[] onRemove(Unit self, int stacks) {
        int defendOld = self.getDefend();
        self.setDefend(0);
        String msgEffBlock = ((self.isEffectBlock() && self.getShield() == 0) ? self.getUnitType().getName() + " " + lang.get("log.is_no_longer") + " " + c_stat + lang.get("log.effect_block") : "");
        if(self.getShield() > 0) return new String[]{formatName(self.getUnitType().getName(), self.getPos()) + " " + c_buff + lang.get("shield") + " " + c_shield + String.format("%,d", (self.getShield() + defendOld))
            + "[WHITE]" + " → " + c_shield + String.format("%,d", self.getShield()) + "[WHITE]/" + c_shield +
            String.format("%,d", self.getStatsDefault().getHp()) + " (" + c_neg + String.format("%,d", (self.getShield() - (self.getShield() + defendOld))) + "[WHITE])", msgEffBlock};
        else return new String[]{formatName(self.getUnitType().getName(), self.getPos(), false) + " " + lang.get("log.loses") + " " + c_shield + String.format("%,d", defendOld) + "[WHITE] " + c_buff + lang.get("shield"), msgEffBlock};
    }
}
