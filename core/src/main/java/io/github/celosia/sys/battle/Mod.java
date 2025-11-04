package io.github.celosia.sys.battle;

import static io.github.celosia.sys.save.Lang.lang;

public enum Mod {

    DURATION_BUFF_DEALT(lang.get("mod.duration_buff_dealt"), true),
    DURATION_BUFF_TAKEN(lang.get("mod.duration_buff_taken"), true),
    DURATION_DEBUFF_DEALT(lang.get("mod.duration_debuff_dealt"), true),
    DURATION_DEBUFF_TAKEN(lang.get("mod.duration_debuff_taken"), false),
    STACKS_BUFF_DEALT(lang.get("mod.stacks_buff_dealt"), true),
    STACKS_BUFF_TAKEN(lang.get("mod.stacks_buff_taken"), true),
    STACKS_DEBUFF_DEALT(lang.get("mod.stacks_debuff_dealt"), true),
    STACKS_DEBUFF_TAKEN(lang.get("mod.stacks_buff_taken"), false),
    RANGE(lang.get("mod.range"), true);

    private final String name;
    private final boolean isPositive;

    Mod(String name, boolean isPositive) {
        this.name = name;
        this.isPositive = isPositive;
    }

    public String getName() {
        return name;
    }

    public boolean isPositive() {
        return isPositive;
    }
}
