package io.github.celosia.sys.battle;

// Interface for applying skill effects
public interface BuffEffect {
    default String onGive(Combatant self) {
        return "";
    }

    default String onRemove(Combatant self) {
        return "";
    }

    default String onUseSkill(Combatant self, Combatant target) {
        return "";
    }

    // todo: should this be onTargetedBySkill? What about non-primary targets? debuff damage? Should that also be a thing?
    default String onTakeDamage(Combatant self) {
        return "";
    }

    default String onTurnEnd(Combatant self) {
        return "";
    }
}
