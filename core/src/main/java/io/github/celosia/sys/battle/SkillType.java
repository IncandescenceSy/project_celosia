package io.github.celosia.sys.battle;

// Types that skills can have
// todo: lang
public enum SkillType {
    STR("Str"),
    MAG("Mag"),
    FTH("Fth"),
    STAT("Stat");

    private String name;

    SkillType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
