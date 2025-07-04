package io.github.celosia.sys.battle;

// A skill with self and target attached
public class SkillTargeting {

    Skill skill;
    Combatant self;
    Combatant target;

    SkillTargeting(Skill skill, Combatant self, Combatant target) {
        this.skill = skill;
        this.self = self;
        this.target = target;
    }

    public Skill getSkill() {
        return skill;
    }

    public Combatant getSelf() {
        return self;
    }

    public Combatant getTarget() {
        return target;
    }
}
