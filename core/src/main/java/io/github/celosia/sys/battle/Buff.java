package io.github.celosia.sys.battle;

public record Buff(String name, String desc, BuffType buffType, int maxStacks, BuffEffect... buffEffects) {
	Buff(String name, String desc, BuffType buffType, BuffEffect... buffEffects) {
		this(name, desc, buffType, 1, buffEffects);
	}
}
