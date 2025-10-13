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

    public ChangeShield() {}

    // Actual value is set in skill_effects/GiveShield.apply()
    @Override
    public void onGive(Unit self, int stacks) {}

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
