package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.IconEntity;

import static io.github.celosia.Main.ELEMENTS;

public class Element extends IconEntity {

    public Element(String name, String desc, String icon) {
        super(name, desc, icon);
        ELEMENTS.add(this);
    }
}
