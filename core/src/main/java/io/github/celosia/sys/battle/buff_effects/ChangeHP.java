package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.*;

import static io.github.celosia.sys.settings.Lang.lang;

// Todo heavily limit %-based damage on bosses
public class ChangeHP implements BuffEffect {

    private final double change; // Amount to change HP by. 1 = +100%
    private final boolean pierce;

    public ChangeHP(double change, boolean pierce) {
        this.change = change;
        this.pierce = pierce;
    }

    public ChangeHP(double change) {
        this(change, false);
    }

    @Override
    public String[] onTurnEnd(Unit self, int stacks) {
        if(change < 0) { // Damage
            Result result = self.damage((int) Math.abs(((self.getStatsDefault().getHp() * change) * stacks) * (Math.max(self.getMultDmgTaken(), 10) / 100d) * (Math.max(self.getMultDoTDmgTaken(), 10) / 100d)), pierce, false);
            return result.getMessages().toArray(String[]::new);
        } else { // Healing
            int hpOld = self.getStatsCur().getHp();
            int hpMax = self.getStatsDefault().getHp();
            int hpNew = (int) Math.max(hpOld, Math.min(hpOld + (((hpMax * change) * stacks) * (Math.max(self.getMultHealingTaken(), 10) / 100d)), hpMax));
            if(hpNew > hpOld) {
                self.getStatsCur().setHp(hpNew);
                return new String[]{lang.get("hp") + " " + String.format("%,d", hpOld) + " -> " + String.format("%,d", hpNew) + " (+" + String.format("%,d", Math.max(hpNew - hpOld, 0)) + ")"};
            } else return new String[]{""};
        }
    }
}
