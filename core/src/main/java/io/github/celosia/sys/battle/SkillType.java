package io.github.celosia.sys.battle;

import static io.github.celosia.sys.save.Lang.lang;

// Types that skills can have
// todo add full names
public enum SkillType {

    STR(lang.get("stat.str")),
    MAG(lang.get("stat.mag")),
    FTH(lang.get("stat.fth")),
    STAT(lang.get("skill_type.stat"));

    private final String name;

    SkillType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
