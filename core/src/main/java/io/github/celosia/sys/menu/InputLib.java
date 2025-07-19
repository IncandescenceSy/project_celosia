package io.github.celosia.sys.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;
import io.github.celosia.sys.Debug;
import io.github.celosia.sys.settings.Keybind;

public class InputLib {
    // How long each keybind has been held down for
    private static final float[] held = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};

    // Sets up button mappings based on the current controller's mapping
    // todo make sure nothing breaks if a controller is missing buttons
    public static void setupController(Controller controller) {
        ControllerMapping mapping = controller.getMapping();

        if (Debug.swapFaceButtons) {
            Button.B.setMapping(mapping.buttonA);
            Button.A.setMapping(mapping.buttonB);
            Button.Y.setMapping(mapping.buttonX);
            Button.X.setMapping(mapping.buttonY);
        } else{
            Button.B.setMapping(mapping.buttonB);
            Button.A.setMapping(mapping.buttonA);
            Button.Y.setMapping(mapping.buttonY);
            Button.X.setMapping(mapping.buttonX);
        }
        Button.DL.setMapping(mapping.buttonDpadLeft);
        Button.DR.setMapping(mapping.buttonDpadRight);
        Button.DU.setMapping(mapping.buttonDpadUp);
        Button.DD.setMapping(mapping.buttonDpadDown);
        Button.L1.setMapping(mapping.buttonL1);
        Button.L2.setMapping(mapping.buttonL2);
        Button.R1.setMapping(mapping.buttonR1);
        Button.R2.setMapping(mapping.buttonR2);
        Button.LX.setMapping(mapping.axisLeftX);
        Button.LY.setMapping(mapping.axisLeftY);
        Button.RX.setMapping(mapping.axisRightX);
        Button.RY.setMapping(mapping.axisRightY);
    }

    // Checks for input
    public static boolean checkInput(Controller controller, boolean allowHold, Keybind... keybinds) {
        for(Keybind keybind : keybinds) if (isKeybindPressed(keybind, controller, allowHold)) return true;
        return false;
    }

    public static boolean checkInput(Controller controller, Keybind... keybinds) {
        return checkInput(controller, false, keybinds);
    }

    public static boolean isKeybindPressed(Keybind keybind, Controller controller, boolean allowHold) {
        if (held[keybind.ordinal()] == 0f) { // Keybind wasn't pressed last frame or has been held
            if (checkKeybind(keybind, controller)) { // Keybind is pressed now
                held[keybind.ordinal()] += Gdx.graphics.getDeltaTime();
                return true;
            }
        } else if (allowHold && held[keybind.ordinal()] >= 0.4f) { // Keybind has been held
            if (checkKeybind(keybind, controller)) { // Keybind is pressed now
                held[keybind.ordinal()] = 0.25f; // Make it take a moment
                return true;
            }
         } else if (checkKeybind(keybind, controller)) { // Keybind has been held, but not for long enough
            held[keybind.ordinal()] += Gdx.graphics.getDeltaTime();
        } else { // Keybind isn't pressed
            held[keybind.ordinal()] = 0;
        }

        return false;
    }

    public static boolean checkKeybind(Keybind keybind, Controller controller) {
        return Gdx.input.isKeyPressed(keybind.getKey()) || (controller != null && (controller.getButton(keybind.getButton().getMapping()) || checkKeybind2Stick(keybind, controller)));
    }

    // todo: stick sensitivity setting?
    public static boolean checkKeybind2Stick(Keybind keybind, Controller controller) {
        switch(keybind) {
            case LEFT:
                return checkAxis(controller, Button.LX.getMapping(), Button.RX.getMapping(), -0.5f);
            case RIGHT:
                return checkAxis(controller, Button.LX.getMapping(), Button.RX.getMapping(), 0.5f);
            case UP:
                return checkAxis(controller, Button.LY.getMapping(), Button.RY.getMapping(), -0.5f);
            case DOWN:
                return checkAxis(controller, Button.LY.getMapping(), Button.RY.getMapping(), 0.5f);
            default:
                return false;
        }
    }

    // Checks both sticks to see if they're tilted further than a specified value
    public static boolean checkAxis(Controller controller, int mapping, int mapping2, float dist) {
        return controller != null && (dist > 0f ? (controller.getAxis(mapping) > dist || controller.getAxis(mapping2) > dist) : (controller.getAxis(mapping) < dist || controller.getAxis(mapping2) < dist));
    }
}
