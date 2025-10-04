package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.IconEntity;

// todo extend ComplexDescEntity
public abstract class EquippableEntity extends IconEntity {

    public EquippableEntity(String name, String desc, String icon) {
        super(name, desc, icon);
    }

    public abstract void apply(Unit unit, boolean give);

    public void equip(Unit unit) {
        this.apply(unit, true);
    }

    public void unequip(Unit unit) {
        this.apply(unit, false);
    }
}
