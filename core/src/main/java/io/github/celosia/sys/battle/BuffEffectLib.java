package io.github.celosia.sys.battle;

public class BuffEffectLib {
	private static void notifyBuffEffects(Unit self, Unit target, BuffEffectNotifier notifier, Object... args) {
		// Handle Passives
		for (Passive passive : self.getPassives())
			for (BuffEffect buffEffect : passive.getBuffEffects())
				notifier.notify(buffEffect, self, target, 1, args);

		// Handle Buffs
		for (BuffInstance buffInstance : self.getBuffInstances())
			for (BuffEffect buffEffect : buffInstance.getBuff().getBuffEffects())
				notifier.notify(buffEffect, self, target, buffInstance.getStacks(), args);
	}

	public static void notifyOnUseSkill(Unit self, Unit target, Skill skill) {
		notifyBuffEffects(self, target,
				(effect, s, t, stacks, args) -> effect.onUseSkill(s, t, stacks, (Skill) args[0]), skill);
	}

	public static void notifyOnGiveBuff(Unit self, Unit target, Buff buff, int turnsMod, int stacksChange) {
		notifyBuffEffects(self, target, (effect, s, t, stacks, args) -> effect.onGiveBuff(s, t, stacks, (Buff) args[0],
				(int) args[1], (int) args[2]), buff, turnsMod, stacksChange);
	}

	public static void notifyOnChangeStage(Unit self, Unit target, StageType stageType, int turnsMod,
			int stacksChange) {
		notifyBuffEffects(self, target, (effect, s, t, stacks, args) -> effect.onChangeStage(s, t, stacks,
				(StageType) args[0], (int) args[1], (int) args[2]), stageType, turnsMod, stacksChange);
	}

	public static void notifyOnHeal(Unit self, Unit target, int heal, double overHeal) {
		notifyBuffEffects(self, target,
				(effect, s, t, stacks, args) -> effect.onHeal(s, t, stacks, (int) args[0], (double) args[1]), heal,
				overHeal);
	}

	public static void notifyOnGiveShield(Unit self, Unit target, int turns, int stacksChange) {
		notifyBuffEffects(self, target,
				(effect, s, t, stacks, args) -> effect.onGiveShield(s, t, stacks, (int) args[0], (int) args[1]), turns,
				stacksChange);
	}
}
