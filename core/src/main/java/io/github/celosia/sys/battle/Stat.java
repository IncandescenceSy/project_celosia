package io.github.celosia.sys.battle;

import static io.github.celosia.sys.settings.Lang.lang;

// Deliberately doesn't include HP
// todo add full names
public enum Stat {
    STR(lang.get("stat.str")),
    MAG(lang.get("stat.mag")),
    FTH(lang.get("stat.fth")),
    AMR(lang.get("stat.amr")),
    RES(lang.get("stat.res")),
    AGI(lang.get("stat.agi"));

    private String name;

    Stat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
