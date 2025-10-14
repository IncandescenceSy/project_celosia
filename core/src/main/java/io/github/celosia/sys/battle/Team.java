package io.github.celosia.sys.battle;

// A set of Units that share some attributes
public class Team {

    private final Unit[] units;
    private int bloom;

    // Todo: field effects

    public Team(Unit[] units) {
        this.units = units;
        bloom = 0;
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
}
