package io.github.celosia.sys.battle;

// Interface for applying skill effects
public interface SkillEffect {
    Result apply(Combatant self, Combatant target, ResultType resultTypePrev);
}
