package io.github.celosia.sys.battle;

// Interface for applying buff effects
public interface BuffEffect {
    default String[] onGive(Unit self, int stacks) {
        return new String[]{""};
    }

    default String[] onRemove(Unit self, int stacks) {
        return new String[]{""};
    }

    default String[] onUseSkill(Unit self, Unit target, int stacks) {
        return new String[]{""};
    }

    // todo: What about non-primary targets? A separate onTakeDamage? Should that also be a thing?
    default String[] onTargetedBySkill(Unit self, int stacks) {
        return new String[]{""};
    }

    default String[] onTurnEnd(Unit self, int stacks) {
        return new String[]{""};
    }
}
