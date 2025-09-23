package io.github.celosia.sys.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.github.tommyettinger.textra.Font;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

// todo refactor
public class Fonts {
	static FontSize koruri20;
	static FontSize koruri30;
	static FontSize koruri40;
	static FontSize koruri60;
	static FontSize koruri80;

	public static void createFonts() {
		// todo pregen fonts for performance (try using libGDX itself to pregen them)
		FreeTypeFontGenerator genKoruri = new FreeTypeFontGenerator(Gdx.files.internal("fnt/koruri.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

		StringBuilder builder = new StringBuilder();

		// Hiragana
		// todo fix (doesnt work)
		// for(char c = 0x3041; c <= 0x309f; c++) builder.append(c);

		// Katakana
		// for(char c = 0x30a0; c <= 0x30ff; c++) builder.append(c);

		// Additional symbols
		builder.append("∞").append("→");

		parameter.characters += builder;

		parameter.magFilter = Texture.TextureFilter.Linear;
		parameter.minFilter = Texture.TextureFilter.Linear;

		parameter.color = Color.WHITE;

		parameter.size = 20;
		koruri20 = new FontSize(new Font(genKoruri.generateFont(parameter)), 20);

		parameter.size = 30;
		koruri30 = new FontSize(new Font(genKoruri.generateFont(parameter)), 30);

		parameter.size = 40;
		koruri40 = new FontSize(new Font(genKoruri.generateFont(parameter)), 40);

		parameter.size = 60;
		koruri60 = new FontSize(new Font(genKoruri.generateFont(parameter)), 60);

		parameter.size = 80;
		koruri80 = new FontSize(new Font(genKoruri.generateFont(parameter)), 80);

		genKoruri.dispose();
	}

    public static class FontSize {
        private final Font font;
        private final int size;

        FontSize(Font font, int size) {
            this.font = font;
            this.size = size;
        }

        public Font getFont() {
            return font;
        }

        public int getSize() {
            return size;
        }
    }

	public enum FontType {
		KORURI(koruri20, koruri30, koruri40, koruri60, koruri80);

		private final Int2ObjectMap<Font> sizes = new Int2ObjectOpenHashMap<>();

		FontType(FontSize... fonts) {
            for(FontSize font : fonts) {
                sizes.put(font.getSize(), font.getFont());
            }
		}

        public Font get(int size) {
            return sizes.get(size);
        }
	}
}
