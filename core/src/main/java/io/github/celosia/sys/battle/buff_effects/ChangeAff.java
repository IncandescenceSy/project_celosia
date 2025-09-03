package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleController.appendToLog;
import static io.github.celosia.sys.menu.TextLib.*;
import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeAff implements BuffEffect {

    private final Element element;
    private final int change; // Amount to add

    public ChangeAff(Element element, int change) {
        this.element = element;
        this.change = change;
    }

    @Override
    public void onGive(Unit self, int stacks) {
        int affOld = self.getAff(element);
        int affNew = affOld + (change * stacks);
        self.setAff(element, affNew);
        appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + element.getName() + " " + lang.get("affinity") + " " +
            getColor(affOld) + Math.clamp(affOld, -5, 5) + "[WHITE]" + " → " + getColor(affNew) + Math.clamp(affNew, -5, 5));
    }

    @Override
    public void onRemove(Unit self, int stacks) {
        int affOld = self.getAff(element);
        int affNew = affOld - (change * stacks);
        self.setAff(element, affNew);
        appendToLog(formatName(self.getUnitType().getName(), self.getPos()) + " " + c_stat + element.getName() + " " + lang.get("affinity") + " " +
            getColor(affOld) + Math.clamp(affOld, -5, 5) + "[WHITE]" + " → " + getColor(affNew) + Math.clamp(affNew, -5, 5));
    }
}
