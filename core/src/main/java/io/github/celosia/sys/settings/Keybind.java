package io.github.celosia.sys.settings;

import com.badlogic.gdx.Input;
import io.github.celosia.sys.menu.Button;

import static io.github.celosia.sys.settings.Lang.lang;

// Todo: alternate keys, remapping
public enum Keybind {
	// spotless:off
	CONFIRM(lang.get("key.confirm"), Input.Keys.Z, Button.A), // (Bottom button)
	BACK(lang.get("key.back"), Input.Keys.X, Button.B), // (Right button)
	MENU(lang.get("key.menu"), Input.Keys.C, Button.Y), // (Top button) Also Open Full Log
	MAP(lang.get("key.map"), Input.Keys.V, Button.X), // (Left button) Also Inspect
	PAGE_L1(lang.get("key.page_l1"), Input.Keys.F, Button.LB),
    PAGE_R1(lang.get("key.page_r1"), Input.Keys.G, Button.RB),
    PAGE_L2(lang.get("key.page_l2"), Input.Keys.S, Button.LT),
    PAGE_R2(lang.get("key.page_r2"), Input.Keys.D, Button.RT),
    LEFT(lang.get("key.left"), Input.Keys.LEFT, Button.DL),
    RIGHT(lang.get("key.right"), Input.Keys.RIGHT, Button.DR),
    UP(lang.get("key.up"), Input.Keys.UP, Button.DU),
    DOWN(lang.get("key.down"), Input.Keys.DOWN, Button.DD);
    // spotless:on

	private final String name;
	private int key;
	private Button button;

	Keybind(String name, int key, Button button) {
		this.name = name;
		this.key = key;
		this.button = button;
	}

	public String getName() {
		return name;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getKey() {
		return key;
	}

	public void setButton(Button button) {
		this.button = button;
	}

	public Button getButton() {
		return button;
	}
}
