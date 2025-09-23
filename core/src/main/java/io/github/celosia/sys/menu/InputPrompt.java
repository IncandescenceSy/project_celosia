package io.github.celosia.sys.menu;

import io.github.celosia.sys.InputHandler;

import static io.github.celosia.sys.menu.TextLib.mergeGlyphs;

public class InputPrompt {
	private final String glyphKey;
	private final String[][] glyphsButton;
	private final String text;

	InputPrompt(String text, String glyphKey, String[]... glyphsButton) {
		this.glyphKey = glyphKey;
		this.glyphsButton = glyphsButton;
		this.text = text;
	}

	public String getText() {
		ControllerType lastUsedController = InputHandler.getLastUsedController();
		StringBuilder glyphBuilder = new StringBuilder();

		if (lastUsedController == ControllerType.NONE) {
			glyphBuilder = new StringBuilder(glyphKey);
		} else
			for (String[] glyphButton : glyphsButton) {
				glyphBuilder.append(glyphButton[lastUsedController.ordinal() - 1]);
			}

		return mergeGlyphs(glyphBuilder.toString()) + " " + text;
	}
}
