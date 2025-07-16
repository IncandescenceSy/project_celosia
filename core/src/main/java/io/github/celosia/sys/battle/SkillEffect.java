package io.github.celosia.sys.battle;

// Interface for applying skill effects
public interface SkillEffect {
    boolean apply(Combatant self, Combatant target);
}
