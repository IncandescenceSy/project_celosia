package io.github.celosia.sys.battle;

import static io.github.celosia.sys.menu.TextLib.*;

public class LogLib {
    public static String getStageStatString(Unit unit, StageType stageType, int stageNew) {
        StringBuilder builder = new StringBuilder();

        builder.append("[WHITE] (");
        int statCount = stageType.getStats().length;
        for(int i = 0; i < statCount; i++) {
            Stat stat = stageType.getStats()[i];
            int statDefault = unit.getStatsDefault().getDisplayStat(stat);
            int statOld = unit.getDisplayStatWithStage(stat);
            int statNew = unit.getDisplayStatWithStage(stat, stageNew);
            int change = statNew - statOld;
            builder.append(c_stat).append(stat.getName()).append(" ").append(getStatColor(statOld, statDefault)).append(String.format("%,d", statOld)).append("[WHITE] → ").append(getStatColor(statNew, statDefault)).append(String.format("%,d", statNew)).append("[WHITE]").append("/").append(c_num).append(String.format("%,d", statDefault)).append("[WHITE] (").append(getColor(change)).append((change >= 0) ? "+" : "").append(String.format("%,d", (statNew - statOld))).append("[WHITE])");

            if(i == statCount - 1) builder.append(")");
            else builder.append(", ");
        }

        return builder.toString();
    }
}
