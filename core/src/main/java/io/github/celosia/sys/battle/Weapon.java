package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.ComplexDescriptionEntity;

import java.util.HashMap;
import java.util.Map;

import static io.github.celosia.Main.WEAPONS;

public class Weapon extends EquippableEntity {

    private final Map<Element, Integer> affinities;
    private final Skill[] skills;
    private final Passive[] passives;

    public Weapon(Builder builder) {
        super(builder);
        this.affinities = builder.affinities;
        this.skills = builder.skills;
        this.passives = builder.passives;
        WEAPONS.add(this);
    }

    public static class Builder extends ComplexDescriptionEntity.Builder {

        private Map<Element, Integer> affinities = new HashMap<>();
        private Skill[] skills = new Skill[] {};
        private Passive[] passives = new Passive[] {};

        public Builder(String name, String desc, String icon) {
            super(name, desc, icon);
        }

        public Builder affinities(int ignis, int glacies, int fulgur, int ventus, int terra, int lux, int malum) {
            affinities = new HashMap<>();
            affinities.put(Elements.IGNIS, ignis);
            affinities.put(Elements.GLACIES, glacies);
            affinities.put(Elements.FULGUR, fulgur);
            affinities.put(Elements.VENTUS, ventus);
            affinities.put(Elements.TERRA, terra);
            affinities.put(Elements.LUX, lux);
            affinities.put(Elements.MALUM, malum);
            return this;
        }

        public Builder affinities(Map<Element, Integer> affinities) {
            this.affinities = affinities;
            return this;
        }

        public Builder skills(Skill... skills) {
            this.skills = skills;
            super.descInclusions(skills);
            return this;
        }

        public Builder passives(Passive... passives) {
            this.passives = passives;
            super.descInclusions(passives);
            return this;
        }

        public Weapon build() {
            return new Weapon(this);
        }
    }

    public Map<Element, Integer> getAffinities() {
        return affinities;
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
            for (Map.Entry<Element, Integer> entry : affinities.entrySet()) {
                unit.getAffinities().merge(entry.getKey(), entry.getValue(), Integer::sum);
            }

            unit.addSkills(skills);
            unit.addPassives(passives);
        } else {
            for (Map.Entry<Element, Integer> entry : affinities.entrySet()) {
                unit.getAffinities().merge(entry.getKey(), entry.getValue() * -1, Integer::sum);
            }

            unit.removeSkills(skills);
            unit.removePassives(passives);
        }
    }

    // todo
    @Override
    public String getDesc() {
        return "";
    }
}
