package io.github.celosia.sys.battle;

import java.util.HashMap;
import java.util.Map;

public class Weapon extends EquippableEntity {

    private final Map<Element, Integer> affinities;
    private final Skill[] skills;
    private final Passive[] passives;

    public Weapon(Builder builder) {
        super(builder.name, builder.desc, builder.icon);
        this.affinities = builder.affinities;
        this.skills = builder.skills;
        this.passives = builder.passives;
    }

    public static class Builder {

        private final String name;
        private final String desc;
        private final String icon;
        private Map<Element, Integer> affinities = null;
        private Skill[] skills = null;
        private Passive[] passives = null;

        public Builder(String name, String desc, String icon) {
            this.name = name;
            this.desc = desc;
            this.icon = icon;
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
            return this;
        }

        public Builder passives(Passive... passives) {
            this.passives = passives;
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
            if (affinities != null) {
                for (Map.Entry<Element, Integer> entry : affinities.entrySet()) {
                    unit.getAffinities().merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }
            if (skills != null) unit.addSkills(skills);
            if (passives != null) unit.addPassives(passives);
        } else {
            if (affinities != null) {
                for (Map.Entry<Element, Integer> entry : affinities.entrySet()) {
                    unit.getAffinities().merge(entry.getKey(), entry.getValue() * -1, Integer::sum);
                }
            }
            if (skills != null) unit.removeSkills(skills);
            if (passives != null) unit.removePassives(passives);
        }
    }
}
