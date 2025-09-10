package io.github.celosia.sys.menu;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

// A path to be drawn
public class Path {
	private Array<Vector2> points;
	private float prog; // Animation progress
	private int dir; // 1 = unfolding; -1 = collapsing
	private final Color color;
	private final float speed;
	private final int thickness;
	private final int prio;

	public Path(Array<Vector2> points, Color color, int thickness, int prio) {
		this.points = points;
		this.prog = 0;
		this.dir = -1;
		this.color = color;
		this.speed = 4;
		this.thickness = thickness;
		this.prio = prio;
	}

	public Path(Array<Vector2> points) {
		this(points, Color.WHITE, 10, 0);
	}

	public Path(int prio) {
		this(new Array<>(), Color.WHITE, 10, prio);
	}

	public void setPoints(Array<Vector2> points) {
		this.points = points;
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

	public void setDir(int dir) {
		this.dir = dir;
	}

	public int getDir() {
		return dir;
	}

	public Color getColor() {
		return color;
	}

	public float getThickness() {
		return thickness;
	}

	public float getSpeed() {
		return speed;
	}

	public int getPrio() {
		return prio;
	}
}
