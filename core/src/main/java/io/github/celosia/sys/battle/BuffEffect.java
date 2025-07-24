package io.github.celosia.sys.battle;

// Interface for applying skill effects
public interface BuffEffect {
    String onGive(Combatant self);

    String onRemove(Combatant self);

    String onUseSkill(Combatant self, Combatant target);

    String onTakeDamage(Combatant self); // todo: should this be onTargetedBySkill? What about non-primary targets? debuff damage? Should that also be a thing?

    String onTurnEnd(Combatant self);
}
