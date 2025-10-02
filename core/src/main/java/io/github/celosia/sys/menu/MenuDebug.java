package io.github.celosia.sys.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.celosia.sys.World;
import io.github.celosia.sys.input.InputLib;
import io.github.celosia.sys.input.Keybind;
import io.github.celosia.sys.render.Fonts;
import io.github.celosia.sys.render.RenderLib;

import static io.github.celosia.Main.MENU_LIST;
import static io.github.celosia.Main.stage3;
import static io.github.celosia.sys.menu.MenuLib.setTextIfChanged;

public class MenuDebug {

    private static int lines;
    private final static TypingLabel TEXT = new TypingLabel("", Fonts.FontType.KORURI.get(30));

    public static void create(MenuLib.MenuType menuType) {
        if (menuType == MenuLib.MenuType.DEBUG_TEXT) {
            lines = 1;
            TEXT.setPosition(World.WIDTH_2, World.HEIGHT_2);
            stage3.addActor(TEXT);
        }
    }

    // todo cleanup
    public static void input(MenuLib.MenuType menuType) {
        if (menuType == MenuLib.MenuType.DEBUG_TEXT) {
            setTextIfChanged(TEXT, "omg its hatsune miku!" + "\nomg miku!!!".repeat(Math.max(0, lines + 1)));

            TEXT.setX(World.WIDTH - TEXT.getWidth());

            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                lines++;
            }

            if (InputLib.checkInput(Keybind.LEFT)) {
                if (InputLib.checkInput(Keybind.UP)) {
                    TEXT.setAlignment(Align.topLeft);
                } else if (InputLib.checkInput(Keybind.DOWN)) {
                    TEXT.setAlignment(Align.bottomLeft);
                } else {
                    TEXT.setAlignment(Align.left);
                }
            } else if (InputLib.checkInput(Keybind.RIGHT)) {
                if (InputLib.checkInput(Keybind.UP)) {
                    TEXT.setAlignment(Align.topRight);
                } else if (InputLib.checkInput(Keybind.DOWN)) {
                    TEXT.setAlignment(Align.bottomRight);
                } else {
                    TEXT.setAlignment(Align.right);
                }
            } else if (InputLib.checkInput(Keybind.UP)) {
                TEXT.setAlignment(Align.top);
            } else if (InputLib.checkInput(Keybind.DOWN)) {
                TEXT.setAlignment(Align.bottom);
            } else if (InputLib.checkInput(Keybind.MENU)) {
                TEXT.setAlignment(Align.center);
            } else if (InputLib.checkInput(Keybind.BACK)) {
                stage3.getRoot().removeActor(TEXT);
                MENU_LIST.removeLast();
            }
        } else if (menuType == MenuLib.MenuType.DEBUG_RES) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                RenderLib.changeScale(1280, 720);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                RenderLib.changeScale(1920, 1080);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                RenderLib.changeScale(2560, 1440);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
                RenderLib.changeScale(3840, 2160);
            }
        }
    }
}
