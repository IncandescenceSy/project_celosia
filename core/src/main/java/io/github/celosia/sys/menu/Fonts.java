package io.github.celosia.sys.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.github.tommyettinger.textra.Font;

public class Fonts {
    static Font koruri20;
    static Font koruri30;
    static Font koruri40;
    static Font koruri80;

    public static void createFonts() {
        // Todo find a way to make fonts less pixely
        FreeTypeFontGenerator genKoruri = new FreeTypeFontGenerator(Gdx.files.internal("fnt/koruri.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.color = Color.WHITE;

        parameter.size = 20;
        koruri20 = new Font(genKoruri.generateFont(parameter));

        parameter.size = 30;
        koruri30 = new Font(genKoruri.generateFont(parameter));

        parameter.size = 40;
        koruri40 = new Font(genKoruri.generateFont(parameter));

        parameter.size = 80;
        koruri80 = new Font(genKoruri.generateFont(parameter));

        genKoruri.dispose();
    }

    public enum FontType {
         KORURI(koruri20, koruri30, koruri40, koruri80);

         // todo other sizes and styles
         private final Font size20;
         private final Font size30;
         private final Font size40;
         private final Font size80;

         FontType(Font size20, Font size30, Font size40, Font size80) {
             this.size20 = size20;
             this.size30 = size30;
             this.size40 = size40;
             this.size80 = size80;
         }

        public Font getSize20() {
            return size20;
        }

        public Font getSize30() {
            return size30;
        }

        public Font getSize40() {
            return size40;
        }

        public Font getSize80() {
            return size80;
        }
    }
}
