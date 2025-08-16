package io.github.celosia.sys.battle;

// A skill with self and target attached
public class Move {

    Skill skill;
    Combatant self;
    int targetPos;

    Move(Skill skill, Combatant self, int targetPos) {
        this.skill = skill;
        this.self = self;
        this.targetPos = targetPos;
    }

    public Skill getSkill() {
        return skill;
    }

    public Combatant getSelf() {
        return self;
    }

    public int getTargetPos() {
        return targetPos;
    }
}
