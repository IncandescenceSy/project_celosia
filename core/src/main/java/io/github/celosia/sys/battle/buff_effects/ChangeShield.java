package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_HP;
import static io.github.celosia.sys.util.TextLib.C_SHIELD;
import static io.github.celosia.sys.util.TextLib.formatName;
import static io.github.celosia.sys.util.TextLib.formatNum;

// Multiple Buffs that contain this effect will interfere with eachother
public class ChangeShield implements BuffEffect {

    private final long change;

    public ChangeShield(int change) {
        this.change = change;
    }

    @Override
    public void onGive(Unit self, int stacks) {
        long hpMax = self.getMaxHp();
        // Add shield (shield + defend cannot exceed max HP)
        long shieldOld = self.getShield();
        long shieldNew = ((self.getDefend() + shieldOld + (change * stacks)) > hpMax) ?
                hpMax - self.getDefend() : (change * hpMax) * stacks;

        long shieldOldDisp = self.getDisplayShield();
        self.setShield(shieldNew);
        long shieldNewDisp = self.getDisplayShield();

        long defendDisp = self.getDisplayDefend();

        appendToLog(lang.format("log.change_shield", formatName(self.getUnitType().getName(), self.getPos()),
                C_SHIELD + formatNum((defendDisp + shieldOldDisp)), C_SHIELD + formatNum((defendDisp + shieldNewDisp)),
                C_HP + formatNum(self.getDisplayMaxHp()),
                C_SHIELD + "+" + formatNum(((defendDisp + shieldNewDisp) - (defendDisp + shieldOldDisp)))));
    }

    @Override
    public void onRemove(Unit self, int stacks) {
        long shieldOldDisp = self.getDisplayShield();
        self.setShield(0);
        long defendDisp = self.getDisplayDefend();

        if (self.getDefend() > 0) {
            appendToLog(lang.format("log.change_shield", formatName(self.getUnitType().getName(), self.getPos()),
                    C_SHIELD + formatNum((defendDisp + shieldOldDisp)), C_SHIELD + formatNum((defendDisp)),
                    C_HP + formatNum(self.getDisplayMaxHp()), C_SHIELD + formatNum(shieldOldDisp * -1)));
        } else {
            appendToLog(lang.format("log.lose_shield", formatName(self.getUnitType().getName(), self.getPos(), false),
                    C_SHIELD + formatNum(shieldOldDisp)));
        }
    }
}
