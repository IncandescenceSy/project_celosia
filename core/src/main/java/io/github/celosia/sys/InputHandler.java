package io.github.celosia.sys;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import io.github.celosia.sys.menu.LabelStyles;
import io.github.celosia.sys.menu.MenuLib;
import io.github.celosia.sys.menu.MenuLib.MenuOptType;
import io.github.celosia.sys.menu.MenuLib.MenuType;
import io.github.celosia.sys.menu.Menus;
import io.github.celosia.sys.settings.Keybinds;

import java.util.List;

// todo
public class InputHandler {

    // Current menu info
    static int index = 0;
    static float cooldown = 0f;
    static MenuOptType optSelected;

    static Label wip;

    static Label debug;

    public static void init(Stage stage) {
        // Debug
        debug = new Label("", LabelStyles.KORURI_40);
        debug.setPosition(45, World.HEIGHT - 130);
        stage.addActor(debug);
    }

    public static MenuType handleInput(MenuType menuType, Stage stage, List<Label> optLabels) {
        debug.setText("fps: " + 1f / Gdx.graphics.getDeltaTime() + "\nactors on stage: " + stage.getActors().size + "\nindex = " + index + "\nmenuType = " + menuType);

        MenuType curMenu = MenuType.values()[menuType.ordinal()];
        switch(curMenu) {
            case MAIN:
                // Handle menu navigation
                Number[] menuStats = MenuLib.handleMenu1D(index, MenuType.MAIN, cooldown);
                index = (int) menuStats[0];
                cooldown = (float) menuStats[1];

                // Handle option color
                MenuLib.handleOptColor(optLabels, index);

                // Handle menu confirmation
                if (Gdx.input.isKeyPressed(Keybinds.CONFIRM.getKey())) {
                    optSelected = MenuLib.MenuOptType.values()[MenuType.MAIN.getOpt(index).getType().ordinal()];
                    switch (optSelected) {
                        case START:
                            MenuLib.removeOpts(optLabels, stage);
                            // Start game
                            return Menus.createMenuWIP(menuType, stage);
                        case QUIT:
                            // Quit game
                            Gdx.app.exit();
                        case MANUAL:
                        case OPTIONS:
                        case CREDITS:
                            MenuLib.removeOpts(optLabels, stage);
                            return Menus.createMenuWIP(menuType, stage);
                    }
                }
                break;
            case WIP:
                if(Gdx.input.isKeyPressed(Keybinds.BACK.getKey())) {
                    stage.getRoot().removeActor(wip);
                    return Menus.createMenuMain(menuType, stage, optLabels);
                }
        }
        return menuType;
    }
}
