package io.github.celosia.sys.entity;

public class IconEntity extends NamedEntity {

    private final String icon;

    public IconEntity(String name, String desc, String icon) {
        super(name, desc);
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getNameWithIcon() {
        return icon + " [WHITE]" + this.getName();
    }

    public String getNameWithIcon(String color) {
        return icon + " " + color + this.getName();
    }
}
