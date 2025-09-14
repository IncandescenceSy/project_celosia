package io.github.celosia.sys.battle;

/**
 * Monster species
 * 
 * @param statsBase
 *            Base stats
 * @param affsBase
 *            Affinities
 * @param passives
 *            Passives
 */
// todo skills and stuff
public record UnitType(String name, Stats statsBase, Affinities affsBase, Passive... passives) {
}
