package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_HP;
import static io.github.celosia.sys.util.TextLib.C_SHIELD;
import static io.github.celosia.sys.util.TextLib.formatName;
import static io.github.celosia.sys.util.TextLib.formatNum;

public class ChangeDefend implements BuffEffect {

    // Defend to add in tenths of a % of max HP (1000 = +100%)
    private final int change;

    public ChangeDefend(int change) {
        this.change = change;
    }

    @Override
    public void onGive(Unit self, int stacks) {
        long hpMax = self.getMaxHp();
        // Add defend (shield + defend cannot exceed max HP)
        long defendOld = self.getDefend();
        long defendNew = (long) ((self.getShield() + defendOld + (((change / 1000d) * hpMax) * stacks) > hpMax) ?
                hpMax - self.getShield() : ((change / 1000d) * hpMax) * stacks);

        long defendOldDisp = self.getDisplayDefend();
        self.setDefend(defendNew);
        long defendNewDisp = self.getDisplayDefend();

        long shieldDisp = self.getDisplayShield();

        appendToLog(lang.format("log.change_shield", formatName(self.getUnitType().getName(), self.getPos()),
                C_SHIELD + formatNum((shieldDisp + defendOldDisp)), C_SHIELD + formatNum((shieldDisp + defendNewDisp)),
                C_HP + formatNum(self.getDisplayMaxHp()),
                C_SHIELD + "+" + formatNum(((shieldDisp + defendNewDisp) - (shieldDisp + defendOldDisp)))));

        if (!self.isEffectBlock() && self.getShield() == 0 && defendOld == 0) {
            appendToLog(lang.format("log.change_boolean_stat.effect_block",
                    formatName(self.getUnitType().getName(), self.getPos(), false), 1));
        }
    }

    @Override
    public void onRemove(Unit self, int stacks) {
        long defendOldDisp = self.getDisplayDefend();
        self.setDefend(0);
        long shieldDisp = self.getDisplayShield();

        if (self.getShield() > 0) {
            appendToLog(lang.format("log.change_shield", formatName(self.getUnitType().getName(), self.getPos()),
                    C_SHIELD + formatNum((shieldDisp + defendOldDisp)), C_SHIELD + formatNum((shieldDisp)),
                    C_HP + formatNum(self.getDisplayMaxHp()), C_SHIELD + formatNum(defendOldDisp * -1)));
        } else {
            appendToLog(lang.format("log.lose_shield", formatName(self.getUnitType().getName(), self.getPos(), false),
                    C_SHIELD + formatNum(defendOldDisp)));
        }
        if (!self.isEffectBlock() && self.getShield() == 0) {
            appendToLog(lang.format("log.change_boolean_stat.effect_block",
                    formatName(self.getUnitType().getName(), self.getPos(), false), 0));
        }
    }
}
