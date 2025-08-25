package io.github.celosia.sys.menu;

public class TextLib {
    public static String formatPossessive(String name) {
        String suffix = name.toLowerCase().endsWith("s") ? "'" : "'s";
        return String.format("%s%s", name, suffix);
    }
}
