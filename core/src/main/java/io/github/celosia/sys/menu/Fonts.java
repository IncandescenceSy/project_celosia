package io.github.celosia.sys.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.github.tommyettinger.textra.Font;

public class Fonts {
    static BitmapFont koruri20;
    static BitmapFont koruri40;
    static BitmapFont koruri80;

    static Font koruri20t;

    public static void createFonts() {
        FreeTypeFontGenerator genKoruri = new FreeTypeFontGenerator(Gdx.files.internal("fnt/koruri.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.color = Color.LIGHT_GRAY;
        parameter.borderColor = Color.BLACK;

        parameter.size = 20;
        parameter.borderWidth = 2;
        koruri20 = genKoruri.generateFont(parameter);

        parameter.size = 40;
        parameter.borderWidth = 3;
        koruri40 = genKoruri.generateFont(parameter);

        parameter.size = 80;
        parameter.borderWidth = 6;
        koruri80 = genKoruri.generateFont(parameter);

        genKoruri.dispose();

        koruri20t = new Font(koruri20);
    }

    public enum FontType {
         KORURI(koruri20, koruri40, koruri80, koruri20t);

         // todo other sizes and styles
         private final BitmapFont size20;
         private final BitmapFont size40;
         private final BitmapFont size80;
         private final Font size20t;

         FontType(BitmapFont size20, BitmapFont size40, BitmapFont size80, Font size20t) {
             this.size20 = size20;
             this.size40 = size40;
             this.size80 = size80;
             this.size20t = size20t;
         }

        public BitmapFont getSize20() {
            return size20;
        }

        public BitmapFont getSize40() {
            return size40;
        }

        public BitmapFont getSize80() {
            return size80;
        }

        public Font getSize20t() {
           return size20t;
        }
    }
}
