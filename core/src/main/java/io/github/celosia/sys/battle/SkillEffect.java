package io.github.celosia.sys.battle;

// Interface for applying skill effects
public interface SkillEffect {
    void apply(Combatant self, Combatant target);
}
