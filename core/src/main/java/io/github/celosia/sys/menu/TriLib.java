package io.github.celosia.sys.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import space.earlygrey.shapedrawer.JoinType;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.List;

public class TriLib {

    // Draws cool black rectangles with a 15-degree angle rising skew and a white outline
    public static void drawCoolRects(PolygonSpriteBatch batch, ShapeDrawer drawer, List<CoolRect> coolRects) {
        batch.begin();

        float delta = Gdx.graphics.getDeltaTime();

        Interpolation i = Interpolation.smooth2; // todo diff interpol for in vs out

        for(CoolRect rect : coolRects) {
            int dir = rect.getDir();
            float prog = MathUtils.clamp(rect.getProg() + (delta * dir * rect.getSpeed() * (dir == -1 ? 2 : 1)), 0f, 1f);
            rect.setProg(prog);

            // Skip the rest if the width is to be near 0
            // todo better fix for the inexplicable tallness
            if(prog <= 0.02f) continue;

            float height = rect.getT() - rect.getB();

            // Top left
            float tlx = rect.getL() + (height / 6);
            float tly = rect.getT();

            // Top right
            float trx = i.apply(tlx, rect.getR() + (height / 6), prog);
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
            drawer.filledTriangle(
                tlx, tly,
                blx, bly,
                trx, try_
            );

            drawer.filledTriangle(
                blx, bly,
                trx, try_,
                brx, bry
            );

            // Outline
            // todo smoother looking lines if possible
            if(rect.isHasOutline()) {
                drawer.setColor(1, 1, 1, 1);

                Array<Vector2> points = new Array<>(false, 4);
                points.add(new Vector2(tlx, tly));
                points.add(new Vector2(blx, bly));
                points.add(new Vector2(brx, bry));
                points.add(new Vector2(trx, try_));
                drawer.path(points,
                    // Change line thickness near the start/end of the animation to make its appearance/disappearance smoother
                    i.apply(0, 10, Math.min(1f, prog * 3.5f)), JoinType.POINTY, false);
            }

        }

        batch.end();
    }
}
