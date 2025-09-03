package io.github.celosia.sys.battle;

import static io.github.celosia.sys.menu.TextLib.formatName;
import static io.github.celosia.sys.settings.Lang.lang;

public class Calcs {
    public static String changeSp(Unit unit, int change) {
        int spOld = unit.getSp();
        int spNew = Math.clamp(spOld + (int) (change * (Math.max(unit.getMultSpGain(), 10) / 100d)), 0, 1000);

        if (spNew != spOld) {
            unit.setSp(spNew);
            return formatName(unit.getUnitType().getName(), unit.getPos()) + " " + lang.get("sp") + " " + String.format("%,d", spOld) + " -> " + String.format("%,d", spNew);
        } else
            return "";
    }

    public static String changeBloom(Team team, Side side, int change) {
        int bloomOld = team.getBloom();
        int bloomNew = Math.clamp(bloomOld + change, 0, 1000);

        if (bloomNew != bloomOld) {
            team.setBloom(bloomNew);
            return formatName((side == Side.ALLY) ? lang.get("player_team") : lang.get("opponent_team"), (side == Side.ALLY) ? 0 : 3) + " " + lang.get("bloom") + " " + String.format("%,d", bloomOld) + " -> " + String.format("%,d", bloomNew);
        } else
           return "";
    }
}
