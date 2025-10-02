package io.github.celosia.sys.entity;

public class IconEntity extends NamedEntity {

    private String icon;

    public IconEntity(String name, String desc, String icon) {
        super(name, desc);
        this.icon = icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public String getNameWithIcon() {
        return icon + " [WHITE]" + this.getName();
    }
}
