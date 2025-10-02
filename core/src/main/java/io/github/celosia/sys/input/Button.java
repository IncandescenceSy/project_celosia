package io.github.celosia.sys.input;

import static io.github.celosia.sys.save.Lang.lang;

// Controller button and stick mappings
public enum Button {

    B(lang.get("button.b"), "B"),
    A(lang.get("button.a"), "A"),
    Y(lang.get("button.y"), "Y"),
    X(lang.get("button.x"), "X"),
    DL(lang.get("key.left"), "DL"), // Dpad left
    DR(lang.get("key.right"), "DR"), // Dpad right
    DU(lang.get("key.up"), "DU"), // Dpad up
    DD(lang.get("key.down"), "DD"), // Dpad down
    LB(lang.get("button.l1"), "LB"),
    RB(lang.get("button.r1"), "RB"),
    LT(lang.get("button.l2"), "LT"),
    RT(lang.get("button.r2"), "RT"),
    LM(lang.get("button.lm"), "LM"), // Left menu / - / share / 2 boxes
    RM(lang.get("button.rm"), "RM"), // Right menu / + / options / hamburger

    // Non-remappable
    LX("LX", "LX"), // Left stick X-axis
    LY("LY", "LY"), // Left stick Y-axis
    RX("RX", "RX"), // Right stick X-axis
    RY("RY", "RY"); // Right stick Y-axis

    private int mapping = -1;
    private final String name;

    // NSW, PS, XB
    private final String[] glyphs;

    Button(String name, String glyph) {
        this.name = name;
        this.glyphs = new String[] { "[+NSW_" + glyph + "]", "[+PS_" + glyph + "]", "[+XB_" + glyph + "]" };
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

    public String[] getGlyphs() {
        return glyphs;
    }
}
