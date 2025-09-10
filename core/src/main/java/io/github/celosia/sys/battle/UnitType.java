package io.github.celosia.sys.battle;

// Monster species
public class UnitType {

	String name;

	// Base stats
	Stats statsBase;

	// Affinities
	private final int affIgnis;
	private final int affGlacies;
	private final int affFulgur;
	private final int affVentus;
	private final int affTerra;
	private final int affLux;
	private final int affMalum;

	// Skills
	// todo

	// Passives
	private final Passive[] passives;

	public UnitType(String name, Stats stats, int affIgnis, int affGlacies, int affFulgur, int affVentus, int affTerra,
			int affLux, int affMalum, Passive... passives) {
		this.name = name;
		this.statsBase = stats;
		this.affIgnis = affIgnis;
		this.affGlacies = affGlacies;
		this.affFulgur = affFulgur;
		this.affVentus = affVentus;
		this.affTerra = affTerra;
		this.affLux = affLux;
		this.affMalum = affMalum;
		this.passives = passives;
	}

	public String getName() {
		return name;
	}

	public Stats getStatsBase() {
		return statsBase;
	}

	public int getAffIgnis() {
		return affIgnis;
	}

	public int getAffGlacies() {
		return affGlacies;
	}

	public int getAffFulgur() {
		return affFulgur;
	}

	public int getAffVentus() {
		return affVentus;
	}

	public int getAffTerra() {
		return affTerra;
	}

	public int getAffLux() {
		return affLux;
	}

	public int getAffMalum() {
		return affMalum;
	}

	public int[] getAffs() {
		return new int[]{affIgnis, affGlacies, affFulgur, affVentus, affTerra, affLux, affMalum};
	}

	public Passive[] getPassives() {
		return passives;
	}
}
