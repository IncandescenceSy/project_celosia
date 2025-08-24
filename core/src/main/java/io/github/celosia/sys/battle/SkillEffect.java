package io.github.celosia.sys.battle;

// Interface for applying skill effects
public interface SkillEffect {
    Result apply(Unit self, Unit target, boolean isMainTarget, ResultType resultTypePrev);

    default boolean isInstant() {
        return false;
    }
}
