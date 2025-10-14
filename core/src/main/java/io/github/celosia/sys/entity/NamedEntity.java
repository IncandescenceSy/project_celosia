package io.github.celosia.sys.entity;

public class NamedEntity {

    private final String name;
    private final String desc;

    public NamedEntity(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}
