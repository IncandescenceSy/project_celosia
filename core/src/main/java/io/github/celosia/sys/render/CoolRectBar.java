package io.github.celosia.sys.render;

import com.badlogic.gdx.graphics.Color;
import io.github.celosia.sys.util.ArrayLib;

import java.util.Arrays;

// todo rename
public class CoolRectBar extends CoolRect {

    private final float[] barProgs;
    private final Color[] barColors;

    public CoolRectBar(CoolRect.Builder builder, Color... barColors) {
        super(builder);
        this.setOutlineThickness(0);
        this.barColors = barColors;
        barProgs = new float[barColors.length];
    }

    public void setBarProg(int index, float barProg) {
        this.barProgs[index] = barProg;
    }

    public float getBarProg(int index) {
        return barProgs[index];
    }

    @Override
    public void draw(float delta) {
        this.setProg(Math.clamp(
                this.getProg() + (delta * this.getDir() * this.getSpeed() * (this.getDir() == -1 ? 2 : 1)), 0f, 1f));

        if (this.getProg() > 0.02f) {
            int l = this.getL();
            int r = this.getR();
            int t = this.getT();
            int b = this.getB();

            this.drawWithoutUpdate(l, r, t, b, this.getColor());

            // Draw overlay bars from longest to shortest so they're all visible
            // todo enforce normal order for equal length
            float[] progsSorted = Arrays.copyOf(barProgs, barProgs.length);
            Color[] colorsSorted = Arrays.copyOf(barColors, barColors.length);
            ArrayLib.sortParallel(progsSorted, colorsSorted);

            for (int i = progsSorted.length - 1; i >= 0; i--) {
                this.drawWithoutUpdate(l, (int) (l + ((r - l) * Math.min(progsSorted[i], this.getProg()))), t, b,
                        colorsSorted[i], this.getProg());
            }
        }
    }
}
