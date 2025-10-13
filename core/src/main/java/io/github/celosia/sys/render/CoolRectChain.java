package io.github.celosia.sys.render;

import com.badlogic.gdx.graphics.Color;

import static io.github.celosia.sys.util.BoolLib.bool2Sign;

// todo rename
public class CoolRectChain extends CoolRect {

    // Width of each division (not counting the first)
    private int[] divisions;

    // Currently selected division gets taller and has a highlight
    private int selectedDiv;
    private int selectedOffset;
    private Color selectedColor;
    private float[] selectedProg;
    private int[] selectedDir;

    public CoolRectChain(CoolRect.Builder builder) {
        super(builder);
        this.selectedColor = Color.PURPLE;
    }

    public CoolRectChain setDivisions(int... divisions) {
        this.divisions = divisions;
        selectedProg = new float[divisions.length];
        selectedDir = new int[divisions.length];
        return this;
    }

    public int[] getDivisions() {
        return divisions;
    }

    public void setSelectedDiv(int selectedDiv) {
        this.selectedDiv = selectedDiv;
    }

    public int getSelectedDiv() {
        return selectedDiv;
    }

    public CoolRectChain setSelectedOffset(int selectedOffset) {
        this.selectedOffset = selectedOffset;
        return this;
    }

    public int getSelectedOffset() {
        return selectedOffset;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    @Override
    public void draw(float delta) {
        this.setProg(Math.clamp(
                this.getProg() + (delta * this.getDir() * this.getSpeed() * (this.getDir() == -1 ? 2 : 1)), 0f, 1f));

        if (this.getProg() > 0.02f) {
            int divTotal = 0;
            for (int i = 0; i < divisions.length; i++) {
                int offset = (int) (selectedOffset * selectedProg[i]);

                int l = this.getL() + divTotal;
                divTotal += divisions[i];
                int r = this.getL() + divTotal - (offset / 5);

                int t = this.getT() + offset;
                int b = this.getB() - offset;

                selectedDir[i] = bool2Sign(selectedDiv == i);
                selectedProg[i] = Math.clamp(selectedProg[i] + (delta * selectedDir[i] * (this.getSpeed() * 2)), 0f,
                        1f);

                this.drawWithoutUpdate(l, r, t, b, this.getColor());

                // Cursor
                this.drawWithoutUpdate(l, r, t, b, selectedColor, Math.min(selectedProg[i], this.getProg()));
            }
        }
    }
}
