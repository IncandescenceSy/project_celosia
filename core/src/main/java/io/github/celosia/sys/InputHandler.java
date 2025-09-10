package io.github.celosia.sys;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import io.github.celosia.sys.menu.InputLib;

import static io.github.celosia.sys.menu.InputLib.isNintendoController;

// Handle detecting whether a keyboard or a controller is being used, and finding the currently in-use controller
public class InputHandler extends InputAdapter implements ControllerListener {
	// Currently in-use controller
	private static Controller controller;

	// Whether the last input came from a controller
	private static boolean lastUsedController = false;

	// Whether the last input came from a Nintendo controller
	private static boolean lastUsedNintendoController = false;

	// Get mappings of and set current controller
	public static void checkController() {
		controller = Controllers.getCurrent();
		if (controller != null)
			InputLib.setupController(controller);
	}

	// Keyboard input events
	@Override
	public boolean keyDown(int keycode) {
		lastUsedController = false;
		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		lastUsedController = false;
		return super.keyDown(keycode);
	}

	// Controller input events
	@Override
	public void connected(Controller controller) {
		lastUsedController = true;
		lastUsedNintendoController = isNintendoController(controller);
	}

	@Override
	public void disconnected(Controller controller) {
		lastUsedController = false;
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		lastUsedController = true;
		lastUsedNintendoController = isNintendoController(controller);
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		lastUsedController = true;
		lastUsedNintendoController = isNintendoController(controller);
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// Ignore small stick movement
		if (Math.abs(value) > 0.2f) {
			lastUsedController = true;
			lastUsedNintendoController = isNintendoController(controller);
		}
		return false;
	}

	public static Controller getController() {
		return controller;
	}

	public static boolean isLastUsedController() {
		return lastUsedController;
	}

	public static boolean isLastUsedNintendoController() {
		return lastUsedNintendoController;
	}
}
