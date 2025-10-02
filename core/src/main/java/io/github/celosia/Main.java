package io.github.celosia;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
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
import io.github.celosia.sys.InputHandler;
import io.github.celosia.sys.World;
import io.github.celosia.sys.battle.BattleController;
import io.github.celosia.sys.battle.BattleControllerLib;
import io.github.celosia.sys.battle.Element;
import io.github.celosia.sys.battle.Elements;
import io.github.celosia.sys.input.InputLib;
import io.github.celosia.sys.input.Keybind;
import io.github.celosia.sys.menu.MenuDebug;
import io.github.celosia.sys.menu.MenuLib;
import io.github.celosia.sys.menu.MenuLib.MenuOptType;
import io.github.celosia.sys.menu.MenuLib.MenuType;
import io.github.celosia.sys.render.CoolRect;
import io.github.celosia.sys.render.CoolRects;
import io.github.celosia.sys.render.Fonts;
import io.github.celosia.sys.render.Fonts.FontType;
import io.github.celosia.sys.render.Path;
import io.github.celosia.sys.render.TriLib;
import io.github.celosia.sys.save.Lang;
import io.github.celosia.sys.save.Settings;
import io.github.celosia.sys.util.ArrayX;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;

import static io.github.celosia.sys.render.RenderLib.changeScale;
import static io.github.celosia.sys.render.TriLib.drawCoolRects;
import static io.github.celosia.sys.render.TriLib.drawPaths;
import static io.github.celosia.sys.util.TextLib.rf;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {

    static InputHandler inputHandler;

    static int index = 0;
    public static final ArrayDeque<MenuType> MENU_LIST = new ArrayDeque<>();
    static MenuOptType optSelected;

    // Rendering
    public static FitViewport viewPort;

    public static ShapeDrawer drawer;
    public static SpriteBatch spriteBatch;
    public static PolygonSpriteBatch polygonSpriteBatch;

    // todo can know (unmodded) size exactly
    // todo rename
    public static CoolRect[] coolRects;
    public static Path[] paths;

    public static Stage stage1;
    public static Stage stage2;
    public static Stage stage3;
    public static Stage stage4;
    public static Stage stage5;

    // Atlases
    // From https://juliocacko.itch.io/free-input-prompts
    public static TextureAtlas atlasPrompts;

    // From https://github.com/tommyettinger/game-icons-net-atlas
    public static TextureAtlas atlasIcons;

    static ArrayX<TypingLabel> optLabels;

    static TextraLabel fps;
    public static TypingLabel popupTitle;
    public static TypingLabel popupText;
    static TextraLabel debug;

    // todo move
    static Texture texBg;

    public static TextraLabel inputGuide;

    public static final ArrayX<Element> ELEMENTS = new ArrayX<>(false, 7);

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        polygonSpriteBatch = new PolygonSpriteBatch();

        viewPort = new FitViewport(World.WIDTH, World.HEIGHT);

        stage1 = new Stage(viewPort);
        stage2 = new Stage(viewPort);
        stage3 = new Stage(viewPort);
        stage4 = new Stage(viewPort);
        stage5 = new Stage(viewPort);

        atlasPrompts = new TextureAtlas("prompts.atlas");
        atlasIcons = new TextureAtlas("icons.atlas");

        inputHandler = new InputHandler();
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(inputHandler);
        Gdx.input.setInputProcessor(multiplexer);
        Controllers.addListener(inputHandler);

        coolRects = new CoolRect[] {
                // Main menu bg
                new CoolRect.Builder(World.WIDTH - 700, 230 + 475 + 5, World.WIDTH - 175 - 92, 230 - 75 + 25).dir(1)
                        .hasOutline().prio(2).build(),

                // Menu cursor. X pos comes from 450 (y diff) / 6 (slant)
                new CoolRect.Builder(0, 0, 0, 0).dir(1).color(Color.PURPLE).prio(2).build(),

                // Disappearing menu cursor
                new CoolRect.Builder(0, 0, 0, 0).color(Color.PURPLE).prio(2).build(),
                // Full log
                new CoolRect.Builder(5, World.HEIGHT + 100, 1300, -100).hasOutline().speed(4).angL(0).prio(3).build(),
                // Centered popup bg
                new CoolRect.Builder(World.WIDTH_2 - 440,
                        World.HEIGHT_2 - 200, World.WIDTH_2 + 440, World.HEIGHT_2 + 200).hasOutline().prio(5).build()
        };

        paths = new Path[] {
                new Path(3)
        };

        // temp todo draw from atlas
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        Texture texture = new Texture(pixmap); // remember to dispose of later
        pixmap.dispose();
        TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);

        drawer = new ShapeDrawer(polygonSpriteBatch, region);

        if (Debug.generateFonts) {
            Fonts.createFonts();
        } else {
            // todo fix (doesnt work well)
            Fonts.loadFonts();
        }

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
        // Debug.displayRealStats = true;

        Settings.showFpsCounter = true;
        Settings.battleSpeed = 0.5f;

        fps = new TextraLabel("", FontType.KORURI.get(20));
        fps.setPosition(10, World.HEIGHT - 15);
        stage5.addActor(fps);

        debug = new TextraLabel("", FontType.KORURI.get(20));
        debug.setPosition(10, World.HEIGHT - 80);
        stage5.addActor(debug);

        inputGuide = new TextraLabel("", FontType.KORURI_BORDER.get(20));
        inputGuide.setPosition(0, 0);
        inputGuide.setAlignment(Align.bottomRight);
        stage3.addActor(inputGuide);

        MENU_LIST.add(MenuType.MAIN);
        createMenuMain();

        // todo if using SteamInput, initial warning popup abt incorrect glyphs
        // recommending using ingame remapping instead

        /// Initialize settings
        // Sets resolution based on Settings.scale
        // The game always initially launches at the closest 16:9 res to the display res
        // The game can't launch already scaled because Settings won't be loaded yet,
        /// and Settings.scale can exceed display res, and if the game launches at >
        /// display res, it causes problems
        if (!Settings.autoRes) {
            changeScale(Settings.scale);
        }

        // todo set target fps and vsync based off of settings (cant set in launcher bc
        // settings wont be loaded yet)

        ELEMENTS.addAll(Elements.VIS, Elements.IGNIS, Elements.GLACIES, Elements.FULGUR, Elements.VENTUS,
                Elements.TERRA, Elements.LUX, Elements.MALUM);
    }

    @Override
    public void render() {
        input();
        draw();
    }

    private void input() {
        debug.setVisible(Debug.showDebugInfo);

        if (Debug.showDebugInfo) {
            debug.setText("actors on stage1: " + stage1.getActors().size + "\nactors on stage2: " +
                    stage2.getActors().size + "\nactors on stage3: " + stage3.getActors().size + "\nmenuList = " +
                    MENU_LIST.toString().replace("[", "").replace("]", ""));
        }

        if (Settings.showFpsCounter) {
            fps.setText((int) (1d / Gdx.graphics.getDeltaTime()) + " FPS");
        }

        InputHandler.checkController();

        switch (MENU_LIST.getLast()) {
            case MAIN:
                if (Debug.enableDebugHotkeys && Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                    MENU_LIST.add(MenuType.DEBUG);
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
                            coolRects[CoolRects.CURSOR_1.ordinal()].setDir(-1).setSpeed(2f);
                            MenuLib.removeOpts(optLabels, stage2);

                            BattleController.create();
                            MENU_LIST.add(MenuType.BATTLE);
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
                    stage5.getRoot().removeActor(popupTitle);
                    stage5.getRoot().removeActor(popupText);
                    coolRects[CoolRects.POPUP_CENTERED.ordinal()].setDir(-1);
                    MENU_LIST.removeLast();
                }
                break;
            case BATTLE, TARGETING, LOG, INSPECT_TARGETING, INSPECT:
                BattleControllerLib.updateStatDisplay();
                BattleController.input();
                break;
            // todo cleanup
            case DEBUG:
                if (InputLib.checkInput(Keybind.BACK)) {
                    MENU_LIST.removeLast();
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                    Debug.showDebugInfo = !Debug.showDebugInfo;
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                    MenuDebug.create(MenuType.DEBUG_TEXT);
                    MENU_LIST.add(MenuType.DEBUG_TEXT);
                } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                    MenuDebug.create(MenuType.DEBUG_RES);
                    MENU_LIST.add(MenuType.DEBUG_RES);
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
            MenuLib.setInputGuideText(MENU_LIST.getLast());
            inputGuide.setX(World.WIDTH - inputGuide.getWidth());
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Lowest priority. Unused
        drawCoolRects(0);
        drawPaths(0);

        // Sprites (will prob need several separate begin/end blocks and a wrapper
        // method eventually)
        spriteBatch.begin();
        spriteBatch.draw(texBg, 0, 0, World.WIDTH * Settings.scale, World.HEIGHT * Settings.scale);
        spriteBatch.end();

        // Low priority. Unused
        drawCoolRects(1);
        drawPaths(1);

        // Lowest priority for actors
        stage1.act();
        stage1.draw();

        // Med priority (like nameplates)
        drawCoolRects(2);
        drawPaths(2);

        // Med priority actors (like text over nameplates)
        stage2.act();
        stage2.draw();

        // High priority (like full log / fullscreen menu bg)
        drawCoolRects(3);
        drawPaths(3);

        // High priority actors (like full log / fullscreen menu / popup text)
        stage3.act();
        stage3.draw();

        // Higher priority (like inspect bg)
        drawCoolRects(4);
        drawPaths(4);

        // Higher priority actors (like inspect text)
        stage4.act();
        stage4.draw();

        // Highest priority (like popups)
        drawCoolRects(5);
        drawPaths(5);

        stage5.act();
        stage5.draw();
    }

    private void createMenuMain() {
        optLabels = new ArrayX<>(false, 16);
        MenuLib.createOpts(optLabels, FontType.KORURI.get(80), stage2);
    }

    // todo fix bugs that arise from being resized (temporary input rejection) or
    // remove resizing
    // and handle monitor res changes while the game is running
    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height, true);
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        polygonSpriteBatch.dispose();
        stage1.dispose();
        stage2.dispose();
        stage3.dispose();
        stage4.dispose();
        stage5.dispose();
        texBg.dispose();
    }
}
