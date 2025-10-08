package io.github.celosia.sys.battle;

import com.badlogic.gdx.Gdx;
import io.github.celosia.sys.Debug;
import io.github.celosia.sys.input.InputLib;
import io.github.celosia.sys.input.Keybind;
import io.github.celosia.sys.menu.MenuLib.MenuType;

import static io.github.celosia.Main.NAV_PATH;
import static io.github.celosia.sys.battle.BattleControllerLib.createFullLog;
import static io.github.celosia.sys.battle.BattleControllerLib.createInspectTargeting;
import static io.github.celosia.sys.battle.BattleControllerLib.handleBattle;
import static io.github.celosia.sys.battle.BattleControllerLib.handleDebug;
import static io.github.celosia.sys.battle.BattleControllerLib.handleInspect;
import static io.github.celosia.sys.battle.BattleControllerLib.handleInspectTargeting;
import static io.github.celosia.sys.battle.BattleControllerLib.handleLog;
import static io.github.celosia.sys.battle.BattleControllerLib.handleSetup;
import static io.github.celosia.sys.battle.BattleControllerLib.handleTargeting;

public class BattleController {

    public static float delayS = 0f;

    // Initialize battle
    public static void create() {
        handleSetup();
    }

    public static void input() {
        // Debug
        if (Debug.enableDebugHotkeys) {
            handleDebug();
        }

        MenuType curMenu = NAV_PATH.getLast();

        if (curMenu == MenuType.LOG) {
            handleLog();
        } else if (curMenu == MenuType.INSPECT) {
            handleInspect();
        } else if (InputLib.checkInput(Keybind.MENU)) {
            createFullLog();
        } else if (curMenu == MenuType.INSPECT_TARGETING) {
            handleInspectTargeting();
        } else if (curMenu == MenuType.TARGETING) {
            handleTargeting();
        } else if (InputLib.checkInput(Keybind.MAP)) {
            createInspectTargeting();
        }

        // Battle progress comes after this check
        else if (delayS > 0f) {
            delayS -= Gdx.graphics.getDeltaTime();
        }
        // Selecting and executing moves
        else if (curMenu == MenuType.BATTLE) {
            handleBattle();
        }
    }
}
