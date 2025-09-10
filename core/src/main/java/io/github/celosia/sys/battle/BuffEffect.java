package io.github.celosia.sys.battle;

// Interface for applying buff effects
public interface BuffEffect {
	default void onGive(Unit self, int stacks) {
	}

	default void onRemove(Unit self, int stacks) {
	}

	default void onUseSkill(Unit self, Unit target, int stacks, Skill skill) {
	}

	default void onTargetedBySkill(Unit self, int stacks) {
	}

	default String[] onTurnEnd(Unit self, int stacks) {
		return new String[]{""};
	}

	default void onGiveBuff(Unit self, Unit target, int stacks, Buff buff, int turns, int stacksChange) {
	}

	default void onChangeStage(Unit self, Unit target, int stacks, StageType stageType, int turns, int stacksChange) {
	}

	default void onHeal(Unit self, Unit target, int stacks, int heal, double overHeal) {
	}

	default void onGiveShield(Unit self, Unit target, int stacks, int turns, int stacksChange) {
	}
}
