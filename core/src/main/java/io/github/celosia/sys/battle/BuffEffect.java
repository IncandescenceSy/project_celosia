package io.github.celosia.sys.battle;

// Interface for applying skill effects
public interface BuffEffect {
    void onGive(Combatant self);

    void onRemove(Combatant self);

    void onUseSkill(Combatant self, Combatant target);

    void onTakeDamage(Combatant self); // todo: should this be onTargetedBySkill? What about non-primary targets? debuff damage? Should that also be a thing?

    void onTurnEnd(Combatant self);
}
