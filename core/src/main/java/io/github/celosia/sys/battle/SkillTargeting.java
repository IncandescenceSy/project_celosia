package io.github.celosia.sys.battle;

// A skill with self and target attached
public class SkillTargeting {

    Skills skill;
    Combatant self;
    Combatant target;

    SkillTargeting(Skills skill, Combatant self, Combatant target) {
        this.skill = skill;
        this.self = self;
        this.target = target;
    }

    public Skills getSkill() {
        return skill;
    }

    public Combatant getSelf() {
        return self;
    }

    public Combatant getTarget() {
        return target;
    }
}
