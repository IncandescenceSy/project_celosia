package io.github.celosia.sys.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import io.github.celosia.sys.settings.Keybinds;

import java.util.List;

import static java.lang.Math.max;

public class MenuLib {
    // Handle a 1-axis menu and returns the new index and cooldown
    public static Number[] handleMenu1D(int index, int optCount, float cooldown) {
        if (cooldown == 0f) {
            int newIndex = MenuLib.checkMovement1D(index, optCount);
            if (newIndex != index) { // Update
                return new Number[]{newIndex, 0.15f};
            } else return new Number[]{index, 0f}; // No change
        } else return new Number[]{index, max(0, cooldown - Gdx.graphics.getDeltaTime())}; // Reduce cooldown
    }

    // Check menu movement input (1-axis) and handle wrapping
    // todo: speed up when button has been held and support multi-axis menus and mouse/touchscreen
    public static int checkMovement1D(int index, int optCount) {
        if (Gdx.input.isKeyJustPressed(Keybinds.UP.getKey()) || Gdx.input.isKeyJustPressed(Keybinds.LEFT.getKey())) {
            return --index < 0 ? optCount - 1 : index--;
        } else if (Gdx.input.isKeyJustPressed(Keybinds.DOWN.getKey()) || Gdx.input.isKeyJustPressed(Keybinds.RIGHT.getKey())) {
            return ++index >= optCount ? 0 : index++;
        } else return index;
    }

    // Handle targeting menu and returns the new index and cooldown
    public static Number[] handleMenuTargeting(int index, float cooldown) {
        if (cooldown == 0f) {
            int newIndex = MenuLib.checkMovementTargeting(index);
            if (newIndex != index) { // Update
                return new Number[]{newIndex, 0.15f};
            } else return new Number[]{index, 0f}; // No change
        } else return new Number[]{index, max(0, cooldown - Gdx.graphics.getDeltaTime())}; // Reduce cooldown
    }

    // Check menu movement input (targeting) and handle wrapping
    // todo: speed up when button has been held and support multi-axis menus and mouse/touchscreen
    public static int checkMovementTargeting(int index) {
        if (Gdx.input.isKeyJustPressed(Keybinds.UP.getKey())) {
            if(index < 5) { // On player side
                return --index < 0 ? 4 : index--;
            } else return --index < 5 ? 9 : index--;
        } else if (Gdx.input.isKeyJustPressed(Keybinds.DOWN.getKey())) {
            if(index < 5) { // On player side
                return ++index >= 5 ? 0 : index++;
            } else return ++index >= 10 ? 5 : index++;
        } else if (Gdx.input.isKeyJustPressed(Keybinds.LEFT.getKey()) || Gdx.input.isKeyJustPressed(Keybinds.RIGHT.getKey())) {
            return (index < 5) ? index + 5 : index - 5;
        } else return index;
    }

    // Change menu option colors to indicate selection
    public static void handleOptColor(List<Label> opts, int index) {
        for(int i = 0; i < opts.size(); i++) {
            if(index == i) opts.get(i).setColor(Color.PINK);
            else opts.get(i).setColor(Color.WHITE);
        }
    }

    // Creates Labels and adds them to a Stage and a List
    public static void createOpts(MenuType menuType, List<Label> labels, LabelStyle style, Stage stage) {
        for(int i = 0; i < menuType.getOptCount(); i++) {
            MenuOpt opt = menuType.getOpt(i);
            Label label = new Label(opt.getText(), style);
            labels.add(label);
            label.setPosition(opt.getPosX(), opt.getPosY(), Align.center); // todo alignment
            stage.addActor(label);
        }
    }

    // Removes Labels from a Stage and a List
    public static void removeOpts(List<Label> labels, Stage stage) {
        for(Label label : labels) {
            //labels.remove(label);
            stage.getRoot().removeActor(label);
        }
    }

    // The different menus in the game
    // todo lang
    public enum MenuType {
        NONE(),
        WIP(),
        MAIN(new MenuOpt(MenuOptType.START, "Start", 1200, 1000),
            new MenuOpt(MenuOptType.MANUAL, "Manual", 1200, 800),
            new MenuOpt(MenuOptType.OPTIONS, "Options", 1200, 600),
            new MenuOpt(MenuOptType.CREDITS, "Credits", 1200, 400),
            new MenuOpt(MenuOptType.QUIT, "Quit", 1200, 200)),
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
