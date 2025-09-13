package io.github.celosia.sys.battle;

import static io.github.celosia.sys.settings.Lang.lang;

// Elements that skills can have
public enum Element {
	// spotless:off
	VIS(lang.get("element.vis")),
    IGNIS(lang.get("element.ignis")),
    GLACIES(lang.get("element.glacies")),
    FULGUR(lang.get("element.fulgur")),
    VENTUS(lang.get("element.ventus")),
    TERRA(lang.get("element.terra")),
    LUX(lang.get("element.lux")),
    MALUM(lang.get("element.malum"));
    // spotless:on
	// FULGUR_MALUM(lang.get("element.fulgur") + "/" + lang.get("element.malum"));

	private final String name;

	Element(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
