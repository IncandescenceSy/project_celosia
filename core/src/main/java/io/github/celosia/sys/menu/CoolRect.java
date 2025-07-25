package io.github.celosia.sys.menu;

import com.badlogic.gdx.graphics.Color;

public class CoolRect {

    private int l; // Left
    private int t; // Top
    private int r; // Right
    private int b; // Bottom
    private float prog; // Animation progress
    private int dir; // 1 = unfolding; -1 = collapsing
    private Color color; // Core color
    private boolean hasOutline; // Whether to draw an outline
    private float speed; // Speed multiplier. 1f = animation completes in 1s. Speed is doubled when closing

    public CoolRect(int l, int t, int r, int b, int dir, Color color, boolean hasOutline, float prog, float speed) {
        this.l = l;
        this.t = t;
        this.r = r;
        this.b = b;
        this.dir = dir;
        this.color = color;
        this.hasOutline = hasOutline;
        this.prog = prog;
        this.speed = speed;
    }

    public CoolRect(int l, int t, int r, int b, int dir) {
        this(l, t, r, b, dir, Color.BLACK, true, 0f, 2f);
    }

    public CoolRect(int l, int t, int r, int b) {
        this(l, t, r, b, -1, Color.BLACK, true, 0f, 2f);
    }

    public CoolRect(int dir, Color color, boolean hasOutline) {
        this(0, 0, 0, 0, dir, color, hasOutline, 0f, 2f);
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

    public void setHasOutline(boolean hasOutline) {
        this.hasOutline = hasOutline;
    }

    public boolean isHasOutline() {
        return hasOutline;
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
}
