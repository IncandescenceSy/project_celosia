package io.github.celosia;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.celosia.sys.battle.*;
import io.github.celosia.sys.menu.Fonts;
import io.github.celosia.sys.menu.LabelStyles;
import io.github.celosia.sys.menu.MenuLib;
import io.github.celosia.sys.menu.MenuLib.MenuOptType;
import io.github.celosia.sys.menu.MenuLib.MenuType;
import io.github.celosia.sys.World;
import io.github.celosia.sys.settings.Keybinds;

import java.util.ArrayList;
import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    SpriteBatch spriteBatch;
    Stage stage;

    // Current menu info
    int index = 0;
    float cooldown = 0f;
    MenuType menuType = MenuType.NONE;
    MenuOptType optSelected;

    // Menu option labels
    List<Label> optLabels = new ArrayList<Label>();

    Label wip;
    Label debug;

    // todo move
    Texture texBg;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        stage = new Stage(new FitViewport(World.WIDTH, World.HEIGHT));

        Fonts.createFonts();
        LabelStyles.createStyles();

        // Background
        texBg = new Texture("bg.png");

        // Debug
        debug = new Label("", LabelStyles.KORURI_20);
        debug.setPosition(20, World.HEIGHT - 70);
        stage.addActor(debug);

        // Setup main menu
        createMenuMain();
    }

    @Override
    public void render() {
        input();
        draw();
    }

    private void input() {
        // debug
        debug.setText("fps: " + 1f / Gdx.graphics.getDeltaTime() + "\nactors on stage: " + stage.getActors().size + "\nindex = " + index + "\nmenuType = " + menuType);

        switch(menuType) {
            case MAIN:
                // Handle menu navigation
                Number[] menuStats = MenuLib.handleMenu1D(index, MenuType.MAIN, cooldown);
                index = (int) menuStats[0];
                cooldown = (float) menuStats[1];

                // Handle option color
                MenuLib.handleOptColor(optLabels, index);

                // Handle menu confirmation
                if (Gdx.input.isKeyPressed(Keybinds.Keybind.CONFIRM.getKey())) {
                    optSelected = MenuOptType.values()[MenuType.MAIN.getOpt(index).getType().ordinal()];
                    switch (optSelected) {
                        case START:
                            MenuLib.removeOpts(optLabels, stage);
                            // Start game
                            BattleController.create(stage);
                            menuType = MenuType.BATTLE;
                            break;
                        case QUIT:
                            // Quit game
                            Gdx.app.exit();
                        case MANUAL:
                        case OPTIONS:
                        case CREDITS:
                            MenuLib.removeOpts(optLabels, stage);
                            createMenuWIP();
                            break;
                    }
                }
                break;
            case WIP:
                if(Gdx.input.isKeyPressed(Keybinds.Keybind.BACK.getKey())) {
                    stage.getRoot().removeActor(wip);
                    createMenuMain();
                }
            case BATTLE:
                BattleController.input();
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(texBg, 0, 0, World.WIDTH, World.HEIGHT);
        spriteBatch.end();

        stage.act();
        stage.draw();
    }

    private void createMenuMain() {
        menuType = MenuType.MAIN;

        // Create options
        optLabels = new ArrayList<Label>();
        MenuLib.createOpts(menuType, optLabels, LabelStyles.KORURI_80, stage);
    }

    private void createMenuWIP() {
        menuType = MenuType.WIP;

        // todo lang
        wip = new Label("This is a work in progress", LabelStyles.KORURI_80);
        wip.setPosition(World.WIDTH_2, World.HEIGHT_2, Align.center);
        stage.addActor(wip);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        stage.dispose();
    }
}
