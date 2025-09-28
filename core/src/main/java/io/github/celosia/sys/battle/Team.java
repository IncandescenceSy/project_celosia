package io.github.celosia.sys.battle;

// A set of Units that share some attributes
public class Team {

	String name;

	Unit[] units;

	int bloom;

	// Todo: field effects

	public Team(Unit[] units) {
		this.units = units;
		bloom = 0;
	}

	public String getName() {
		return name;
	}

	public int getBloom() {
		return bloom;
	}

	public void setBloom(int blm) {
		this.bloom = blm;
	}

	public Unit[] getUnits() {
		return units;
	}

	public void setUnits(Unit[] units) {
		this.units = units;
	}
}
