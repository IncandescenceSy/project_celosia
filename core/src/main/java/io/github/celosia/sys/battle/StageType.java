package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.IconEntity;

import static io.github.celosia.Main.STAGE_TYPES;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.getSign;

public class StageType extends IconEntity {

    private final Stat[] stats;

    StageType(String name, String desc, String icon, Stat... stats) {
        super(name, desc, icon);
        this.stats = stats;
        STAGE_TYPES.add(this);
    }

    public Stat[] getStats() {
        return stats;
    }

    public String getTurnsStacksFormatted(Unit unit) {
        int stage = unit.getStage(this);
        return getSign(stage) + stage + "(" + unit.getStageTurns(this) + ")";
    }

    public String getNameWithIconAndSign(int stage) {
        return this.getNameWithIcon() + " " + ((stage > 0) ? lang.get("up") : lang.get("down"));
    }
}
