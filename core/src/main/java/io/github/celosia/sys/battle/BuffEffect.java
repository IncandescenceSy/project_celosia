package io.github.celosia.sys.battle;

import java.util.List;

// Interface for applying skill effects
public interface BuffEffect {
    default String[] onGive(Combatant self) {
        return new String[]{""};
    }

    default String[] onRemove(Combatant self) {
        return new String[]{""};
    }

    default String[] onUseSkill(Combatant self, Combatant target) {
        return new String[]{""};
    }

    // todo: What about non-primary targets?A separate onTakeDamage? Should that also be a thing?
    default String[] onTargetedBySkill(Combatant self) {
        return new String[]{""};
    }

    default String[] onTurnEnd(Combatant self) {
        return new String[]{""};
    }
}
