package io.github.celosia.sys.menu;

import io.github.celosia.sys.battle.Side;

import static io.github.celosia.sys.battle.PosLib.getSide;

public class TextLib {
    public static String tags = "{FADE}{SLIDE}";

    public static String c_ally = "[#2d74f5]";
    public static String c_ally_l = "[#528cf5]";
    public static String c_opp = "[#ff4545]";
    public static String c_opp_l = "[#ff6060]";
    public static String c_turn = "[#a034ff]";

    public static String formatName(String name, int pos, boolean possessive) {
        String suffix = (possessive) ? name.toLowerCase().endsWith("s") ? "'" : "'s" : "";
        String color = (getSide(pos) == Side.ALLY) ? c_ally : c_opp;
       return color + String.format("%s%s", name, suffix) + "[WHITE]";
    }

    public static String formatName(String name, int pos) {
       return formatName(name, pos, true);
    }
}
