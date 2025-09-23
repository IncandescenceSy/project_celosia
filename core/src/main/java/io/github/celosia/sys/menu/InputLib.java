package io.github.celosia.sys.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;
import io.github.celosia.sys.Debug;
import io.github.celosia.sys.InputHandler;
import io.github.celosia.sys.settings.Keybind;
import io.github.celosia.sys.settings.Settings;

import static io.github.celosia.Main.inputGuide;

public class InputLib {
	// How long each keybind has been held down for
	private static final double[] held = new double[12];

	// Default holdDelay (time between triggers when holding keybind down)
	private static final float HOLD_DELAY = 0.1f;

	// Sets up button mappings based on the current controller's mapping
	// todo make sure nothing breaks if a controller is missing buttons
	public static void setupController(Controller controller) {
		ControllerMapping mapping = controller.getMapping();

		if (Debug.alwaysUseNSWLayout
				|| (Settings.detectNintendoController && getControllerType(controller) == ControllerType.NSW)) {
			Button.B.setMapping(mapping.buttonA);
			Button.A.setMapping(mapping.buttonB);
			Button.Y.setMapping(mapping.buttonX);
			Button.X.setMapping(mapping.buttonY);
		} else {
			Button.B.setMapping(mapping.buttonB);
			Button.A.setMapping(mapping.buttonA);
			Button.Y.setMapping(mapping.buttonY);
			Button.X.setMapping(mapping.buttonX);
		}
		Button.DL.setMapping(mapping.buttonDpadLeft);
		Button.DR.setMapping(mapping.buttonDpadRight);
		Button.DU.setMapping(mapping.buttonDpadUp);
		Button.DD.setMapping(mapping.buttonDpadDown);
		Button.LB.setMapping(mapping.buttonL1);
		Button.RB.setMapping(mapping.buttonR1);
		if (Debug.treatTriggersAsAxes) {
			Button.LT.setMapping(4);
			Button.RT.setMapping(5);
		} else {
			Button.LT.setMapping(mapping.buttonL2);
			Button.RT.setMapping(mapping.buttonR2);
		}
		Button.LX.setMapping(mapping.axisLeftX);
		Button.LY.setMapping(mapping.axisLeftY);
		Button.RX.setMapping(mapping.axisRightX);
		Button.RY.setMapping(mapping.axisRightY);
	}

	// Checks for input between the keyboard and the currently in-use controller
	public static boolean checkInput(boolean allowHold, float holdDelay, Keybind... keybinds) {
		for (Keybind keybind : keybinds)
			if (isKeybindPressed(keybind, allowHold, holdDelay))
				return true;
		return false;
	}

	public static boolean checkInput(boolean allowHold, Keybind... keybinds) {
		return checkInput(allowHold, HOLD_DELAY, keybinds);
	}

	public static boolean checkInput(Keybind... keybinds) {
		return checkInput(false, HOLD_DELAY, keybinds);
	}

	public static boolean isKeybindPressed(Keybind keybind, boolean allowHold, float holdDelay) {
		if (held[keybind.ordinal()] == 0f) { // Keybind wasn't pressed last frame or has been held
			if (checkKeybind(keybind)) { // Keybind is pressed now
				held[keybind.ordinal()] += Gdx.graphics.getDeltaTime();
				return true;
			}
		} else if (allowHold && held[keybind.ordinal()] >= 0.3f) { // Keybind has been held
			if (checkKeybind(keybind)) { // Keybind is pressed now
				held[keybind.ordinal()] = 0.3f - holdDelay; // Make it take a moment
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
		return Gdx.input.isKeyPressed(keybind.getKey()) || (InputHandler.getController() != null
				&& (InputHandler.getController().getButton(keybind.getButton().getMapping())
						|| checkKeybind2Axis(keybind)));
	}

	// todo: stick sensitivity setting? also dont make L1/R1 automatically map to R
	// stick
	public static boolean checkKeybind2Axis(Keybind keybind) {
		return switch (keybind) {
			case LEFT -> checkAxis(Button.LX.getMapping(), -0.4f);
			case RIGHT -> checkAxis(Button.LX.getMapping(), 0.4f);
			case UP -> checkAxis(Button.LY.getMapping(), -0.4f);
			case DOWN -> checkAxis(Button.LY.getMapping(), 0.4f);
			case PAGE_L1 -> checkAxis(Button.RY.getMapping(), -0.4f);
			case PAGE_R1 -> checkAxis(Button.RY.getMapping(), 0.4f);
			case PAGE_L2 -> checkAxis(Button.LT.getMapping(), 0.4f);
			case PAGE_R2 -> checkAxis(Button.RT.getMapping(), 0.4f);
			default -> false;
		};
	}

	// Checks a stick axis to see if it's tilted further than a specified value
	public static boolean checkAxis(int mapping, float dist) {
		return InputHandler.getController() != null && (dist > 0f
				? InputHandler.getController().getAxis(mapping) > dist
				: InputHandler.getController().getAxis(mapping) < dist);
	}

	// Tries to determine what kind of controller is in use via its name. Defaults
	// to XB
	public static ControllerType getControllerType(Controller controller) {
		String name = controller.getName().toLowerCase();

		if (name.contains("nintendo") || name.contains("switch")) {
			return ControllerType.NSW;
		}

		if (name.contains("sony") || name.contains("playstation")) {
			return ControllerType.PS;
		}

		return ControllerType.XB;
	}

	public static void setInputGuideText(MenuLib.MenuType menuType) {
		switch (menuType) {
			case POPUP :
				inputGuide.setText(getInputString(InputPrompts.CLOSE));
				break;
			case MAIN :
				inputGuide.setText(getInputString(InputPrompts.CONFIRM, InputPrompts.MOVE_UD));
				break;
			case BATTLE :
				inputGuide.setText(getInputString(InputPrompts.MOVE_UD, InputPrompts.CONFIRM, InputPrompts.BACK,
						InputPrompts.LOG, InputPrompts.INSPECT));
				break;
			case TARGETING :
				inputGuide.setText(
						getInputString(InputPrompts.MOVE, InputPrompts.CONFIRM, InputPrompts.BACK, InputPrompts.LOG));
				break;
			case LOG :
				inputGuide.setText(getInputString(InputPrompts.MOVE_UD, InputPrompts.TOP, InputPrompts.BOTTOM,
						InputPrompts.BACK_LOG));
				break;
			case DEBUG :
				inputGuide.setText("1 Toggle Debug Info  2 Text Debug Menu  " + getInputString(InputPrompts.BACK));
				break;
			case DEBUG_TEXT :
				inputGuide.setText("←→↑↓ Alignment  " + getInputString(InputPrompts.BACK));
				break;
			default :
				inputGuide.setText("");
				break;
		}
	}

	public static String getInputString(InputPrompt... inputPrompts) {
		StringBuilder inputs = new StringBuilder();

		for (InputPrompt inputPrompt : inputPrompts) {
			inputs.append(inputPrompt.getText());
			inputs.append("  ");
		}

		return inputs.toString();
	}

	// Converts a keycode to a glyph
	// todo should eg ) be [+KB_Shift][+KB_Zero]
	public static String toGlyph(int keycode) {
		if (keycode < 0) {
			throw new IllegalArgumentException("keycode cannot be negative, keycode: " + keycode);
		}
		if (keycode > Input.Keys.MAX_KEYCODE) {
			throw new IllegalArgumentException("keycode cannot be greater than 255, keycode: " + keycode);
		}

		return switch (keycode) {
			case Input.Keys.UNKNOWN -> "Unknown";
			case Input.Keys.SOFT_LEFT -> "Soft Left";
			case Input.Keys.SOFT_RIGHT -> "Soft Right";
			case Input.Keys.HOME -> "[+KB_Home]";
			case Input.Keys.BACK -> "Back";
			case Input.Keys.CALL -> "Call";
			case Input.Keys.ENDCALL -> "End Call";
			case Input.Keys.NUM_0 -> "[+KB_Zero]";
			case Input.Keys.NUM_1 -> "[+KB_One]";
			case Input.Keys.NUM_2 -> "[+KB_Two]";
			case Input.Keys.NUM_3 -> "[+KB_Three]";
			case Input.Keys.NUM_4 -> "[+KB_Four]";
			case Input.Keys.NUM_5 -> "[+KB_Five]";
			case Input.Keys.NUM_6 -> "[+KB_Six]";
			case Input.Keys.NUM_7 -> "[+KB_Seven]";
			case Input.Keys.NUM_8 -> "[+KB_Eight]";
			case Input.Keys.NUM_9 -> "[+KB_Nine]";
			case Input.Keys.STAR -> "[+KB_Asterisk]";
			case Input.Keys.POUND -> "#";
			case Input.Keys.UP -> "[+KB_Up]";
			case Input.Keys.DOWN -> "[+KB_Down]";
			case Input.Keys.LEFT -> "[+KB_Left]";
			case Input.Keys.RIGHT -> "[+KB_Right]";
			case Input.Keys.CENTER -> "Center";
			case Input.Keys.VOLUME_UP -> "Vol Up";
			case Input.Keys.VOLUME_DOWN -> "Vol Down";
			case Input.Keys.POWER -> "Power";
			case Input.Keys.CAMERA -> "Camera";
			case Input.Keys.CLEAR -> "Clear";
			case Input.Keys.A -> "[+KB_A]";
			case Input.Keys.B -> "[+KB_B]";
			case Input.Keys.C -> "[+KB_C]";
			case Input.Keys.D -> "[+KB_D]";
			case Input.Keys.E -> "[+KB_E]";
			case Input.Keys.F -> "[+KB_F]";
			case Input.Keys.G -> "[+KB_G]";
			case Input.Keys.H -> "[+KB_H]";
			case Input.Keys.I -> "[+KB_I]";
			case Input.Keys.J -> "[+KB_J]";
			case Input.Keys.K -> "[+KB_K]";
			case Input.Keys.L -> "[+KB_L]";
			case Input.Keys.M -> "[+KB_M]";
			case Input.Keys.N -> "[+KB_N]";
			case Input.Keys.O -> "[+KB_O]";
			case Input.Keys.P -> "[+KB_P]";
			case Input.Keys.Q -> "[+KB_Q]";
			case Input.Keys.R -> "[+KB_R]";
			case Input.Keys.S -> "[+KB_S]";
			case Input.Keys.T -> "[+KB_T]";
			case Input.Keys.U -> "[+KB_U]";
			case Input.Keys.V -> "[+KB_V]";
			case Input.Keys.W -> "[+KB_W]";
			case Input.Keys.X -> "[+KB_X]";
			case Input.Keys.Y -> "[+KB_Y]";
			case Input.Keys.Z -> "[+KB_Z]";
			case Input.Keys.COMMA -> ",";
			case Input.Keys.PERIOD -> ".";
			case Input.Keys.ALT_LEFT -> "L[+KB_Alt]";
			case Input.Keys.ALT_RIGHT -> "R[+KB_Alt]";
			case Input.Keys.SHIFT_LEFT -> "L[+KB_Shift]";
			case Input.Keys.SHIFT_RIGHT -> "R[+KB_Shift]";
			case Input.Keys.TAB -> "[+Tab]";
			case Input.Keys.SPACE -> "[+Space]";
			case Input.Keys.SYM -> "SYM";
			case Input.Keys.EXPLORER -> "Explorer";
			case Input.Keys.ENVELOPE -> "Envelope";
			case Input.Keys.ENTER -> "Enter";
			case Input.Keys.DEL -> "[+KB_Backspace]"; // also BACKSPACE
			case Input.Keys.GRAVE -> "`";
			case Input.Keys.MINUS -> "[+KB_Minus]";
			case Input.Keys.EQUALS -> "=";
			case Input.Keys.LEFT_BRACKET -> "[+KB_Bracket_Open]";
			case Input.Keys.RIGHT_BRACKET -> "[+KB_Bracket_Close]";
			case Input.Keys.BACKSLASH -> "\\";
			case Input.Keys.SEMICOLON -> "[+KB_Semicolon]";
			case Input.Keys.APOSTROPHE -> "'";
			case Input.Keys.SLASH -> "[+KB_Slash]";
			case Input.Keys.AT -> "@";
			case Input.Keys.NUM -> "Num";
			case Input.Keys.HEADSETHOOK -> "Headset Hook";
			case Input.Keys.FOCUS -> "Focus";
			case Input.Keys.PLUS -> "[+KB_Plus]";
			case Input.Keys.MENU -> "Menu";
			case Input.Keys.NOTIFICATION -> "Notification";
			case Input.Keys.SEARCH -> "Search";
			case Input.Keys.MEDIA_PLAY_PAUSE -> "Play/Pause";
			case Input.Keys.MEDIA_STOP -> "Stop Media";
			case Input.Keys.MEDIA_NEXT -> "Next Media";
			case Input.Keys.MEDIA_PREVIOUS -> "Prev Media";
			case Input.Keys.MEDIA_REWIND -> "Rewind";
			case Input.Keys.MEDIA_FAST_FORWARD -> "Fast Forward";
			case Input.Keys.MUTE -> "Mute";
			case Input.Keys.PAGE_UP -> "[+KB_PageUp]";
			case Input.Keys.PAGE_DOWN -> "[+KB_PageDown]";
			case Input.Keys.PICTSYMBOLS -> "PICTSYMBOLS";
			case Input.Keys.SWITCH_CHARSET -> "SWITCH_CHARSET";
			case Input.Keys.FORWARD_DEL -> "Forward Delete";
			case Input.Keys.CONTROL_LEFT -> "L[+KB_Ctrl]";
			case Input.Keys.CONTROL_RIGHT -> "R[+KB_Ctrl]";
			case Input.Keys.ESCAPE -> "[+KB_Esc]";
			case Input.Keys.END -> "[+KB_End]";
			case Input.Keys.INSERT -> "Insert";
			case Input.Keys.NUMPAD_0 -> "Num [+KB_Zero]";
			case Input.Keys.NUMPAD_1 -> "Num [+KB_One]";
			case Input.Keys.NUMPAD_2 -> "Num [+KB_Two]";
			case Input.Keys.NUMPAD_3 -> "Num [+KB_Three]";
			case Input.Keys.NUMPAD_4 -> "Num [+KB_Four]";
			case Input.Keys.NUMPAD_5 -> "Num [+KB_Five]";
			case Input.Keys.NUMPAD_6 -> "Num [+KB_Six]";
			case Input.Keys.NUMPAD_7 -> "Num [+KB_Seven]";
			case Input.Keys.NUMPAD_8 -> "Num [+KB_Eight]";
			case Input.Keys.NUMPAD_9 -> "Num [+KB_Nine]";
			case Input.Keys.COLON -> ":";
			case Input.Keys.F1 -> "[+KB_F1]";
			case Input.Keys.F2 -> "[+KB_F2]";
			case Input.Keys.F3 -> "[+KB_F3]";
			case Input.Keys.F4 -> "[+KB_F4]";
			case Input.Keys.F5 -> "[+KB_F5]";
			case Input.Keys.F6 -> "[+KB_F6]";
			case Input.Keys.F7 -> "[+KB_F7]";
			case Input.Keys.F8 -> "[+KB_F8]";
			case Input.Keys.F9 -> "[+KB_F9]";
			case Input.Keys.F10 -> "[+KB_F10]";
			case Input.Keys.F11 -> "[+KB_F11]";
			case Input.Keys.F12 -> "[+KB_F12]";
			case Input.Keys.F13 -> "F13";
			case Input.Keys.F14 -> "F14";
			case Input.Keys.F15 -> "F15";
			case Input.Keys.F16 -> "F16";
			case Input.Keys.F17 -> "F17";
			case Input.Keys.F18 -> "F18";
			case Input.Keys.F19 -> "F19";
			case Input.Keys.F20 -> "F20";
			case Input.Keys.F21 -> "F21";
			case Input.Keys.F22 -> "F22";
			case Input.Keys.F23 -> "F23";
			case Input.Keys.F24 -> "F24";
			case Input.Keys.NUMPAD_DIVIDE -> "Num [+KB_Slash]";
			case Input.Keys.NUMPAD_MULTIPLY -> "Num [+KB_Asterisk]";
			case Input.Keys.NUMPAD_SUBTRACT -> "Num [+KB_Minus]";
			case Input.Keys.NUMPAD_ADD -> "Num [+KB_Plus]";
			case Input.Keys.NUMPAD_DOT -> "Num .";
			case Input.Keys.NUMPAD_COMMA -> "Num ,";
			case Input.Keys.NUMPAD_ENTER -> "Num [+KB_Enter]";
			case Input.Keys.NUMPAD_EQUALS -> "Num =";
			case Input.Keys.NUMPAD_LEFT_PAREN -> "Num (";
			case Input.Keys.NUMPAD_RIGHT_PAREN -> "Num )";
			case Input.Keys.NUM_LOCK -> "[+KB_Num_Lock]";
			case Input.Keys.CAPS_LOCK -> "[+KB_Caps_Lock]";
			case Input.Keys.SCROLL_LOCK -> "Scroll Lock";
			case Input.Keys.PAUSE -> "Pause";
			case Input.Keys.PRINT_SCREEN -> "[+KB_Prt_Scrn]";
			default -> null; // key name not found
		};
	}
}
