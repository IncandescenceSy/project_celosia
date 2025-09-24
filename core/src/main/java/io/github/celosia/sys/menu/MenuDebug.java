package io.github.celosia.sys.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.celosia.sys.World;
import io.github.celosia.sys.settings.Keybind;

import static io.github.celosia.Main.menuList;
import static io.github.celosia.Main.stage3;
import static io.github.celosia.sys.menu.MenuLib.setTextIfChanged;

public class MenuDebug {
	private static int lines;
	private final static TypingLabel text = new TypingLabel("", Fonts.FontType.KORURI.get(30));

	public static void create(MenuLib.MenuType menuType) {
		if (menuType == MenuLib.MenuType.DEBUG_TEXT) {
			lines = 1;
			text.setPosition(World.WIDTH_2, World.HEIGHT_2);
			stage3.addActor(text);
		}
	}

	public static void input(MenuLib.MenuType menuType) {
		if (menuType == MenuLib.MenuType.DEBUG_TEXT) {
			StringBuilder str = new StringBuilder("omg its hatsune miku!");
			str.append("\nomg miku!!!".repeat(Math.max(0, lines + 1)));
			setTextIfChanged(text, str.toString());

			text.setX(World.WIDTH - text.getWidth());

			if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
				lines++;
			}

			if (InputLib.checkInput(Keybind.LEFT)) {
				if (InputLib.checkInput(Keybind.UP)) {
					text.setAlignment(Align.topLeft);
				} else if (InputLib.checkInput(Keybind.DOWN)) {
					text.setAlignment(Align.bottomLeft);
				} else {
					text.setAlignment(Align.left);
				}
			} else if (InputLib.checkInput(Keybind.RIGHT)) {
				if (InputLib.checkInput(Keybind.UP)) {
					text.setAlignment(Align.topRight);
				} else if (InputLib.checkInput(Keybind.DOWN)) {
					text.setAlignment(Align.bottomRight);
				} else {
					text.setAlignment(Align.right);
				}
			} else if (InputLib.checkInput(Keybind.UP)) {
				text.setAlignment(Align.top);
			} else if (InputLib.checkInput(Keybind.DOWN)) {
				text.setAlignment(Align.bottom);
			} else if (InputLib.checkInput(Keybind.MENU)) {
				text.setAlignment(Align.center);
			} else if (InputLib.checkInput(Keybind.BACK)) {
				stage3.getRoot().removeActor(text);
				menuList.removeLast();
			}
		}
	}
}
