package io.github.celosia.sys.battle;

// A set of Combatants that share some attributes
public class Team {

    String name;

    Combatant[] cmbs;

    int bloom; // Bloom

    // Todo: field effects

    public Team(Combatant[] cmbs) {
        this.cmbs = cmbs;
        bloom = 10;
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

    public Combatant[] getCmbs() {
        return cmbs;
    }

    public void setCmbs(Combatant[] cmbs) {
        this.cmbs = cmbs;
    }
}
