package io.github.celosia.sys.battle;

// Interface for applying buff effects
public interface BuffEffect {
    default String[] onGive(Unit self, int stacks) {
        return new String[]{""};
    }

    default String[] onRemove(Unit self, int stacks) {
        return new String[]{""};
    }

    default String[] onUseSkill(Unit self, Unit target, int stacks, Skill skill) {
        return new String[]{""};
    }

    // todo: What about non-primary targets? A separate onTakeDamage? Should that also be a thing?
    default String[] onTargetedBySkill(Unit self, int stacks) {
        return new String[]{""};
    }

    default String[] onTurnEnd(Unit self, int stacks) {
        return new String[]{""};
    }

    default String[] onGiveBuff(Unit self, Unit target, int stacks, Buff buff, int turns, int stacksChange) {
        return new String[]{""};
    }

    default String[] onChangeStage(Unit self, Unit target, int stacks, StageType stageType, int turns, int stacksChange) {
        return new String[]{""};
    }

    default String[] onHeal(Unit self, Unit target, int stacks, int heal, double overHeal) {
        return new String[]{""};
    }

    default String[] onGiveShield(Unit self, Unit target, int stacks, int turns, int stacksChange) {
        return new String[]{""};
    }
}
