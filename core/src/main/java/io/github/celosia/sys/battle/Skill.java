package io.github.celosia.sys.battle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Skill {
	private final String name;
	private final String desc;
	private final Element element;
	private final Range range;
	private final int cost;
	private final int cooldown;

	// Skills happen in order of prio, and in order of user Agi within each prio
	// Prio brackets:
	// -9: Always happens last (Slow Follow-Ups)
	// -1: Happens late
	// 0: Normal
	// +1: Happens early (most prio skills)
	// +2: Happens very early (certain special prio skills?)
	// +3: Happens before attacks (Defend, Protect, Ally Switch)
	// +9: Always happens immediately (Follow-Ups)
	private final int prio;

	private final boolean isBloom;

	private final List<SkillRole> skillRoles;
	private final List<SkillEffect> skillEffects;

	Skill(Builder builder) {
		name = builder.name;
		desc = builder.desc;
		element = builder.element;
		range = builder.range;
		cost = builder.cost;
		cooldown = builder.cooldown;
		prio = builder.prio;
		isBloom = builder.isBloom;
		skillRoles = builder.skillRoles;
		skillEffects = builder.skillEffects;
	}

	public static class Builder {
		private final String name;
		private final String desc;
		private final Element element;
		private final Range range;
		private final int cost;
		private int cooldown = 0;
		private int prio = 0;
		private boolean isBloom = false;

		private final List<SkillRole> skillRoles = new ArrayList<>();
		private final List<SkillEffect> skillEffects = new ArrayList<>();

		public Builder(String name, String desc, Element element, Range range, int cost) {
			this.name = name;
			this.desc = desc;
			this.element = element;
			this.range = range;
			this.cost = cost;
		}

		public Builder cooldown(int cooldown) {
			this.cooldown = cooldown;
			return this;
		}

		public Builder prio(int prio) {
			this.prio = prio;
			return this;
		}

		public Builder bloom() {
			isBloom = true;
			return this;
		}

		public Builder role(SkillRole skillRole) {
			this.skillRoles.add(skillRole);
			return this;
		}

		public Builder roles(SkillRole... skillRoles) {
			this.skillRoles.addAll(Arrays.asList(skillRoles));
			return this;
		}

		public Builder effect(SkillEffect skillEffect) {
			this.skillEffects.add(skillEffect);
			return this;
		}

		public Builder effects(SkillEffect... skillEffects) {
			this.skillEffects.addAll(Arrays.asList(skillEffects));
			return this;
		}

		public Skill build() {
			return new Skill(this);
		}
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public Element getElement() {
		return element;
	}

	public Range getRange() {
		return range;
	}

	public int getCost() {
		return cost;
	}

	public int getCooldown() {
		return cooldown;
	}

	public int getPrio() {
		return prio;
	}

	public boolean isBloom() {
		return isBloom;
	}

	public List<SkillRole> getSkillRoles() {
		return skillRoles;
	}

	public boolean hasRole(SkillRole skillRole) {
		return skillRoles.contains(skillRole);
	}

	public List<SkillEffect> getSkillEffects() {
		return skillEffects;
	}

	public boolean isRangeSelf() {
		return range == Ranges.SELF || range == Ranges.SELF_UP_DOWN;
	}

	public boolean shouldTargetOpponent() {
		return range.side() == Side.OPPONENT || this.hasRole(SkillRole.ATTACK)
				|| this.hasRole(SkillRole.DEBUFF_DEFENSIVE) || this.hasRole(SkillRole.DEBUFF_OFFENSIVE);
	}

	public SkillInstance toSkillInstance() {
		return new SkillInstance(this);
	}
}
