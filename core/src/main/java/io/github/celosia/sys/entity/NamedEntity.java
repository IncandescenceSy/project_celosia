package io.github.celosia.sys.entity;

public class NamedEntity {

    private String name;
    private String desc;

    public NamedEntity(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
