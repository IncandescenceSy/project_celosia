package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BooleanStat;
import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.BoolLib.isBothTruthy;
import static io.github.celosia.sys.util.TextLib.formatName;
import static io.github.celosia.sys.util.TextLib.formatNum;

public record ChangeBooleanStat(BooleanStat stat, int change) implements BuffEffect {

    @Override
    public void onGive(Unit self, int stacks) {
        calc(self, change * stacks);
    }

    @Override
    public void onRemove(Unit self, int stacks) {
        calc(self, (change * stacks) * -1);
    }

    public void calc(Unit self, int changeFull) {
        int statOld = self.getBooleanStat(stat);
        int statNew = statOld + changeFull;
        self.setBooleanStat(stat, statNew);
        if (!isBothTruthy(statOld, statNew) && (stat != BooleanStat.EFFECT_BLOCK)) {
            appendToLog(lang.format(stat.getLogMsgLangId(),
                    formatName(self.getUnitType().getName(), self.getPos(), stat.isPossessiveNameInLogMsg()), statNew,
                    formatNum(self.getSp())));
        }
    }
}
