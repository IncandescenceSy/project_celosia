package io.github.celosia.sys.battle;

import static io.github.celosia.sys.settings.Lang.lang;

// Types of targeting that skills can employ
public enum Targeting {
    SELF(lang.get("targeting.self")),
    OTHER_1R(lang.get("targeting.other_1r")),
    OTHER_2R(lang.get("targeting.other_2r")),
    OTHER_3R(lang.get("targeting.other_3r")),
    OTHER_1R_OR_SELF(lang.get("targeting.other_1r_or_self")),
    OTHER_2R_OR_SELF(lang.get("targeting.other_2r_or_self")),
    OTHER_3R_OR_SELF(lang.get("targeting.other_3r_or_self")),
    OTHERS_2_1R(lang.get("targeting.others_2_1r")),
    OTHERS_2_2R(lang.get("targeting.others_2_2r")),
    OTHERS_2_3R(lang.get("targeting.others_2_3r")),
    ALLY_1R(lang.get("targeting.ally_1r")),
    ALLY_2R(lang.get("targeting.ally_2r")),
    ALLY_3R(lang.get("targeting.ally_3r")),
    ALLIES_2_1R(lang.get("targeting.allies_2_1r")),
    ALLIES_2_2R(lang.get("targeting.allies_2_2r")),
    ALLIES_2_3R(lang.get("targeting.allies_2_3r")),
    OPPONENT_1R(lang.get("targeting.opponent_1r")),
    OPPONENT_2R(lang.get("targeting.opponent_2r")),
    OPPONENT_3R(lang.get("targeting.opponent_3r")),
    TEAM(lang.get("targeting.team")),
    ALL(lang.get("targeting.all")),
    ALL_OTHERS(lang.get("targeting.all_others")),
    ADJACENT(lang.get("targeting.adjacent")),
    SELF_UP_DOWN(lang.get("targeting.self_up_down")),
    ACROSS(lang.get("targeting.across")),
    ACROSS_UP_DOWN(lang.get("targeting.across_up_down")),
    COLUMN_OF_3_1R(lang.get("targeting.column_of_3_1r")),
    COLUMN_OF_3_2R(lang.get("targeting.column_of_3_2r"));

    private final String name;

    Targeting(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
