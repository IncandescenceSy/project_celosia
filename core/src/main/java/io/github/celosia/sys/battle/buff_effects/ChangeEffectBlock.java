package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;

import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeEffectBlock implements BuffEffect {

    private final int change; // Amount to add

    public ChangeEffectBlock(int change) {
        this.change = change;
    }

    @Override
    public String[] onGive(Combatant self) {
        int effectBlockOld = self.getEffectBlock();
        int effectBlockNew = effectBlockOld + change;
        self.setEffectBlock(effectBlockNew);
        return new String[]{(effectBlockNew > 0 && self.getShield() == 0 && self.getDefend() == 0) ? self.getCmbType().getName() + " " + lang.get("log.is_now") + " " + lang.get("log.effect_block") : ""};
    }

    @Override
    public String[] onRemove(Combatant self) {
        int effectBlockOld = self.getEffectBlock();
        int effectBlockNew = effectBlockOld - change;
        self.setEffectBlock(effectBlockNew);
        return new String[]{(effectBlockNew <= 0 && self.getShield() == 0 && self.getDefend() == 0) ? self.getCmbType().getName() + " " + lang.get("log.is_no_longer") + " " + lang.get("log.effect_block") : ""};
    }
}
