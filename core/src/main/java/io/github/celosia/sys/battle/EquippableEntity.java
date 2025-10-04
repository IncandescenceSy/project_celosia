package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.ComplexDescriptionEntity;

public abstract class EquippableEntity extends ComplexDescriptionEntity {

    public EquippableEntity(ComplexDescriptionEntity.Builder builder) {
        super(builder);
    }

    public abstract void apply(Unit unit, boolean give);

    public void equip(Unit unit) {
        this.apply(unit, true);
    }

    public void unequip(Unit unit) {
        this.apply(unit, false);
    }
}
