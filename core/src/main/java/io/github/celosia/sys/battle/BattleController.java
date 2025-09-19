package io.github.celosia.sys.battle;

import com.badlogic.gdx.Gdx;
import io.github.celosia.sys.Debug;
import io.github.celosia.sys.menu.InputLib;
import io.github.celosia.sys.menu.MenuLib.MenuType;
import io.github.celosia.sys.settings.Keybind;

import static io.github.celosia.Main.menuList;
import static io.github.celosia.sys.battle.BattleControllerLib.createFullLog;
import static io.github.celosia.sys.battle.BattleControllerLib.handleBattle;
import static io.github.celosia.sys.battle.BattleControllerLib.handleDebug;
import static io.github.celosia.sys.battle.BattleControllerLib.handleLog;
import static io.github.celosia.sys.battle.BattleControllerLib.handleSetup;
import static io.github.celosia.sys.battle.BattleControllerLib.handleTargeting;

public class BattleController {
	// Time to wait in seconds
	public static float wait = 0f;

	// Initialize battle
	public static void create() {
		handleSetup();
	}

	public static void input() {
		// Debug
		if (Debug.enableDebugHotkeys) {
			handleDebug();
		}

		if (menuList.getLast() == MenuType.LOG) { // Fullscreen log
			handleLog();
		} else if (InputLib.checkInput(Keybind.MENU)) { // Create fullscreen log
			createFullLog();
		} else if (wait > 0f) {
            wait -= Gdx.graphics.getDeltaTime();
        } else if (menuList.getLast() == MenuType.BATTLE) { // Selecting and executing moves
			handleBattle();
		} else if (menuList.getLast() == MenuType.TARGETING) { // Picking a target
			handleTargeting();
		}
	}
}
