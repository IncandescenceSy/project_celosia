package io.github.celosia.sys.menu;

import com.badlogic.gdx.graphics.Color;

// Warning: bad code that I don't care enough to fix as long as it works
// todo refactor
public class CoolRect {

	// Why not just store the actual corners instead of weird in between points?
	// Don't think about it too hard
	private int l; // Left
	private int t; // Top
	private int r; // Right
	private int b; // Bottom

	private float prog; // Animation progress
	private int dir; // 1 = unfolding; -1 = collapsing
	private Color color; // Core color
	private boolean hasOutline; // Whether to draw an outline
	private float speed; // Speed multiplier. 1f = animation completes in 1s. 2f = 0.5s. Speed is doubled
							// when closing
	private int angL; // Not actually an angle. Don't worry about it
	private int angR;
	private int prio; // Rendering order priority 0-4. Bigger number = higher layer

	public CoolRect(Builder builder) {
		l = builder.l;
		t = builder.t;
		r = builder.r;
		b = builder.b;
		dir = builder.dir;
		color = builder.color;
		hasOutline = builder.hasOutline;
		prog = 0;
		speed = builder.speed;
		angL = builder.angL;
		angR = builder.angR;
		prio = builder.prio;
	}

	public static class Builder {
		private final int l;
		private final int t;
		private final int r;
		private final int b;
		private int dir = -1;
		private Color color = Color.BLACK;
		private boolean hasOutline = false;
		private float speed = 2;
		private int angL = 15;
		private int angR = 15;
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

		public Builder hasOutline() {
			hasOutline = true;
			return this;
		}

		public Builder speed(float speed) {
			this.speed = speed;
			return this;
		}

		public Builder angL(int angL) {
			this.angL = angL;
			return this;
		}

		public Builder angR(int angR) {
			this.angR = angR;
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

	public void setAngL(int angL) {
		this.angL = angL;
	}

	public int getAngL() {
		return angL;
	}

	public void setAngR(int angR) {
		this.angR = angR;
	}

	public int getAngR() {
		return angR;
	}

	public void setPrio(int prio) {
		this.prio = prio;
	}

	public int getPrio() {
		return prio;
	}
}
