package io.github.celosia.sys.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import space.earlygrey.shapedrawer.JoinType;

import static io.github.celosia.Main.drawer;

// todo rename
public class CoolRect {

    private int l; // Left
    private int t; // Top
    private int r; // Right
    private int b; // Bottom

    private float prog; // Animation progress
    private int dir; // 1 = unfolding; -1 = collapsing
    private Color color; // Core color
    private int outlineThickness;
    private float speed; // Speed multiplier. 1f = animation completes in 1s. 2f = 0.5s. Speed is doubled
                         // when closing
    private int slantL; // Move X by 1 for every slant Y
    private int slantR;
    private int prio; // Rendering order priority 0-4. Bigger number = higher layer

    public CoolRect(Builder builder) {
        l = builder.l;
        t = builder.t;
        r = builder.r;
        b = builder.b;
        dir = builder.dir;
        color = builder.color;
        outlineThickness = builder.outlineThickness;
        prog = 0;
        speed = builder.speed;
        slantL = builder.slantL;
        slantR = builder.slantR;
        prio = builder.prio;
    }

    public static class Builder {

        private final int l;
        private final int t;
        private final int r;
        private final int b;

        private int dir = -1;
        private Color color = Color.BLACK;
        private int outlineThickness = 10;
        private float speed = 2;
        private int slantL = 6;
        private int slantR = 6;
        private int prio = 0;

        public Builder(int l, int t, int r, int b) {
            this.l = l;
            this.t = t;
            this.r = r;
            this.b = b;
        }

        public Builder dir(int dir) {
            if (dir == -1 || dir == 1) {
                this.dir = dir;
            } else {
                this.dir = -1;
            }

            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        public Builder outlineThickness(int outlineThickness) {
            this.outlineThickness = outlineThickness;
            return this;
        }

        public Builder noOutline() {
            outlineThickness = 0;
            return this;
        }

        public Builder speed(float speed) {
            this.speed = speed;
            return this;
        }

        public Builder slantL(int slantL) {
            this.slantL = slantL;
            return this;
        }

        public Builder slantR(int slantR) {
            this.slantR = slantR;
            return this;
        }

        public Builder prio(int prio) {
            this.prio = prio;
            return this;
        }

        public CoolRect build() {
            return new CoolRect(this);
        }
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getL() {
        return l;
    }

    public void setT(int t) {
        this.t = t;
    }

    public int getT() {
        return t;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getR() {
        return r;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getB() {
        return b;
    }

    public void setLR(int l, int r) {
        this.l = l;
        this.r = r;
    }

    public void setTB(int t, int b) {
        this.t = t;
        this.b = b;
    }

    public void setPos(int l, int t, int r, int b) {
        this.l = l;
        this.t = t;
        this.r = r;
        this.b = b;
    }

    // Returns this so it can be chained for convenience
    public CoolRect setDir(int dir) {
        this.dir = dir;
        return this;
    }

    public int getDir() {
        return dir;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setOutlineThickness(int outlineThickness) {
        this.outlineThickness = outlineThickness;
    }

    public int getOutlineThickness() {
        return outlineThickness;
    }

    public void setProg(float prog) {
        this.prog = prog;
    }

    public float getProg() {
        return prog;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSlantL(int slantL) {
        this.slantL = slantL;
    }

    public int getSlantL() {
        return slantL;
    }

    public void setSlantR(int slantR) {
        this.slantR = slantR;
    }

    public int getSlantR() {
        return slantR;
    }

    public CoolRect setPrio(int prio) {
        this.prio = prio;
        return this;
    }

    public int getPrio() {
        return prio;
    }

    public void draw(float delta) {
        prog = Math.clamp(prog + (delta * dir * speed * (dir == -1 ? 2 : 1)), 0f, 1f);

        // Skip the rest if the width is to be near 0
        // todo better fix for the inexplicable tallness
        if (prog > 0.02f) this.drawWithoutUpdate();
    }

    // Main logic without update and configurable vertices for the sake of inheritors with custom draw logic
    public void drawWithoutUpdate(int l, int r, int t, int b, Color color, float prog) {
        float height = t - b;

        float angLOff = (slantL > 0) ? (height / slantL) : 0;
        float angROff = (slantR > 0) ? (height / slantR) : 0;

        // Top left
        float tlx = l + angLOff;
        float tly = t;

        // Top right
        float trx = Interpolation.smooth2.apply(tlx, r + angROff, prog);
        float try_ = t;

        // Bottom left
        float blx = l;
        float bly = b;

        // Bottom right
        float brx = Interpolation.smooth2.apply(blx, r, prog);
        float bry = b;

        // Center
        drawer.setColor(color.r, color.g, color.b, 1);

        drawer.filledTriangle(tlx, tly, blx, bly, trx, try_);
        drawer.filledTriangle(blx, bly, trx, try_, brx, bry);

        // Outline
        if (outlineThickness > 0) {
            drawer.setColor(1, 1, 1, 1);

            Array<Vector2> points = new Array<>(false, 4);
            points.addAll(new Vector2(tlx, tly), new Vector2(blx, bly), new Vector2(brx, bry), new Vector2(trx, try_));

            // Change line thickness near the start/end of the animation to make its appearance/disappearance smoother
            drawer.path(points, Interpolation.smooth2.apply(0, outlineThickness, Math.min(1f, prog * 3.5f)),
                    JoinType.POINTY, false);
        }
    }

    public void drawWithoutUpdate(int l, int r, int t, int b, Color color) {
        this.drawWithoutUpdate(l, r, t, b, color, prog);
    }

    public void drawWithoutUpdate() {
        this.drawWithoutUpdate(l, r, t, b, color, prog);
    }
}
