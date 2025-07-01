package io.github.celosia.sys.battle;

// Elements that skills can have
// todo: lang
public enum Element {
    VIS("Vis"),
    IGNIS("Ignis"),
    GLACIES("Glacies"),
    FULGUR("Fulgur"),
    VENTUS("Ventus"),
    TERRA("Terra"),
    LUX("Lux"),
    MALUM("Malum");

    private String name;

    Element(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
