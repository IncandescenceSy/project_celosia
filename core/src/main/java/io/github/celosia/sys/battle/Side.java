package io.github.celosia.sys.battle;

public enum Side {
	// spotless:off
	ALLY(0),
    OPPONENT(1),
    BOTH(2);
    // spotless:on

	final int id;

	Side(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
