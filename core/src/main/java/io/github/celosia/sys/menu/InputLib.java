package io.github.celosia.sys.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;
import io.github.celosia.sys.Debug;
import io.github.celosia.sys.InputHandler;
import io.github.celosia.sys.settings.Keybind;
import io.github.celosia.sys.settings.Settings;

public class InputLib {
    // How long each keybind has been held down for
    private static final double[] held = new double[12];

    // Sets up button mappings based on the current controller's mapping
    // todo make sure nothing breaks if a controller is missing buttons
    public static void setupController(Controller controller) {
        ControllerMapping mapping = controller.getMapping();

        if (Debug.alwaysUseNintendoLayout || (Settings.detectNintendoController && isNintendoController(controller))) {
            Button.B.setMapping(mapping.buttonA);
            Button.A.setMapping(mapping.buttonB);
        } else {
            Button.B.setMapping(mapping.buttonB);
            Button.A.setMapping(mapping.buttonA);
        }
        Button.Y.setMapping(mapping.buttonY);
        Button.X.setMapping(mapping.buttonX);
        Button.DL.setMapping(mapping.buttonDpadLeft);
        Button.DR.setMapping(mapping.buttonDpadRight);
        Button.DU.setMapping(mapping.buttonDpadUp);
        Button.DD.setMapping(mapping.buttonDpadDown);
        Button.L1.setMapping(mapping.buttonL1);
        Button.R1.setMapping(mapping.buttonR1);
        if(Debug.treatTriggersAsAxes) {
            Button.L2.setMapping(4);
            Button.R2.setMapping(5);
        } else {
            Button.L2.setMapping(mapping.buttonL2);
            Button.R2.setMapping(mapping.buttonR2);
        }
        Button.LX.setMapping(mapping.axisLeftX);
        Button.LY.setMapping(mapping.axisLeftY);
        Button.RX.setMapping(mapping.axisRightX);
        Button.RY.setMapping(mapping.axisRightY);
    }

    // Checks for input between the keyboard and the currently in-use controller
    public static boolean checkInput(boolean allowHold, Keybind... keybinds) {
        for(Keybind keybind : keybinds) if (isKeybindPressed(keybind, allowHold)) return true;
        return false;
    }

    public static boolean checkInput(Keybind... keybinds) {
        return checkInput(false, keybinds);
    }

    public static boolean isKeybindPressed(Keybind keybind, boolean allowHold) {
        if (held[keybind.ordinal()] == 0f) { // Keybind wasn't pressed last frame or has been held
            if (checkKeybind(keybind)) { // Keybind is pressed now
                held[keybind.ordinal()] += Gdx.graphics.getDeltaTime();
                return true;
            }
        } else if (allowHold && held[keybind.ordinal()] >= 0.3f) { // Keybind has been held
            if (checkKeybind(keybind)) { // Keybind is pressed now
                held[keybind.ordinal()] = 0.2f; // Make it take a moment
                return true;
            }
         } else if (checkKeybind(keybind)) { // Keybind has been held, but not for long enough
            held[keybind.ordinal()] += Gdx.graphics.getDeltaTime();
        } else { // Keybind isn't pressed
            held[keybind.ordinal()] = 0;
        }

        return false;
    }

    public static boolean checkKeybind(Keybind keybind) {
        return Gdx.input.isKeyPressed(keybind.getKey()) || (InputHandler.getController() != null && (InputHandler.getController().getButton(keybind.getButton().getMapping()) || checkKeybind2Axis(keybind)));
    }

    // todo: stick sensitivity setting? also dont make L1/R1 automatically map to R stick
    public static boolean checkKeybind2Axis(Keybind keybind) {
        return switch (keybind) {
            case LEFT -> checkAxis(Button.LX.getMapping(), -0.4f);
            case RIGHT -> checkAxis(Button.LX.getMapping(), 0.4f);
            case UP -> checkAxis(Button.LY.getMapping(), -0.4f);
            case DOWN -> checkAxis(Button.LY.getMapping(), 0.4f);
            case PAGE_L1 -> checkAxis(Button.RY.getMapping(), -0.4f);
            case PAGE_R1 -> checkAxis(Button.RY.getMapping(), 0.4f);
            case PAGE_L2 -> checkAxis(Button.L2.getMapping(), 0.4f);
            case PAGE_R2 -> checkAxis(Button.R2.getMapping(), 0.4f);
            default -> false;
        };
    }

    // Checks a stick axis to see if it's tilted further than a specified value
    public static boolean checkAxis(int mapping, float dist) {
        return InputHandler.getController() != null && (dist > 0f ? InputHandler.getController().getAxis(mapping) > dist : InputHandler.getController().getAxis(mapping) < dist);
    }

    // Tries to determine if a controller is a Nintendo controller via its name
    public static boolean isNintendoController(Controller controller) {
        String name = controller.getName().toLowerCase();
        return name.contains("nintendo") || name.contains("switch");
    }
}
