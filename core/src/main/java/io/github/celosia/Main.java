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
import io.github.celosia.sys.menu.CoolRect;
import io.github.celosia.sys.menu.CoolRects;
import io.github.celosia.sys.menu.Fonts;
import io.github.celosia.sys.menu.Fonts.FontType;
import io.github.celosia.sys.menu.InputLib;
import io.github.celosia.sys.menu.LabelStyles;
import io.github.celosia.sys.menu.MenuDebug;
import io.github.celosia.sys.menu.MenuLib;
import io.github.celosia.sys.menu.MenuLib.MenuOptType;
import io.github.celosia.sys.menu.MenuLib.MenuType;
import io.github.celosia.sys.menu.Path;
import io.github.celosia.sys.menu.Paths;
import io.github.celosia.sys.menu.TextLib;
import io.github.celosia.sys.settings.Keybind;
import io.github.celosia.sys.settings.Lang;
import io.github.celosia.sys.settings.Settings;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static io.github.celosia.sys.menu.TextLib.rf;
import static io.github.celosia.sys.menu.TriLib.drawCoolRects;
import static io.github.celosia.sys.menu.TriLib.drawPaths;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends ApplicationAdapter {
	InputHandler inputHandler;

	// Current menu info
	int index = 0;
	public static List<MenuType> menuList = new ArrayList<>();
	MenuOptType optSelected;

	// Rendering (in order)
	public static SpriteBatch spriteBatch;
	public static Stage stage1;
	public static List<CoolRect> coolRects = new ArrayList<>();
	public static PolygonSpriteBatch polygonSpriteBatch;
	public static ShapeDrawer drawer;
	public static Stage stage2;
	public static List<Path> paths = new ArrayList<>();
	public static Stage stage3;

    // Atlases
    // Key/Button prompt images courtesy https://juliocacko.itch.io/free-input-prompts
    public static TextureAtlas atlasPrompt;

	// Menu option labels
	List<TypingLabel> optLabels = new ArrayList<>();

	TextraLabel fps; // FPS counter
	static TypingLabel popup;
	static TypingLabel popup2;
	TextraLabel debug;

	// todo move
	Texture texBg;

    // Input guide
    public static TextraLabel inputGuide;

	@Override
	public void create() {
		spriteBatch = new SpriteBatch();
		polygonSpriteBatch = new PolygonSpriteBatch();

		stage1 = new Stage(new FitViewport(World.WIDTH, World.HEIGHT));
		stage2 = new Stage(new FitViewport(World.WIDTH, World.HEIGHT));
		stage3 = new Stage(new FitViewport(World.WIDTH, World.HEIGHT));

        atlasPrompt = new TextureAtlas("prompt.atlas");

		// Handle detecting whether a keyboard or a controller is being used, and
		// finding the currently in-use controller
		inputHandler = new InputHandler();
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(inputHandler);
		Gdx.input.setInputProcessor(multiplexer);
		Controllers.addListener(inputHandler);

		// CoolRects
		// temp
		// todo less magic numbers
		// Main menu bg
		coolRects.add(CoolRects.MENU_MAIN.ordinal(),
				new CoolRect.Builder(World.WIDTH - 700, 230 + 475 + 5, World.WIDTH - 175 - 92, 230 - 75 + 25).dir(1)
						.hasOutline().prio(2).build());
		// Menu cursor. X pos comes from 450 (y diff) / 6 (slant)
		coolRects.add(CoolRects.CURSOR_1.ordinal(),
				new CoolRect.Builder(0, 0, 0, 0).dir(1).color(Color.PURPLE).prio(2).build());
		// Disappearing menu cursor
		coolRects.add(CoolRects.CURSOR_2.ordinal(),
				new CoolRect.Builder(0, 0, 0, 0).color(Color.PURPLE).prio(2).build());
		// Triangle that covers most of the left half of the screen
		coolRects.add(CoolRects.COVER_LEFT.ordinal(),
				new CoolRect.Builder(5, World.HEIGHT + 100, 1300, -100).hasOutline().speed(4).angL(0).prio(3).build());
		// Centered popup bg
		coolRects.add(CoolRects.POPUP_CENTERED.ordinal(), new CoolRect.Builder(World.WIDTH_2 - 440,
				World.HEIGHT_2 - 200, World.WIDTH_2 + 440, World.HEIGHT_2 + 200).hasOutline().prio(3).build());

		// Paths
		paths.add(Paths.SCROLLBAR.ordinal(), new Path(3));

		// temp todo draw from atlas
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.drawPixel(0, 0);
		Texture texture = new Texture(pixmap); // remember to dispose of later
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
		// Lang.createBundle(new Locale("ja", "JP"))

		// Setup formatters
		rf.setMaximumFractionDigits(2);
		rf.setMinimumFractionDigits(0);

		// Debug
		// Make sure the log can interpret special characters
		System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

		Debug.enableDebugHotkeys = true;
		Debug.showDebugInfo = true;
		//Debug.alwaysUseNSWLayout = true;
		// Debug.selectOpponentMoves = true;
		// Debug.displayRealStats = true;

		Settings.showFpsCounter = true;
		Settings.battleSpeed = 0.1f;

		fps = new TextraLabel("", FontType.KORURI.get(20));
		fps.setPosition(10, World.HEIGHT - 15);
		stage3.addActor(fps);

		debug = new TextraLabel("", FontType.KORURI.get(20));
		debug.setPosition(10, World.HEIGHT - 80);
		stage3.addActor(debug);

        // todo outline
        inputGuide = new TextraLabel("", FontType.KORURI.get(20));
        inputGuide.setPosition(0, 0);
        inputGuide.setAlignment(Align.bottomRight);
        stage3.addActor(inputGuide);

		// Setup main menu
		menuList.add(MenuType.MAIN);
		createMenuMain();

		// todo if using SteamInput, initial warning popup abt incorrect glyphs
		// recommending using ingame remapping instead
	}

	@Override
	public void render() {
		input();
		draw();
	}

	private void input() {
		// debug
        debug.setVisible(Debug.showDebugInfo);

		if (Debug.showDebugInfo) {
			debug.setText("actors on stage1: " + stage1.getActors().size + "\nactors on stage2: "
					+ stage2.getActors().size + "\nactors on stage3: " + stage3.getActors().size + "\nmenuList = "
					+ menuList.toString().replace("[", "").replace("]", ""));
		}

		// Display FPS counter
		if (Settings.showFpsCounter) {
			fps.setText((int) (1d / Gdx.graphics.getDeltaTime()) + " FPS");
		}

		// Get mappings of current controller
		InputHandler.checkController();

		switch (menuList.getLast()) {
			case MAIN :
                // Debug menu
                if(Gdx.input.isKeyJustPressed(Input.Keys.T)) {
                    menuList.add(MenuType.DEBUG);
                }

				// Handle menu navigation
				index = MenuLib.checkMovement1D(index, MenuType.MAIN.getOptCount());

				// Handle option color
				MenuLib.handleCursor(coolRects.get(CoolRects.CURSOR_1.ordinal()),
						coolRects.get(CoolRects.CURSOR_2.ordinal()), index, World.WIDTH - 700 + 75,
						World.WIDTH - 175 - 92 + 75 - 10, 230 + 475, 100 + 4);

				// Handle menu confirmation
				if (InputLib.checkInput(Keybind.CONFIRM)) {
					optSelected = MenuOptType.values()[MenuType.MAIN.getOpt(index).getType().ordinal()];
					switch (optSelected) {
						case START :
							coolRects.get(CoolRects.MENU_MAIN.ordinal()).setDir(-1);
							coolRects.get(CoolRects.CURSOR_1.ordinal()).setDir(-1).setSpeed(2f);
							MenuLib.removeOpts(optLabels, stage2);
							// Start game
							BattleController.create();
							menuList.add(MenuType.BATTLE);

							break;
						case QUIT :
							// Quit game
							Gdx.app.exit();
						case MANUAL, OPTIONS, CREDITS :
							createPopup("WIP",
									"This isn't finished yet!\n{WAVE=0.25;0.5;0.5}{GRADIENT=pink;violet}{SPIN}Girls are now praying, please wait warmly...");
							break;
					}
				} else if (InputLib.checkInput(Keybind.BACK)) {
					if (index == MenuType.MAIN.getOptCount() - 1) {
						// Quit game
						Gdx.app.exit();
					} else {
                        index = MenuType.MAIN.getOptCount() - 1;
                    }
				}
				break;
			case POPUP :
				if (InputLib.checkInput(Keybind.CONFIRM, Keybind.BACK)) {
					stage3.getRoot().removeActor(popup);
					stage3.getRoot().removeActor(popup2);
					coolRects.get(CoolRects.POPUP_CENTERED.ordinal()).setDir(-1);
					menuList.removeLast();
				}
				break;
			case BATTLE, TARGETING, LOG :
				BattleControllerLib.updateStatDisplay();
				BattleController.input();
				break;
            case DEBUG :
                if(InputLib.checkInput(Keybind.BACK)) {
                    menuList.removeLast();
                } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                    Debug.showDebugInfo ^= true; // Invert boolean
                } else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
                    MenuDebug.create(MenuType.DEBUG_TEXT);
                    menuList.add(MenuType.DEBUG_TEXT);
                }
            case DEBUG_TEXT :
                MenuDebug.input(MenuType.DEBUG_TEXT);
		}

        // Input guide
        if (Settings.showInputGuide) {
            InputLib.setInputGuideText(menuList.getLast());
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

		// High priority actors (like text over nameplates)
		stage2.act();
		stage2.draw();

		// Higher priority (like full log / fullscreen menu bg / popups)
		drawCoolRects(3);
		drawPaths(3);

		// Highest priority actors (like full log / fullscreen menu / popup text)
		stage3.act();
		stage3.draw();

		// Highest priority. Unused
		drawCoolRects(4);
		drawPaths(4);
	}

	private void createMenuMain() {
		// Create options
		optLabels = new ArrayList<>();
		MenuLib.createOpts(optLabels, FontType.KORURI.get(80), stage2);
	}

	public static void createPopup(String name, String desc) {
		menuList.add(MenuType.POPUP);

		popup = new TypingLabel(TextLib.tags + name, FontType.KORURI.get(80));
		popup.setPosition(World.WIDTH_2, World.HEIGHT_2 + 120, Align.center);
		stage3.addActor(popup);

		popup2 = new TypingLabel(TextLib.tags + desc, FontType.KORURI.get(30));
		popup2.setPosition(World.WIDTH_2 - 440, World.HEIGHT_2 + 20, Align.left);
		stage3.addActor(popup2);

		coolRects.get(CoolRects.POPUP_CENTERED.ordinal()).setDir(1);
	}

	@Override
	public void resize(int width, int height) {
		stage1.getViewport().update(width, height, true);
		stage2.getViewport().update(width, height, true);
		stage3.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		polygonSpriteBatch.dispose();
		stage1.dispose();
		stage2.dispose();
		stage3.dispose();
		texBg.dispose();
	}
}
