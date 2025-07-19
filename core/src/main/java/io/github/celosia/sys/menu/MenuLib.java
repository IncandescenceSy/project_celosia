package io.github.celosia.sys.menu;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.celosia.sys.settings.Keybind;

import java.util.List;

import static io.github.celosia.sys.settings.Lang.lang;

public class MenuLib {
    // Check menu movement input (1-axis) and handle wrapping
    // todo support multi-axis menus + support mouse/touchscreen
    public static int checkMovement1D(int index, int optCount, Controller controller) {
        if (InputLib.checkInput(controller, true, Keybind.UP, Keybind.LEFT)) {
            return --index < 0 ? optCount - 1 : index;
        } else if (InputLib.checkInput(controller, true, Keybind.DOWN, Keybind.RIGHT)) {
            return ++index >= optCount ? 0 : index;
        } else return index;
    }

    // Check menu movement input (targeting) and handle wrapping
    // todo: complex targeting (dread)
    public static int checkMovementTargeting(int index, Controller controller) {
        if (InputLib.checkInput(controller, true, Keybind.UP)) {
            if (index < 4) { // On player side
                return --index < 0 ? 3 : index;
            } else return --index < 4 ? 7 : index;
        } else if (InputLib.checkInput(controller, true, Keybind.DOWN)) {
            if (index < 4) { // On player side
                return ++index >= 4 ? 0 : index;
            } else return ++index >= 8 ? 4 : index;
        } else if (InputLib.checkInput(controller, true, Keybind.LEFT, Keybind.RIGHT)) {
            return (index < 4) ? index + 4 : index - 4;
        } else return index;
    }

    // Change menu option colors to indicate selection
    public static void handleOptColor(List<TypingLabel> opts, int index) {
        for(int i = 0; i < opts.size(); i++) {
            if (index == i) opts.get(i).setColor(Color.PINK);
            else opts.get(i).setColor(Color.WHITE);
        }
    }

    // Moves the cursor around and creates cursor afterimages
    // index = selected option
    // xL = left of cursor rect at top of box
    // xR = right of cursor rect at top of box
    // yT = top of cursor rect at top of box
    // yOff = how much it should move down per index
    public static void handleCursor(CoolRect cursor, CoolRect afterimage, int index, int xL, int xR, int yT, int yOff) {
        int tOld = cursor.getT();
        int tNew = yT - (yOff * index);

        // Value changed, restart animation and create afterimage
        if (tNew != tOld && tOld != 0) {
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
        cursor.setL(xL - (yOff * index) / 6);
        cursor.setR(xR - (yOff * index) / 6);
    }

    // Creates Labels and adds them to a Stage and a List
    public static void createOpts(MenuType menuType, List<TypingLabel> labels, Font font, Stage stage) {
        for(int i = 0; i < menuType.getOptCount(); i++) {
            MenuOpt opt = menuType.getOpt(i);
            TypingLabel label = new TypingLabel(opt.getText(), font);
            labels.add(label);
            label.setPosition(opt.getPosX(), opt.getPosY(), Align.left);
            stage.addActor(label);
        }
    }

    // Removes Labels from a Stage and a List
    public static void removeOpts(List<TypingLabel> labels, Stage stage) {
        for(TypingLabel label : labels) {
            //labels.remove(label);
            stage.getRoot().removeActor(label);
        }
    }

    public static void setTextIfChanged(TypingLabel label, String text) {
        if (!label.getOriginalText().toString().equals("{NORMAL}" + text)) label.setText(text); // todo idk if this works on text with tokens
    }

    // The different menus in the game
    // todo lang
    public enum MenuType {
        NONE(),
        WIP(),
        MAIN(new MenuOpt(MenuOptType.START, "{SPEED=0.2}{FADE}{SHRINK}" + lang.get("menu.start"), 1900 + 80, 230 + 400),
            new MenuOpt(MenuOptType.MANUAL, "{SPEED=0.2}{FADE}{SHRINK}" + lang.get("menu.manual"), 1900 + 60, 230 + 300),
            new MenuOpt(MenuOptType.OPTIONS, "{SPEED=0.2}{FADE}{SHRINK}" + lang.get("menu.options"), 1900 + 40, 230 + 200),
            new MenuOpt(MenuOptType.CREDITS, "{SPEED=0.2}{FADE}{SHRINK}" + lang.get("menu.credits"), 1900 + 20, 230 + 100),
            new MenuOpt(MenuOptType.QUIT, "{SPEED=0.2}{FADE}{SHRINK}" + lang.get("menu.quit"), 1900, 230)),
        BATTLE(),
        TARGETING(),
        SKILLS();

        private MenuOpt[] opts = new MenuOpt[]{new MenuOpt(MenuOptType.NONE, "", 0, 0)};

        MenuType() {
        }

        MenuType(MenuOpt... opts) {
            this.opts = opts;
        }

        public int getOptCount() {
            return opts.length;
        }

        public MenuOpt getOpt(int index) {
            return opts[index];
        }

        public MenuOpt[] getOpts() {
            return opts;
        }
    }

    // The different menu options in the game
    public enum MenuOptType {
        NONE,
        START,
        QUIT,
        OPTIONS,
        MANUAL,
        CREDITS;
    }
}
