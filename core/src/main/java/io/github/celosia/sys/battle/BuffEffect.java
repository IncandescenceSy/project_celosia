package io.github.celosia.sys.battle;

// Interface for applying skill effects
public interface BuffEffect {
    void onGive(Combatant self);

    void onRemove(Combatant self);

    void onUseSkill(Combatant self, Combatant target);

    void onTakeDamage(Combatant self);

    void onTurnEnd(Combatant self);
}
