package io.github.celosia;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.tommyettinger.textra.TextraLabel;
import com.github.tommyettinger.textra.TypingConfig;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.celosia.sys.Debug;
import io.github.celosia.sys.InputHandler;
import io.github.celosia.sys.World;
import io.github.celosia.sys.battle.BattleController;
import io.github.celosia.sys.menu.*;
import io.github.celosia.sys.menu.Fonts.FontType;
import io.github.celosia.sys.menu.MenuLib.MenuOptType;
import io.github.celosia.sys.menu.MenuLib.MenuType;
import io.github.celosia.sys.settings.Keybind;
import io.github.celosia.sys.settings.Lang;
import io.github.celosia.sys.settings.Settings;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.menu.TriLib.drawCoolRects;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    SpriteBatch spriteBatch;
    PolygonSpriteBatch polygonSpriteBatch;
    ShapeDrawer drawer;
    Stage stage;
    InputHandler inputHandler;

    // Current menu info
    int index = 0;
    MenuType menuType;
    MenuOptType optSelected;

    // All cool rectangle elements with their interpolation progress
    List<CoolRect> coolRects = new ArrayList<>();

    // Menu option labels
    List<TypingLabel> optLabels = new ArrayList<>();

    TypingLabel wip;
    TypingLabel wip2;
    TextraLabel debug;

    // todo move
    Texture texBg;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        polygonSpriteBatch = new PolygonSpriteBatch();

        // todo figure out how to support changing window size and resolution
        stage = new Stage(new FitViewport(World.WIDTH, World.HEIGHT));

        // Handle detecting whether a keyboard or a controller is being used, and finding the currently in-use controller
        inputHandler = new InputHandler();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
        Controllers.addListener(inputHandler);

        // temp
        // todo less magic numbers
        coolRects.add(CoolRects.MENU_MAIN.ordinal(), new CoolRect(World.WIDTH - 700, 230 + 475 + 5, World.WIDTH - 175 - 92, 230 - 75 + 25, 1)); // Main menu bg
        coolRects.add(CoolRects.POPUP_CENTERED.ordinal(), new CoolRect(World.WIDTH_2 - 440, World.HEIGHT_2 - 200, World.WIDTH_2 + 440, World.HEIGHT_2 + 200)); // Centered popup bg
        coolRects.add(CoolRects.CURSOR_1.ordinal(), new CoolRect(1, Color.PURPLE, false)); // Menu cursor. X pos comes from 450 (y diff) / 6 (15-degree angle)
        coolRects.add(CoolRects.CURSOR_2.ordinal(), new CoolRect(-1, Color.PURPLE, false)); // Disappearing menu cursor

        // temp todo draw from atlas
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        Texture texture = new Texture(pixmap); //remember to dispose of later
        pixmap.dispose();
        TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);

        drawer = new ShapeDrawer(polygonSpriteBatch, region);

        Fonts.createFonts();
        LabelStyles.createStyles();

        // Background
        texBg = new Texture("bg.png");

        // Internal config
        TypingConfig.DEFAULT_SPEED_PER_CHAR = 0.01f;

        // Lang
        // Default (US English)
        Lang.createBundle();
        //Lang.createBundle(new Locale("ja", "JP"));


        menuType = MenuType.NONE;

        // Debug
        Debug.enableDebugHotkeys = true;
        Debug.showDebugInfo = true;
        Debug.swapFaceButtons = true;

        debug = new TextraLabel("", FontType.KORURI.getSize20());
        debug.setPosition(20, World.HEIGHT - 70);
        stage.addActor(debug);

        // Setup main menu
        menuType = MenuType.MAIN;
        createMenuMain();
    }

    @Override
    public void render() {
        input();
        draw();
    }

    private void input() {
        // debug
        if(Debug.showDebugInfo) debug.setText("fps: " + 1f / Gdx.graphics.getDeltaTime() + "\nactors on stage: " + stage.getActors().size + "\nindex = " + index + "\nmenuType = " + menuType);

        // Get mappings of current controller
        InputHandler.checkController();

        switch(menuType) {
            case MAIN:
                // Handle menu navigation
                index = MenuLib.checkMovement1D(index, MenuType.MAIN.getOptCount());

                // Handle option color
                MenuLib.handleCursor(coolRects.get(CoolRects.CURSOR_1.ordinal()), coolRects.get(CoolRects.CURSOR_2.ordinal()), index, World.WIDTH - 700 + 75, World.WIDTH - 175 - 92 + 75 - 10, 230 + 475, 100 + 4);

                // Handle menu confirmation
                if (InputLib.checkInput(Keybind.CONFIRM)) {
                    optSelected = MenuOptType.values()[MenuType.MAIN.getOpt(index).getType().ordinal()];
                    switch (optSelected) {
                        case START:
                            coolRects.get(CoolRects.MENU_MAIN.ordinal()).setDir(-1);
                            coolRects.get(CoolRects.CURSOR_1.ordinal()).setDir(-1).setSpeed(2f);
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
                            createMenuWIP();
                            break;
                    }
                } else if (InputLib.checkInput(Keybind.BACK)) {
                    if(index == MenuType.MAIN.getOptCount() - 1) {
                        // Quit game
                        Gdx.app.exit();
                    } else index = MenuType.MAIN.getOptCount() - 1;
                }
                break;
            case WIP:
                if (InputLib.checkInput(Keybind.CONFIRM, Keybind.BACK)) {
                    stage.getRoot().removeActor(wip);
                    stage.getRoot().removeActor(wip2);
                    coolRects.get(CoolRects.POPUP_CENTERED.ordinal()).setDir(-1);
                    menuType = MenuType.MAIN;
                }
                break;
            case BATTLE:
            case TARGETING:
            case SKILLS:
                BattleController.updateStatDisplay();
                menuType = BattleController.input(menuType);
                break;
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(texBg, 0, 0, World.WIDTH * Settings.scale, World.HEIGHT * Settings.scale);
        spriteBatch.end();

        // Draw cool rectangles
        drawCoolRects(polygonSpriteBatch, drawer, coolRects);

        stage.act();
        stage.draw();
    }

    private void createMenuMain() {
        // Create options
        optLabels = new ArrayList<>();
        MenuLib.createOpts(menuType, optLabels, FontType.KORURI.getSize80(), stage);
    }

    private void createMenuWIP() {
        menuType = MenuType.WIP;

        wip = new TypingLabel("{SPEED=0.1}{FADE}{SHRINK}WIP", FontType.KORURI.getSize80());
        wip.setPosition(World.WIDTH_2, World.HEIGHT_2 + 120, Align.center);
        stage.addActor(wip);

        wip2 = new TypingLabel("{FADE}{SLIDE}This isn't finished yet!\n{WAVE=0.25;0.5;0.5}{GRADIENT=pink;violet}{SPIN}Girls are now praying, please wait warmly...", FontType.KORURI.getSize30());
        wip2.setPosition(World.WIDTH_2 - 440, World.HEIGHT_2 + 20, Align.left);
        stage.addActor(wip2);

        coolRects.get(CoolRects.POPUP_CENTERED.ordinal()).setDir(1);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        polygonSpriteBatch.dispose();
        stage.dispose();
        texBg.dispose();
    }
}
