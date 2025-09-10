package io.github.celosia.sys.battle;

public class Passive {
	private final String name;
	private final String desc;
	private final BuffEffect[] buffEffects;

	Passive(String name, String desc, BuffEffect... buffEffects) {
		this.name = name;
		this.desc = desc;
		this.buffEffects = buffEffects;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public BuffEffect[] getBuffEffects() {
		return buffEffects;
	}
}
