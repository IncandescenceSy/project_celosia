package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.menu.TextLib.c_hp;
import static io.github.celosia.sys.menu.TextLib.c_pos;
import static io.github.celosia.sys.settings.Lang.lang;

// todo protect bosses against %-based damage
public class ChangeHp implements BuffEffect {
    private final double change; // Amount to change HP by. If isPercentage, 1 = +100%
    private final boolean isImmediate; // If true, happens onGive. If false, happens onTurnEnd
    private final boolean isPercentage; // If false, uses the raw number of change instead
    private final boolean isPierce;

    public ChangeHp(Builder builder) {
        change = builder.change;
        isImmediate = builder.isImmediate;
        isPercentage = builder.isPercentage;
        isPierce = builder.isPierce;
    }

    public static class Builder {
        private final double change;
        private boolean isImmediate = false;
        private boolean isPercentage = true;
        private boolean isPierce = false;

        public Builder(double change) {
            this.change = change;
        }

        public Builder immediate() {
            this.isImmediate = true;
            return this;
        }

        public Builder notPercentage() {
            this.isPercentage = false;
            return this;
        }

        public Builder pierce() {
            this.isPierce = true;
            return this;
        }

        public ChangeHp build() {
            return new ChangeHp(this);
        }
    }

    @Override
    public String[] onGive(Unit self, int stacks) {
        if(isImmediate) {
            return calc(self, stacks);
        } else return new String[]{""};
    }

    @Override
    public String[] onTurnEnd(Unit self, int stacks) {
        if(!isImmediate) {
            return calc(self, stacks);
        } else return new String[]{""};
    }

    private String[] calc(Unit self, int stacks) {
        if (change < 0) { // Damage
            int dmg = (isPercentage) ? (int) Math.abs(((self.getStatsDefault().getHp() * change) * stacks) * (Math.max(self.getMultDmgTaken(), 10) / 100d) *
                ((isImmediate) ? 1 : (Math.max(self.getMultDoTDmgTaken(), 10) / 100d))) : (int) (change * (Math.max(self.getMultDmgTaken(), 10) / 100d));
            Result result = self.damage(dmg, isPierce, false);
            return result.getMessages().toArray(String[]::new);
        } else { // Healing
            int hpOld = self.getStatsCur().getHp();
            int hpMax = self.getStatsDefault().getHp();
            int heal = (int) (change * ((isPercentage) ? hpMax : 1) * stacks * (Math.max(self.getMultHealingTaken(), 10) / 100d));
            int hpNew = Math.max(hpOld, Math.min(hpOld + heal, hpMax));
            if (hpNew > hpOld) {
                self.getStatsCur().setHp(hpNew);
                return new String[]{lang.get("hp") + " " + c_hp + String.format("%,d", hpOld) + "[WHITE] → " + c_hp + String.format("%,d", hpNew) + c_pos + " (+" + String.format("%,d", Math.max(hpNew - hpOld, 0)) + ")"};
            } else return new String[]{""};
        }
    }
}
