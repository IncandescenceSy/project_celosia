package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.ComplexDescriptionEntity;

import static io.github.celosia.sys.save.Lang.lang;

public class Accessory extends EquippableEntity {

    private final Skill[] skills;
    private final Passive[] passives;

    public Accessory(Builder builder) {
        super(builder);
        this.skills = builder.skills;
        this.passives = builder.passives;
    }

    public static class Builder extends ComplexDescriptionEntity.Builder {
        private Skill[] skills = new Skill[] {};
        private Passive[] passives = new Passive[] {};

        Builder(String name, String desc, String icon) {
            super(name, desc, icon);
        }

        public Builder skill(Skill skill) {
            skills = new Skill[] {skill};
            super.descInclusion(skill);
            return this;
        }

        public Builder skills(Skill... skills) {
            this.skills = skills;
            super.descInclusions(skills);
            return this;
        }

        public Builder passive(Passive passive) {
            passives = new Passive[] {passive};
            super.descInclusion(passive);
            return this;
        }

        public Builder passives(Passive... passives) {
            this.passives = passives;
            super.descInclusions(passives);
            return this;
        }

        public Accessory build() {
            return new Accessory(this);
        }
    }

    public Skill[] getSkills() {
        return skills;
    }

    public Passive[] getPassives() {
        return passives;
    }

    @Override
    public void apply(Unit unit, boolean give) {
        if (give) {
            unit.addSkills(skills);
            unit.addPassives(passives);
        } else {
            unit.removeSkills(skills);
            unit.removePassives(passives);
        }
    }

    @Override
    public String getDesc() {
        return lang.format("accessory_desc", this.getPartialDesc());
    }
}
