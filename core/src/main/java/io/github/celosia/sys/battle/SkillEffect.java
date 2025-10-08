package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.IconEntity;

import java.util.Optional;

// Interface for applying skill effects
public interface SkillEffect {

    ResultType apply(Unit self, Unit target, boolean isMainTarget, ResultType resultTypePrev);

    boolean isInstant();

    default SkillType getSkillType() {
        return SkillType.STAT;
    }

    default int getPow() {
        return 0;
    }

    default Optional<IconEntity> getDescInclusion() {
        return Optional.empty();
    }
}
