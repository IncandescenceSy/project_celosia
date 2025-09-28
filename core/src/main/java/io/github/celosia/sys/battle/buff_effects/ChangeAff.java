package io.github.celosia.sys.battle.buff_effects;

import io.github.celosia.sys.battle.BuffEffect;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.Unit;

import static io.github.celosia.sys.battle.BattleControllerLib.appendToLog;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.formatNum;
import static io.github.celosia.sys.menu.TextLib.getColor;
import static io.github.celosia.sys.menu.TextLib.getSign;
import static io.github.celosia.sys.settings.Lang.lang;

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
		int affOld = self.getAffsCur().getAff(element);
		int affNew = affOld + changeFull;
		self.getAffsCur().setAff(element, affNew);

		appendToLog(lang.format("log.change_aff", formatName(self.getUnitType().name(), self.getPos()),
				element.getName(), getColor(affOld) + getSign(affOld) + formatNum(affOld),
				getColor(affNew) + getSign(affNew) + formatNum(affNew)));
	}
}
