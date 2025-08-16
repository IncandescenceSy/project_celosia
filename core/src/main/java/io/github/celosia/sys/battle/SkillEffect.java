package io.github.celosia.sys.battle;

// Interface for applying skill effects
public interface SkillEffect {
    Result apply(Combatant self, Combatant target, boolean isMainTarget, ResultType resultTypePrev);

    default boolean isInstant() {
        return false;
    }
}
