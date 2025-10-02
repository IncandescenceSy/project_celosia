package io.github.celosia.sys.battle;

public class BuffInstance {

    private final Buff buff;
    private int turns;
    private int stacks;

    public BuffInstance(Buff buff, int turns, int stacks) {
        this.buff = buff;
        this.turns = turns;
        this.stacks = stacks;
    }

    public Buff getBuff() {
        return buff;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public int getTurns() {
        return turns;
    }

    public void setStacks(int stacks) {
        this.stacks = stacks;
    }

    public int getStacks() {
        return stacks;
    }
}
