package io.github.celosia.sys.battle;

// Monster species
public class CombatantType {

    String name;

    // Base statsL
    Stats statsBase;

    // Affinities
    int affIgnis;
    int affGlacies;
    int affFulgur;
    int affVentus;
    int affTerra;
    int affLux;
    int affMalum;

    // Passives
    // todo

    // Skills
    // todo

    public CombatantType(String name, Stats stats,
                         int affIgnis, int affGlacies, int affFulgur, int affVentus, int affTerra, int affLux, int affMalum) {
        this.name = name;

        this.statsBase = stats;

        this.affIgnis = affIgnis;
        this.affGlacies = affGlacies;
        this.affFulgur = affFulgur;
        this.affVentus = affVentus;
        this.affTerra = affTerra;
        this.affLux = affLux;
        this.affMalum = affMalum;
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
}
