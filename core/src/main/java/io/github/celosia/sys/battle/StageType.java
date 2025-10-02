package io.github.celosia.sys.battle;

import static io.github.celosia.sys.save.Lang.lang;

public enum StageType {

    ATK(lang.get("stage.atk"), "[LIGHT RED][+energy-sword]"),
    DEF(lang.get("stage.def"), "[#006eff][+rosa-shield]"),
    FTH(lang.get("stat.fth"), "[LIGHT PURPLE][+star-altar]"),
    AGI(lang.get("stat.agi"), "[LIGHT GREEN][+walking-boot]");

    private final String name;
    private final String icon;

    StageType(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public Stat[] getStats() {
        return switch (this) {
            case ATK -> new Stat[] { Stat.STR, Stat.MAG };
            case DEF -> new Stat[] { Stat.AMR, Stat.RES };
            case FTH -> new Stat[] { Stat.FTH };
            case AGI -> new Stat[] { Stat.AGI };
        };
    }
}
