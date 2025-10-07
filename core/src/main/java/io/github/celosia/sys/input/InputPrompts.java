package io.github.celosia.sys.input;

import static io.github.celosia.sys.menu.MenuLib.toGlyph;
import static io.github.celosia.sys.save.Lang.lang;

public class InputPrompts {

    public static final InputPrompt CONFIRM = new InputPrompt(lang.get("input.confirm"),
            toGlyph(Keybind.CONFIRM.getKey()),
            Keybind.CONFIRM.getButton().getGlyphs());
    public static final InputPrompt BACK = new InputPrompt(lang.get("input.back"), toGlyph(Keybind.BACK.getKey()),
            Keybind.BACK.getButton().getGlyphs());
    public static final InputPrompt BACK_LOG = new InputPrompt(lang.get("input.back"),
            toGlyph(Keybind.BACK.getKey()) + "/" + toGlyph(Keybind.MENU.getKey()), Keybind.BACK.getButton().getGlyphs(),
            Keybind.MENU.getButton().getGlyphs());
    public static final InputPrompt MOVE = new InputPrompt(lang.get("input.move"),
            toGlyph(Keybind.UP.getKey()) + "/" + toGlyph(Keybind.DOWN.getKey()) + "/" + toGlyph(Keybind.LEFT.getKey()) +
                    "/" + toGlyph(Keybind.RIGHT.getKey()),
            Keybind.UP.getButton().getGlyphs(), Keybind.DOWN.getButton().getGlyphs(),
            Keybind.LEFT.getButton().getGlyphs(), Keybind.RIGHT.getButton().getGlyphs());
    public static final InputPrompt MOVE_UD = new InputPrompt(lang.get("input.move"),
            toGlyph(Keybind.UP.getKey()) + "/" + toGlyph(Keybind.DOWN.getKey()), Keybind.UP.getButton().getGlyphs(),
            Keybind.DOWN.getButton().getGlyphs());
    public static final InputPrompt MOVE_LR = new InputPrompt(lang.get("input.move"),
            toGlyph(Keybind.LEFT.getKey()) + "/" + toGlyph(Keybind.RIGHT.getKey()),
            Keybind.LEFT.getButton().getGlyphs(), Keybind.RIGHT.getButton().getGlyphs());
    public static final InputPrompt CLOSE = new InputPrompt(lang.get("input.close"),
            toGlyph(Keybind.CONFIRM.getKey()) + "/" + toGlyph(Keybind.BACK.getKey()),
            Keybind.CONFIRM.getButton().getGlyphs(), Keybind.BACK.getButton().getGlyphs());
    public static final InputPrompt TOP = new InputPrompt(lang.get("input.top"), toGlyph(Keybind.PAGE_L2.getKey()),
            Keybind.PAGE_L2.getButton().getGlyphs());
    public static final InputPrompt BOTTOM = new InputPrompt(lang.get("input.bottom"),
            toGlyph(Keybind.PAGE_R2.getKey()),
            Keybind.PAGE_R2.getButton().getGlyphs());
    public static final InputPrompt LOG = new InputPrompt(lang.get("input.log"), toGlyph(Keybind.MENU.getKey()),
            Keybind.MENU.getButton().getGlyphs());
    public static final InputPrompt INSPECT = new InputPrompt(lang.get("input.inspect"), toGlyph(Keybind.MAP.getKey()),
            Keybind.MAP.getButton().getGlyphs());

    // Inspect menu
    public static final InputPrompt I_STAT = new InputPrompt(lang.get("input.inspect.stat"),
            toGlyph(Keybind.MENU.getKey()), Keybind.MENU.getButton().getGlyphs());
    public static final InputPrompt I_AFFINITY = new InputPrompt(lang.get("input.inspect.affinity"),
            toGlyph(Keybind.MAP.getKey()), Keybind.MAP.getButton().getGlyphs());
    public static final InputPrompt I_EQUIP = new InputPrompt(lang.get("input.inspect.equip"),
            toGlyph(Keybind.CONFIRM.getKey()), Keybind.CONFIRM.getButton().getGlyphs());
    public static final InputPrompt I_MULT = new InputPrompt(lang.get("input.inspect.mult"),
            toGlyph(Keybind.PAGE_L1.getKey()), Keybind.PAGE_L1.getButton().getGlyphs());
    public static final InputPrompt I_MOD = new InputPrompt(lang.get("input.inspect.mod"),
            toGlyph(Keybind.PAGE_L2.getKey()), Keybind.PAGE_R1.getButton().getGlyphs());
    public static final InputPrompt I_OTHER = new InputPrompt(lang.get("input.inspect.other"),
            toGlyph(Keybind.UP.getKey()) + "/" + toGlyph(Keybind.DOWN.getKey()), Keybind.UP.getButton().getGlyphs(),
            Keybind.DOWN.getButton().getGlyphs());
}
