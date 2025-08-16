package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Combatant;
import io.github.celosia.sys.battle.Element;

import static io.github.celosia.sys.settings.Lang.lang;

public class ChangeAff implements BuffEffect {

    private final Element element;
    private final int change; // Amount to add

    public ChangeAff(Element element, int change) {
        this.element = element;
        this.change = change;
    }

    @Override
    public String onGive(Combatant self) {
        int affOld = self.getAff(element);
        int affNew = affOld + change;
        self.setAff(element, affNew);
        return self.getCmbType().getName() + "'s " + element.getName() + " " + lang.get("affinity") + " " + Math.clamp(affOld, -5, 5) + " -> " + Math.clamp(affNew, -5, 5);
    }

    @Override
    public String onRemove(Combatant self) {
        int affOld = self.getAff(element);
        int affNew = affOld - change;
        self.setAff(element, affNew);
        return self.getCmbType().getName() + "'s " + element.getName() + " " + lang.get("affinity") + " " + Math.clamp(affOld, -5, 5) + " -> " + Math.clamp(affNew, -5, 5);
    }
}
