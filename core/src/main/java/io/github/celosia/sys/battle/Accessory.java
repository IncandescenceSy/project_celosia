package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.IconEntity;

public class Accessory extends IconEntity implements Equippable {

    private final Passive passive;

    public Accessory(String name, String desc, String icon, Passive passive) {
        super(name, desc, icon);
        this.passive = passive;
    }

    public Passive getPassive() {
        return passive;
    }

    @Override
    public void apply(Unit unit) {
        unit.addPassive(passive);
    }
}
