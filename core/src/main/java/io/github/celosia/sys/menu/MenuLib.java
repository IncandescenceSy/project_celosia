package io.github.celosia.sys.menu;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.TextraLabel;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.celosia.sys.Debug;
import io.github.celosia.sys.battle.Range;
import io.github.celosia.sys.battle.Ranges;
import io.github.celosia.sys.battle.Side;
import io.github.celosia.sys.input.InputLib;
import io.github.celosia.sys.input.InputPrompt;
import io.github.celosia.sys.input.InputPrompts;
import io.github.celosia.sys.input.Keybind;
import io.github.celosia.sys.render.CoolRect;

import java.util.List;

import static io.github.celosia.Main.NAV_PATH;
import static io.github.celosia.Main.inputGuide;
import static io.github.celosia.sys.battle.PosLib.getRelativeSide;

public class MenuLib {

    // Check menu movement input (1-axis) and handle wrapping
    // todo support multi-axis menus + support mouse/touchscreen
    public static int checkMovement1D(int index, int optCount) {
        if (InputLib.checkInput(true, Keybind.UP, Keybind.LEFT)) {
            return --index < 0 ? optCount - 1 : index;
        }

        if (InputLib.checkInput(true, Keybind.DOWN, Keybind.RIGHT)) {
            return ++index >= optCount ? 0 : index;
        }

        return Math.min(index, optCount - 1);
    }

    public static int checkMovement1D(int index, int optCount, Keybind dec, Keybind inc) {
        if (InputLib.checkInput(true, dec)) {
            return --index < 0 ? optCount - 1 : index;
        }

        if (InputLib.checkInput(true, inc)) {
            return ++index >= optCount ? 0 : index;
        }

        return Math.min(index, optCount - 1);
    }

    // Check menu movement input (targeting) and handle wrapping
    public static int checkMovementTargeting(int index, int selectingMove, Range range) {
        // Lock cursor to self for self Ranges
        if (range == Ranges.SELF || range == Ranges.SELF_UP_DOWN) {
            return selectingMove;
        }

        int newIndex = index;

        // Move selection
        if (InputLib.checkInput(true, Keybind.UP)) {
            if (index < 4) { // On player side
                newIndex = (index - 1) < 0 ? 3 : index - 1;
            } else {
                newIndex = (index - 1) < 4 ? 7 : index - 1;
            }
        } else if (InputLib.checkInput(true, Keybind.DOWN)) {
            if (index < 4) { // On player side
                newIndex = (index + 1) >= 4 ? 0 : index + 1;
            } else {
                newIndex = (index + 1) >= 8 ? 4 : index + 1;
            }
        } else if (InputLib.checkInput(true, Keybind.LEFT, Keybind.RIGHT)) {
            newIndex = (index < 4) ? index + 4 : index - 4;
        }

        // Lock cursor to valid side
        if (range.side() == Side.BOTH) {
            return newIndex;
        }

        if (range.side() == getRelativeSide(selectingMove, newIndex)) {
            return newIndex;
        }

        return index;
    }

    // Check for scrolling the battle log
    public static int checkLogScroll(int logScroll, int lines, int off) {
        // Up
        if (InputLib.checkInput(true, 0.005f, Keybind.UP)) {
            return Math.min(++logScroll, Math.max(lines - off, 0));
        }

        // Down
        if (InputLib.checkInput(true, 0.005f, Keybind.DOWN)) {
            return Math.max(--logScroll, 0);
        }

        // To top
        if (InputLib.checkInput(false, Keybind.PAGE_L2)) {
            return Math.max(lines - off, 0);
        }

        // To bottom
        if (InputLib.checkInput(false, Keybind.PAGE_R2)) {
            return 0;
        }

        return logScroll;
    }

    // Change menu option colors to indicate selection
    public static void handleOptColor(TextraLabel[] opts, int index) {
        for (int i = 0; i < opts.length; i++) {
            if (index == i) {
                opts[i].setColor(Color.PINK);
            } else {
                opts[i].setColor(Color.WHITE);
            }
        }
    }

    public static void handleOptColor(List<TextraLabel> opts, int index) {
        for (int i = 0; i < opts.size(); i++) {
            if (index == i) {
                opts.get(i).setColor(Color.PINK);
            } else {
                opts.get(i).setColor(Color.WHITE);
            }
        }
    }

    // Moves the cursor around and creates cursor afterimages
    // index = selected option
    // xL = left of cursor rect at top of box
    // xR = right of cursor rect at top of box
    // yT = top of cursor rect at top of box
    // yOff = how much it should move down per index
    // todo fix rendering afterimage on menu switch
    public static void handleCursor(CoolRect cursor, CoolRect afterimage, int index, int xL, int xR, int yT, int yOff,
                                    boolean changeX) {
        int tOld = cursor.getT();
        int tNew = yT - (yOff * index);

        // Value changed, restart animation and create afterimage
        if (tNew != tOld && tOld != -1) {
            cursor.setProg(0f);
            cursor.setSpeed(4f);

            afterimage.setL(cursor.getL());
            afterimage.setT(tOld);
            afterimage.setR(cursor.getR());
            afterimage.setB(cursor.getB());

            afterimage.setProg(1f);
            afterimage.setSpeed(2f);
            afterimage.setDir(-1);
        }

        cursor.setT(tNew);
        cursor.setB(yT - (yOff * (index + 1)));
        if (changeX) {
            cursor.setL(xL - (yOff * index) / cursor.getSlantL());
            cursor.setR(xR - (yOff * index) / cursor.getSlantR());
        }
    }

    public static void handleCursor(CoolRect cursor, CoolRect afterimage, int index, int xL, int xR, int yT, int yOff) {
        handleCursor(cursor, afterimage, index, xL, xR, yT, yOff, true);
    }

    // Creates Labels and adds them to a Stage and a List
    public static void createOpts(List<TypingLabel> labels, Font font, Stage stage) {
        for (int i = 0; i < NAV_PATH.getLast().getOptCount(); i++) {
            MenuOpt opt = NAV_PATH.getLast().getOpt(i);
            TypingLabel label = new TypingLabel(opt.getText(), font);
            labels.add(label);
            label.setPosition(opt.getPosX(), opt.getPosY(), Align.left);
            stage.addActor(label);
        }
    }

    // Removes Labels
    public static void removeOpts(List<TypingLabel> labels, Stage stage) {
        for (TypingLabel label : labels) {
            stage.getRoot().removeActor(label);
        }
    }

    public static void setTextIfChanged(TypingLabel label, String text) {
        if (!label.getOriginalText().toString().equals("{NORMAL}" + text)) {
            label.setText(text); // todo idk if this works on text with tokens
        }
    }

    public static void setInputGuideText(MenuType menuType) {
        switch (menuType) {
            case POPUP:
                inputGuide.setText(getInputString(InputPrompts.CLOSE));
                break;
            case MAIN:
                inputGuide.setText(getInputString(InputPrompts.CONFIRM, InputPrompts.MOVE_UD) +
                        ((Debug.showDebugInfo && Debug.enableDebugHotkeys) ? "[+KB_ONE] Debug" : ""));
                break;
            case BATTLE:
                inputGuide.setText(getInputString(InputPrompts.MOVE_UD, InputPrompts.CONFIRM, InputPrompts.BACK,
                        InputPrompts.LOG, InputPrompts.INSPECT));
                break;
            case TARGETING, INSPECT_TARGETING:
                inputGuide.setText(
                        getInputString(InputPrompts.MOVE, InputPrompts.CONFIRM, InputPrompts.BACK, InputPrompts.LOG));
                break;
            case LOG:
                inputGuide.setText(getInputString(InputPrompts.MOVE_UD, InputPrompts.TOP, InputPrompts.BOTTOM,
                        InputPrompts.BACK_LOG));
                break;
            case DEBUG:
                inputGuide.setText(
                        "[+KB_ONE] Toggle Debug Info  [+KB_TWO] Text Debug Menu  [+KB_THREE] Resolution Debug Menu  " +
                                getInputString(InputPrompts.BACK));
                break;
            case DEBUG_TEXT:
                inputGuide.setText(
                        "[+KB_ONE] Add line  " + getInputString(InputPrompts.MOVE) + getInputString(InputPrompts.BACK));
                break;
            case DEBUG_RES:
                inputGuide.setText("[+KB_ONE][+KB_TWO][+KB_THREE][+KB_FOUR] Change resolution  " +
                        getInputString(InputPrompts.BACK));
                break;
            default:
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
    public static String toGlyph(int keycode) {
        // todo remove/catch errors depending on whats needed
        if (keycode < 0) {
            throw new IllegalArgumentException("keycode cannot be negative, keycode: " + keycode);
        }
        if (keycode > Input.Keys.MAX_KEYCODE) {
            throw new IllegalArgumentException("keycode cannot be greater than 255, keycode: " + keycode);
        }

        // todo remove unused keycodes (such as anything that requires holding shift)
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
            default -> ""; // key name not found
        };
    }
}
