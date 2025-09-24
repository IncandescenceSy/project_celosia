package io.github.celosia.sys.battle;

public record Buff(String name, String desc, String icon, BuffType buffType, int maxStacks, BuffEffect... buffEffects) {
	Buff(String name, String desc, String icon, BuffType buffType, BuffEffect... buffEffects) {
		this(name, desc, icon, buffType, 1, buffEffects);
	}
}
