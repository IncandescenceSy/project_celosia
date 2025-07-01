package io.github.celosia.sys.menu;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import io.github.celosia.sys.World;
import io.github.celosia.sys.menu.MenuLib.MenuType;

import java.util.ArrayList;
import java.util.List;

public class Menus {

    public static MenuType createMenuMain(MenuType menuType, Stage stage, List<Label> optLabels) {
        // Create options
        optLabels = new ArrayList<Label>();
        MenuLib.createOpts(menuType, optLabels, LabelStyles.KORURI_80, stage);

        return MenuType.MAIN;
    }

    public static MenuType createMenuWIP(MenuType menuType, Stage stage) {
        // todo lang
        Label wip = new Label("This is a work in progress", LabelStyles.KORURI_80);
        wip.setPosition(World.WIDTH_2, World.HEIGHT_2, Align.center);
        stage.addActor(wip);
        return MenuType.WIP;
    }
}
