package io.github.celosia.sys.menu;

import static io.github.celosia.sys.settings.Lang.lang;

// Controller button and stick mappings
public enum Button {
    B(lang.get("button.b")),
    A(lang.get("button.a")),
    Y(lang.get("button.y")),
    X(lang.get("button.x")),
    DL(lang.get("key.left")), // Dpad left
    DR(lang.get("key.right")), // Dpad right
    DU(lang.get("key.up")), // Dpad up
    DD(lang.get("key.down")), // Dpad down
    L1(lang.get("button.l1")),
    R1(lang.get("button.r1")),
    L2(lang.get("button.l2")),
    R2(lang.get("button.r2")),
    LX(""), // Left stick X-axis
    LY(""), // Left stick Y-axis
    RX(""), // Right stick X-axis
    RY(""); // Right stick Y-axis

    private int mapping = -1;
    private final String name;

    Button(String name) {
        this.name = name;
    }

    public void setMapping(int mapping) {
        this.mapping = mapping;
    }

    public int getMapping() {
        return mapping;
    }

    public String getName() {
        return name;
    }
}
