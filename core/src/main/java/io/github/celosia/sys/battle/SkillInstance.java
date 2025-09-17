package io.github.celosia.sys.battle;

public class SkillInstance {
	private final Skill skill; // todo allow changing?
	private int cooldown;

	public SkillInstance(Skill skill) {
		this.skill = skill;
		cooldown = 0;
	}

	public Skill getSkill() {
		return skill;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void decrementCooldown() {
		cooldown = Math.max(cooldown - 1, 0);
	}
}
