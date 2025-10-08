package io.github.celosia.sys.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.celosia.Main;
import io.github.celosia.sys.menu.MenuLib;
import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;

import static io.github.celosia.Main.coolRects;
import static io.github.celosia.Main.drawer;
import static io.github.celosia.Main.paths;
import static io.github.celosia.Main.polygonSpriteBatch;
import static io.github.celosia.Main.popupText;
import static io.github.celosia.Main.popupTitle;

public class TriLib {

    // Draws cool rectangles with optionally slanted sides and an optional outline
    public static void drawCoolRects(int prio) {
        polygonSpriteBatch.begin();

        float delta = Gdx.graphics.getDeltaTime();

        Interpolation i = Interpolation.smooth2; // todo diff interpol for in vs out

        for (CoolRect rect : coolRects) {
            if (rect.getPrio() == prio) {
                int dir = rect.getDir();
                float prog = Math.clamp(rect.getProg() + (delta * dir * rect.getSpeed() * (dir == -1 ? 2 : 1)), 0f, 1f);
                rect.setProg(prog);

                // Skip the rest if the width is to be near 0
                // todo better fix for the inexplicable tallness
                if (prog <= 0.02f) {
                    continue;
                }

                float height = rect.getT() - rect.getB();

                float angLOff = (rect.getAngL() > 0) ? (height / (90f / rect.getAngL())) : 0;
                float angROff = (rect.getAngR() > 0) ? (height / (90f / rect.getAngR())) : 0;

                // Top left
                float tlx = (rect.getL() + angLOff);
                float tly = rect.getT();

                // Top right
                float trx = i.apply(tlx, (rect.getR() + angROff), prog);
                float try_ = rect.getT();

                // Bottom left
                float blx = rect.getL();
                float bly = rect.getB();

                // Bottom right
                float brx = i.apply(blx, rect.getR(), prog);
                float bry = rect.getB();

                // Center
                Color color = rect.getColor();
                drawer.setColor(color.r, color.g, color.b, 1);

                // Todo change to TriangleStrip Mesh to avoid duplicate vertices
                drawer.filledTriangle(tlx, tly, blx, bly, trx, try_);

                drawer.filledTriangle(blx, bly, trx, try_, brx, bry);

                // Outline
                if (rect.hasOutline()) {
                    drawer.setColor(1, 1, 1, 1);

                    Array<Vector2> points = new Array<>(false, 4);
                    points.addAll(new Vector2(tlx, tly), new Vector2(blx, bly), new Vector2(brx, bry),
                            new Vector2(trx, try_));
                    drawer.path(points,
                            // Change line thickness near the start/end of the animation to make its
                            // appearance/disappearance smoother
                            i.apply(0, 10, Math.min(1f, prog * 3.5f)), JoinType.POINTY, false);
                }

            }
        }

        polygonSpriteBatch.end();
    }

    public static void drawPaths(int prio) {
        polygonSpriteBatch.begin();

        float delta = Gdx.graphics.getDeltaTime();

        Interpolation i = Interpolation.smooth2;

        for (Path path : paths) {
            if (path.getPrio() == prio) {
                Array<Vector2> points = path.getPoints();

                if (points != null && !points.isEmpty()) {

                    int dir = path.getDir();
                    float prog = Math.clamp(path.getProg() + (delta * dir * path.getSpeed() * (dir == -1 ? 2 : 1)), 0f,
                            1f);
                    path.setProg(prog);

                    // Skip the rest if the width is to be near 0
                    // todo is this needed
                    if (prog <= 0.02f) {
                        continue;
                    }

                    Color color = path.getColor();
                    drawer.setColor(color.r, color.g, color.b, 1);

                    drawer.path(points,
                            // Change line thickness near the start/end of the animation to make its
                            // appearance/disappearance smoother
                            i.apply(0, path.getThickness(), Math.min(1f, prog)), JoinType.POINTY,
                            false);
                }
            }
        }

        polygonSpriteBatch.end();
    }

    // Draws a regular triangle
    // todo wip
    public static void drawTri(PolygonSpriteBatch batch, ShapeDrawer drawer, int tlx, int tly, int blx, int bly,
                               int trx, int try_) {
        batch.begin();
        drawer.filledTriangle(tlx, tly, blx, bly, trx, try_);
        batch.end();
    }

    public static void createPopup(String name, String desc) {
        Main.NAV_PATH.add(MenuLib.MenuType.POPUP);

        Main.stage5.addActor(Main.popupTitle);
        Main.stage5.addActor(Main.popupText);

        popupTitle.setText(name);
        popupText.setText(desc);

        coolRects[CoolRects.POPUP_CENTERED.ordinal()].setDir(1);
    }
}
