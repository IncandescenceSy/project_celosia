package io.github.celosia.sys.battle;

import com.badlogic.gdx.utils.IntArray;

import static io.github.celosia.Main.RANGES;
import static io.github.celosia.sys.battle.PosLib.getAcross;
import static io.github.celosia.sys.battle.PosLib.getTeamWithout;
import static io.github.celosia.sys.battle.PosLib.getUpDown;

public record Range(String name, int rangeVertical, Side side, boolean canTargetSelf, int targetCount,
                    Target... targets) {

    public Range(Builder builder) {
        this(builder.name, builder.rangeVertical, builder.side, builder.canTargetSelf, builder.targetCount,
                builder.targets);
        RANGES.add(this);
    }

    public static class Builder {

        private final String name;
        private final int rangeVertical;
        private final Side side;
        private final Target[] targets;

        private boolean canTargetSelf = false;
        private int targetCount = 1;

        public Builder(String name, int rangeVertical, Side side, Target... targets) {
            this.name = name;
            this.rangeVertical = rangeVertical;
            this.side = side;
            this.targets = targets;
        }

        public Builder canTargetSelf() {
            canTargetSelf = true;
            return this;
        }

        public Builder targetCount(int targetCount) {
            this.targetCount = targetCount;
            return this;
        }

        public Range build() {
            return new Range(this);
        }
    }

    public int[] getTargetPositions(int posSelf, int posTarget) {
        IntArray pos = new IntArray();

        for (Target target : targets) {
            switch (target) {
                case Target.SELF:
                    pos.add(posSelf);
                    break;
                case Target.SELF_UP:
                    pos.add(getUpDown(posSelf, -1));
                    break;
                case Target.SELF_DOWN:
                    pos.add(getUpDown(posSelf, 1));
                    break;
                case Target.SELF_ACROSS:
                    pos.add(getAcross(posSelf));
                    break;
                case Target.SELF_ACROSS_UP:
                    pos.add(getUpDown(getAcross(posSelf), -1));
                    break;
                case Target.SELF_ACROSS_DOWN:
                    pos.add(getUpDown(getAcross(posSelf), 1));
                    break;
                case Target.SELF_TEAM:
                    pos.addAll(getTeamWithout(posSelf));
                    break;
                case Target.TARGET:
                    pos.add(posTarget);
                    break;
                case Target.TARGET_UP:
                    pos.add(getUpDown(posTarget, -1));
                    break;
                case Target.TARGET_DOWN:
                    pos.add(getUpDown(posTarget, 1));
                    break;
                case Target.TARGET_TEAM:
                    pos.addAll(getTeamWithout(posTarget));
                    break;
            }
        }

        return pos.toArray();
    }
}
