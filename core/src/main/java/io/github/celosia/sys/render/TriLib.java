package io.github.celosia.sys.render;

import com.badlogic.gdx.Gdx;
import io.github.celosia.Main;
import io.github.celosia.sys.menu.MenuType;

import static io.github.celosia.Main.coolRectBars;
import static io.github.celosia.Main.coolRectChains;
import static io.github.celosia.Main.coolRects;
import static io.github.celosia.Main.paths;
import static io.github.celosia.Main.polygonSpriteBatch;
import static io.github.celosia.Main.popupText;
import static io.github.celosia.Main.popupTitle;

public class TriLib {

    public static void drawShapes(int prio) {
        polygonSpriteBatch.begin();

        float delta = Gdx.graphics.getDeltaTime();

        for (CoolRect rect : coolRects) {
            if (rect.getPrio() == prio) {
                rect.draw(delta);
            }
        }

        for (CoolRectBar rect : coolRectBars) {
            if (rect.getPrio() == prio) {
                rect.draw(delta);
            }
        }

        for (CoolRectChain rect : coolRectChains) {
            if (rect.getPrio() == prio) {
                rect.draw(delta);
            }
        }

        for (Path path : paths) {
            if (path.getPrio() == prio) {
                path.draw(delta);
            }
        }

        polygonSpriteBatch.end();
    }

    public static void createPopup(String name, String desc) {
        Main.NAV_PATH.add(MenuType.POPUP);

        Main.stage4.addActor(Main.popupTitle);
        Main.stage4.addActor(Main.popupText);

        popupTitle.setText(name);
        popupText.setText(desc);

        coolRects[CoolRects.POPUP_CENTERED.ordinal()].setDir(1);
    }
}
