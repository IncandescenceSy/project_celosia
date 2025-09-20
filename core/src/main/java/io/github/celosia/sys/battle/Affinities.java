package io.github.celosia.sys.battle;

// todo change to Map<Element, int> for prospective modders?
public class Affinities {
	private int ignis;
	private int glacies;
	private int fulgur;
	private int ventus;
	private int terra;
	private int lux;
	private int malum;

	public Affinities(int ignis, int glacies, int fulgur, int ventus, int terra, int lux, int malum) {
		this.ignis = ignis;
		this.glacies = glacies;
		this.fulgur = fulgur;
		this.ventus = ventus;
		this.terra = terra;
		this.lux = lux;
		this.malum = malum;
	}

	public Affinities(Affinities affs) {
		ignis = affs.ignis;
		glacies = affs.glacies;
		fulgur = affs.fulgur;
		ventus = affs.ventus;
		terra = affs.terra;
		lux = affs.lux;
		malum = affs.malum;
	}

	public void setIgnis(int ignis) {
		this.ignis = ignis;
	}

	public int getIgnis() {
		return ignis;
	}

	public void setGlacies(int glacies) {
		this.glacies = glacies;
	}

	public int getGlacies() {
		return glacies;
	}

	public void setFulgur(int fulgur) {
		this.fulgur = fulgur;
	}

	public int getFulgur() {
		return fulgur;
	}

	public void setVentus(int ventus) {
		this.ventus = ventus;
	}

	public int getVentus() {
		return ventus;
	}

	public void setTerra(int terra) {
		this.terra = terra;
	}

	public int getTerra() {
		return terra;
	}

	public void setLux(int lux) {
		this.lux = lux;
	}

	public int getLux() {
		return lux;
	}

	public void setMalum(int malum) {
		this.malum = malum;
	}

	public int getMalum() {
		return malum;
	}

	public void setAffs(int ignis, int glacies, int fulgur, int ventus, int terra, int lux, int malum) {
		this.ignis = ignis;
		this.glacies = glacies;
		this.fulgur = fulgur;
		this.ventus = ventus;
		this.terra = terra;
		this.lux = lux;
		this.malum = malum;
	}

	public int[] getAffs() {
		return new int[]{ignis, glacies, fulgur, ventus, terra, lux, malum};
	}

	public void setAff(Element element, int aff) {
		switch (element) {
			case IGNIS -> ignis = aff;
			case GLACIES -> glacies = aff;
			case FULGUR -> fulgur = aff;
			case VENTUS -> ventus = aff;
			case TERRA -> terra = aff;
			case LUX -> lux = aff;
			case MALUM -> malum = aff;
		}
	}

	public int getAff(Element element) {
		return switch (element) {
			case VIS -> 0;
			case IGNIS -> ignis;
			case GLACIES -> glacies;
			case FULGUR -> fulgur;
			case VENTUS -> ventus;
			case TERRA -> terra;
			case LUX -> lux;
			case MALUM -> malum;
		};
	}
}
