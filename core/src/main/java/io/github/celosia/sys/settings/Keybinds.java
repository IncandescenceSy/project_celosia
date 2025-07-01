package io.github.celosia.sys.settings;

import com.badlogic.gdx.Input;

public class Keybinds {
    // Todo: alternate keys, remapping, controller support
    public enum Keybind {
        CONFIRM(Input.Keys.Z),
        BACK(Input.Keys.X),
        MENU(Input.Keys.C),
        ATTACK(Input.Keys.Z),
        DEFEND(Input.Keys.X),
        SKILL(Input.Keys.C),
        UP(Input.Keys.UP),
        LEFT(Input.Keys.LEFT),
        RIGHT(Input.Keys.RIGHT),
        DOWN(Input.Keys.DOWN);

        private final int key;

        Keybind(int key) {
            this.key = key;
        }

        public int getKey() {
            return key;
        }
    }
}
