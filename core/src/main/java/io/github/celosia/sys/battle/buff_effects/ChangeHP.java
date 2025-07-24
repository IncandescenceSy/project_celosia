package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Result;

// Todo heavily limit %-based damage on bosses
public class ChangeHP implements BuffEffect {

    private final float change; // Amount to change HP by. 1f = 100%

    public ChangeHP(float change) {
        this.change = change;
    }

    @Override
    public String onGive(Combatant self) {
        return "";
    }

    @Override
    public String onRemove(Combatant self) {
        return "";
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
        Result result = self.damage((int) (self.getStatsDefault().getHp() * change));
        String[] msgs = result.getMessages();
        // todo account for barrier/defend
        return ("HP" + ((msgs[0] != null) ? msgs[0].split("[.*HP]")[2] : "")) + ((msgs[1] != null) ? msgs[1].split("[.*HP]")[2] : "");
    }
}
