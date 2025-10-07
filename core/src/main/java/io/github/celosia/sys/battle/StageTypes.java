package io.github.celosia.sys.battle;

import static io.github.celosia.sys.save.Lang.lang;

public class StageTypes {

    public static final StageType ATK = new StageType(lang.get("stage.atk"), "todo", "[LIGHT RED][+energy-sword]",
            Stat.STR, Stat.MAG);
    public static final StageType DEF = new StageType(lang.get("stage.def"), "todo", "[#006eff][+rosa-shield]",
            Stat.AMR, Stat.RES);
    public static final StageType FTH = new StageType(lang.get("stat.fth"), "todo", "[LIGHT PURPLE][+star-altar]",
            Stat.FTH);
    public static final StageType AGI = new StageType(lang.get("stat.agi"), "todo", "[LIGHT GREEN][+walking-boot]",
            Stat.AGI);
}
