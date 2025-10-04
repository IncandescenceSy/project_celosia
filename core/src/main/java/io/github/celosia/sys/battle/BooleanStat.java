package io.github.celosia.sys.battle;

import static io.github.celosia.sys.save.Lang.lang;

public enum BooleanStat {

    EFFECT_BLOCK(lang.get("bool.effect_block"), "log.change_boolean_stat.effect_block", true, false),
    INFINITE_SP(lang.get("bool.infinite_sp"), "log.change_boolean_stat.infinite_sp", true, true),
    UNABLE_TO_ACT(lang.get("bool.unable_to_act"), "log.change_boolean_stat.unable_to_act", false, false),
    UNABLE_TO_ACT_IMMUNITY(lang.get("bool.unable_to_act_immunity"), "log.change_boolean_stat.unable_to_act_immune",
            true, false),
    EQUIP_DISABLED(lang.get("bool.equip_disabled"), "log.change_boolean_stat.equip_disabled", false, true),
    EQUIP_DISABLED_IMMUNITY(lang.get("bool.equip_disabled_immunity"), "log.change_boolean_stat.equip_disabled_immune",
            true, false);

    private final String name;
    private final String logMsgLangId;
    private final boolean isPositive;
    private final boolean possessiveNameInLogMsg;

    BooleanStat(String name, String logMsgLangId, boolean isPositive, boolean possessiveNameInLogMsg) {
        this.name = name;
        this.logMsgLangId = logMsgLangId;
        this.isPositive = isPositive;
        this.possessiveNameInLogMsg = possessiveNameInLogMsg;
    }

    public String getName() {
        return name;
    }

    public String getLogMsgLangId() {
        return logMsgLangId;
    }

    public boolean isPositive() {
        return isPositive;
    }

    public boolean isPossessiveNameInLogMsg() {
        return possessiveNameInLogMsg;
    }
}
