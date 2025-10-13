package io.github.celosia.sys.menu;

import static io.github.celosia.sys.save.Lang.lang;

// The different menus in the game
// todo fix magic numbers
public enum MenuType {

    NONE(),
    POPUP(),
    MAIN(
            new MenuOpt(MenuOptType.START, "{SPEED=0.2}{FADE}{SHRINK}" + lang.get("menu.start"), 1900 + 80,
                    230 + 400),
            new MenuOpt(MenuOptType.MANUAL, "{SPEED=0.2}{FADE}{SHRINK}" + lang.get("menu.manual"), 1900 + 60,
                    230 + 300),
            new MenuOpt(MenuOptType.OPTIONS, "{SPEED=0.2}{FADE}{SHRINK}" + lang.get("menu.options"), 1900 + 40,
                    230 + 200),
            new MenuOpt(MenuOptType.CREDITS, "{SPEED=0.2}{FADE}{SHRINK}" + lang.get("menu.credits"), 1900 + 20,
                    230 + 100),
            new MenuOpt(MenuOptType.QUIT, "{SPEED=0.2}{FADE}{SHRINK}" + lang.get("menu.quit"), 1900, 230)),
    BATTLE(),
    TARGETING(),
    LOG(),
    INSPECT_TARGETING(),
    INSPECT(),
    DEBUG(),
    DEBUG_TEXT(),
    DEBUG_RES();

    private MenuOpt[] opts = new MenuOpt[] { new MenuOpt(MenuOptType.NONE, "", 0, 0) };

    MenuType() {}

    MenuType(MenuOpt... opts) {
        this.opts = opts;
    }

    public int getOptCount() {
        return opts.length;
    }

    public MenuOpt getOpt(int index) {
        return opts[index];
    }

    public MenuOpt[] getOpts() {
        return opts;
    }
}
