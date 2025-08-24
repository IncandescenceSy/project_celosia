package io.github.celosia.sys.battle;

// Interface for applying passive effects
public interface PassiveEffect {
    default String[] onBattleStart(Unit self) {
        return new String[]{""};
    }

    default String[] onGiveBuff(Unit self, Unit target, Buff buff, int turns, int stacks) {
        return new String[]{""};
    }

    default String[] onHeal(Unit self, Unit target, int heal, double overHeal) {
        return new String[]{""};
    }

    default String[] onGiveShield(Unit self, Unit target, int turns, int stacks) {
        return new String[]{""};
    }

    default String[] onChangeStage(Unit self, Unit target, StageType stageType, int turns, int stacks) {
        return new String[]{""};
    }
}
