package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.ComplexDescriptionEntity;
import io.github.celosia.sys.entity.IconEntity;

public class Passive extends ComplexDescriptionEntity {

    private final BuffEffect[] buffEffects;

    Passive(Builder builder) {
        super(builder);
        this.buffEffects = builder.buffEffects;
    }

    public static class Builder extends ComplexDescriptionEntity.Builder {

        private BuffEffect[] buffEffects = new BuffEffect[] {};

        public Builder(String name, String desc, String icon) {
            super(name, desc, icon);
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

        public Builder effects(BuffEffect... buffEffects) {
            this.buffEffects = buffEffects;
            return this;
        }

        public Passive build() {
            return new Passive(this);
        }
    }

    public BuffEffect[] getBuffEffects() {
        return buffEffects;
    }

    // todo
    @Override
    public String getDesc() {
        return "";
    }
}
