package io.github.celosia.sys.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import org.jetbrains.annotations.Nullable;

import static io.github.celosia.sys.input.ControllerType.NONE;
import static io.github.celosia.sys.input.InputLib.getControllerType;

public class InputHandler extends InputAdapter implements ControllerListener {

    // The controller the last input came from, or null
    @Nullable private static Controller controller;

    // The kind of controller the last input came from
    // NONE means the last input came from a keyboard
    private static ControllerType lastUsedControllerType = NONE;

    public static void checkController() {
        controller = Controllers.getCurrent();
        if (controller != null) {
            InputLib.setupController(controller);
        }
    }

    // Keyboard input events
    @Override
    public boolean keyDown(int keycode) {
        lastUsedControllerType = NONE;
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        lastUsedControllerType = NONE;
        return super.keyDown(keycode);
    }

    // Controller input events
    @Override
    public void connected(Controller controller) {
        lastUsedControllerType = getControllerType(controller);
    }

    @Override
    public void disconnected(Controller controller) {
        lastUsedControllerType = NONE;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        lastUsedControllerType = getControllerType(controller);
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        lastUsedControllerType = getControllerType(controller);
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        // Ignore small movement
        if (Math.abs(value) > 0.2f) {
            lastUsedControllerType = getControllerType(controller);
        }
        return false;
    }

    @Nullable
    public static Controller getController() {
        return controller;
    }

    public static boolean isLastUsedController() {
        return lastUsedControllerType != NONE;
    }

    public static ControllerType getLastUsedControllerType() {
        return lastUsedControllerType;
    }
}
