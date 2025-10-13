package io.github.celosia;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.tommyettinger.textra.TextraLabel;
import com.github.tommyettinger.textra.TypingConfig;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.celosia.sys.Debug;
import io.github.celosia.sys.GameMod;
import io.github.celosia.sys.World;
import io.github.celosia.sys.battle.Accessory;
import io.github.celosia.sys.battle.BattleController;
import io.github.celosia.sys.battle.BattleControllerLib;
import io.github.celosia.sys.battle.Buff;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.Passive;
import io.github.celosia.sys.battle.Range;
import io.github.celosia.sys.battle.Skill;
import io.github.celosia.sys.battle.StageType;
import io.github.celosia.sys.battle.UnitType;
import io.github.celosia.sys.battle.Weapon;
import io.github.celosia.sys.input.InputHandler;
import io.github.celosia.sys.input.InputLib;
import io.github.celosia.sys.input.Keybind;
import io.github.celosia.sys.menu.MenuDebug;
import io.github.celosia.sys.menu.MenuLib;
import io.github.celosia.sys.menu.MenuOptType;
import io.github.celosia.sys.menu.MenuType;
import io.github.celosia.sys.render.CoolRect;
import io.github.celosia.sys.render.CoolRectBar;
import io.github.celosia.sys.render.CoolRectChain;
import io.github.celosia.sys.render.CoolRects;
import io.github.celosia.sys.render.Fonts;
import io.github.celosia.sys.render.Fonts.FontType;
import io.github.celosia.sys.render.Path;
import io.github.celosia.sys.render.TriLib;
import io.github.celosia.sys.save.Lang;
import io.github.celosia.sys.save.Settings;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.menu.MenuDebug.drawF3MenuBg;
import static io.github.celosia.sys.render.TriLib.drawShapes;
import static io.github.celosia.sys.util.TextLib.rf;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
// todo cleanup
public class Main extends ApplicationAdapter {

    static InputHandler inputHandler;

    static int index = 0;
    public static final ArrayDeque<MenuType> NAV_PATH = new ArrayDeque<>();
    static MenuOptType optSelected;

    // Rendering
    public static FitViewport viewPort;
    public static Camera camera;

    public static ShapeDrawer drawer;
    public static SpriteBatch spriteBatch;
    public static PolygonSpriteBatch polygonSpriteBatch;

    // todo can know (unmodded) size exactly
    // todo rename
    public static CoolRect[] coolRects;
    public static CoolRectChain[] coolRectChains;
    public static CoolRectBar[] coolRectBars;
    public static Path[] paths;

    public static Stage stage0;
    public static Stage stage1;
    public static Stage stage2;
    public static Stage stage3;
    public static Stage stage4;

    // Atlases
    // From https://juliocacko.itch.io/free-input-prompts
    public static TextureAtlas atlasPrompts;

    // From https://github.com/tommyettinger/game-icons-net-atlas
    public static TextureAtlas atlasIcons;

    static List<TypingLabel> optLabels;

    public static TypingLabel popupTitle;
    public static TypingLabel popupText;

    static boolean isF3MenuEnabled = false;

    // todo move
    static Texture texBg;

    public static TextraLabel inputGuide;

    // Lists of stuff. Mostly for mods. todo impl + set exact sizes
    // New objects of these types are added automatically
    public static final List<Element> ELEMENTS = new ArrayList<>(8);
    public static final List<Skill> SKILLS = new ArrayList<>(512);
    public static final List<Buff> BUFFS = new ArrayList<>(256);
    public static final List<StageType> STAGE_TYPES = new ArrayList<>(4);
    public static final List<Passive> PASSIVES = new ArrayList<>(256);
    public static final List<Accessory> ACCESSORIES = new ArrayList<>(64);
    public static final List<Weapon> WEAPONS = new ArrayList<>(64);
    public static final List<UnitType> UNIT_TYPES = new ArrayList<>(64);
    public static final List<Range> RANGES = new ArrayList<>(16);

    // You have to add your mod to this List manually (maybe? todo)
    public static final List<GameMod> GAME_MODS = new ArrayList<>();

    @Override
    public void create() {
        // todo make assertions work in dev
        assert false;

        spriteBatch = new SpriteBatch();
        polygonSpriteBatch = new PolygonSpriteBatch();

        viewPort = new FitViewport(World.WIDTH, World.HEIGHT);
        camera = viewPort.getCamera();

        stage0 = new Stage(viewPort);
        stage1 = new Stage(viewPort);
        stage2 = new Stage(viewPort);
        stage3 = new Stage(viewPort);
        stage4 = new Stage(viewPort);

        atlasPrompts = new TextureAtlas("prompts.atlas");
        atlasIcons = new TextureAtlas("icons.atlas");

        inputHandler = new InputHandler();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
        Controllers.addListener(inputHandler);

        coolRects = new CoolRect[] {
                // MENU_MAIN
                new CoolRect.Builder(World.WIDTH - 700, 230 + 475 + 5, World.WIDTH - 175 - 92, 230 - 75 + 25).dir(1)
                        .prio(1).build(),
                // COVER_LEFT
                new CoolRect.Builder(5, World.HEIGHT + 100, 1300, -100).speed(4).slantL(0).prio(2).build(),
                // CURSOR_1
                new CoolRect.Builder(-1, -1, -1, -1).dir(1).color(Color.PURPLE).noOutline().prio(1).build(),
                // CURSOR_2 (afterimage)
                new CoolRect.Builder(-1, -1, -1, -1).color(Color.PURPLE).noOutline().prio(1).build(),
                // POPUP_CENTERED
                new CoolRect.Builder(World.WIDTH_2 - 440, World.HEIGHT_2 - 200, World.WIDTH_2 + 440,
                        World.HEIGHT_2 + 200).prio(4).build(),
        };

        coolRectChains = new CoolRectChain[] {
                // INSPECT_TARGET
                new CoolRectChain(new CoolRect.Builder(345, World.HEIGHT - 27, -1, World.HEIGHT - 71)
                        .outlineThickness(5).speed(3).prio(3)).setSelectedOffset(10),
                // INSPECT_PAGES
                new CoolRectChain(new CoolRect.Builder(425, World.HEIGHT - 297, -1, World.HEIGHT - 334)
                        .outlineThickness(5).speed(3).prio(3)).setSelectedOffset(10)
        };

        // todo u0-7
        coolRectBars = new CoolRectBar[] {
                // HP_INSPECT
                new CoolRectBar(
                        new CoolRect.Builder(325, World.HEIGHT - 97, 605, World.HEIGHT - 117).prio(3).color(Color.RED),
                        Color.GREEN, Color.CYAN, Color.PINK),
                // SP_INSPECT
                new CoolRectBar(new CoolRect.Builder(325, World.HEIGHT - 127, 605, World.HEIGHT - 147).prio(3)
                        .color(Color.VIOLET), Color.PURPLE)
        };

        paths = new Path[] {
                new Path(), // Scrollbar, Inspect page tab left side
                new Path(), // Inspect page tab right side
                new Path(), // Inspect stats page multipliers
                new Path(), // Inspect stats page modifiers
                new Path() // Inspect stats page other stats
        };

        // temp todo draw from atlas
        Texture texture = new Texture("white_pixel.png"); // remember to dispose of later
        TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);

        drawer = new ShapeDrawer(polygonSpriteBatch, region);

        Fonts.createFonts();

        texBg = new Texture("bg.png");

        TypingConfig.DEFAULT_SPEED_PER_CHAR = 0.01f;

        /// Lang
        // Default (US English)
        Lang.createBundle();
        // Lang.createBundle(new Locale("ja", "JP"))

        // Setup formatters
        rf.setMaximumFractionDigits(2);
        rf.setMinimumFractionDigits(0);

        // Make sure the log can interpret special characters
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        Debug.enableDebugHotkeys = true;
        Debug.showDebugInfo = true;
        // Debug.alwaysUseNSWLayout = true;
        // Debug.selectOpponentMoves = true;

        Settings.showFpsCounter = true;
        Settings.battleSpeed = 0.5f;

        popupTitle = new TypingLabel("", Fonts.FontType.KORURI.get(30));
        popupTitle.setAlignment(Align.center);
        popupTitle.setPosition(World.WIDTH_2, World.HEIGHT_2 + 150);

        popupText = new TypingLabel("", Fonts.FontType.KORURI.get(20));
        popupText.setPosition(World.WIDTH_2 - 420, World.HEIGHT_2 + 80);

        inputGuide = new TextraLabel("", FontType.KORURI_BORDER.get(20));
        inputGuide.setPosition(0, 0);
        inputGuide.setAlignment(Align.bottomRight);
        stage2.addActor(inputGuide);

        NAV_PATH.add(MenuType.MAIN);
        createMenuMain();

        // todo if using SteamInput, initial warning popup abt incorrect glyphs recommending using ingame remapping
        // instead

        /// Initialize settings
        // todo i think the following explanation is no longer true
        // Sets resolution based on Settings.scale
        // The game always initially launches at the closest 16:9 res to the display res
        // The game can't launch already scaled because Settings won't be loaded yet,
        // and Settings.scale can exceed display res, and if the game launches at >
        // display res, it causes problems
        /*
         * if (!Settings.autoRes) {
         * changeScale(todo);
         * }
         */

        // todo set target fps and vsync based off of settings (can set in launcher?)

        MenuDebug.init();
    }

    @Override
    public void render() {
        input();
        draw();
    }

    private void input() {
        isF3MenuEnabled ^= Gdx.input.isKeyJustPressed(Input.Keys.F3);
        MenuDebug.handleF3Menu(isF3MenuEnabled);

        InputHandler.checkController();

        switch (NAV_PATH.getLast()) {
            case MAIN:
                if (Debug.enableDebugHotkeys && Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                    NAV_PATH.add(MenuType.DEBUG);
                }

                index = MenuLib.checkMovement1D(index, MenuType.MAIN.getOptCount());

                MenuLib.handleCursor(coolRects[CoolRects.CURSOR_1.ordinal()],
                        coolRects[CoolRects.CURSOR_2.ordinal()], index, World.WIDTH - 700 + 75,
                        World.WIDTH - 175 - 92 + 75 - 10, 230 + 475, 100 + 4);

                if (InputLib.checkInput(Keybind.CONFIRM)) {
                    optSelected = MenuOptType.values()[MenuType.MAIN.getOpt(index).getType().ordinal()];
                    switch (optSelected) {
                        case START:
                            coolRects[CoolRects.MENU_MAIN.ordinal()].setDir(-1);
                            coolRects[CoolRects.CURSOR_1.ordinal()].setDir(-1);
                            MenuLib.removeOpts(optLabels, stage1);

                            BattleController.create();
                            NAV_PATH.add(MenuType.BATTLE);
                            break;
                        case QUIT:
                            Gdx.app.exit();
                            break;
                        case MANUAL, OPTIONS, CREDITS:
                            TriLib.createPopup("WIP",
                                    "This isn't finished yet!\n{WAVE=0.25;0.5;0.5}{GRADIENT=pink;violet}{SPIN}Girls are now praying, please wait warmly...");
                            break;
                    }
                } else if (InputLib.checkInput(Keybind.BACK)) {
                    if (index == MenuType.MAIN.getOptCount() - 1) {
                        Gdx.app.exit();
                    } else {
                        index = MenuType.MAIN.getOptCount() - 1;
                    }
                }
                break;
            case POPUP:
                if (InputLib.checkInput(Keybind.CONFIRM, Keybind.BACK)) {
                    stage4.getRoot().removeActor(popupTitle);
                    stage4.getRoot().removeActor(popupText);
                    coolRects[CoolRects.POPUP_CENTERED.ordinal()].setDir(-1);
                    NAV_PATH.removeLast();
                }
                break;
            case BATTLE, TARGETING, LOG, INSPECT_TARGETING, INSPECT:
                BattleControllerLib.updateStatDisplay();
                BattleController.input();
                break;
            // todo cleanup
            case DEBUG:
                if (InputLib.checkInput(Keybind.BACK)) {
                    NAV_PATH.removeLast();
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                    Debug.showDebugInfo = !Debug.showDebugInfo;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                    MenuDebug.create(MenuType.DEBUG_TEXT);
                    NAV_PATH.add(MenuType.DEBUG_TEXT);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                    MenuDebug.create(MenuType.DEBUG_RES);
                    NAV_PATH.add(MenuType.DEBUG_RES);
                }
                break;
            case DEBUG_TEXT:
                MenuDebug.input(MenuType.DEBUG_TEXT);
                break;
            case DEBUG_RES:
                MenuDebug.input(MenuType.DEBUG_RES);
                break;
            default:
                // You would not believe how difficult it can be to debug without this
                throw new RuntimeException("Unrecognized MenuType");
        }

        if (Settings.showInputGuide) {
            MenuLib.setInputGuideText(NAV_PATH.getLast());
            inputGuide.setX(World.WIDTH - inputGuide.getWidth());
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setProjectionMatrix(camera.combined);
        polygonSpriteBatch.setProjectionMatrix(camera.combined);

        // Sprites (will prob need several separate begin/end blocks and a wrapper
        // method eventually)
        spriteBatch.begin();
        spriteBatch.draw(texBg, 0, 0, World.WIDTH, World.HEIGHT);
        spriteBatch.end();

        // Low priority. Unused
        drawShapes(0);

        // Lowest priority for actors
        stage0.act();
        stage0.draw();

        // Med priority (like nameplates)
        drawShapes(1);

        // Med priority actors (like text over nameplates)
        stage1.act();
        stage1.draw();

        // High priority (like full log / fullscreen menu bg)
        drawShapes(2);

        // High priority actors (like full log / fullscreen menu / popup text)
        stage2.act();
        stage2.draw();

        // Higher priority (like inspect bg)
        drawShapes(3);

        // Higher priority actors (like inspect text)
        stage3.act();
        stage3.draw();

        // Highest priority (like popups)
        drawShapes(4);

        // todo placeholder for unit sprite
        if (NAV_PATH.getLast() == MenuType.INSPECT) {
            polygonSpriteBatch.begin();
            drawer.setColor(Color.WHITE);
            drawer.rectangle(30, World.HEIGHT - 256 - 30, 256, 256);
            polygonSpriteBatch.end();
        }

        if (isF3MenuEnabled) drawF3MenuBg();

        stage4.act();
        stage4.draw();
    }

    private void createMenuMain() {
        optLabels = new ArrayList<>();
        MenuLib.createOpts(optLabels, FontType.KORURI.get(80), stage1);
    }

    // todo fix bugs that arise from being resized (temporary input rejection) or remove drag resizing
    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height, true);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        polygonSpriteBatch.dispose();
        stage0.dispose();
        stage1.dispose();
        stage2.dispose();
        stage3.dispose();
        stage4.dispose();
        texBg.dispose();
    }
}
