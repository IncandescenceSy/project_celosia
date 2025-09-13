package io.github.celosia.sys.battle;

public class Buff {
	private final String name;
	private final String desc;
	private final BuffType buffType;
	private final int maxStacks;
	private final BuffEffect[] buffEffects;

	Buff(String name, String desc, BuffType buffType, int maxStacks, BuffEffect... buffEffects) {
		this.name = name;
		this.desc = desc;
		this.buffType = buffType;
		this.maxStacks = maxStacks;
		this.buffEffects = buffEffects;
	}

	Buff(String name, String desc, BuffType buffType, BuffEffect... buffEffects) {
		this(name, desc, buffType, 1, buffEffects);
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public BuffType getBuffType() {
		return buffType;
	}

	public int getMaxStacks() {
		return maxStacks;
	}

	public BuffEffect[] getBuffEffects() {
		return buffEffects;
	}
}
