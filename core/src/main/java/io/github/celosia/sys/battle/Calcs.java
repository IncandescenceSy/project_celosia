package io.github.celosia.sys.battle;

import static io.github.celosia.sys.menu.TextLib.*;
import static io.github.celosia.sys.settings.Lang.lang;

public class Calcs {
    public static String changeSp(Unit unit, int change) {
        int spOld = unit.getSp();
        int spNew = Math.clamp(spOld + (int) (change * (Math.max(unit.getMultSpGain(), 1000) / 10000d)), 0, 1000);

        if (spNew != spOld) {
            unit.setSp(spNew);
            return formatName(unit.getUnitType().getName(), unit.getPos()) + " " + c_stat + lang.get("sp") + " " + c_sp + String.format("%,d", spOld) + "[WHITE] → " + c_sp + String.format("%,d", spNew)
                + "[WHITE] (" + getColor(change) + ((change > 0) ? "+" : "") + (spNew - spOld) + "[WHITE])";
        } else
            return "";
    }

    public static String changeBloom(Team team, Side side, int change) {
        int bloomOld = team.getBloom();
        int bloomNew = Math.clamp(bloomOld + change, 0, 1000);

        if (bloomNew != bloomOld) {
            team.setBloom(bloomNew);
            return formatName((side == Side.ALLY) ? lang.get("player_team") : lang.get("opponent_team"), (side == Side.ALLY) ? 0 : 3) + " " + c_stat + lang.get("bloom") + " " + c_bloom + String.format("%,d", bloomOld) + "[WHITE] → " + c_bloom + String.format("%,d", bloomNew);
        } else
           return "";
    }

    public static int getStatWithStage(int stat, int statDefault, int stage) {
        return stat + (int) (statDefault * (((double) stage / 10) / ((stage < 0) ? 2 : 1)));
    }

    public static int getDisplayStatWithStage(int stat, int statDefault, int stage) {
        return getStatWithStage(stat, statDefault, stage) / 10000;
    }
}
