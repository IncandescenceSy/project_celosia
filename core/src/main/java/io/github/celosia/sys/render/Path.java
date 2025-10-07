package io.github.celosia.sys.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

// A path to be drawn
public class Path {

    private Array<Vector2> points;
    private float prog; // Animation progress
    private int dir; // 1 = unfolding; -1 = collapsing
    private Color color;
    private float speed;
    private int thickness;
    private int prio;

    public Path() {
        this.points = new Array<>();
        this.prog = 0;
        this.dir = -1;
        this.color = Color.WHITE;
        this.speed = 4;
        this.thickness = 5;
        this.prio = 3;
    }

    // Return this for easy chaining
    public Path setPoints(Array<Vector2> points) {
        this.points = points;
        return this;
    }

    public Array<Vector2> getPoints() {
        return points;
    }

    public void setProg(float prog) {
        this.prog = prog;
    }

    public float getProg() {
        return prog;
    }

    public Path setDir(int dir) {
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

    public Path setThickness(int thickness) {
        this.thickness = thickness;
        return this;
    }

    public float getThickness() {
        return thickness;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setPrio(int prio) {
        this.prio = prio;
    }

    public int getPrio() {
        return prio;
    }
}
