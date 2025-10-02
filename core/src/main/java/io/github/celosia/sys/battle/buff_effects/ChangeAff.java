package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.formatName;
import static io.github.celosia.sys.util.TextLib.formatNum;
import static io.github.celosia.sys.util.TextLib.getColor;
import static io.github.celosia.sys.util.TextLib.getSign;

public class ChangeAff implements BuffEffect {

    private final Element element;
    private final int change;

    public ChangeAff(Element element, int change) {
        this.element = element;
        this.change = change;
    }

    @Override
    public void onGive(Unit self, int stacks) {
        calc(self, change * stacks);
    }

    @Override
    public void onRemove(Unit self, int stacks) {
        calc(self, (change * stacks) * -1);
    }

    public void calc(Unit self, int changeFull) {
        int affOld = self.getAffinity(element);
        int affNew = affOld + changeFull;
        self.getAffinities().put(element, affNew);

        appendToLog(lang.format("log.change_aff", formatName(self.getUnitType().getName(), self.getPos()),
                element.getNameWithIcon(), getColor(affOld) + getSign(affOld) + formatNum(affOld),
                getColor(affNew) + getSign(affNew) + formatNum(affNew)));
    }
}
