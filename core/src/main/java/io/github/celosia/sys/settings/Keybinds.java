package io.github.celosia.sys.settings;

import com.badlogic.gdx.Input;

import static io.github.celosia.sys.settings.Lang.lang;

// Todo: alternate keys, remapping, controller support
public enum Keybinds {
        CONFIRM(lang.get("confirm"), Input.Keys.Z),
        BACK(lang.get("back"), Input.Keys.X),
        MENU(lang.get("menu"), Input.Keys.C),
        ATTACK(lang.get("skill.attack"), Input.Keys.Z),
        DEFEND(lang.get("skill.defend"), Input.Keys.X),
        SKILLS(lang.get("skills"), Input.Keys.C),
        UP(lang.get("up"), Input.Keys.UP),
        LEFT(lang.get("left"), Input.Keys.LEFT),
        RIGHT(lang.get("right"), Input.Keys.RIGHT),
        DOWN(lang.get("down"), Input.Keys.DOWN);

        private final String name;
        private final int key;

        Keybinds(String name, int key) {
            this.name = name;
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public int getKey() {
                return key;
        }
}
