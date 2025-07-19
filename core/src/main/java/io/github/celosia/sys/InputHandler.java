package io.github.celosia.sys;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;

// Only exists so any input will let the game know whether a keyboard or controller is being used. Does nothing else
public class InputHandler extends InputAdapter implements ControllerListener {
    private static boolean lastUsedController = false;

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
    }

    @Override
    public void disconnected(Controller controller) {
        lastUsedController = false;
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        lastUsedController = true;
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        lastUsedController = true;
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        // Ignore small stick movement
        if (Math.abs(value) > 0.2f) lastUsedController = true;
        return false;
    }

    public static boolean isLastUsedController() {
        return lastUsedController;
    }
}
