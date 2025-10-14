package io.github.celosia.sys.battle;

import static io.github.celosia.sys.save.Lang.lang;

public class Ranges {

    public static final Range SELF = new Range.Builder(lang.get("range.self"), 3, Side.ALLY, Target.SELF)
            .canTargetSelf().build();
    public static final Range OTHER_1R = new Range.Builder(lang.get("range.other_1r"), 1, Side.BOTH, Target.TARGET)
            .build();
    public static final Range OTHER_2R = new Range.Builder(lang.get("range.other_2r"), 2, Side.BOTH, Target.TARGET)
            .build();
    public static final Range OTHER_3R = new Range.Builder(lang.get("range.other_3r"), 3, Side.BOTH, Target.TARGET)
            .build();
    public static final Range OTHER_1R_OR_SELF = new Range.Builder(lang.get("range.other_1r_or_self"), 1, Side.BOTH,
            Target.TARGET).canTargetSelf().build();
    public static final Range OTHER_2R_OR_SELF = new Range.Builder(lang.get("range.other_2r_or_self"), 2, Side.BOTH,
            Target.TARGET).canTargetSelf().build();
    public static final Range OTHER_3R_OR_SELF = new Range.Builder(lang.get("range.other_3r_or_self"), 3, Side.BOTH,
            Target.TARGET).canTargetSelf().build();
    public static final Range OTHERS_2_1R = new Range.Builder(lang.get("range.others_2_1r"), 1, Side.BOTH,
            Target.TARGET).targetCount(2).build();
    public static final Range OTHERS_2_2R = new Range.Builder(lang.get("range.others_2_2r"), 2, Side.BOTH,
            Target.TARGET).targetCount(2).build();
    public static final Range OTHERS_2_3R = new Range.Builder(lang.get("range.others_2_3r"), 3, Side.BOTH,
            Target.TARGET).targetCount(2).build();
    public static final Range ALLY_1R = new Range.Builder(lang.get("range.ally_1r"), 1, Side.ALLY, Target.TARGET)
            .build();
    public static final Range ALLY_2R = new Range.Builder(lang.get("range.ally_2r"), 2, Side.ALLY, Target.TARGET)
            .build();
    public static final Range ALLY_3R = new Range.Builder(lang.get("range.ally_3r"), 3, Side.ALLY, Target.TARGET)
            .build();
    public static final Range ALLIES_2_1R = new Range.Builder(lang.get("range.allies_2_1r"), 1, Side.ALLY,
            Target.TARGET).targetCount(2).build();
    public static final Range ALLIES_2_2R = new Range.Builder(lang.get("range.allies_2_2r"), 2, Side.ALLY,
            Target.TARGET).targetCount(2).build();
    public static final Range ALLIES_2_3R = new Range.Builder(lang.get("range.allies_2_3r"), 3, Side.ALLY,
            Target.TARGET).targetCount(2).build();
    public static final Range OPPONENT_1R = new Range.Builder(lang.get("range.opponent_1r"), 1, Side.OPPONENT,
            Target.TARGET).build();
    public static final Range OPPONENT_2R = new Range.Builder(lang.get("range.opponent_2r"), 2, Side.OPPONENT,
            Target.TARGET).build();
    public static final Range OPPONENT_3R = new Range.Builder(lang.get("range.opponent_3r"), 3, Side.OPPONENT,
            Target.TARGET).build();
    public static final Range TEAM = new Range.Builder(lang.get("range.team"), 3, Side.BOTH, Target.TARGET,
            Target.TARGET_TEAM).canTargetSelf().build();
    public static final Range ALL = new Range.Builder(lang.get("range.all"), 3, Side.BOTH, Target.SELF,
            Target.SELF_TEAM, Target.TARGET, Target.TARGET_TEAM).canTargetSelf().build();
    public static final Range ALL_OTHERS = new Range.Builder(lang.get("range.all_others"), 3, Side.BOTH,
            Target.SELF_TEAM,
            Target.TARGET, Target.TARGET_TEAM).build();
    public static final Range ADJACENT = new Range.Builder(lang.get("range.adjacent"), 1, Side.BOTH, Target.SELF_UP,
            Target.SELF_DOWN, Target.SELF_ACROSS, Target.SELF_ACROSS_UP, Target.SELF_ACROSS_DOWN).build();
    public static final Range SELF_UP_DOWN = new Range.Builder(lang.get("range.self_up_down"), 3, Side.ALLY,
            Target.SELF, Target.SELF_UP, Target.SELF_DOWN).canTargetSelf().build();
    public static final Range ACROSS = new Range.Builder(lang.get("range.across"), 1, Side.OPPONENT, Target.SELF_ACROSS)
            .build();
    public static final Range ACROSS_UP_DOWN = new Range.Builder(lang.get("range.across_up_down"), 0, Side.OPPONENT,
            Target.SELF_ACROSS, Target.SELF_ACROSS_UP, Target.SELF_ACROSS_DOWN).build();
    public static final Range COLUMN_OF_3_1R = new Range.Builder(lang.get("range.column_of_3_1r"), 1, Side.BOTH,
            Target.TARGET, Target.TARGET_UP, Target.TARGET_DOWN).canTargetSelf().build();
    public static final Range COLUMN_OF_3_2R = new Range.Builder(lang.get("range.column_of_3_2r"), 2, Side.BOTH,
            Target.TARGET, Target.TARGET_UP, Target.TARGET_DOWN).canTargetSelf().build();
}
