package io.github.celosia.sys.battle;

@FunctionalInterface
public interface BuffEffectNotifier {
    String[] notify(BuffEffect effect, Unit self, Unit target, int stacks, Object... args);
}
