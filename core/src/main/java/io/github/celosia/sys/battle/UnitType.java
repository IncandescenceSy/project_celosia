package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.NamedEntity;

import java.util.Map;

import static io.github.celosia.Main.UNIT_TYPES;

// todo skills and stuff
public class UnitType extends NamedEntity {

    private final Stats statsBase;
    private final Map<Element, Integer> affinities;
    private final Passive[] passives;

    public UnitType(String name, String desc, Stats statsBase, Map<Element, Integer> affinities, Passive... passives) {
        super(name, desc);
        this.statsBase = statsBase;
        this.affinities = affinities;
        this.passives = passives;
        UNIT_TYPES.add(this);
    }

    public Stats getStatsBase() {
        return statsBase;
    }

    public Map<Element, Integer> getAffinities() {
        return affinities;
    }

    public Passive[] getPassives() {
        return passives;
    }
}
