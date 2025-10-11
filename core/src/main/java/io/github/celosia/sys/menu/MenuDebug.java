package io.github.celosia.sys.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.TextraLabel;
import com.github.tommyettinger.textra.TypingLabel;
import io.github.celosia.Main;
import io.github.celosia.sys.World;
import io.github.celosia.sys.input.ControllerType;
import io.github.celosia.sys.input.InputHandler;
import io.github.celosia.sys.input.InputLib;
import io.github.celosia.sys.input.Keybind;
import io.github.celosia.sys.render.Fonts;
import io.github.celosia.sys.render.RenderLib;
import io.github.celosia.sys.save.Settings;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.List;

import static io.github.celosia.Main.NAV_PATH;
import static io.github.celosia.Main.stage3;
import static io.github.celosia.sys.menu.MenuLib.setTextIfChanged;
import static io.github.celosia.sys.save.Lang.lang;

public class MenuDebug {

    private static int lines;
    private static final TypingLabel TEXT = new TypingLabel("", Fonts.FontType.KORURI.get(30));

    // F3 menu
    public static final TextraLabel F3L = new TextraLabel("", Fonts.FontType.KORURI.get(20));
    public static final TextraLabel F3R = new TextraLabel("", Fonts.FontType.KORURI.get(20));

    private static final Runtime RUNTIME = Runtime.getRuntime();
    private static final int MB = 1024 * 1024;

    private static final SystemInfo SYSTEM_INFO = new SystemInfo();
    private static final HardwareAbstractionLayer HARDWARE = SYSTEM_INFO.getHardware();

    private static final CentralProcessor CPU = HARDWARE.getProcessor();
    private static final String CPU_NAME = CPU.getProcessorIdentifier().getName();
    // todo if it seems common for CPU_NAME to include the core count, remove this
    // or dynamically display it depending on if it does?
    private static final int CPU_CORES = CPU.getPhysicalProcessorCount();

    private static final List<GraphicsCard> GPUS = HARDWARE.getGraphicsCards();
    private static final int GPU_COUNT = GPUS.size();
    // todo are multi-gpus ever relevant? maybe with iGPU + GPU?
    private static final String GPU_NAME = Gdx.gl.glGetString(GL20.GL_RENDERER);
    // todo fix incorrect vram reporting or just remove this
    private static final long GPU_VRAM = GPUS.getFirst().getVRam();

    private static final long TOTAL_RAM = HARDWARE.getMemory().getTotal();

    public static void init() {
        F3L.setAlignment(Align.topLeft);
        F3L.setPosition(10, World.HEIGHT - 10);
        Main.stage5.addActor(F3L);
        F3L.setZIndex(10000);

        F3R.setAlignment(Align.topRight);
        F3R.setPosition(World.WIDTH - 10, World.HEIGHT - 10);
        Main.stage5.addActor(F3R);
        F3R.setZIndex(10000);
    }

    public static void create(MenuLib.MenuType menuType) {
        if (menuType == MenuLib.MenuType.DEBUG_TEXT) {
            lines = 1;
            TEXT.setPosition(World.WIDTH_2, World.HEIGHT_2);
            stage3.addActor(TEXT);
        }
    }

    // todo cleanup
    public static void input(MenuLib.MenuType menuType) {
        if (menuType == MenuLib.MenuType.DEBUG_TEXT) {
            setTextIfChanged(TEXT, "omg its hatsune miku!" + "\nomg miku!!!".repeat(lines + 1));

            TEXT.setX(World.WIDTH - TEXT.getWidth());

            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) lines++;

            // im gonna write really bad code and no one can stop me because it's just a debug menu that i'll remove
            // when im done with it anyway. Unless I forget
            if (InputLib.checkInput(Keybind.LEFT)) {
                if (InputLib.checkInput(Keybind.UP)) TEXT.setAlignment(Align.topLeft);
                else if (InputLib.checkInput(Keybind.DOWN)) TEXT.setAlignment(Align.bottomLeft);
                else TEXT.setAlignment(Align.left);
            } else if (InputLib.checkInput(Keybind.RIGHT)) {
                if (InputLib.checkInput(Keybind.UP)) TEXT.setAlignment(Align.topRight);
                else if (InputLib.checkInput(Keybind.DOWN)) TEXT.setAlignment(Align.bottomRight);
                else TEXT.setAlignment(Align.right);
            } else if (InputLib.checkInput(Keybind.UP)) TEXT.setAlignment(Align.top);
            else if (InputLib.checkInput(Keybind.DOWN)) TEXT.setAlignment(Align.bottom);
            else if (InputLib.checkInput(Keybind.MENU)) TEXT.setAlignment(Align.center);
            else if (InputLib.checkInput(Keybind.BACK)) {
                stage3.getRoot().removeActor(TEXT);
                NAV_PATH.removeLast();
            }
        } else if (menuType == MenuLib.MenuType.DEBUG_RES) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) RenderLib.changeScale(640, 360);
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) RenderLib.changeScale(1920, 1080);
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) RenderLib.changeScale(2560, 1440);
            else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) RenderLib.changeScale(3840, 2160);
            else if (InputLib.checkInput(Keybind.BACK)) NAV_PATH.removeLast();
        }
    }

    public static void handleF3Menu(boolean enabled) {
        F3L.setVisible(enabled);
        F3R.setVisible(enabled);

        if (!enabled) return;

        String vsync = Settings.useVsync ? " " + lang.get("f3.vsync") : "";

        long memAlloc = RUNTIME.totalMemory();

        String gpuCount = "";
        if (GPU_COUNT > 1) {
            gpuCount = " " + lang.format("f3.gpu_count", GPU_COUNT - 1);
        }

        String lastInputSource = lang.get("f3.keyboard");

        if (InputHandler.getLastUsedControllerType() != ControllerType.NONE) {
            Controller controller = InputHandler.getController();
            assert controller != null;
            lastInputSource = controller.getName() + " " +
                    lang.format("f3.predicted_type", InputLib.getControllerType(controller).toString());
        }

        // spotless:off
        F3L.setText(
                lang.get("f3.press_f3") + "\n" +
                lang.format("f3.fps", Gdx.graphics.getFramesPerSecond(), RenderLib.getTargetFPS()) + vsync + "\n" +
                lang.format("f3.resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) + "\n" +
                lang.format("f3.nav_path", NAV_PATH.toString().replace("[", "").replace("]", "")) + "\n" +
                lang.format("f3.overworld_location", "todo", 0, 0) + "\n" + // todo
                lang.format("f3.actor_count", Main.stage1.getActors().size, Main.stage2.getActors().size,
                        Main.stage3.getActors().size, Main.stage4.getActors().size, Main.stage5.getActors().size) + "\n" +
                lang.format("f3.loaded_mod_count", 0) // todo
        );

        F3R.setText(
                lang.format("f3.os", System.getProperty("os.name")) + "\n" +
                lang.format("f3.ram", (memAlloc - RUNTIME.freeMemory()) / MB, memAlloc / MB, RUNTIME.maxMemory() / MB, TOTAL_RAM / MB) + "\n" +
                lang.format("f3.cpu", CPU_NAME) + " " + lang.format("f3.cpu_cores", CPU_CORES) + "\n" +
                lang.format("f3.gpu", GPU_NAME) + " " + lang.format("f3.gpu_vram", GPU_VRAM / MB) + gpuCount + "\n" +
                lang.format("f3.last_input_source", lastInputSource)
        );
        // spotless:on
    }

    public static void drawF3MenuBg() {
        Main.polygonSpriteBatch.begin();

        float w = F3L.getPrefWidth() + 20;
        float h = F3L.getPrefHeight() + 20;
        Main.drawer.filledRectangle(0, World.HEIGHT - h, w, h, RenderLib.TRANS_BLACK);

        w = F3R.getPrefWidth() + 20;
        h = F3R.getPrefHeight() + 20;
        Main.drawer.filledRectangle(World.WIDTH - w, World.HEIGHT - h, World.WIDTH, h, RenderLib.TRANS_BLACK);

        Main.polygonSpriteBatch.end();
    }
}
