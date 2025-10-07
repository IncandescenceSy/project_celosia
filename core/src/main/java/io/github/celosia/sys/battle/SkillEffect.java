package io.github.celosia.sys.battle;

import io.github.celosia.sys.entity.IconEntity;

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

    default IconEntity getDescInclusion() {
        return null;
    }
}
