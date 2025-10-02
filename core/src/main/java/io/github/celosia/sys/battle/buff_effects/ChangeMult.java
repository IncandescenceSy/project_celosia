package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Mult;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_STAT;
import static io.github.celosia.sys.util.TextLib.formatName;
import static io.github.celosia.sys.util.TextLib.formatNum;
import static io.github.celosia.sys.util.TextLib.getExpChangeColor;
import static io.github.celosia.sys.util.TextLib.getExpColor;
import static io.github.celosia.sys.util.TextLib.getMultChangeColor;
import static io.github.celosia.sys.util.TextLib.getMultColor;
import static io.github.celosia.sys.util.TextLib.getMultWithExpChangeColor;
import static io.github.celosia.sys.util.TextLib.getMultWithExpColor;
import static io.github.celosia.sys.util.TextLib.getSign;

public class ChangeMult implements BuffEffect {

    private final Mult mult;
    // Amount to add to mult in 10ths of a % (1000 = +100%)
    private final int changeMult;
    // Amount to add to exp in 100ths (100 = +1)
    private final int changeExp;

    public ChangeMult(Mult mult, int changeMult, int changeExp) {
        this.mult = mult;
        this.changeMult = changeMult;
        this.changeExp = changeExp;
    }

    public ChangeMult(Mult mult, int changeMult) {
        this(mult, changeMult, 0);
    }

    @Override
    public void onGive(Unit self, int stacks) {
        calc(self, changeMult * stacks, changeExp * stacks, true);
    }

    @Override
    public void onRemove(Unit self, int stacks) {
        calc(self, (changeMult * stacks) * -1, (changeExp * stacks) * -1, false);
    }

    public void calc(Unit self, int changeMultFull, int changeExpFull, boolean giving) {
        // Minimum mult to display
        int multMin = (mult == Mult.PERCENTAGE_DMG_TAKEN) ? 1 : 100;

        int multOld = self.getMult(mult);
        int multNew = multOld + changeMultFull;
        double changeMultDisplay = (Math.max(multNew, multMin) - Math.max(multOld, multMin)) / 10d;
        int expOld = self.getExp(mult);
        int expNew = expOld + changeExpFull;
        double changeExpDisplay = (Math.max(expNew, 10) - Math.max(expOld, 10)) / 100d;
        double multWithExpOld = self.getMultWithExp(mult);

        self.setMult(mult, multNew);
        self.setExp(mult, expNew);

        double multWithExpNew = self.getMultWithExp(mult);
        double changeMultWithExpDisplay = Math.max(multWithExpNew, 0.1) - Math.max(multWithExpOld, 0.1);

        // The final mult with the exponent calculated as displays
        String calcedOld = "";
        String calcedNew = "";
        String calcedChange = lang.get("log.change_mult.calced.change.default");

        if (expOld != 100) {
            calcedOld = lang.format("log.change_mult.calced", getExpColor(expOld, multOld, mult),
                    formatNum(expOld / 100d),
                    getMultWithExpColor(multWithExpOld, mult) + formatNum(multWithExpOld * 100));
        }

        if (expNew != 100) {
            calcedNew = lang.format("log.change_mult.calced", getExpColor(expNew, multNew, mult),
                    formatNum(expNew / 100d),
                    getMultWithExpColor(multWithExpNew, mult) + formatNum(multWithExpNew * 100));
        }

        if (expOld != expNew) {
            calcedChange = lang.format("log.change_mult.calced.change",
                    getExpChangeColor(changeExpFull, (giving) ? multNew : multOld, mult),
                    getSign(changeExpDisplay) + formatNum(changeExpDisplay),
                    getMultWithExpChangeColor(changeMultWithExpDisplay, mult),
                    getSign(changeMultWithExpDisplay) + formatNum(changeMultWithExpDisplay * 100));
        }

        appendToLog(lang.format("log.change_mult", formatName(self.getUnitType().getName(), self.getPos()),
                C_STAT + mult.getName(), getMultColor(multOld, mult) + formatNum(Math.max(multOld, multMin) / 10d),
                calcedOld, getMultColor(multNew, mult) + formatNum(Math.max(multNew, multMin) / 10d), calcedNew,
                getMultChangeColor(changeMultFull, mult) + getSign(changeMultDisplay) + formatNum(changeMultDisplay),
                calcedChange));
    }
}
