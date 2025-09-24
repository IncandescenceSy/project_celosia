package io.github.celosia.sys.battle;

import static io.github.celosia.sys.settings.Lang.lang;

public enum StageType {
	// spotless:off
	ATK(lang.get("stage.atk"), "[LIGHT RED][+energy-sword]"),
    DEF(lang.get("stage.def"), "[LIGHT azure][+rosa-shield]"),
    FTH(lang.get("stat.fth"), "[LIGHT PURPLE][+star-altar]"),
    AGI(lang.get("stat.agi"), "[LIGHT GREEN][+walking-boot]");
    // spotless:on

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
			case ATK -> new Stat[]{Stat.STR, Stat.MAG};
			case DEF -> new Stat[]{Stat.AMR, Stat.RES};
			case FTH -> new Stat[]{Stat.FTH};
			case AGI -> new Stat[]{Stat.AGI};
		};
	}
}
