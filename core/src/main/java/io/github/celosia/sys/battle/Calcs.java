package io.github.celosia.sys.battle;

import static io.github.celosia.sys.battle.BattleLib.STAT_MULT_HIDDEN;
import static io.github.celosia.sys.menu.TextLib.C_BLOOM;
import static io.github.celosia.sys.menu.TextLib.C_SP;
import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.menu.TextLib.formatNum;
import static io.github.celosia.sys.menu.TextLib.getColor;
import static io.github.celosia.sys.menu.TextLib.getSign;
import static io.github.celosia.sys.settings.Lang.lang;

public class Calcs {
	public static String changeSp(Unit unit, int change) {
		int spOld = unit.getSp();
		int spNew = Math.clamp(spOld + (int) (change * unit.getMultWithExpSpGain()), 0, 1000);

		if (spNew != spOld) {
			unit.setSp(spNew);
			return lang.format("log.change_sp", formatName(unit.getUnitType().name(), unit.getPos()),
					C_SP + formatNum(spOld), C_SP + formatNum(spNew),
					getColor(change) + getSign(change) + (spNew - spOld));
		} else
			return "";
	}

	public static String changeBloom(Team team, Side side, int change) {
		int bloomOld = team.getBloom();
		int bloomNew = Math.clamp(bloomOld + change, 0, 1000);

		if (bloomNew != bloomOld) {
			team.setBloom(bloomNew);
			return lang.format("log.change_bloom", side.getId(), C_BLOOM + formatNum(bloomOld),
					C_BLOOM + formatNum(bloomNew), getColor(change) + getSign(change) + (bloomNew - bloomOld));
		} else {
			return "";
		}
	}

	public static long getStatWithStage(long stat, long statDefault, int stage) {
		return stat + (int) (statDefault * (((double) stage / 10) / ((stage < 0) ? 2 : 1)));
	}

	public static long getDisplayStatWithStage(long stat, long statDefault, int stage) {
		return getStatWithStage(stat, statDefault, stage) / STAT_MULT_HIDDEN;
	}
}
