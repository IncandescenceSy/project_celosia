package io.github.celosia.sys;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import io.github.celosia.sys.menu.ControllerType;
import io.github.celosia.sys.menu.InputLib;

import static io.github.celosia.sys.menu.ControllerType.NONE;
import static io.github.celosia.sys.menu.InputLib.getControllerType;

// Handle detecting whether a keyboard or a controller is being used, and finding the currently in-use controller
public class InputHandler extends InputAdapter implements ControllerListener {
	// Currently in-use controller
	private static Controller controller;

	// The kind of controller the last input came from
	// NONE means the last input came from a keyboard
	private static ControllerType lastUsedController = NONE;

	// Get mappings of and set current controller
	public static void checkController() {
		controller = Controllers.getCurrent();
		if (controller != null) {
			InputLib.setupController(controller);
		}
	}

	// Keyboard input events
	@Override
	public boolean keyDown(int keycode) {
		lastUsedController = NONE;
		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		lastUsedController = NONE;
		return super.keyDown(keycode);
	}

	// Controller input events
	@Override
	public void connected(Controller controller) {
		lastUsedController = getControllerType(controller);
	}

	@Override
	public void disconnected(Controller controller) {
		lastUsedController = NONE;
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		lastUsedController = getControllerType(controller);
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		lastUsedController = getControllerType(controller);
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		// Ignore small stick movement
		if (Math.abs(value) > 0.2f) {
			lastUsedController = getControllerType(controller);
		}
		return false;
	}

	public static Controller getController() {
		return controller;
	}

	public static boolean isLastUsedController() {
		return lastUsedController != NONE;
	}

	public static ControllerType getLastUsedController() {
		return lastUsedController;
	}
}
