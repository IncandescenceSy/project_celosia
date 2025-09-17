package io.github.celosia.sys.battle;

// Interface for applying buff effects
public interface BuffEffect {
	default void onGive(Unit self, int stacks) {
	}

	default void onRemove(Unit self, int stacks) {
	}

	default void onUseSkill(Unit self, Unit target, int stacks, Skill skill) {
	}

	// target = skill user
	default void onTargetedBySkill(Unit self, Unit target, int stacks, Skill skill) {
	}

	default String[] onTurnEnd(Unit self, int stacks) {
		return new String[]{""};
	}

	default void onDealDamage(Unit self, Unit target, int stacks, long damage, Element element) {
	}

	// target = attacker if there is one, otherwise target = self
	default void onTakeDamage(Unit self, Unit target, int stacks, long damage, Element element) {
	}

	default void onDealHeal(Unit self, Unit target, int stacks, long heal, int overHeal) {
	}

	// target = healer if there is one, otherwise target = self
	default void onTakeHeal(Unit self, Unit target, int stacks, long heal, int overHeal) {
	}

	default void onDealShield(Unit self, Unit target, int stacks, int turns, long heal) {
	}

	// target = shielder if there is one, otherwise target = self
	default void onTakeShield(Unit self, Unit target, int stacks, int turns, long heal) {
	}

	default void onGiveBuff(Unit self, Unit target, int stacks, Buff buff, int turns, int stacksChange) {
	}

	default void onChangeStage(Unit self, Unit target, int stacks, StageType stageType, int turns, int stacksChange) {
	}
}
