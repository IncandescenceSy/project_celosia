package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.ComplexDescriptionEntity;
import io.github.celosia.sys.entity.IconEntity;

import static io.github.celosia.Main.BUFFS;
import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_NUM;

public class Buff extends ComplexDescriptionEntity {

    private final BuffType buffType;
    private final int maxStacks;
    private final BuffEffect[] buffEffects;

    public Buff(Builder builder) {
        super(builder);
        this.buffType = builder.buffType;
        this.maxStacks = builder.maxStacks;
        this.buffEffects = builder.buffEffects;
        BUFFS.add(this);
    }

    public static class Builder extends ComplexDescriptionEntity.Builder {

        private final BuffType buffType;
        private int maxStacks = 1;
        private BuffEffect[] buffEffects = new BuffEffect[] {};

        public Builder(String name, String desc, String icon, BuffType buffType) {
            super(name, desc, icon);
            this.buffType = buffType;
        }

        // Override so they can be used without casting
        @Override
        public Builder descArgs(String... descArgs) {
            super.descArgs(descArgs);
            return this;
        }

        @Override
        public Builder descInclusions(IconEntity... descInclusions) {
            super.descInclusions(descInclusions);
            return this;
        }

        public Builder maxStacks(int maxStacks) {
            this.maxStacks = maxStacks;
            return this;
        }

        public Builder effects(BuffEffect... buffEffects) {
            this.buffEffects = buffEffects;
            return this;
        }

        public Buff build() {
            return new Buff(this);
        }
    }

    public BuffType getBuffType() {
        return buffType;
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public BuffEffect[] getBuffEffects() {
        return buffEffects;
    }

    @Override
    public String getDesc() {
        return lang.format("buff_desc", buffType.getName(),
                (maxStacks == 1) ? "" : lang.format("buff_desc.stacks_to", C_NUM + maxStacks), super.getPartialDesc());
    }
}
