package io.github.celosia.sys.battle;

import static io.github.celosia.sys.settings.Lang.lang;

public enum StageType {
	ATK(lang.get("stage.atk")), DEF(lang.get("stage.def")), FTH(lang.get("stat.fth")), AGI(lang.get("stat.agi"));

	private final String name;

	StageType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
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
