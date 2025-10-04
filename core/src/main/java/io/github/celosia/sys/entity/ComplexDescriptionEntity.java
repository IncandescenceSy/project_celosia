package io.github.celosia.sys.entity;

import io.github.celosia.sys.battle.Skill;

import static io.github.celosia.sys.save.Lang.lang;
import static io.github.celosia.sys.util.TextLib.C_BUFF;
import static io.github.celosia.sys.util.TextLib.C_SKILL;

// Allows the entity's description to include the names and descriptions of other IconEntities
// desc is now treated as a raw lang key and should not be formatted for display
public abstract class ComplexDescriptionEntity extends IconEntity {

    private final String[] descArgs;
    private final IconEntity[] descInclusions;

    public ComplexDescriptionEntity(Builder builder) {
        super(builder.name, builder.desc, builder.icon);
        descArgs = builder.descArgs;
        descInclusions = builder.descInclusions;
    }

    public static class Builder {

        private final String name;
        private final String desc;
        private final String icon;
        private String[] descArgs;
        private IconEntity[] descInclusions = new IconEntity[] {};

        public Builder(String name, String desc, String icon) {
            this.name = name;
            this.desc = desc;
            this.icon = icon;
        }

        public Builder descArgs(String... descArgs) {
            this.descArgs = descArgs;
            return this;
        }

        public Builder descInclusion(IconEntity descInclusion) {
            descInclusions = new IconEntity[] { descInclusion };
            return this;
        }

        public Builder descInclusions(IconEntity... descInclusions) {
            this.descInclusions = descInclusions;
            return this;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        public String getIcon() {
            return icon;
        }

        public String[] getDescArgs() {
            return descArgs;
        }

        public IconEntity[] getDescInclusions() {
            return descInclusions;
        }
    }

    @Override
    public abstract String getDesc();

    // todo address warning
    public String getPartialDesc() {
        StringBuilder partialDesc = new StringBuilder(lang.format(super.getDesc(), descArgs));

        if (descInclusions.length > 0) {
            partialDesc.append("\n");
        }

        for (IconEntity entity : descInclusions) {
            String color = (entity instanceof Skill) ? C_SKILL : C_BUFF;
            partialDesc.append("\n[WHITE](").append(entity.getNameWithIcon(color)).append("[WHITE]: ")
                    .append(entity.getDesc().replace("\n", ". ")).append("[WHITE])");
        }

        return partialDesc.toString();
    }

    public String[] getDescArgs() {
        return descArgs;
    }

    public IconEntity[] getDescInclusions() {
        return descInclusions;
    }
}
