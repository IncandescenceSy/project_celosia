package io.github.celosia.sys.menu;

import io.github.celosia.sys.settings.Keybind;

import static io.github.celosia.sys.menu.MenuLib.toGlyph;
import static io.github.celosia.sys.settings.Lang.lang;

public class InputPrompts {
	static InputPrompt CONFIRM = new InputPrompt(lang.get("input.confirm"), toGlyph(Keybind.CONFIRM.getKey()),
			Keybind.CONFIRM.getButton().getGlyphs());
	static InputPrompt BACK = new InputPrompt(lang.get("input.back"), toGlyph(Keybind.BACK.getKey()),
			Keybind.BACK.getButton().getGlyphs());
	static InputPrompt BACK_LOG = new InputPrompt(lang.get("input.back"),
			toGlyph(Keybind.BACK.getKey()) + "/" + toGlyph(Keybind.MENU.getKey()), Keybind.BACK.getButton().getGlyphs(),
			Keybind.MENU.getButton().getGlyphs());
	static InputPrompt MOVE = new InputPrompt(lang.get("input.move"),
			toGlyph(Keybind.UP.getKey()) + "/" + toGlyph(Keybind.DOWN.getKey()) + "/" + toGlyph(Keybind.LEFT.getKey())
					+ "/" + toGlyph(Keybind.RIGHT.getKey()),
			Keybind.UP.getButton().getGlyphs(), Keybind.DOWN.getButton().getGlyphs(),
			Keybind.LEFT.getButton().getGlyphs(), Keybind.RIGHT.getButton().getGlyphs());
	static InputPrompt MOVE_UD = new InputPrompt(lang.get("input.move"),
			toGlyph(Keybind.UP.getKey()) + "/" + toGlyph(Keybind.DOWN.getKey()), Keybind.UP.getButton().getGlyphs(),
			Keybind.DOWN.getButton().getGlyphs());
	static InputPrompt MOVE_LR = new InputPrompt(lang.get("input.move"),
			toGlyph(Keybind.LEFT.getKey()) + "/" + toGlyph(Keybind.RIGHT.getKey()),
			Keybind.LEFT.getButton().getGlyphs(), Keybind.RIGHT.getButton().getGlyphs());
	static InputPrompt CLOSE = new InputPrompt(lang.get("input.close"),
			toGlyph(Keybind.CONFIRM.getKey()) + "/" + toGlyph(Keybind.BACK.getKey()),
			Keybind.CONFIRM.getButton().getGlyphs(), Keybind.BACK.getButton().getGlyphs());
	static InputPrompt TOP = new InputPrompt(lang.get("input.top"), toGlyph(Keybind.PAGE_L2.getKey()),
			Keybind.PAGE_L2.getButton().getGlyphs());
	static InputPrompt BOTTOM = new InputPrompt(lang.get("input.bottom"), toGlyph(Keybind.PAGE_R2.getKey()),
			Keybind.PAGE_R2.getButton().getGlyphs());
	static InputPrompt LOG = new InputPrompt(lang.get("input.log"), toGlyph(Keybind.MENU.getKey()),
			Keybind.MENU.getButton().getGlyphs());
	static InputPrompt INSPECT = new InputPrompt(lang.get("input.inspect"), toGlyph(Keybind.MAP.getKey()),
			Keybind.MAP.getButton().getGlyphs());
}
