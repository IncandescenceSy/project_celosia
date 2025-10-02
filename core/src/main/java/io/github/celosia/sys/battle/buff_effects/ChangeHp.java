package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Elements;
import io.github.celosia.sys.battle.Result;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_HP;
import static io.github.celosia.sys.util.TextLib.formatNum;
import static io.github.celosia.sys.util.TextLib.getColor;
import static io.github.celosia.sys.util.TextLib.getSign;

public class ChangeHp implements BuffEffect {

    // Amount to change HP by. If isPercentage, 1000 = +100%
    private final int change;
    private final boolean isImmediate;
    private final boolean isPercentage;
    private final boolean isPierce;

    public ChangeHp(Builder builder) {
        change = builder.change;
        isImmediate = builder.isImmediate;
        isPercentage = builder.isPercentage;
        isPierce = builder.isPierce;
    }

    public static class Builder {

        private final int change;
        private boolean isImmediate = false;
        private boolean isPercentage = true;
        private boolean isPierce = false;

        public Builder(int change) {
            this.change = change;
        }

        public Builder immediate() {
            isImmediate = true;
            return this;
        }

        public Builder notPercentage() {
            isPercentage = false;
            return this;
        }

        public Builder pierce() {
            isPierce = true;
            return this;
        }

        public ChangeHp build() {
            return new ChangeHp(this);
        }
    }

    // todo this might need to display the name if immediate
    @Override
    public void onGive(Unit self, int stacks) {
        if (isImmediate) {
            appendToLog(calc(self, stacks));
        }
    }

    @Override
    public String[] onTurnEnd(Unit self, int stacks) {
        if (!isImmediate) {
            return calc(self, stacks);
        }

        return new String[] { "" };
    }

    private String[] calc(Unit self, int stacks) {
        // Damage
        if (change < 0) {
            double multDoTDmgTaken = ((isImmediate) ? 1 : self.getMultWithExpDoTDmgTaken());
            long dmg = (isPercentage) ?
                    (long) Math.abs(((self.getMaxHp() * (change / 1000d)) * stacks) * self.getMultWithExpDmgTaken() *
                            multDoTDmgTaken * self.getMultWithExpPercentageDmgTaken()) :
                    (long) (change * self.getMultWithExpDmgTaken() * multDoTDmgTaken);
            self.onTakeDamage(self, dmg, Elements.VIS);
            Result result = self.damage(dmg, isPierce, false);
            return result.messages().toArray(String[]::new);
        }

        // Healing
        long hpOld = self.getHp();
        long hpMax = self.getMaxHp();
        long heal = (long) (change * ((isPercentage) ? hpMax : 1) * stacks * self.getMultWithExpHealingTaken());
        long hpNew = Math.max(hpOld, Math.min(hpOld + heal, hpMax));

        if (hpNew > hpOld) {
            self.onTakeHeal(self, heal, 0);

            long hpOldDisp = self.getDisplayHp();
            self.setHp(hpNew);
            long hpNewDisp = self.getDisplayHp();
            long hpMaxDisp = self.getDisplayMaxHp();
            long changeFullDisp = Math.max(hpNewDisp - hpOldDisp, 0);

            return new String[] { lang.format("log.change_hp", "", C_HP + formatNum(hpOldDisp),
                    C_HP + formatNum(hpNewDisp), C_HP + formatNum(hpMaxDisp),
                    getColor(changeFullDisp) + getSign(changeFullDisp) + formatNum(changeFullDisp)) };
        }

        return new String[] { "" };
    }
}
