package io.github.celosia.sys.menu;

import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class LabelStyles {
    public static final LabelStyle KORURI_20 = new LabelStyle();
    public static final LabelStyle KORURI_40 = new LabelStyle();
    public static final LabelStyle KORURI_80 = new LabelStyle();

    public static void createStyles() {
        KORURI_20.font = Fonts.FontType.KORURI.getSize20();
        KORURI_40.font = Fonts.FontType.KORURI.getSize40();
        KORURI_80.font = Fonts.FontType.KORURI.getSize80();
    }
}
